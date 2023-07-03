package io.github.mellivorines.cloud.drive.web.service

import com.google.common.collect.Lists
import io.github.mellivorines.cloud.drive.web.constants.CommonConstant
import io.github.mellivorines.cloud.drive.web.dao.entity.UserFile
import io.github.mellivorines.cloud.drive.web.dao.input.UserFileInput
import io.github.mellivorines.cloud.drive.web.dao.repository.UserFileRepository
import io.github.mellivorines.cloud.drive.web.exception.BizException
import io.github.mellivorines.cloud.drive.web.utils.StringListUtil
import org.apache.commons.lang3.ObjectUtils
import org.springframework.stereotype.Service


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/3
 */
@Service
class UserFileService(private val userFileRepository: UserFileRepository) {

    /**
     * 创建文件夹
     *
     * @param [parentId] 父文件ID
     * @param [folderName] 文件夹名称
     * @param [userId] 用户ID
     */
    fun createFolder(parentId: String, folderName: String, userId: String) :List<UserFile>{
        saveUserFile(parentId, folderName, CommonConstant.FolderFlagEnum.YES.code, null, null, userId, null)
        return list(parentId, CommonConstant.ALL_FILE_TYPE, userId, CommonConstant.DelFlagEnum.NO.code)
    }

    private fun list(parentId: String, fileTypes: String, userId: String, delFlag: Int) :List<UserFile>{


        var fileTypeArray: List<Int>? = null
        if (CommonConstant.ZERO_LONG.toString() == parentId) return Lists.newArrayList()
        if (fileTypes != CommonConstant.ALL_FILE_TYPE) {
            fileTypeArray = StringListUtil.string2IntegerList(fileTypes)
        }

        return userFileRepository.findByUserIdAndFileTypeAndParentIdAndDelFlag(userId,fileTypeArray,parentId,delFlag)
    }


    /**
     * 保存用户文件信息
     *
     * @param parentId
     * @param filename
     * @param folderFlag
     * @param fileType
     * @param realFileId
     * @param userId
     * @param fileSizeDesc
     * @return
     */
    private fun saveUserFile(
        parentId: String,
        filename: String,
        folderFlag: Int,
        fileType: Int?,
        realFileId: String?,
        userId: String,
        fileSizeDesc: String?
    ) {
        val userFileInput = UserFileInput(
            null,
            userId,
            parentId,
            realFileId,
            filename,
            folderFlag,
            fileSizeDesc,
            null,
            0,
            userId,
            null,
            userId,
            null
        )
        val save = userFileRepository.save(userFileInput)
        if (ObjectUtils.isEmpty(save)) {
            throw BizException(msg = "保存文件信息失败")
        }
    }

}