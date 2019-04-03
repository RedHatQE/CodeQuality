def jobName = 'cpp-coverage-dsl-sample'
def giturl = 'https://github.com/RedHatQE/CodeQuality.git'
def sonarProperties = '''
    sonar.projectKey=test-files_1_0_cpp_full-analysis
    sonar.projectName=CPP Testfiles
    sonar.projectVersion=1.0
    sonar.sources=${WORKSPACE}/examples/cpp-test-repo
    sonar.projectBaseDir=${WORKSPACE}/examples/cpp-test-repo
    sonar.language=c++
    sonar.inclusions=**/*.cpp
    sonar.cxx.coverage.reportPath=${WORKSPACE}/examples/cpp-test-repo/report.xml
    sonar.login=test
    sonar.password=test
    sonar.ws.timeout=180
       '''.stripIndent()

job(jobName) {
    label('sonarqube-upshift')
    scm {
        git(giturl)
    }
    triggers {
        cron '0 8 * * *'
    }
    steps {
        shell '''
            # compile test files with coverage and mapping flags
            cd examples/cpp-test-repo
            g++ -g --coverage -lcppunit *.cpp -o testcpp

            # generate runtime coverage metrics report
            ./testcpp
            gcov -o $(pwd) -f $(pwd)/main.cpp

            # aggregate generated reports to xml report
            gcovr -r $(pwd) -x --object-directory=$(pwd) > report.xml
        '''
    }
    configure {
        it / 'builders' << 'hudson.plugins.sonar.SonarRunnerBuilder' {
            properties ("$sonarProperties")
    }
  }
}
