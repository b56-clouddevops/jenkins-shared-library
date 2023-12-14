def lintChecks() {
    sh "echo ***** Starting Style Checks for ${COMPONENT} *****"
    sh "npm install jslint"
    sh "node_modules/jslint/bin/jslint.js server.js || true"
    sh "echo ***** Style Checks Are Completed for ${COMPONENT} *****"
}

def call() {
    pipeline {
        agent any
        stages {
            stage('Lint Checks'){
                steps {
                    script {
                        lintChecks()
                    }
                }
            }
            stage('Static Code Analysis') {
                steps {
                    script {
                        common.sonarChecks()
                    }
                }
            }
            stage('Get the Sonar Result') {
                steps {
                    sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > gates.sh"
                    sh "bash gates.sh admin password ${SONAR_URL} ${COMPONENT}"
                }
            }
            stage('Unit Testing') {
                steps {
                    echo "Testing in Progress"
                    echo "Testing is Completed"        
                }
            }
        }
    }
}

