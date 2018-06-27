pipeline {
    agent { node { label 'ssh_slave' } }
    options {
      skipDefaultCheckout true
    }
    triggers {
      cron('0 8 * * *')
    }
    stages {
        stage('Deploy') {
            steps {
                // clone project and install dependencies
                git url: 'https://github.com/shakedlokits/CodeQuality.git', branch: 'java-test-repo'
                sh 'dnf install -y maven java-1.8.0-openjdk-devel.x86_64'
            }
        }
        stage('Analyse') {
            steps {
                // coverage tests initialization script
                sh '''mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent \
                  install -Dmaven.test.failure.ignore=true || true'''
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
              """

              // initite pre-configured sonar scanner tool on project
              // 'slokits_test_env' is our cnfigured tool name, see yours
              // in the Jenkins tool configuration
              // NOTE: pay attention we are using maven under sonar env
              withSonarQubeEnv('slokits_test_env') {
                sh 'mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -U -Dmaven.test.failure.ignore=true org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar'

              }
            }
        }
    }
}
