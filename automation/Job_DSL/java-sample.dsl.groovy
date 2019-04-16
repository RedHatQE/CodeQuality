def jobName = 'java-coverage-dsl-sample'
def giturl = 'https://github.com/RedHatQE/CodeQuality.git'
def sonarProperties = '''
    sonar.projectKey=sonarqube_java_analysis
    sonar.projectName=Java Analysis
    sonar.projectVersion=1.0
    sonar.sources=${WORKSPACE}/examples/java-test-repo
    sonar.projectBaseDir=${WORKSPACE}/examples/java-test-repo
    sonar.language=java
    sonar.inclusions=**/*.java
    sonar.exclusions=tests/**/*.java
    sonar.login=test
    sonar.password=test
    sonar.ws.timeout=180
    sonar.java.binaries=target/maven-test-project-1.0.jar
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
           cd examples/java-test-repo
           mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=true || true
        '''
    }
    configure {
        it / 'builders' << 'hudson.plugins.sonar.SonarRunnerBuilder' {
            properties ("$sonarProperties")
    }
  }
}
