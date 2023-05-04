# gateway-poc

Ao rodar a pipe do jenkins em`./gateway-poc/jenkins`as chaves geradas nos testes são adicionadas como parâmetros de build e o job`/dev/Config-Repo-Key`é chamado. Esse job roda uma outra pipe atualiza os valores das chaves no **gateway.yml** do projeto [config-repo-poc](https://github.com/axelaviloff/config-repo-poc)
