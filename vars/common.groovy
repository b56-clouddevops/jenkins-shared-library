def sonarChecks() {
     stage('Sonar Checks') {
          sh '''
          echo sonar Checks in progress
          # sonar-scanner -Dsonar.host.url=http://172.31.45.101:9000 ${ARGS} -Dsonar.projectKey=${COMPONENT}  -Dsonar.login=${SONAR_CRED_USR} -Dsonar.password=${SONAR_CRED_PSW}
          echo sonar Checks Completed
          '''
     }
}

def lintChecks() {
     stage('lint checks') {
          if(env.APP_TYPE == "maven") {
               sh '''
                    echo ***** Starting Style Checks for ${COMPONENT} *****
                    mvn checkstyle:check || true
                    echo ***** Style Checks Are Completed for ${COMPONENT} *****
               '''
          }
          else if(env.APP_TYPE == "node") {
               sh '''
                    echo ***** Starting Style Checks for ${COMPONENT} *****
                    npm install jslint
                    node_modules/jslint/bin/jslint.js server.js || true
                    echo ***** Style Checks Are Completed for ${COMPONENT} *****
               '''
          }
          else if(env.APP_TYPE == "python") {
               sh '''
                    echo ***** Starting Style Checks for ${COMPONENT} *****
                    pip3 install pylint
                    pytlint *.py || true
                    echo ***** Style Checks Are Completed for ${COMPONENT} *****
               '''
          }
          else {
               sh '''
                    echo ***** Starting Style Checks for ${COMPONENT} *****
                    echo ***** Style Checks Are Completed for ${COMPONENT} *****
               '''
          }
     }
}


def testCases() {
    stage('Test Cases') {
        def stages = [:]

        stages["Unit Testing"] = {
               echo "Unit Testing In Progress"
               echo "Unit Testing Is Completed"
        }
        stages["Intergration Testing"] = {
               echo "Intergration Testing In Progress"
               echo "Intergration Testing Is Completed"
        }
        stages["Functional Testing"] = {
               echo "Functional Testing In Progress"
               echo "Functional Testing Is Completed"
        }
        parallel(stages)
    }
}