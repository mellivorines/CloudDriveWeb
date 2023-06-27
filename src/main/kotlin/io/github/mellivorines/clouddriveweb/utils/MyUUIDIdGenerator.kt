package io.github.mellivorines.clouddriveweb.utils

import org.babyfish.jimmer.sql.meta.UserIdGenerator
import java.util.*


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/23
 */
class MyUUIDIdGenerator : UserIdGenerator<String> {

    override fun generate(entityType: Class<*>?): String {
        return UUID.randomUUID().toString().replace("-", "")
    }
}