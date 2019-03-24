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
                dir('examples/cpp-test-repo') {
                // compile test files with coverage and mapping flags
                sh 'g++ -g --coverage -lcppunit *.cpp -o testcpp'
                
                // generate runtime coverage metrics report
                sh './testcpp'
                sh 'gcov -o $(pwd) -f $(pwd)/main.cpp'

                // aggregate generated reports to xml report
                sh 'gcovr -r $(pwd) -x --object-directory=$(pwd) > report.xml'
                }
            }
        }
        stage('Report') {
            steps {

              /*
              sonar runner parameters, set sources and baseDir to project home
              =======================
              projectKey (string): SonarQube project identification key (unique)
              projectName (string): SonarQube project name (NOT unique)
              projectVersion (string): SonarQube project version (unique)
              sources (string): source code home directory
              projectBaseDir (string): project home directory (same as sources)
              language (string): project language(cpp)
              inclusions (string): file inclusion pattern
              cxx.coverage.reportPath (string): xml coverage report path
              login (string): SonarQube server user name
              password (string): SonarQube server user password
               */
              writeFile file: "${pwd()}/sonar-project.properties", text: """
              sonar.projectKey=test-files_1_0_cpp_full-analysis
              sonar.projectName=CPP Testfiles
              sonar.projectVersion=1.0
              sonar.sources=${pwd()}/examples/cpp-test-repo
              sonar.projectBaseDir=${pwd()}/examples/cpp-test-repo
              sonar.language=c++
              sonar.inclusions=**/*.cpp
              sonar.cxx.coverage.reportPath=${pwd()}/examples/cpp-test-repo/report.xml
              sonar.login=test
              sonar.password=test
              sonar.ws.timeout=180
              """

              // initite sonar scanner tool on project
              // 'sonarqube_prod' is our cnfigured tool name, see yours
              // in the Jenkins tool configuration
              withSonarQubeEnv('sonarqube_prod') {
                sh "${tool 'sonar-scanner-2.8'}/bin/sonar-scanner"

              }
            }
        }
    }
}
