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
		    file(credentialsId: 'Gateway-Secret-File', variable: 'gatewaysecret')
		])
	        {
	            sh "cp \$gatewaysecret ./src/main/resources/application-secret.yml"
	    	}
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
        stage('Deploy'){
            steps {
                sh '''
                    docker stop gateway || true
                    docker rm gateway || true
                    docker rmi gateway-img || true
                    docker build -t gateway-img:latest .
		    docker run --network spharos-network -d --name gateway gateway-img
                '''
            }
        }
    }
}

