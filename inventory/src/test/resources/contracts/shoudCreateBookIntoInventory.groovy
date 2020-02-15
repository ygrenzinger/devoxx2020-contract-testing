package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/v1/books'
        body("""
        {
            "name": "Scala",
            "price": "28.7"
        }
        """)
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status OK()
        body(
                id: value(
                        consumer(anyUuid()),
                        producer(regex('[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}'))
                ),
                name: "Scala",
                price: $(anyDouble()),
                stock: 0
        )
        headers {
            contentType(applicationJson())
        }
    }
}