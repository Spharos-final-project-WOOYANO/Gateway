pipeline {
    agent any

    stages {
        stage('Check') {
            steps {
                git branch: 'develop',credentialsId:'jenkins-github-access-token', url:'https://github.com/Spharos-final-project-WOOYANO/Gateway'
            }
        }
        stage('Build'){
            steps{
                sh '''
                    pwd
                    chmod +x ./gradlew
                    ./gradlew build -x test
                '''
            }
        }
        stage('DockerSize'){
            steps {
                sh '''
                    docker stop Eureka-Gateway || true
                    docker rm Eureka-Gateway || true
                    docker rmi Eureka-Gateway-Img || true
                    docker build -t Eureka-Gateway-Img:latest .
                '''
            }
        }
        stage('Deploy'){
            steps{
                sh 'docker run -d --name Eureka-Gateway -p 8080:8000 Eureka-Gateway-Img'
            }
        }
    }
}
