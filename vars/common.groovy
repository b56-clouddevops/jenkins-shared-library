def sonarChecks() {
     sh '''
     sonar-scanner -Dsonar.host.url=http://172.31.45.101:9000 ${ARGS} -Dsonar.projectKey=${COMPONENT} -Dsonar.login=admin -Dsonar.password=password
     '''
}