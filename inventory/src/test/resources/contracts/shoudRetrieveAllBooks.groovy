package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url '/v1/books'
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status OK()
        body("""
        [{
            "id": "d4d37e73-77a0-4616-8bd2-5ed983d45d14",
            "name": "Java",
            "price": "19.9",
            "stock": 100
        },{
            "id": "8364948b-6221-4cd8-9fd9-db0d17d45ef8",
            "name": "Kotlin",
            "price": "22.4",
            "stock": 0
        }]
        """)
        headers {
            contentType(applicationJson())
        }
    }
}
