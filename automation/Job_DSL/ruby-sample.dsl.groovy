def jobName = 'ruby-coverage-dsl-sample'
def giturl = 'https://github.com/RedHatQE/CodeQuality.git'
def sonarProperties = '''
    sonar.projectKey=sonarqube_ruby_analysis
    sonar.projectName=Ruby Analysis
    sonar.projectVersion=1.0
    sonar.sources=${WORKSPACE}/examples/ruby-test-repo
    sonar.projectBaseDir=${WORKSPACE}/examples/ruby-test-repo
    sonar.language=ruby
    sonar.inclusions=**/*.rb
    sonar.exclusions=tests/**/*.rb
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
           cd examples/ruby-test-repo
           bundler install
           COVERAGE=on ruby main.rb
        '''
    }
    configure {
        it / 'builders' << 'hudson.plugins.sonar.SonarRunnerBuilder' {
            properties ("$sonarProperties")
    }
  }
}
