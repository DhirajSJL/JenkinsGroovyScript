pipeline {
    agent any

    tools {
        maven 'Maven_3.9.8_for_Java'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/DhirajSJL/Docker_Stuff.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Deploy') {
            steps {
                sshagent(['JenkinsDocker']) {
                    // Transfer files to remote server
                    sh 'scp -o StrictHostKeyChecking=no target/*.jar ubuntu@65.0.86.49:/home/ubuntu/Java_code'
                    sh 'scp -o StrictHostKeyChecking=no Dockerfile ubuntu@65.0.86.49:/home/ubuntu/Java_code'
                    sh 'scp -o StrictHostKeyChecking=no docker-compose.yml ubuntu@65.0.86.49:/home/ubuntu/Java_code'

                    // Execute docker-compose commands on the remote server
                    sh '''
                        ssh -o StrictHostKeyChecking=no ubuntu@65.0.86.49 '
                            cd /home/ubuntu/Java_code &&
                            sudo docker compose down || true &&
                            sudo docker compose up -d --build
                        '
                    '''
                }
            }
        }
    }
}
