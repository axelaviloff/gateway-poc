pipeline {
    agent {
        label 'java11'
    }
    tools {
        jdk 'java11'
    }
    environment {
        gradle = "./gradlew"
        NEW_AES = ""
        NEW_IV = ""
    }
    
    stages {
    	stage("Build") {
    	     steps {
    	     	script {
    	     	    sh "${gradle} build"
    	     	}
    	     }
    	
    	}
    
        stage("Generate Keys") {
            steps {
                script {
                    NEW_AES = testAndGetKey("AES")
                    NEW_IV = testAndGetKey("IV")

                }
            }
        }
        stage("Call config-repo-poc job") {
            steps {
                script {
                    def parameters = [
                        string(name: "AES", value: NEW_AES),
                        string(name: "IV", value: NEW_IV)
                    ];
                    build(job: "CUSTOMER_SUCCESS/Rivendell/dev/Config-Repo-Key", parameters: parameters)
                }
            }
        }
    }
    post {
        success {
            echo "sucesso!"
        }
        failure {
            echo "falhou!"
        }
    }
}

def testAndGetKey(String keyType) {
    def out = sh(script: "${gradle} test --tests com.axel.gatewaypoc.utils.GenerateDefaultPairKeysTest | grep '${keyType}' ", returnStdout: true)
    def generatedKey = out.trim().split(" ")[1]
    println "${keyType} = ${generatedKey}"
    return generatedKey
}
