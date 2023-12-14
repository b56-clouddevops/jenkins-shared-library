def lintChecks() {
    sh "echo ***** Starting Style Checks for ${COMPONENT} *****"
    sh "mvn checkstyle:check || true"
    sh "echo ***** Style Checks Are Completed for ${COMPONENT} *****"
}

def call() {
    pipeline {
        agent any
        tools {
            maven 'maven-396' 
        }
        stages {
            stage('Lint Checks'){
                steps {
                    script {
                        lintChecks()
                    }
                }
            }
            stage('Compiling Java Code ') {
                steps {
                    sh "mvn clean compile"
                    sh "ls -ltr target/"
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
        }
    }
}



