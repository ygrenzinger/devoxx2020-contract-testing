package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/v1/books/d4d37e73-77a0-4616-8bd2-5ed983d45d14/stock/reduce/2'
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status OK()
        body(
                stock: 98
        )
        headers {
            contentType(applicationJson())
        }
    }
}