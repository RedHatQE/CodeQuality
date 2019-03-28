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

                // install coverage dependencies from Gemfile
                dir('examples/ruby-test-repo'){
                sh 'bundler install'
                }
            }
        }
        stage('Analyse') {
            steps {
                dir('examples/ruby-test-repo'){
                // run tests with coverage and export results to yml
                sh 'COVERAGE=on ruby main.rb'
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
            language (string): project language(ruby)
            inclusions (string): file inclusion pattern
            exclusions (string): file exclusion pattern
            login (string): SonarQube server user name
            password (string): SonarQube server user password
             */
            steps {
              writeFile file: "${pwd()}/sonar-project.properties", text: """
              sonar.projectKey=test-files_1_0_ruby_coverage
              sonar.projectName=Ruby Testfiles
              sonar.projectVersion=1.0
              sonar.sources=${pwd()}/examples/ruby-test-repo
              sonar.projectBaseDir=${pwd()}/examples/ruby-test-repo
              sonar.language=ruby
              sonar.inclusions=**/*.rb
              sonar.exclusions=tests/**/*.rb
              sonar.login=test
              sonar.password=test
              sonar.ws.timeout=180
              """

              // initite pre-configured sonar scanner tool on project
              // 'sonarqube_prod' is our cnfigured tool name, see yours
              // in the Jenkins tool configuration
              withSonarQubeEnv('sonarqube_prod') {
                sh "${tool 'sonar-scanner-2.8'}/bin/sonar-scanner"

              }
            }
        }
    }
}
