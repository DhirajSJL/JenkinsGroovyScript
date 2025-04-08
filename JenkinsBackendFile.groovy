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
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@65.1.106.15 "sudo systemctl restart tomcat"'
                    }
                }
            }
    }
        post {
    success {
        emailext body: '''
        <html>
            <body style="font-family: Arial, sans-serif; margin: 20px; line-height: 1.6;">
                <div style="border: 1px solid #ddd; border-radius: 8px; padding: 20px; background-color: #f9f9f9;">
                    <h2 style="color: #4CAF50;">🎉 Java App War File Deployed Successfully!</h2>
                    <p style="font-size: 16px; color: #555;">
                        <strong>Version:</strong> # $BUILD_NUMBER<br/>
                        <strong>Deployment Status:</strong> $BUILD_STATUS
                    </p>
                    <p style="font-size: 16px; color: #555;">
                        The application has been deployed successfully on the following URL:<br/>
                                          </p>
                    <hr style="border: 1px solid #ddd; margin: 20px 0;">
                    <p style="font-size: 14px; color: #888;">
                        <strong>Console Output:</strong><br/>
                        <a href="$BUILD_URL" style="color: #4CAF50; text-decoration: none;">View Build Logs</a>
                    </p>
                    <div style="background-color: #f5f5f5; padding: 10px; border: 1px solid #ddd; margin-top: 20px; border-radius: 8px;">
                        <strong>Last 50 lines of the build log:</strong>
                        <pre style="background: #333; color: #fff; padding: 10px; border-radius: 5px; font-family: monospace; font-size: 13px;">
                        ${BUILD_LOG, maxLines=50, escapeHtml=true}
                        </pre>
                    </div>
                    <footer style="margin-top: 20px; font-size: 12px; color: #999;">
                        -- This is an automated notification from Jenkins --
                    </footer>
                </div>
            </body>
        </html>
        ''',
        subject: '✅ Java App WAR File Deployment Success - $PROJECT_NAME',
        to: 'dhiraj6.ldv@gmail.com, abdallah.kammruddin@dextero.in',
        mimeType: 'text/html'
    }
    failure {
        emailext body: '''
        <html>
            <body style="font-family: Arial, sans-serif; margin: 20px; line-height: 1.6;">
                <div style="border: 1px solid #ddd; border-radius: 8px; padding: 20px; background-color: #fef2f2;">
                    <h2 style="color: #E53935;">🚨 Java App WAR File Deployment Failed!</h2>
                    <p style="font-size: 16px; color: #555;">
                        <strong>Version:</strong> # $BUILD_NUMBER<br/>
                        <strong>Deployment Status:</strong> $BUILD_STATUS
                    </p>
                    <p style="font-size: 16px; color: #555;">
                        Please review the console output to identify and resolve the issue:<br/>
                        <a href="$BUILD_URL" style="color: #E53935; text-decoration: none; font-weight: bold;">
                            View Build Logs
                        </a>
                    </p>
                    <hr style="border: 1px solid #ddd; margin: 20px 0;">
                    <div style="background-color: #f5f5f5; padding: 10px; border: 1px solid #ddd; margin-top: 20px; border-radius: 8px;">
                        <strong>Last 50 lines of the build log:</strong>
                        <pre style="background: #333; color: #fff; padding: 10px; border-radius: 5px; font-family: monospace; font-size: 13px;">
                        ${BUILD_LOG, maxLines=50, escapeHtml=true}
                        </pre>
                    </div>
                    <footer style="margin-top: 20px; font-size: 12px; color: #999;">
                        -- This is an automated notification from Jenkins --
                    </footer>
                </div>
            </body>
        </html>
        ''',
        subject: '❌ Java App WAR File Deployment Failed - $PROJECT_NAME',
        to: 'abdallah.kammruddin@dextero.in, dhiraj6.ldv@gmail.com',
        mimeType: 'text/html'
    }
}
}