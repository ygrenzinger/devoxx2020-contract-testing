package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    ignored()
    request {
        method 'GET'
        url 'xxx'
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