package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/v1/books'
        body("""
        {
            "name": "Kotlin",
            "price": "120"
        }
        """)
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status OK()
        body("""
        {
            "id": "dc8493d6-e2e3-47da-a806-d1e8ff7cd4df",
            "name": "Kotlin",
            "price": "120",
            "stock": 0
        }
        """)
        headers {
            contentType(applicationJson())
        }
    }
}