
# Projeto desafio de testes de API SoftDesign

## Descrição

Projeto de automação de testes Web e API 

## Tecnologias

- Java 21
- Maven
- JUnit
- RestAssured

## Estrutura do projeto

```
src
│main
| ├─java
| | ├─org.softdesign
| | | ├─config
| | | | ├─ConfigReader.java
| | | ├─model
| | | | ├─login
| | | | | ├─LoginResquest.java
| | | | ├─product
| | | | | ├─ProductResquest.java
| | | | | ├─ProductResponse.java
| | | | ├─user
| | | | | ├─UserResponse.java
| ├─resources
| | ├─config.properties
│test
│  ├─java
│  │  ├─org.softdesign.controller
│  │  │  │  ├─AuthControllerTest.java
│  │  │  │  |─ProductControllerTest.java
│  │  │  │  |─UserControllerTest.java
pom.xml
README.md
```

## Execução

O projeto pode ser executado de 2 formas:

- Apenas para visualização rápida via console 

```
mvn test
```
- Para execução e geração de relatório visual para consulta

```
mvn site
```

## Relatório

Os relatórios de execução dos testes é gerado na pasta `target/surefire-reports`
- Informações com resultados de sucesso e erro: .TXT
- Informações mais detalhadas de execução com trechos de código: .XML

Caso tenha sito executado o comando *mvn site*, o relatório visual gerado estará no diretório `target/site`  
Ao acessar o diretório, procure o arquivo *surefire-report.html* e execute no browser de sua preferência

## Observações
Foram encontrados bugs nos testes. Como esse repositório é somente para estudo, vou listar os problemas encontrados aqui:
- Classe AuthControllerTest.testLogin
  - No teste de Login com suecesso, espera retornar status code 200, porém, retorna 201.
- Classe ProductControllerTest.testCreateProduct 
  - Ao crear o produto, se espera que o id do response retorne 101, porém, retorna 195.
- Classe UserControllerTest.testGetUserById 
  -  Ao validar os dados de responde do último teste, se espera que o image do response retorne '...', porém, retorna 'https://dummyjson.com/icon/emilys/128'.

## Autor

- Davi Rodrigues Gonçalves
