pipeline {
    agent any

    stages {
        stage('Check') {
            steps {
                git branch: 'develop',credentialsId:'git-hook-PAT', url:'https://github.com/Spharos-final-project-WOOYANO/Gateway'
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
                    docker stop eureka-gateway || true
                    docker rm eureka-gateway || true
                    docker rmi eureka-gateway-img || true
                    docker build -t eureka-gateway-img:latest .
                '''
            }
        }
        stage('Deploy'){
            steps{
                sh 'docker run -d --name eureka-gateway -p 8080:8000 eureka-gateway-img'
            }
        }
    }
}
