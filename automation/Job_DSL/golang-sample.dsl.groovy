def jobName = 'golang-coverage-dsl-sample'
def giturl = 'https://github.com/RedHatQE/CodeQuality.git'
def sonarProperties = '''
    sonar.projectKey=sonarqube_golang_analysis
    sonar.projectName=Testfiles Golang Analysis
    sonar.projectVersion=1.0
    sonar.sources=${WORKSPACE}/gocode/src/github.com/examples/golang-test-repo/
    sonar.projectBaseDir=${WORKSPACE}/gocode/src/github.com/examples/golang-test-repo/
    sonar.go.coverage.reportPaths=cover.out
    sonar.language=go
    sonar.inclusions=**/size.go
    sonar.exclusions=**/*_test.go
    sonar.login=test
    sonar.password=test
    sonar.ws.timeout=180
       '''.stripIndent()

job(jobName) {
    label('sonarqube-upshift')
    scm {
       git {
            remote {
                url giturl
            }
            extensions {
                relativeTargetDirectory('${WORKSPACE}/gocode/src/github.com/')
            }
        }
    }
    triggers {
        cron '0 8 * * *'
    }
    steps {
        shell '''
            # Generating Coverage report
            cd ${WORKSPACE}/gocode/src/github.com/examples/golang-test-repo/
            export GOPATH=${WORKSPACE}/gocode/
            export PATH="${WORKSPACE}/gocode/bin:$PATH"
            go test -coverprofile=cover.out

            # Download tool and convert report to XML file
            go get github.com/axw/gocov/gocov
            go get github.com/AlekSi/gocov-xml
            gocov convert cover.out | gocov-xml > coverage.xml
        '''
    }
    configure {
        it / 'builders' << 'hudson.plugins.sonar.SonarRunnerBuilder' {
            properties ("$sonarProperties")
        }
    }
    publishers {
        cobertura('**/gocode/src/github.com/examples/golang-test-repo/coverage.xml')
    }
}
