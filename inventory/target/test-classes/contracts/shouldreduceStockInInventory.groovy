package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method(POST())
        url $(
                consumer(regex('/v1/books/[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}/stock/reduce/[0-9]+')),
                producer('/v1/books/d4d37e73-77a0-4616-8bd2-5ed983d45d14/stock/reduce/5')
        )
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status(OK())
        headers {
            contentType(applicationJson())
        }
        body(
                "id": value(c("48bd1c8a-4fff-4db7-8967-9a59791415bd"), p(anyUuid())),
                "name": value(c("Clean code"), p(anyNonBlankString())),
                "price": value(c(49.99), p(anyNumber())),
                "stock": value(c(30), p(anyPositiveInt()))
        )
    }
}