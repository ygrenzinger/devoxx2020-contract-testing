import { TestBed } from '@angular/core/testing';
import { Matchers, PactWeb } from '@pact-foundation/pact-web';
import { HttpClientModule } from '@angular/common/http';
import { CheckoutService } from './checkout.service';
import { Order } from './typings';

describe('CheckoutServiceContract', () => {
  let provider;

  // Setup Pact mock server for this service
  beforeAll(async () => {

    provider = await new PactWeb({
      port: 1235
    });

    // required for slower CI environments
    await new Promise(resolve => setTimeout(resolve, 2000));

    // Required if run with `singleRun: false`
    await provider.removeInteractions();
  });

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [CheckoutService],
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

  const order: Order = {
    bookId: '47baae30-09a7-41a4-9593-b181316cd1a2',
    number: 5,
    clientId: '0fcfd742-0f59-4843-90b1-66504ed10468'
  };

  describe('CheckoutService', () => {

    beforeAll((done) => {
      provider.addInteraction({
        state: ``,
        uponReceiving: 'Checkout an order',
        withRequest: {
          method: 'POST',
          path: '/v1/checkouts',
          body: order,
          headers: {
            'Content-Type': 'application/json'
          }
        },
        willRespondWith: {
          status: 200,
          body: Matchers.somethingLike(order),
          headers: {
            'Content-Type': 'application/json'
          }
        }
      }).then(done, error => done.fail(error));
    });

    it('should checkout', (done) => {
      const checkoutService: CheckoutService = TestBed.get(CheckoutService);
      checkoutService.checkout(order).subscribe(response => {
        expect(response).toEqual(order);
        done();
      }, error => {
        done.fail(error);
      });
    });

  });

});
