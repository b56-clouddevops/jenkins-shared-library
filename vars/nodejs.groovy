def lintChecks() {
    sh "echo ***** Starting Style Checks ${COMPONENT} *****"
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
                        // lintChecks()
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

