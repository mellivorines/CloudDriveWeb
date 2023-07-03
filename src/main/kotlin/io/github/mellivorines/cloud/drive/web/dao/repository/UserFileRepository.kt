package io.github.mellivorines.cloud.drive.web.dao.repository

import io.github.mellivorines.cloud.drive.web.dao.entity.*
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.valueIn
import org.springframework.stereotype.Repository

/**
 * <p>
 * UserFileRepository 接口
 * </p>
 *
 * @author lilinxi
 * @date 2023-07-01
 */
@Repository
interface UserFileRepository : KRepository<UserFile, String> {
    fun findByUserIdAndFileTypeAndParentIdAndDelFlag(
        userId: String,
        fileTypeArray: List<Int>?,
        parentId: String,
        delFlag: Int
    ): List<UserFile> {
        return sql
            .createQuery(UserFile::class) {
                where(table.userId eq(userId) )
                fileTypeArray?.let {
                    where(table.fileType valueIn it)
                }
                where(table.parentId eq(parentId) )
                where(table.delFlag eq(delFlag) )
                select(table)
            }
            .execute()
    }

}

