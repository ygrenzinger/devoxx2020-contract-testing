# Workshop - Don't break your customers anymore with contract testing

Delivering a (micro) service only to find out that one has broken the environment is annoying.

In increasingly (micro)service-oriented architectures, checking that a new version of your service will still be able to communicate with others becomes more and more important. 

Contract testing offers a solution to verify interactions during the test phase. 
In this workshop, we will present how to implement contract testing, in particular first guided by the API provider then by the consumer as well as by HTTP calls or messages. 

We will use Spring Cloud Contract, very well integrated with Spring Boot applications, as well as Pact for a front to back interaction having the advantage of being multilingual. 

## Part 1

In this part of the workshop, you will implement **provider driven contract** with Spring Cloud Contract.

To have a global view of this part, the following UML diagram shows the relationship of the parts within Spring Cloud Contract:
![Spring Cloud Contract Diagram](spring-cloud-contract-diagram.png)

You can read a bit the [introduction to Spring Cloud Contract](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/getting-started.html#getting-started-introducing-spring-cloud-contract)

In inventory project, the goal is to learn how to test APIs and the groovy DSL. 

- Go to Maven __pom.xml__ and uncomment the Spring Cloud Contract test dependancy and plugin [doc here](https://cloud.spring.io/spring-cloud-contract/spring-cloud-contract-maven-plugin/) 
- A base testing class is mandatory and configure the parent class of generated test classes. It handles the context needed for the tests. You have to configure the location of base contract class available here `com.devoxx.inventory.contracts.ContractsBase` by configuring the maven plugin with line `<baseClassForTests>` (see here)[https://cloud.spring.io/spring-cloud-contract/spring-cloud-contract-maven-plugin/junit.html].
- run `mvn clean test` to see the plugin generate tests in this class `_target/generated-test-sources_/org/springframework/cloud/contract/verifier/tests/ContractVerifierTest.java`.
- The contract `shouldRetrieveAllBooks` should pass now. To better understand, the [Groovy DSL documentation is here](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl)
- Uncomment the test `shouldRetrieveBook`. You need to generate again the contract tests. to go fast, use `mvn spring-cloud-contract:generateTests` directly, then run `mvn test` to run the test (or execute `ContractVerifierTest` directly in your IDE). It fails due to wrong url in the controller. You can easily correct it. You can also see the use of [dynamic and regex properties](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl-dynamic-properties)
- **important** : Look how the url is defined, there is two parts : consumer and producer
  - Read more [here](https://docs.spring.io/spring-cloud-contract/docs/current/reference/html/project-features.html#contract-dsl-regex)
- __info__ : You can test only the relevant fields in the response.
- __info__ : to directly generate the contract tests, use `mvn spring-cloud-contract:generateTests` directly to avoid running full maven build.
- __info__ : For very complex use case, you can also [reference the parameters from the request](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl-referencing-request-from-response)
- Uncomment the test `shouldCreateBookIntoInventory` and make it test the create POST endpoint. The problem here is the random UUID. You can either control the UUID generator in the test or use matchers.
  - We want to verify that we receive a POST on /v1/books with as body a JSON Book containing :
    - a name field which is a string, e.g: "Clean code"
    - a price field which is a number, e.g: 49.99
    - and a stock field which is an integer, e.g. 45
  - then we expect the response to have the status Created (Http 201) with as a body a JSON Book containing :
    
        - an id filed which is an UUID, e.g : "48bd1c8a-4fff-4db7-8967-9a59791415bd"
        - a name field which is a string, e.g: "Clean code"
        - a price field which is a number, e.g: 49.99
        - and a stock field which is an integer, e.g. 45
- Create the contract `shouldReduceStockInInventory` and make it test the reduce stock POST endpoint. You also will have to uncomment it to show some ATDD practices :)
  - we want to verify that we receive a POST on /v1/books/d4d37e73-77a0-4616-8bd2-5ed983d45d14/stock/reduce/2 aka reducing by 2 the number of Java books
  - then we expect the response to have the status OK (Http 200) with as a body a JSON Book containing a stock field which is an integer, e.g. 92
- Do mvn install on inventory project for making stub accessible for consumers

In checkout project, the goal are to learn how to use the contract test as stubs in the consumer and to create a message contract.

- Make `CheckoutApplicationTests.java` tests pass. It's an integration test for the inventory service call to retrieve a book and the checkout controller.
- At the top of the class, begin to add [AutoConfigureStubRunner annotation](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#features-stub-runner-retrieving) with ids of the producer you consume (here the inventory project). `ids` looks like this : `groupId:artefactId:+:stubs:portNumber`. You need to also configure `stubsMode = StubRunnerProperties.StubsMode.LOCAL`
- You can now run the test `CheckoutApplicationTests`
- You have an error when consuming the stub. To fix it, you have have to correctly configure restTemplate. Luckily for you, it's just uncommenting the configuration lines for Accept and Content-Type in `CheckoutApplication.java`
- You can also remove the @Disabled on test `should_checkout_order`
- Implement the `shouldSendOrderToDelivery` a contract for a message which is created in method [see Output Triggered by a Method](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#contract-dsl-output-triggered-method)
  - Uncomment the MessageVerifier injection dans `ContractsBase.java`
  - Implement `sendOrder` function to send a message to delivery service (you can use the delivery interface)
  - We want to verify that if you trigger `sendOrder` you will have the following output message JSON body :
    - the bookId field which is the UUID, e.g: "d4d37e73-77a0-4616-8bd2-5ed983d45d14"
    - a quantity field which is a number, e.g: 2
    - a clientId field which is a string, e.g: "yannick"
    - and createdAt field the current date time - You can use dependency injection or matchers ;)
- You can now run again the test `CheckoutApplicationTests` to check that the message is sent
- mvn install for making stub accessible for the message consumer

In delivery project:

- Make test pass in `DeliveryApplicationTests.java` in delivery service to [trigger stub message](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#features-messaging-consumer)

### Bonus

In checkout project:

- Implement the `shouldCheckoutOrder` contract. Don't forget to add the baseClass and add it in the plugin configuration (same directory as in inventory if you want). You can also use the WireMock stub from inventory contract testing.
  - We want to verify that we receive a POST on /v1/checkouts with as body a JSON Order containing :
    - the bookId field which is the UUID, e.g: "d4d37e73-77a0-4616-8bd2-5ed983d45d14"
    - a quantity field which is a number, e.g: 2
    - and a clientId field which is a string, e.g: "yannick"
  - then we expect the response to have the status OK (Http 200) with as a body a JSON ValidatedOrder containing :
    - the same fields as input but, more importantly, a createdAt field with the current date time. You can use dependency injection or matchers ;) 
- _bonus_ : You can add configuration in yml for stub runner to avoid duplication in the annotations

## Part 2

In opposition to part one, part two will be about **consumer driven contract testing**. As the name says, in consumer driven contract testing, the contract is defined by the consumer. This means, each consumer will explain to the provider which service it needs.

### Pact

[Spring cloud contract](https://spring.io/projects/spring-cloud-contract) is a nice tool but it's only for Java / Spring Cloud providers.

That's why we will use [Pact](https://docs.pact.io/). Pact is quite similar to Spring Cloud Contract but really enforce the consumer driven principle.

#### The consumer Java doc
[Pact](https://docs.pact.io/implementation_guides/jvm/consumer/junit5)

#### A first consumer : the checkout service

##### Write a contract

Open the ```checkout``` service. 
- open the pom.xml file
- uncomment the following section : 
```xml
		<dependency>
			<groupId>au.com.dius.pact.consumer</groupId>
			<artifactId>junit5</artifactId>
			<version>4.3.6</version>
		</dependency>
```

Now we will write the contract 
- open ```InventoryContractTest```class which contains a start
- uncomment the mandatory annotation ```@PactTestFor(providerName = "inventory-service", port = "8080")``` which tell that we will test against the provider named ```ìventory-service```
- uncomment the ```getBookContract``` method : 
```java
@Pact(provider = "inventory-service", consumer = "checkout-service")
    public V4Pact getBookContract(PactBuilder builder) {
        return builder.usingLegacyDsl()
                .given("A product") // This is the state
                .uponReceiving("Get a single product")
                .matchPath("/v1/books/.+")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                    .stringType("name", "maxime") // seams implementation is not finished yet
                )
                .toPact(V4Pact.class);
    }
```

You should recognize most of the names here. 
So we expect a `HTTP GET` on `/v1/books/{id}` to return a `HTTP 200` with a json body.

The json body is not quite finished.

Implement the matcher so that the contract expect this kind of body : 
```json
{ 
  "id": "cc171e35-2426-4663-adef-954f42af12fe", 
  "name": "Domain Driven Design",
  "price": 49.90,
  "stock": 12
}
```
Please not that this is an exemple, you should test the structure.

You'll find some documentationa bout matchers [here](https://docs.pact.io/implementation_guides/jvm/consumer/junit#building-json-bodies-with-pactdsljsonbody-dsl)

Now you can run the the test (in your IDE or with `mvn test`) to see the contract created in `target/pacts`.
You should find a file named : `checkout-service-inventory-service.json`.
This file is the exchange format of the contract.

Now is time to see whether or not our consumer was properly programmed.

##### Test the contract

Now that we have a contract it's time to test it against the consumer.
Yes witg consumer driven contract test we begin with the consumer.

Still in the `InventoryContractTest` you'll find a test, just below the contract definition :
```Java
@Test
void test() {
    Book book = inventoryClient.retrieveBook("15");
    // Verify the content of book
}
```

Our inventory client is called and it's result is put in the `book` variable. But this one is not tested.

Please add the verification that the `book` variable contains the values of the contract.

Run the test, everything should works properly.

#### Publish the contract

Now we will go to the provider ad a test to check that it's verify the contract as well.

But first we need to make the contract available.

##### Create a pactflow.io account

To store and share contracts between services, we will use the [pact broker](https://docs.pact.io/pact_broker).
To ease setup here we will ask you to create an account in [pactflow.io](pactflow.io)

This is totally free don't worry.

Go to [pactflow.io](pactflow.io), click on the [Try for free](https://pactflow.io/try-for-free?utm_source=homepage&utm_content=header) button and follow the process until you get your own pact broker instance.

##### Publish your contract

First we need to add a maven plugin.  Go in the `pom.xml` file and uncoment the following plugin : 
```xml
<plugin>
  <groupId>au.com.dius.pact.provider</groupId>
  <artifactId>maven</artifactId>
  <version>4.1.11</version>
  <configuration>
    <pactBrokerUrl>https://[my instance].pactflow.io</pactBrokerUrl>
    <pactBrokerToken>[my api key]</pactBrokerToken> <!-- Replace TOKEN with the actual token -->
    <pactBrokerAuthenticationScheme>Bearer</pactBrokerAuthenticationScheme>
  </configuration>
</plugin>
```

You'll have to replace both the pact broker URL which should look like : `https://XXX.pactflow.io`.

You also need to give the key to allow the plugin to connect and send the contracts.
This api key is available in the settings of your pact broker instance.
Make sure to use the __Read/Write token__ otherwise no contract will be saved.

Everything is now ready.
By running `mvn pact:publish` your contract will be sent to the pact broker.
You are now able to see your new contract in your own pact broker.

#### Verify the provider : inventory-service

Open the `inventory` project.

This is our provider. You can see the `BookController` class which declare some endpoints.

First we have to add the dependency. Go in the pom.xml and uncomment the pact provider dependency : 
```xml
<dependency>
  <groupId>au.com.dius.pact.provider</groupId>
  <artifactId>junit5spring</artifactId>
  <version>4.3.5</version>
  <scope>test</scope>
</dependency>
```
This a package dedicated to works with spring but you can find one only for junit5 [here](https://docs.pact.io/implementation_guides/jvm/provider/junit5spring)

Second we have to tell pact where to get the contracts it should verify.
- Open the configuration file `src/test/resources/application.yml`
- uncomment the configuration :
```yml
pactbroker:
  host: [your pact broker instance URL, ex: magelle.pactflow.io]
  auth:
    token: [your token]
```

We can now go the the contract verification.
- Go in the `ContractVerificationTest` class which is our test class
- uncomment `@PactBroker` which tells pact we will use the pact broker
- uncomment `@Provider("inventory-service")` which tells pact we want to test all the contracts which have `inventory-service` as the provider
- You can now run the test ... which will fails : `MissingStateChangeMethod: Did not find a test class method annotated with @State("A product")`
 - Pact tries to tells us that we defined a contract with the state : "A Product" 
 - That's true remember we wrotte : `.given("A product")` -> This is the state
 - a state is a way to communicate a needed state in which the provider whould be to be able to test the contract.
- simply add the following method to comply :
```java
@State("A product")
void givenAProduct() {
    when(bookInventory.findBook(any())).thenReturn(
            new Book("ac9ab4d7-eaea-49a1-af1e-36b5acc29584",
                    "Clean Code",
                    BigDecimal.valueOf(39.99),
                    5)
    );
}
```
- Run the test again, this should works now
 - You hsould see in the logs that your contract was tested : `Verifying a pact between checkout-service (0.0.1-SNAPSHOT) and inventory-service`
 - you can try breaking the contract to watch the test fail again
 - If you look in yout pact broker instance, you should see that the contract is now tagged with a __Success__
 - This means the last provider verification was successful.

#### The front end

First of all you can go in ```book-shop-basket```. This is our frontend.

Book-Shop-Basket is the basket of our book shop. Its role is to present all books to the user allow it to select some and checkout the basket.

The ```book-shop-basket``` will need to get all available books from the ```inventory``` service. Then it will need to send the basket to the ```checkout``` service.

We will need two contracts : 

- get all book from inventory

- send the bask for checkout

As we are in consumer driven contract testing the contracts will be defined by ... the consumer. ```book-shop-basket``` in our case.

#### Mandatory Set up

As all tool Pact needs some set up.

Here we will need to :

- Add the Pact dependencies

- Configure karma

- Write the contract

- Write the test of the client

Don't worry we will guide you.

##### Installation of Pact

No action required for you here. We already include the dependencies : 

```
"@pact-foundation/karma-pact": "^3.1.0",
"@pact-foundation/pact-node": "^10.17.1",
"@pact-foundation/pact-web": "^9.17.2",
```

That's it !!

##### Configure Karma

After installing Pact we need to link it with our test framework. Here we use karma.

In ```book-shop-basket```, open the ```karma.conf.js``` file.

This is the karma configuration.

Uncomment the ```pact``` and ```proxies``` entries.

You should see something such as : 

```
pact: [
   {
     cors: true,
     consumer: 'book-shop-basket',
     provider: 'inventory-service',
     port: 1234,
     spec: 3,
     log: path.resolve(process.cwd(), 'logs', 'mockserver-integration.log'),
     dir: path.resolve('pacts')
   }
],
 proxies: {
   '/v1/books': 'http://localhost:1234/v1/books'
}
```

This configuration describe all the relations we will test.

So we tell Pact that we will create contracts between ```book-shop-basket``` and ```inventory-service```.

```port: 1234``` define the port on which the stub server will be available.

```spec: 3``` express that we will use the version 3 of the contract specification as there is several.

```log``` and ```dir``` configure respectively where will be write the logs and the contracts.

Then comes the proxy configuration.

While doing the tests, Pact will set up a stub server. This stub will be available on the port 1234. But the front end does not know this and it is necessary to set up a proxy to redirect all http calls to the stub server.

##### Read an existing contract

There is already a start of a test file here : ```src/app/inventory.service.pact.spec.ts```.

In this file, you'll see, in order : 

The setup :

```
  // Setup Pact mock server for this service
  beforeAll(async () => {

    provider = await new PactWeb({
      port: 1234
    });

    // required for slower CI environments
    await new Promise(resolve => setTimeout(resolve, 2000));

    // Required if run with `singleRun: false`
    await provider.removeInteractions();
  });
```

The after test vérifications

```
  // Verify test
  afterEach(async () => {
    await provider.verify();
  });

  // Create contract
  afterAll(async () => {
    await provider.finalize();
  });
```

The definition of the interaction

```
beforeAll((done) => {
  provider.addInteraction({
    state: ``,
    uponReceiving: 'Get books inventory',
    withRequest: {
      method: 'GET',
      path: '/v1/books'
    },
    willRespondWith: {
      status: 200,
      body: Matchers.somethingLike(books),
      headers: {
        'Content-Type': 'application/json'
      }
    }
  }).then(done, error => done.fail(error));
});
```

The test of the client.

```
it('should get book inventory', (done) => {
      const inventoryService: InventoryService = TestBed.get(InventoryService);
      inventoryService.allBooks().subscribe(response => {
        expect(response).toEqual(books);
        done();
      }, error => {
        done.fail(error);
      });
    });
```

You can run the tests with ```npm test``` or ```yarn test```.
This should create the contracts files in the ```pacts``` folder.

#### Write a contract

Now we to send the order to the ```checkout``` service.
To do that we will write a contract between ```book-shop-basket``` and ```checkout``` service

This interaction will be :
- An HTTP POST
- on /v1/checkouts
- with, as a body an list of JSON object with:
  - the bookId field which is the UUID, e.g: "d4d37e73-77a0-4616-8bd2-5ed983d45d14"
  - a quantity field which is a number, e.g: 2
  - a clientId field which is a string, e.g: "yannick"

Run ``` npm test``` or ```yarn test``` to generate the pact files.

**Bravo ! You have successfully reached the end of workshop**
