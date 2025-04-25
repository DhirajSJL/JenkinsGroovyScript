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
                    sh 'scp -o StrictHostKeyChecking=no target/*.jar ubuntu@13.234.119.31:/home/ubuntu/Java_code'
                    sh 'scp -o StrictHostKeyChecking=no Dockerfile ubuntu@13.234.119.31:/home/ubuntu/Java_code'
                    sh 'scp -o StrictHostKeyChecking=no docker-compose.yml ubuntu@13.234.119.31:/home/ubuntu/Java_code'

                    // Execute docker-compose commands on the remote server
                    sh '''
                        ssh -o StrictHostKeyChecking=no ubuntu@13.234.119.31 '
                            cd /home/ubuntu/Java_code &&
                            docker-compose down || true &&
                            docker-compose up -d --build
                        '
                    '''
                }
            }
        }
    }
}
