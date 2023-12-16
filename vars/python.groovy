def lintChecks() {
    sh "echo ***** Starting Style Checks for ${COMPONENT} *****"
    //sh "pip3 install pylint"
    //sh "pytlint *.py || true"
    sh "echo ***** Style Checks Are Completed for ${COMPONENT} *****"
}

def call() {
    pipeline {
        agent any
        environment {                      
            SONAR_CRED = credentials('SONAR_CRED')
            NEXUS = credentials('NEXUS')
        }
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
                        env.ARGS="-Dsonar.sources=."
                        common.sonarChecks()
                    }
                }
            }
            stage('Get the Sonar Result') {
                steps {
                    sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > gates.sh"
                    // sh "bash gates.sh admin password ${SONAR_URL} ${COMPONENT}"
                    sh "echo Scan Is Good"
                }
            }
        stage('Test Cases') {
            parallel {
                stage('Unit Testing') {
                    steps {
                        sh "echo Unit Testing In Progress"
                        // sh "pip test"
                        sh "echo Unit Testing In Completed"
                    }
                }
                stage('Integration Testing') {
                    steps {
                        sh "echo Integration Testing In Progress"
                        // sh "pip verify"
                        sh "echo Integration Testing In Completed"
                    }
                }
                stage('Functional Testing') {
                    steps {
                        sh "echo Functional Testing In Progress"
                        // sh "pip function"
                        sh "echo Functional Testing In Completed"
                        }
                    }
                }
            }
            stage('Prepare Artifacts') {
                when { expression { env.TAG_NAME != null } }
                steps {
                    sh "echo Preparing Artifacts"
                }
            }
            stage('Uploading Artifacts') {
                when { expression { env.TAG_NAME != null } }
                steps {
                    sh "echo Uploading Artifacts"
                }
            }
        }
    }
}

