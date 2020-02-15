# Ne cassez plus vos consommateurs grâce au contract testing

Livrer un (micro) service pour s'apercevoir que l’on a cassé l’environnement, c’est agaçant.

Dans des architectures de plus en plus orientées (micro) services, et les S.I. complexes, vérifier qu’une nouvelle version de votre service continuera à pouvoir communiquer avec les autres devient de plus en plus important. 

Les tests de contrats proposent une solution pour vérifier les interactions lors de la phase de test. 
Dans cet atelier, nous présenterons comment mettre en place le test de contrat, en particulier guidé par le consommateur de l’API, au sein de votre architecture entre le front, vos backends aussi bien par appel HTTP ou messages. 

Nous utiliserons Spring Cloud Contract, très bien intégré aux applications Spring Boot, ainsi que Pact ayant l’avantage d’être polyglotte. 

## Todo

- Définir les ojectifs de l'atelier
- Définir le scénario de l'atelier
- Découper le scénario en étapes
- Coder les différentes étapes
- Créer un repository git avec une branche par étape
- Faire des slides pour présenter le Contract testing et l'atelier
 

 ## Lab

 Contract DSL
 https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl

 - Add maven plugin  
 - Add first contract like GET /v1/books
 - Add base contract class to make the tests pass
 - run _spring-cloud-contract:generateTests_ and look at _target/generated-test-sources_/.../ContractVerifierTest.java
 - You can run them directly as a typical java test class
 - Adding [static contract with groovy DSL](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl)
 - You can test only the relevant fields
 - Using [dynamic and regex properties](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl-dynamic-properties)
 - understand the _$(consumer/stub/client(...), producer/test/server(...))_
 - You can also [reference the parameters from the request](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl-referencing-request-from-response)
