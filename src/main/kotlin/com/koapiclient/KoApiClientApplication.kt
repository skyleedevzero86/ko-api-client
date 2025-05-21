package com.koapiclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KoApiClientApplication

fun main(args: Array<String>) {
    runApplication<KoApiClientApplication>(*args)
}
