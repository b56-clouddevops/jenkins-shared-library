def call() {
    node {
        git branch: 'main', url: "https://github.com/b56-clouddevops/${COMPONENT}.git"
        common.lintChecks()
        if(env.TAG_NAME != null) {
            stage('Generating & Pushing Artifacts') {
                if(env.APP_TYPE == "nodejs") {
                    sh "echo Generating Artifacts"
                    sh "npm install"
                }    
                else if(env.APP_TYPE == "python") {
                    sh "echo Generating Artifacts For ${COMPONENT}"
                    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip *.py  *.ini requirements.txt"
                } 
                else if(env.APP_TYPE == "maven") {
                    sh "mvn clean package"
                    sh "mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar"
                    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar"
                } 
                else if(env.APP_TYPE == "angularjs") {
                    sh "cd static/"       
                    sh "zip -r ../${COMPONENT}-${TAG_NAME}.zip *"
                } 
                else {
                    sh "Selected Component Doesn't exist"
                    }
                }
                sh "sudo wget https://truststore.pki.rds.amazonaws.com/global/global-bundle.pem"
                sh "sudo docker build -t 355449129696.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:${TAG_NAME} ."
                sh "sudo aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 355449129696.dkr.ecr.us-east-1.amazonaws.com"
                sh "sudo docker push 355449129696.dkr.ecr.us-east-1.amazonaws.com/frontend:${TAG_NAME}"
        }
    }
}
