package contracts

import org.springframework.cloud.contract.spec.Contract


Contract.make {
    request {
        method GET()
        url $(
                consumer(regex('/v1/books/[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}')),
                producer('/v1/books/d4d37e73-77a0-4616-8bd2-5ed983d45d14')
        )
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status OK()
        body(
                "id": anyUuid(),
                "name": "Java",
                "price": 19.9,
                "stock": 100
        )
        headers {
            contentType(applicationJson())
        }
    }
}

