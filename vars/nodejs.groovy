def lintChecks('component') {
    sh "echo ***** Starting Style Checks for ${component} *****"
    sh "npm install jslint"
    sh "node_modules/jslint/bin/jslint.js server.js || true"
    sh "echo ***** Style Checks Are Completed *****"
}

def call('component') {
    pipeline {
        agent any
        stages {
            stage('Lint Checks'){
                steps {
                    script {
                        lintChecks('component')
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

