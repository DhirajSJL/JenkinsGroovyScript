pipeline {
    agent any
        tools {
            maven 'Maven_3.9.8_for_java'
    }
    stages {
            stage ('Checkout')
            {
                steps {
                    git branch: 'main', url: 'https://github.com/DhirajSJL/JenkinsBackendApps.git'
                }
            }
            stage ('Build')
            {
                steps{
                    sh 'mvn clean package'
                }
            }
            // stage ('Restart Nginx server')
            // {
            //     steps 
            //     {
            //         sshagent(['RemoteServer'])
            //         {
            //         sh 'ssh -o StrictHostKeyChecking=no ubuntu@23.23.8.104 "sudo systemctl restart nginx"'
            //         }
            //     }
            // }
    }
}