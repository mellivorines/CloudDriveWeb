package io.github.mellivorines.clouddriveweb.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class CloudDrive(
    @Value("\${CloudDrive.file.path}")
    val path: String
)
