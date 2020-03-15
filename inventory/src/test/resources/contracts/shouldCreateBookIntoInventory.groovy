package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        url '/v1/books'
        headers {
            contentType(applicationJson())
        }
        body(
                "name": anyNonEmptyString(),
                "price": anyDouble(),
                "stock": anyPositiveInt()
        )
    }
    response {
        status CREATED()
        headers {
            contentType(applicationJson())
        }
        body(
                "id": value(c("48bd1c8a-4fff-4db7-8967-9a59791415bd"), p(anyUuid())),
                "name": value(c("Clean code"), p(fromRequest().body("name"))),
                "price": value(c(49.99), p(fromRequest().body("price"))),
                "stock": value(c(30), p(fromRequest().body("stock")))
        )
    }
}
