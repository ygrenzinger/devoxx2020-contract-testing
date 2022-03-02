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

### Spring Cloud Contract - Consumer Driven.

First we will configure Spring Cloud Contract in a consumer driven way. 

You have to checkout "part2" branch and initialise a repository for contracts. To initialize the local Git repository, you can use the Golang tool provided in the `./tool` directory and associated OS. It will create a repository, initialize it with some contracts in a consumer driven way and give you back the url to use. You can also read the associated [README](./tool/README.md).

Next you can use the repo url to configure the Spring Cloud Contract maven plugin in inventory and checkout projects [See how to configure here](https://cloud.spring.io/spring-cloud-contract/spring-cloud-contract-maven-plugin/configs.html). 

Run `mvn clean spring-cloud-contract:generateTests` to see the generated test files (one for each consumer) in `target/generated-test-sources`.

You also have to correctly configure the consumers using the Git repository [See how to configure here](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/project-features.html#features-stub-runner-stubs-per-consumer): 
- in checkout application.yml
- in delivery DeliveryApplicationTests.yml

You now have Spring Cloud Contract configured in a consumer driven way !

### Pact

[Spring cloud contract](https://spring.io/projects/spring-cloud-contract) is a nice tool but it's only for Java / Spring Cloud providers.

That's why we will use [Pact](https://docs.pact.io/). Pact is quite similar to Spring Cloud Contract but really enfore the consumer driven principle.

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
beforeAll(async () => {

	// Setup Pact and start Backend stub
    provider = await new PactWeb({
      consumer: 'book-shop-basket',
      provider: 'inventory-service',
      port: 1234
    });

    // required for slower CI environments
    await new Promise(resolve => setTimeout(resolve, 2000));

	// Remove all all existing interactions
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

  // Create the contract
  afterAll(async () => {
    await provider.finalize();
  });
```

The definition of the interaction

```
eforeAll((done) => {
      provider.addInteraction({
        state: ``,
        uponReceiving: 'Get books inventory',
        withRequest: {
          method: 'GET',
          path: '/v1/books'
        },
        willRespondWith: {
          status: 200,
          body: Matchers.somethingLike([
            {
              id: 'e72ad291-5818-4e92-9344-a8050656c9b2',
              name: 'Clean Code: A Handbook of Agile Software Craftsmanship',
              price: 30
            }
          ]),
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

#### Back to Spring Cloud Contract

Now you have a new Pact contracts for your front end as a consumer of inventory and checkout providers.

You can push them in your "remote" contract's repository using the previous URL / directory without the .git at the end. 
- Create directories for this new consumer for inventory and checkout providers and add the associated Pact contracts 
- Add the pact plugin dependency in Spring Cloud Contract maven plugin. [More information here](https://cloud.spring.io/spring-cloud-static/spring-cloud-contract/2.2.1.RELEASE/reference/html/howto.html#how-to-use-pact-broker-pact) 
```
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-contract-pact</artifactId>
        <version>2.2.1.RELEASE</version>
    </dependency>
</dependencies>
```

Run `mvn clean spring-cloud-contract:generateTests` to see the newly generated test file with the same name as the directories created previously in `target/generated-test-sources`.

**Bravo ! You have successfully reached the end of workshop**
