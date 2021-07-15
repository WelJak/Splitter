package com.weljak.splitter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SplitterApplication

fun main(args: Array<String>) {
    runApplication<SplitterApplication>(*args)
}
