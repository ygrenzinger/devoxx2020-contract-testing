package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    label 'should send order'
    input {
        triggeredBy('sendOrder()')
    }
    outputMessage {
        sentTo('orders')
        body(
            bookId: 'd4d37e73-77a0-4616-8bd2-5ed983d45d14',
            number: 2,
            clientId: 'yannick',
            createdAt: anyDateTime()
        )
        headers {
            header('SENDER', 'checkout')
            messagingContentType(applicationJson())
        }
    }
}