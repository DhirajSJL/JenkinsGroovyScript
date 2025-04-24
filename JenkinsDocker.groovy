pipeline {
    agent any
        tools {
            maven 'Maven_3.9.8_for_Java'
    }
    stages {
            stage ('Checkout')
            {
                steps {
                    git branch: 'main', url: 'https://github.com/DhirajSJL/Docker_Stuff.git'
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
                    sshagent(['JenkinsDocker'])
                    {
                    sh 'scp -o StrictHostKeyChecking=no target/*.jar ubuntu@13.234.119.31:/home/ubuntu/Java_code'
                    sh 'scp -o StrictHostKeyChecking=no Dockerfile ubuntu@13.234.119.31:/home/ubuntu/Java_code'
                    //sh 'ssh -o StrictHostKeyChecking=no ubuntu@13.201.131.117 "sudo mv /home/ubuntu/*.war /opt/tomcat/webapps/Java_app.war"'
                    //sh 'ssh -o StrictHostKeyChecking=no ubuntu@13.201.131.117 "sudo systemctl restart tomcat"'
                    }
                }
            }
}
}