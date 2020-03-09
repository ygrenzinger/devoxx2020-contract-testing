# Ne cassez plus vos consommateurs grâce au contract testing

Livrer un (micro) service pour s'apercevoir que l’on a cassé l’environnement, c’est agaçant.

Dans des architectures de plus en plus orientées (micro) services, et les S.I. complexes, vérifier qu’une nouvelle version de votre service continuera à pouvoir communiquer avec les autres devient de plus en plus important. 

Les tests de contrats proposent une solution pour vérifier les interactions lors de la phase de test. 
Dans cet atelier, nous présenterons comment mettre en place le test de contrat, en particulier guidé par le consommateur de l’API, au sein de votre architecture entre le front, vos backends aussi bien par appel HTTP ou messages. 

Nous utiliserons Spring Cloud Contract, très bien intégré aux applications Spring Boot, ainsi que Pact ayant l’avantage d’être polyglotte. 


 ## Lab

 ### Part 1

You can read a bit the [introduction to Spring Cloud Contract](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/getting-started.html#getting-started-introducing-spring-cloud-contract)

In inventory project, the goal is to learn how to test APIs and the groovy DSL. 
- Go to Maven pom.xml and uncomment the Spring Cloud Contract test dependancy and plugin [doc here](https://cloud.spring.io/spring-cloud-contract/spring-cloud-contract-maven-plugin/) 
- A base testing class is mandatory and configure the parent class of generated test classes. It handles the context needed for the tests. You have to configure the location of base contract class available here `com.devoxx.inventory.contracts.ContractsBase` by configuring the maven plugin with line `<baseClassForTests>` (see here)[https://cloud.spring.io/spring-cloud-contract/spring-cloud-contract-maven-plugin/junit.html].
- run `mvn clean test` to see the plugin generate tests in this class `_target/generated-test-sources_/.../ContractVerifierTest.java`. You can run it as a typical java test class.
- The contract `shoudRetrieveAllBooks` should pass now. To better understand, the [Groovy DSL documentation is here](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl)
- Uncomment the test `shoudRetrieveBook`. You need to generate again the contract tests. to go fast, use the `spring-cloud-contract:generateTests` goal of plugin directly. It fails due to wrong url in the controller. You can easily correct it. You can also see the use of [dynamic and regex properties](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl-dynamic-properties)
-  **important** : understand the _$(consumer/stub/client(...), producer/test/server(...))_
- _info_ : You can test only the relevant fields in the response.
- _info_ : to directly generate the contract tests, use the `spring-cloud-contract:generateTests` goal of plugin directly to avoid running full maven build.
- _info_ : For very complex use case, you can also [reference the parameters from the request](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl-referencing-request-from-response)
- Uncomment the test `shoudCreateBookIntoInventory` and make it test the create POST endpoint. The problem here is the random UUID. You can either control the UUID generator in the test or use matchers.
  - We want to verify that we receive a POST on /v1/books with as body a JSON Book containing :
       - a name field which is a string, e.g: "Clean code"
       - a price field which is a number, e.g: 49.99
       - and a stock field which is an integer, e.g. 45
  - then we expect the response to have the status OK (Http 200) with as a body a JSON Book containing :
        - an id filed which is an UUID, e.g : "48bd1c8a-4fff-4db7-8967-9a59791415bd"
        - a name field which is a string, e.g: "Clean code"
        - a price field which is a number, e.g: 49.99
        - and a stock field which is an integer, e.g. 45
- Create the contract `shoudReduceStockInInventory` and make it test the reduce stock POST endpoint. You also have to uncomment it to show some ATDD practices :)
  - We want to verify that we receive a POST on /v1/books/d4d37e73-77a0-4616-8bd2-5ed983d45d14/stock/reduce/2 aka reducing by 2 the number of Java books
  - then we expect the response to have the status OK (Http 200) with as a body a JSON Book containing a stock field which is an integer, e.g. 92
- You can run and tests your stub by using `generateStubs` goal
- Do mvn install on inventory project for making stub accessible for consumers


In checkout project, the goal are to learn how to use the contract test as stubs (with WireMock) in the consumer and to create a message contract.
- Make `CheckoutApplicationTests.java` tests pass. It's an integration test for the inventory service call to retrieve a book and the the checkout controller.
- At the top of the class, begin to add [AutoConfigureStubRunner annotation](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#features-stub-runner-retrieving) with ids of the producer you consume (here inventory). `ids` looks like this : `groupId:artefactId:+:stubs:portNumber`. You need to also configure `stubsMode = StubRunnerProperties.StubsMode.LOCAL`
- You have an error when consuming the WireMock stub. To fix it, you have have to correctly configure restTemplate. Luckily for you, it's just uncommenting the configuration lines for Accept and Content-Type in `CheckoutApplication.java`
- You can also remove the @Disabled on test `should_checkout_order`
- Implement the `shouldSendOrderToDelivery` a contract for a message which is created in method [see Output Triggered by a Method](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl-output-triggered-method)
    - Uncomment the MessageVerifier injection
    - Implement `sendOrder` function to send a message to delivery service (you can use the delivery interface)
    - We want to verify that if you trigger `sendOrder` you will have the following output message JSON body :
       - the bookId field which is the UUID, e.g: "d4d37e73-77a0-4616-8bd2-5ed983d45d14"
       - a quantity field which is a number, e.g: 2
       - a clientId field which is a string, e.g: "yannick"
       - and createdAt field the current date time - You can use dependency injection or matchers ;)
- mvn install for making stub accessible for consumer


In delivery project:
 - Make test pass in `DeliveryApplicationTests.java` in delivery service to [trigger stub message](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#features-messaging-consumer)


### Part 2

Init a local distant repo:
- git init --bare test-repo.git
- git clone test-repo.git/ test-clone
- touch README.md
- git add .
- git commit -m "add README"
- git push origin master

Run the spring-cloud-contract convert and generateStubs then
Add maven configuration to push to repo
            <plugin>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-contract-maven-plugin</artifactId>
                <version>2.2.1.RELEASE</version>
                <extensions>true</extensions>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <!-- By default we will not push the stubs back to SCM,
                            you have to explicitly add it as a goal -->
                            <goal>pushStubsToScm</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <contractsMode>LOCAL</contractsMode>
                    <contractsRepositoryUrl>git://file:///Users/ygrenzinger/git/contract.git</contractsRepositoryUrl>
                    <contractDependency>
                        <groupId>${project.groupId}</groupId>
                        <artifactId>${project.artifactId}</artifactId>
                        <version>${project.version}</version>
                    </contractDependency>
                    <testFramework>JUNIT5</testFramework>
                    <baseClassForTests>com.devoxx.checkout.contracts.ContractsBase</baseClassForTests>
                </configuration>
            </plugin>
Run the spring-cloud-contract:pushStubsToScm in maven inside the inventory project
Use stubs mode remote and add repository root


## Bonus

In checkout project:
- Implement the `shouldCheckoutOrder` contract. Don't forget to add the baseClass and add it in the plugin configuration (same directory as in inventory if you want). You can also use the WireMock stub from inventory contract testing.
  - We want to verify that we receive a POST on /v1/checkouts with as body a JSON Order containing :
       - the bookId field which is the UUID, e.g: "d4d37e73-77a0-4616-8bd2-5ed983d45d14"
       - a quantity field which is a number, e.g: 2
       - and a clientId field which is a string, e.g: "yannick"
  - then we expect the response to have the status OK (Http 200) with as a body a JSON ValidatedOrder containing :
       - the same fields as input but, more importantly, a createdAt field with the current date time. You can use dependency injection or matchers ;) 
- _bonus_ : You can add configuration in yml for stub runner to avoid duplication in the annotations


https://github.com/spring-cloud-samples/spring-cloud-contract-samples/blob/master/producer_with_git/pom.xml#L98
https://cloud-samples.spring.io/spring-cloud-contract-samples/workshops.html#_setup


 
