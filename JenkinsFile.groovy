pipeline {
    agent any
    stages {
        stage ('Checkout')
        {
            steps {
                git branch: 'main', url: 'https://github.com/DhirajSJL/JenkinsFrontEndApp.git'
            }
        }
        stage ('Deploy')
        {
            steps{
                sshagent(['RemoteServer']) {
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@23.23.8.104 "sudo rm -r /var/www/html/index.*"'
                    sh 'scp -o StrictHostKeyChecking=no -r * ubuntu@23.23.8.104:/home/ubuntu/index.html'       
                    }
            }
        }
    }
}