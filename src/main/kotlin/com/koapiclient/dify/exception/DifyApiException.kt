package com.koapiclient.dify.exception

class DifyApiException : RuntimeException {

    var statusCode: Int = 0

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(message: String, statusCode: Int) : super(message) {
        this.statusCode = statusCode
    }

    fun getStatusCode(): Int {
        return statusCode
    }
}