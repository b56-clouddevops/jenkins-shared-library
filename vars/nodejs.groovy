def lintChecks() {
    sh "echo ***** Starting Style Checks for ${COMPONENT} *****"
    sh "npm install jslint"
    sh "node_modules/jslint/bin/jslint.js server.js || true"
    sh "echo ***** Style Checks Are Completed for ${COMPONENT} *****"
}

def sonarChecks() {
     sh '''
     sonar-scanner -Dsonar.host.url=http://172.31.45.101:9000 -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=admin -Dsonar.password=password

     '''
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

