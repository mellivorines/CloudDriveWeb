package io.github.mellivorines.clouddriveweb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CloudDriveWebApplication

fun main(args: Array<String>) {
    runApplication<CloudDriveWebApplication>(*args)
}
