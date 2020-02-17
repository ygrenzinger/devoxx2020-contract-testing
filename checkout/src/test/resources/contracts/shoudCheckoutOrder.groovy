package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/v1/checkouts'
        body("""
        {
            "bookId": "d4d37e73-77a0-4616-8bd2-5ed983d45d14",
            "number": 2,
            "clientId": "yannick"
        }
        """)
        headers {
            header('Content-Type', 'application/json')
        }
    }
    response {
        status 200
        body(
            bookId: 'd4d37e73-77a0-4616-8bd2-5ed983d45d14',
            number: 2,
            clientId: 'yannick',
            createdAt: anyDateTime()
        )
        headers {
            header('Content-Type': 'application/json')
        }
    }
}