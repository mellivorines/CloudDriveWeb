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
     * @param [parentId] 父文件ID
     * @param [folderName] 文件夹名称
     * @param [userId] 用户ID
     * @return [List<UserFile>?]返回结果
     */
    fun createFolder(parentId: String, folderName: String, userId: String) :List<UserFile>?{
        saveUserFile(parentId, folderName, CommonConstant.FolderFlagEnum.YES.code, null, null, userId, null)
        return list(parentId, CommonConstant.ALL_FILE_TYPE, userId, CommonConstant.DelFlagEnum.NO.code)
    }

    /**
     * 获取文件列表
     * @param [parentId] 父ID
     * @param [fileTypes]文件类型
     * @param [userId]用户ID
     * @return [List<UserFile>?]返回结果
     */
    fun list(parentId: String?, fileTypes: String?, userId: String): List<UserFile>? {
        return list(parentId, fileTypes!!, userId, CommonConstant.DelFlagEnum.NO.code)
    }

    /**
     * 查询文件夹文件
     *
     * @param [parentId]父ID
     * @param [fileTypes]文件类型
     * @param [userId]用户ID
     * @param [delFlag]是否删除
     * @return [List<UserFile>?]返回结果
     */
    private fun list(parentId: String?, fileTypes: String, userId: String, delFlag: Int) :List<UserFile>?{


        var fileTypeArray: List<Int>? = null
        if (CommonConstant.ZERO_LONG.toString() == parentId) return Lists.newArrayList()
        if (fileTypes != CommonConstant.ALL_FILE_TYPE) {
            fileTypeArray = StringListUtil.string2IntegerList(fileTypes)
        }

        return userFileRepository.findByUserIdAndFileTypeAndParentIdAndDelFlag(userId,fileTypeArray,parentId,delFlag)
    }


    /**
     * 保存用户文件信息
     * @param [parentId]父ID
     * @param [filename]文件名称
     * @param [folderFlag]是否是文件夹
     * @param [fileType]文件类型
     * @param [realFileId]文件ID
     * @param [userId]用户ID
     * @param [fileSizeDesc]
     */
    private fun saveUserFile(parentId: String, filename: String, folderFlag: Int, fileType: Int?, realFileId: String?, userId: String, fileSizeDesc: String?) {
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


    /**
     * 文件重命名
     * @param [updateUserFileInput] 更新文件信息
     * @return [List<UserFile>?]返回结果
     */
    fun updateFilename(updateUserFileInput: UserFileInput): List<UserFile>? {
        val findById = updateUserFileInput.fileId?.let { userFileRepository.findById(it).get() }
        return if (findById != null) {
            if (findById.filename == updateUserFileInput.filename){
                throw BizException(msg = "名称已被占用,请使用一个新名称")
            }else {
                list(updateUserFileInput.parentId,CommonConstant.ALL_FILE_TYPE,updateUserFileInput.userId,CommonConstant.ZERO_INT)
            }
        } else {
            list(updateUserFileInput.parentId,CommonConstant.ALL_FILE_TYPE,updateUserFileInput.userId,CommonConstant.ZERO_INT)
        }
    }

    /**
     * 批量删除文件
     * @param [fileIds] 文件ID
     * @param [parentId]父ID
     * @param [userId]用户ID
     * @return [Any?]
     */
    fun delete(fileIds: List<String>, parentId: String, userId: String): List<UserFile>? {
        //删除文件
        userFileRepository.deleteByIds(fileIds)

        //刷新分享文件的文件状态
        TODO("刷新分享文件的文件状态")

        //返回文件夹下的文件
        return list(parentId,CommonConstant.ALL_FILE_TYPE,userId)
    }

}