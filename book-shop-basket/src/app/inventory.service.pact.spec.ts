import { TestBed } from '@angular/core/testing';
import { Matchers, PactWeb } from '@pact-foundation/pact-web';
import { HttpClientModule } from "@angular/common/http";
import { InventoryService } from "./inventory.service";
import { Book } from './typings';

describe('InventoryServiceContract', () => {
  let provider;

  // Setup Pact mock server for this service
  beforeAll(async () => {

    provider = await new PactWeb({
      consumer: 'book-shop-basket',
      provider: 'inventory-service',
      port: 1234
    });

    // required for slower CI environments
    await new Promise(resolve => setTimeout(resolve, 2000));

    // Required if run with `singleRun: false`
    await provider.removeInteractions();
  });

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [InventoryService],
    });
  });

  // Verify test
  afterEach(async () => {
    await provider.verify();
  });

  // Create contract
  afterAll(async () => {
    await provider.finalize();
  });

  const books: Book[] = [
    {
      id: 'e72ad291-5818-4e92-9344-a8050656c9b2',
      name: 'Clean Code: A Handbook of Agile Software Craftsmanship',
      price: 30
    }
  ];

  describe('InventoryService', () => {

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

    it('should get book inventory', (done) => {
      const inventoryService: InventoryService = TestBed.get(InventoryService);
      inventoryService.allBooks().subscribe(response => {
        expect(response).toEqual(books);
        done();
      }, error => {
        done.fail(error);
      });
    });

  });

});
