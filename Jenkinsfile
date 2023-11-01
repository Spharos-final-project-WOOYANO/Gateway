pipeline {
    agent any
    stages {
        stage('Check') {
            steps {
                git branch: 'develop',credentialsId:'0-shingo', url:'https://github.com/Spharos-final-project-WOOYANO/Gateway'
            }
        }
	stage('Gateway-Secret-File Download'){
	    steps{
		withCredentials([
		    file(credentialsId: 'Gateway-Secret-File', variable: 'gatewaySecret')
		])
	        {
	            sh "cp \$gatewaySecret ./src/main/resources/application-secret.yml"
	    	}
	    }
	}
        stage('Build'){
            steps{
                script {
                    sh '''
                        pwd
                        chmod +x ./gradlew
                        ./gradlew build -x test
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
                sh 'docker run --network spharos-network --name gateway -p 8000:8000 gateway-img'

            }
        }
    }
}

