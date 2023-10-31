pipeline {
    agent any
    stages {
        stage('Check') {
            steps {
                git branch: 'develop',credentialsId:'0-shingo', url:'https://github.com/Spharos-final-project-WOOYANO/Gateway'
            }
        }
        stage('Build'){
            steps{
                script {
                    sh '''
                        pwd
                        chmod +x ./gradlew
                        ./gradlew build
                    '''
                    
                }
                    
            }
        }
        stage('DockerSize'){
            steps {
                sh '''
                    docker stop gatway || true
                    docker rm gateway || true
                    docker rmi gateway-img || true
                    docker build -t gateway-img:latest .
                '''
            }
        }
        stage('Deploy'){
            steps{
                sh 'docker run -d --name gateway -p 8761:8000 gateway-img'
            }
        }
    }
}

