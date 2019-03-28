pipeline {
    agent { node { label 'sonarqube-upshift' } }
    options {
      skipDefaultCheckout true
    }
    triggers {
      cron('0 8 * * *')
    }
    stages {
        stage('Deploy') {
            steps {
                // clone project
                git url: 'https://github.com/RedHatQE/CodeQuality.git'
            }
        }
        stage('Analyse') {
            steps {
                // coverage tests initialization script
                dir('examples/java-test-repo'){
                sh '''mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent \
                  install -Dmaven.test.failure.ignore=true || true'''
                }
            }
        }
        stage('Report') {
            /*
            sonar runner parameters, set sources and baseDir to project home
            ========================

            projectKey (string): SonarQube project identification key (unique)
            projectName (string): SonarQube project name (NOT unique)
            projectVersion (string): SonarQube project version (unique)
            sources (string): source code home directory
            projectBaseDir (string): project home directory (same as sources)
            language (string): project language(java)
            inclusions (string): file inclusion pattern
            exclusions (string): file exclusion pattern
            login (string): SonarQube server user name
            password (string): SonarQube server user password
            java.binaries (string): REQUIRED Since SonarJava 4.12
             */
            steps {
              writeFile file: "${pwd()}/sonar-project.properties", text: """
              sonar.projectKey=test-files_1_0_java_full-analysis
              sonar.projectName=Java Testfiles
              sonar.projectVersion=1.0
              sonar.sources=${pwd()}
              sonar.projectBaseDir=${pwd()}
              sonar.language=java
              sonar.inclusions=**/*.java
              sonar.exclusions=src/test/**/*.java
              sonar.login=test
              sonar.password=test
              sonar.ws.timeout=180
              sonar.java.binaries=**/target/classes
              """

              // initite pre-configured sonar scanner tool on project
              // 'sonarqube_prod' is our cnfigured tool name, see yours
              // in the Jenkins tool configuration
              // NOTE: pay attention we are using maven under sonar env
              withSonarQubeEnv('sonarqube_prod') {
                sh "${tool 'sonar-scanner-2.8'}/bin/sonar-scanner"
              }
            }
        }
    }
}
