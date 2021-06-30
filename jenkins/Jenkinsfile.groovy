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
    
        stage("Generate Keys") {
            steps {
                script {
                    testAndGetKeys()

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

def testAndGetKeys() {
    def out = sh(script: "${gradle} test --tests com.axel.gatewaypoc.utils.GenerateDefaultPairKeysTest | grep -E 'AES|IV'", returnStdout: true)
    out = out.trim().replaceAll("\\s+", " ").split(" ")
    println "KEYS = ${out}"
    NEW_AES = out[1]
    NEW_IV = out[3]
}
