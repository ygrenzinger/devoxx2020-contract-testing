package contracts

import org.springframework.cloud.contract.spec.Contract


Contract.make {
    request {
        method(POST())
        url "/v1/checkouts"
        headers {
            contentType(applicationJson())
        }
        body(
            "bookId": value(c(anyUuid()), p("d4d37e73-77a0-4616-8bd2-5ed983d45d14")),
            "number": value(c(anyPositiveInt()), p(10)),
            "clientId": value(c(anyNonBlankString()), p("yannick.grenzinger"))
        )
    }
    response {
        status(OK())
        headers {
            contentType(applicationJson())
        }
        body(
                "bookId": value(c(fromRequest().body("bookId")), p(anyUuid())),
                "number": value(c(fromRequest().body("number")), p(anyPositiveInt())),
                "clientId": value(c(fromRequest().body("clientId")), p(anyNonBlankString())),
                "createdAt": value(c("2016-11-09T11:44:44.797"), p(anyDateTime()))
        )
    }
}
