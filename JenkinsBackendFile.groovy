pipeline {
    agent any
        tools {
            maven 'Maven_3.9.8_for_java'
    }
    stages {
            stage ('Checkout')
            {
                steps {
                    git branch: 'main', url: 'https://github.com/DhirajSJL/Jenkins_Backend.git'
                }
            }
            stage ('Build')
            {
                steps{
                    sh 'mvn clean package'
                }
            }
            stage ('Deploy')
            {
                steps 
                {
                    sshagent(['JenkinsBackendRemoteServer'])
                    {
                    sh 'scp -o StrictHostKeyChecking=no target/*.war ubuntu@65.1.106.15:/home/ubuntu/'
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@65.1.106.15 "sudo mv /home/ubuntu/*.war /opt/tomcat/webapps/Java_app.war"'
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@65.1.106.15 "sudo systemctl restart nginx"'
                    }
                }
            }
    }
}