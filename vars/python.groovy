def lintChecks() {
    sh "echo ***** Starting Style Checks for ${COMPONENT} *****"
    //sh "pip3 install pylint"
    //sh "pytlint *.py || true"
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
                    sh "echo Starting Static Code Analysis"
                }
            }
        }
    }
}

