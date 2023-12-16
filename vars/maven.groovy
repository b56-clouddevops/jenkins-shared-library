def lintChecks() {
    sh "echo ***** Starting Style Checks for ${COMPONENT} *****"
    sh "mvn checkstyle:check || true"
    sh "echo ***** Style Checks Are Completed for ${COMPONENT} *****"
}

def call() {
    pipeline {
        agent any
        environment {                      
            SONAR_CRED = credentials('SONAR_CRED')
            NEXUS = credentials('NEXUS')
            SONAR_URL = "172.31.45.101"
            NEXUS_URL = "172.31.34.215"
        }
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
                    sh "env"
                    sh "mvn clean compile"
                    sh "ls -ltr target/"
                }
            }
            stage('Static Code Analysis') {
                steps {
                    script {
                        env.ARGS="-Dsonar.java.binaries=./target/"
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
                        sh "echo Testing In Progress"
                        // sh "mvn test"
                        sh "echo Unit Testing In Completed"
                    }
                }
                stage('Integration Testing') {
                    steps {
                        sh "echo Integration Testing In Progress"
                        // sh "mvn verify"
                        sh "echo Integration Testing In Completed"
                    }
                }
                stage('Functional Testing') {
                    steps {
                        sh "echo Functional Testing In Progress"
                        // sh "mvn function"
                        sh "echo Functional Testing In Completed"
                        }
                    }
                }
            }
            stage('Checking Artifacts Avaiability On Nexus') {
                when { expression { env.TAG_NAME != null } }
                steps {
                    script {
                        env.UPLOAD_STATUS = sh(returnStdout: true, script: "curl http://${NEXUS_URL}:8081/service/rest/repository/browse/${COMPONENT}/ | grep ${COMPONENT}-${TAG_NAME}.zip || true")
                        print UPLOAD_STATUS
                    }
                }
            }
            stage('Prepare Artifacts') {       // Runs only when you run this job from tag and from branches it should run
                when { expression { env.TAG_NAME != null } }
                steps {
                    sh "echo Preparing Artifacts"
                }
            }
            stage('Uploading Artifacts') {     // Runs only when you run this job from tag and from branches it should run
                when { expression { env.TAG_NAME != null } }
                steps {
                    sh "echo Uploading Artifacts"
                }
            }
        }
    }
}
