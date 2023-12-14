def lintChecks() {
    sh "echo ***** Starting Style Checks for ${COMPONENT} *****"
    //sh "pip3 install pylint"
    //sh "pytlint *.py || true"
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
                    script {
                        env.ARGS="-Dsonar.sources=."
                        sonarChecks()
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

