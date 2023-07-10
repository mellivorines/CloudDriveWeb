package io.github.mellivorines.cloud.drive.web.dao.repository

import io.github.mellivorines.cloud.drive.web.dao.entity.*
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.ilike
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
        parentId: String?,
        delFlag: Int
    ): List<UserFile>? {
        return sql
            .createQuery(UserFile::class) {
                where(table.userId eq (userId))
                fileTypeArray?.let {
                    where(table.fileType valueIn it)
                }
                parentId?.let {
                    where(table.parentId eq (it))
                }
                where(table.delFlag eq (delFlag))
                select(table)
            }
            .execute()
    }

    fun findByFileIdAndUserId(fileId: String, userId: String): UserFile?

    fun findFolderListByUserId(userId: String): List<UserFile>? {
        return sql.createQuery(UserFile::class) {
            where(table.userId eq (userId))
            where(table.delFlag eq (0))
            where(table.folderFlag eq (1))
            select(table)
        }.execute()
    }

    fun findUserFileListByUserIdAndFilenameAndFileTypes(
        userId: String,
        keyword: String,
        fileTypeArray: List<Int>
    ): List<UserFile>? {
        return sql.createQuery(UserFile::class) {
            where(table.userId eq (userId))
            where(table.filename ilike (keyword))
            where(table.delFlag eq (0))
            where(table.fileType valueIn (fileTypeArray))
            select(table)
        }.execute()
    }

    fun findFolderListByParentId(parentId: String): List<UserFile>?

}

