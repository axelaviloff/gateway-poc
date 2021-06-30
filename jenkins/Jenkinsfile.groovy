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
                    testAndGetKey()

                }
            }
        }
        
        stage("Print Keys") {
            steps {
                script {
                    println "AES = ${NEW_AES}"
                    println "IV = ${NEW_IV}"

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

def testAndGetKey() {
    def out = sh(script: "${gradle} test --tests com.axel.gatewaypoc.utils.GenerateDefaultPairKeysTest | grep -E 'AES|IV'", returnStdout: true)
    out = out.trim().replaceAll("\\s+", " ").split(" ")
    println "SAÍDA TESTE ${out3}"
    NEW_AES = out[1]
    NEW_IV = out[3]
}
