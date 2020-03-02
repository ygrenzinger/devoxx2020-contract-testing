# Ne cassez plus vos consommateurs grâce au contract testing

Livrer un (micro) service pour s'apercevoir que l’on a cassé l’environnement, c’est agaçant.

Dans des architectures de plus en plus orientées (micro) services, et les S.I. complexes, vérifier qu’une nouvelle version de votre service continuera à pouvoir communiquer avec les autres devient de plus en plus important. 

Les tests de contrats proposent une solution pour vérifier les interactions lors de la phase de test. 
Dans cet atelier, nous présenterons comment mettre en place le test de contrat, en particulier guidé par le consommateur de l’API, au sein de votre architecture entre le front, vos backends aussi bien par appel HTTP ou messages. 

Nous utiliserons Spring Cloud Contract, très bien intégré aux applications Spring Boot, ainsi que Pact ayant l’avantage d’être polyglotte. 


 ## Lab

You can read a bit the [introduction to Spring Cloud Contract](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/getting-started.html#getting-started-introducing-spring-cloud-contract
)

In inventory project:
 - The maven plugin [doc here](https://cloud.spring.io/spring-cloud-contract/spring-cloud-contract-maven-plugin/) is already added in POM.xml spring-cloud-contract-maven-plugin
 - You have to configure the location base contract class available here `com.devoxx.inventory.contracts.ContractsBase` in the maven plugin by adding the location of in configuration `<baseClassForTests>` (see here)[https://cloud.spring.io/spring-cloud-contract/spring-cloud-contract-maven-plugin/junit.html]. This base class create configure ...
 - run `mvn clean test` to see the plugin generate tests in this class `_target/generated-test-sources_/.../ContractVerifierTest.java`. You can run it as a typical java test class.
- the test `shoudRetrieveAllBooksWithStock` fails due to wrong url in the code. You can easily correct it.
- Add contracts for other REST endpoints


 - Make first contract `shoudRetrieveAllBooks` pass
 - Add  to make the tests pass
 - run _spring-cloud-contract:generateTests_ and look at 
 - You can run them directly 
 - Adding [static contract with groovy DSL](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl)
 - You can test only the relevant fields
 - Using [dynamic and regex properties](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl-dynamic-properties)
 - understand the _$(consumer/stub/client(...), producer/test/server(...))_
 - You can also [reference the parameters from the request](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl-referencing-request-from-response)

 - mvn install  for making stub accessible for consumers
 - using Contract WireMock stub on the consumer "Checkout" side [see AutoConfigureStubRunner](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#features-stub-runner-retrieving)
 - fixing missing config to make WireMock request pass with stub runner 
 - Add configuration in yml for stub runner to avoid duplication
 - Add a contract (and baseclass) for /v1/checkouts
 - Adding a contract for a message which is created in method [see Output Triggered by a Method](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl-output-triggered-method)

 
 - mvn install  for making stub accessible for consumer
 - Complete test in delivery service to [trigger stub message](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#features-messaging-consumer)


## Part 2

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


 