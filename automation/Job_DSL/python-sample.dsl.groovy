def jobName = 'python-coverage-dsl-sample'
def giturl = 'https://github.com/RedHatQE/CodeQuality.git'
def sonarProperties = '''
    sonar.projectKey=sonarqube_python_analysis
    sonar.projectName=Python Analysis
    sonar.projectVersion=1.0
    sonar.sources=${WORKSPACE}/examples/python-test-repo
    sonar.projectBaseDir=${WORKSPACE}/examples/python-test-repo
    sonar.python.coverage.reportPath=coverage.xml
    sonar.language=py
    sonar.inclusions=**/*.py
    sonar.exclusions=tests/**/*.py
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
           cd examples/python-test-repo
           coverage run --source . main.py
           coverage xml
        '''
    }
    configure {
        it / 'builders' << 'hudson.plugins.sonar.SonarRunnerBuilder' {
            properties ("$sonarProperties")
    }
  }
}
