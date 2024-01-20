def call() {
   properties([
        parameters([
            choice(choices: 'dev\nprod', description: 'Select the environment', name: "ENV"),
        ]),
    ])
    node {
        ansiColor('xterm') {
            // git branch: 'main', url: 'https://github.com/b56-clouddevops/terraform-vpc.git'
            stage('Creatingggg Network') {
                git branch: 'main', url: 'https://github.com/b56-clouddevops/terraform-vpc.git'
                sh '''
                       terrafile -f env-${ENV}/Terrafile
                       terraform init --backend-config=env-${ENV}/${ENV}-backend.tfvars  -reconfigure
                       terraform plan -var-file=env-${ENV}/${ENV}.tfvars
                       terraform apply -var-file=env-${ENV}/${ENV}.tfvars -auto-approve
                '''             
            }
            stage('Creating LoadBalancerts') {
                git branch: 'main', url: 'https://github.com/b56-clouddevops/terraform-loadbalancerts.git'
                sh '''
                       terrafile -f env-${ENV}/Terrafile
                       terraform init --backend-config=env-${ENV}/${ENV}-backend.tfvars  -reconfigure
                       terraform plan -var-file=env-${ENV}/${ENV}.tfvars
                       terraform apply -var-file=env-${ENV}/${ENV}.tfvars -auto-approve
                '''             
            }
            stage('Creating Databases') {
                git branch: 'main', url: 'https://github.com/b56-clouddevops/terraform-databases.git'
                sh '''
                       terrafile -f env-${ENV}/Terrafile
                       terraform init --backend-config=env-${ENV}/${ENV}-backend.tfvars  -reconfigure
                       terraform plan -var-file=env-${ENV}/${ENV}.tfvars
                       terraform apply -var-file=env-${ENV}/${ENV}.tfvars -auto-approve
                '''             
            }
        }
    }
}


// pipeline {
//     agent any 
//     options {
//         ansiColor('xterm')
//     }
//     parameters {
//         choice(name: 'ENV', choices: ['dev', 'prod'], description: 'Select the environment')
//         choice(name: 'ACTION', choices: ['apply', 'destroy'], description: 'Select apply or destroy')
//     }
//     stages {
//         stage('Terraform Init') {
//             steps {
//                 sh "terrafile -f env-dev/Terrafile"
//                 sh "terraform init -backend-config=env-${ENV}/${ENV}-backend.tfvars"
//             }
//         }
//         stage('Terraform Plan') {
//             steps {
//                 sh "terraform plan -var-file=env-${ENV}/${ENV}.tfvars"
//             }
//         }
//         stage('Terraform Apply') {
//             steps {
//                 sh "terraform ${ACTION} -auto-approve -var-file=env-${ENV}/${ENV}.tfvars"
//             }
//         }
//     }
// }