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
            steps {
              // initite pre-configured sonar scanner tool on project
              // 'sonarqube_prod' is our cnfigured tool name, see yours
              // in the Jenkins tool configuration
              // NOTE: pay attention we are using maven under sonar env
              withSonarQubeEnv('sonarqube_prod') {
                dir('examples/java-test-repo'){                                 
                sh "mvn sonar:sonar"                                            
                }    
              }
            }
        }
    }
}
