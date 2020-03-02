package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    ignored()
    request {
        method 'POST'
        url '/v1/books'
        body("""
        """)
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status OK()
        body("""
        """)
        headers {
            contentType(applicationJson())
        }
    }
}