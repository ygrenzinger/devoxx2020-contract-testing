# Ne cassez plus vos consommateurs grâce au contract testing

Livrer un (micro) service pour s'apercevoir que l’on a cassé l’environnement, c’est agaçant.

Dans des architectures de plus en plus orientées (micro) services, et les S.I. complexes, vérifier qu’une nouvelle version de votre service continuera à pouvoir communiquer avec les autres devient de plus en plus important. 

Les tests de contrats proposent une solution pour vérifier les interactions lors de la phase de test. 
Dans cet atelier, nous présenterons comment mettre en place le test de contrat, en particulier guidé par le consommateur de l’API, au sein de votre architecture entre le front, vos backends aussi bien par appel HTTP ou messages. 

Nous utiliserons Spring Cloud Contract, très bien intégré aux applications Spring Boot, ainsi que Pact ayant l’avantage d’être polyglotte. 


 ## Lab

 ### Part 1

You can read a bit the [introduction to Spring Cloud Contract](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/getting-started.html#getting-started-introducing-spring-cloud-contract
)

In inventory project:
- Go to Maven pom.xml and uncomment the Spring Cloud Contract test dependancy and plugin [doc here](https://cloud.spring.io/spring-cloud-contract/spring-cloud-contract-maven-plugin/) 
- A base testing class is mandatory and configure the parent class of generated test classes. It handles the context needed for the tests. You have to configure the location of base contract class available here `com.devoxx.inventory.contracts.ContractsBase` by configuring the maven plugin with line `<baseClassForTests>` (see here)[https://cloud.spring.io/spring-cloud-contract/spring-cloud-contract-maven-plugin/junit.html].
- run `mvn clean test` to see the plugin generate tests in this class `_target/generated-test-sources_/.../ContractVerifierTest.java`. You can run it as a typical java test class.
- The contract `shoudRetrieveAllBooks` should pass now. To better understand, the [Groovy DSL documentation is here](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl)
- Uncomment the test `shoudRetrieveBook`. You need to generate again the contract tests. to go fast, use the `spring-cloud-contract:generateTests` goal of plugin directly. It fails due to wrong url in the controller. You can easily correct it. You can also see the use of [dynamic and regex properties](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl-dynamic-properties)
-  **important** : understand the _$(consumer/stub/client(...), producer/test/server(...))_
- _info_ : You can test only the relevant fields in the response.
- _info_ : to directly generate the contract tests, use the `spring-cloud-contract:generateTests` goal of plugin directly to avoid running full maven build.
- _info_ : For very complex use case, you can also [reference the parameters from the request](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl-referencing-request-from-response)
- Uncomment the test `shoudCreateBookIntoInventory` and make it test the endpoint : create a new book. You need to tailor a specific example: the POST request with the JSON body (all except the ID) associated to the http status and JSON response. The problem here is  how to test the random UUID. You can either control the UUID generator in the test or use matchers.
- Create the contract `shoudReduceStockInInventory` and make it test the reduce stock POST endpoint. You also have to uncomment it to show some ATDD practices :)
- You can run and tests your stub by using `generateStubs` goal
- Do mvn install on inventory project for making stub accessible for consumers


In checkout project:
- add [AutoConfigureStubRunner annotation](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#features-stub-runner-retrieving) with ids of the producer you consume (here inventory). `ids` looks like this : `groupId:artefactId:+:stubs:portNumber`. You need to also configure `stubsMode = StubRunnerProperties.StubsMode.LOCAL`
- fixing config to make WireMock request pass with stub runner by uncommenting the restTemplate configuration lines for Accept and Content-Type in `CheckoutApplication.java`
- remove the @Disabled on test `should_checkout_order`
- Complete contract for controller endpoint. Don't forget to add the baseClass. You can use the WireMock stub from inventory.
- Adding a contract for a message which is created in method [see Output Triggered by a Method](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl-output-triggered-method)
- _bonus_ : Add configuration in yml for stub runner to avoid duplication
- mvn install  for making stub accessible for consumer


In delivery project:
 - Complete test in delivery service to [trigger stub message](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#features-messaging-consumer)


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


https://github.com/spring-cloud-samples/spring-cloud-contract-samples/blob/master/producer_with_git/pom.xml#L98
https://cloud-samples.spring.io/spring-cloud-contract-samples/workshops.html#_setup


 
