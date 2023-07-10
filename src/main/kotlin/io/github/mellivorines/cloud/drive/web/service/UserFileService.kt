package io.github.mellivorines.cloud.drive.web.service

import com.google.common.collect.Lists
import io.github.mellivorines.cloud.drive.web.constants.CommonConstant
import io.github.mellivorines.cloud.drive.web.dao.entity.File
import io.github.mellivorines.cloud.drive.web.dao.entity.UserFile
import io.github.mellivorines.cloud.drive.web.dao.input.UserFileInput
import io.github.mellivorines.cloud.drive.web.dao.repository.UserFileRepository
import io.github.mellivorines.cloud.drive.web.exception.BizException
import io.github.mellivorines.cloud.drive.web.model.tree.FolderTreeNode
import io.github.mellivorines.cloud.drive.web.storage.core.StorageManager
import io.github.mellivorines.cloud.drive.web.utils.FileUtil
import io.github.mellivorines.cloud.drive.web.utils.HttpUtil
import io.github.mellivorines.cloud.drive.web.utils.StringListUtil.string2IntegerList
import io.github.mellivorines.cloud.drive.web.utils.type.context.FileTypeContext.getFileTypeCode
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.lang3.ObjectUtils
import org.reflections.Reflections.log
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*
import java.util.stream.Collectors


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/3
 */
@Service
class UserFileService(
    private val userFileRepository: UserFileRepository,
    private val storageManager: StorageManager,
    private val userSearchHistoryService: UserSearchHistoryService,
    private val fileService: FileService
) {

    /**
     * 创建文件夹
     * @param [parentId] 父文件ID
     * @param [folderName] 文件夹名称
     * @param [userId] 用户ID
     * @return [List<UserFile>?]返回结果
     */
    fun createFolder(parentId: String, folderName: String, userId: String): List<UserFile>? {
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
    private fun list(parentId: String?, fileTypes: String, userId: String, delFlag: Int): List<UserFile>? {


        var fileTypeArray: List<Int>? = null
        if (CommonConstant.ZERO_LONG.toString() == parentId) return Lists.newArrayList()
        if (fileTypes != CommonConstant.ALL_FILE_TYPE) {
            fileTypeArray = string2IntegerList(fileTypes)
        }

        return userFileRepository.findByUserIdAndFileTypeAndParentIdAndDelFlag(userId, fileTypeArray, parentId, delFlag)
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


    /**
     * 文件重命名
     * @param [updateUserFileInput] 更新文件信息
     * @return [List<UserFile>?]返回结果
     */
    fun updateFilename(updateUserFileInput: UserFileInput): List<UserFile>? {
        val findById = updateUserFileInput.fileId?.let { userFileRepository.findById(it).get() }
        return if (findById != null) {
            if (findById.filename == updateUserFileInput.filename) {
                throw BizException(msg = "名称已被占用,请使用一个新名称")
            } else {
                list(
                    updateUserFileInput.parentId,
                    CommonConstant.ALL_FILE_TYPE,
                    updateUserFileInput.userId,
                    CommonConstant.ZERO_INT
                )
            }
        } else {
            list(
                updateUserFileInput.parentId,
                CommonConstant.ALL_FILE_TYPE,
                updateUserFileInput.userId,
                CommonConstant.ZERO_INT
            )
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
        return list(parentId, CommonConstant.ALL_FILE_TYPE, userId)
    }

    /**
     * 文件分片上传
     *
     * @param file
     * @param parentId
     * @param userId
     * @param md5
     * @param chunks
     * @param chunk
     * @param size
     * @param name
     * @return
     */
    fun uploadWithChunk(
        file: MultipartFile,
        parentId: String,
        userId: String,
        md5: String,
        chunks: Int,
        chunk: Int,
        size: Long,
        name: String
    ): List<UserFile>? {
        val panFile = uploadRealFileWithChunk(file, userId, md5, chunks, chunk, size, name)
        if (panFile != null) {
            saveUserFile(
                parentId,
                name,
                CommonConstant.FolderFlagEnum.NO.code,
                getFileTypeCode(name),
                panFile.fileId,
                userId,
                null
            )
            return list(parentId, CommonConstant.ALL_FILE_TYPE, userId)
        }
        return null
    }

    /**
     * 文件上传
     *
     * @param file
     * @param parentId
     * @param md5
     * @param size
     * @return
     */
    fun upload(file: MultipartFile, parentId: String, userId: String, md5: String, size: Long): List<UserFile>? {
        val panFile = uploadRealFile(file, userId, md5, size)
        val filename = file.originalFilename
        if (panFile != null) {
            saveUserFile(
                parentId,
                filename!!,
                CommonConstant.FolderFlagEnum.NO.code,
                getFileTypeCode(filename),
                panFile.fileId,
                userId,
                null
            )
        }

        return list(parentId, CommonConstant.ALL_FILE_TYPE, userId)
    }

    /**
     * 预览单个文件
     *
     * @param fileId
     * @param userId
     * @param response
     */
    fun preview(fileId: String, response: HttpServletResponse, userId: String) {
        if (checkIsFolder(fileId, userId)) {
            throw BizException(msg = "不能预览文件夹")
        }
        val userFile = userFileRepository.findById(fileId).get()
        val fileDetail = fileService.getFileDetail(userFile.realFileId)
        preview(response, fileDetail.realPath, fileDetail.filePreviewContentType)
    }

    /**
     * 文件预览
     *
     * @param filePath
     * @param response
     */
    private fun preview(response: HttpServletResponse, filePath: String, filePreviewContentType: String) {
        addCommonResponseHeader(response, filePreviewContentType)
        read2OutputStream(filePath, response)
    }

    /**
     * 文件写入响应实体
     *
     * @param filePath
     * @param response
     */
    private fun read2OutputStream(filePath: String, response: HttpServletResponse) {
        try {
            storageManager.read2OutputStream(filePath, response.outputStream)
        } catch (e: Exception) {
            log.error("文件写入响应实体失败", e)
        }
    }

    /**
     * 查询文件详情
     *
     * @param fileId
     * @param userId
     * @return
     */
    fun detail(fileId: String, userId: String): UserFile? {
        return userFileRepository.findByFileIdAndUserId(fileId, userId)
    }

    /**
     * 获取面包屑列表
     *
     * @param fileId
     * @param userId
     * @return
     */
    fun getBreadcrumbs(fileId: String, userId: String): List<UserFile>? {
        return userFileRepository.findFolderListByUserId(userId)
    }

    /**
     * 通过文件名搜索文件
     *
     * @param keyword
     * @param userId
     * @return
     */
    fun search(keyword: String, fileTypes: String, userId: String): List<UserFile>? {
        saveUserSearchHistory(keyword, userId)
        var fileTypeArray: List<Int> = listOf()
        if (fileTypes != CommonConstant.ALL_FILE_TYPE) {
            fileTypeArray = string2IntegerList(fileTypes)
        }
        return userFileRepository.findUserFileListByUserIdAndFilenameAndFileTypes(userId, keyword, fileTypeArray)
    }

    /**
     * 保存用户搜索关键字信息
     *
     * @param keyword
     * @param userId
     */
    private fun saveUserSearchHistory(keyword: String, userId: String) {
        userSearchHistoryService.save(keyword, userId)
    }

    /**
     * 添加公用的响应头
     *
     * @param response
     * @param contentTypeValue
     */
    private fun addCommonResponseHeader(response: HttpServletResponse, contentTypeValue: String) {
        response.reset()
        HttpUtil.addCorsResponseHeader(response)
        response.setHeader(CommonConstant.CONTENT_TYPE_STR, contentTypeValue)
        response.contentType = contentTypeValue
    }

    /**
     * 文件下载
     *
     * @param fileId
     * @param response
     * @param userId
     */
    fun download(fileId: String, response: HttpServletResponse, userId: String) {
        try {
            getRPanUserFileByFileIdAndParentId(fileId, userId)
        } catch (e: Exception) {
            throw BizException(msg = "您没有下载权限")
        }
        download(fileId, response)
    }

    /**
     * 获取文件夹树
     *
     * @param userId
     * @return
     */
    fun getFolderTree(userId: String): List<FolderTreeNode>? {
        val folderList = userFileRepository.findFolderListByUserId(userId)
        return folderList?.let { assembleFolderTree(it) }
    }

    /**
     * 拼装文件夹树
     *
     * @param folderList
     * @return
     */
    private fun assembleFolderTree(folderList: List<UserFile>): List<FolderTreeNode> {
        if (CollectionUtils.isEmpty(folderList)) {
            return Lists.newArrayList()
        }
        val folderTreeNodeList: List<FolderTreeNode> = folderList.map {
            assembleFolderTreeNode(it)
        }
        val directoryTreeNodeParentGroup: Map<String, List<FolderTreeNode>> =
            folderTreeNodeList.groupBy { FolderTreeNode::parentId.name }

        folderTreeNodeList.forEach { node ->
            val children: List<FolderTreeNode>? = directoryTreeNodeParentGroup[node.id]
            if (children != null) {
                node.childrens = children
            }
        }
        return folderTreeNodeList.stream().filter { node: FolderTreeNode ->
            CommonConstant.ZERO_LONG.toString() == node.parentId
        }.collect(Collectors.toList())
    }

    /**
     * 秒传文件
     *
     * @param parentId
     * @param filename
     * @param md5
     * @param userId
     * @return
     */
    fun secUpload(parentId: String, filename: String, md5: String, userId: String): Boolean {
        val fileList = fileService.getFileListByMd5(md5)
        return if (fileList != null) {
            val panFile = fileList[CommonConstant.ZERO_INT]
            saveUserFile(
                parentId,
                filename,
                CommonConstant.FolderFlagEnum.NO.code,
                getFileTypeCode(filename),
                panFile.fileId,
                userId,
                panFile.fileSizeDesc
            )
            true
        } else {
            false
        }
    }

    /**
     * 批量转移文件
     *
     * @param fileIds
     * @param parentId
     * @param targetParentId
     * @param userId
     * @return
     */
    fun transfer(fileIds: List<String>, parentId: String, targetParentId: String, userId: String): List<UserFile>? {
        if (!checkIsFolder(targetParentId, userId)) {
            throw BizException(msg = "请选择要转移到的文件夹")
        }
        if (!checkTargetParentIdAvailable(targetParentId, fileIds)) {
            throw BizException(msg = "要转移的文件中包含选中的目标文件夹,请重新选择")
        }
        // 查询所有要被转移的文件信息
        val toBeTransferredFileInfoList: List<UserFile> = userFileRepository.findByIds(
            fileIds
        )
        toBeTransferredFileInfoList.stream().forEach { userFile -> transferOne(userFile, targetParentId, userId) }
        return list(parentId, CommonConstant.ALL_FILE_TYPE, userId)
    }

    /**
     * 转移一个文件
     *
     * @param userFile
     * @param targetParentId
     */
    private fun transferOne(userFile: UserFile, targetParentId: String, userId: String) {
        if (userFile.parentId == targetParentId) {
            return
        }
        val userFileInput = UserFileInput(
            userFile.fileId,
            userFile.userId,
            targetParentId,
            userFile.realFileId,
            userFile.filename,
            userFile.folderFlag,
            userFile.fileSizeDesc,
            userFile.fileType,
            userFile.delFlag,
            userFile.createUser,
            userFile.createTime,
            userId,
            null,
        )

        handleDuplicateFileName(userFileInput)
        // 修改文件信息
        userFileRepository.update(userFileInput)

    }

    /**
     * 处理重复文件名
     *
     * @param userFile
     */
    private fun handleDuplicateFileName(userFile: UserFileInput) {
        var newFileName: String = userFile.filename
        val newFileNameWithoutSuffix: String
        val newFileNameSuffix: String
        val newFileNamePointPosition = newFileName.lastIndexOf(CommonConstant.POINT_STR)
        if (newFileNamePointPosition == CommonConstant.MINUS_ONE_INT) {
            newFileNameWithoutSuffix = newFileName
            newFileNameSuffix = CommonConstant.EMPTY_STR
        } else {
            newFileNameWithoutSuffix = newFileName.substring(CommonConstant.ZERO_INT, newFileNamePointPosition)
            newFileNameSuffix = FileUtil.getFileSuffix(newFileName)
        }
        val rPanUserFileVOList: List<UserFile>? =
            userFileRepository.findByUserIdAndFileTypeAndParentIdAndDelFlag(
                userFile.userId,
                null,
                userFile.parentId,
                CommonConstant.DelFlagEnum.NO.code
            )
        val noDuplicateFileNameFlag =
            rPanUserFileVOList?.stream()?.noneMatch { rPanUserFileVO: UserFile ->
                userFile.filename == rPanUserFileVO.filename
            }
        if (noDuplicateFileNameFlag == true) {
            return
        }
        val duplicateFileNameList = rPanUserFileVOList?.stream()
            ?.map(UserFile::filename)
            ?.filter { fileName: String ->
                fileName.startsWith(
                    newFileNameWithoutSuffix
                )
            }
            ?.filter { fileName: String ->
                val pointPosition: Int = fileName.lastIndexOf(CommonConstant.POINT_STR)
                var fileNameSuffix = CommonConstant.EMPTY_STR
                if (pointPosition != CommonConstant.MINUS_ONE_INT) {
                    fileNameSuffix = FileUtil.getFileSuffix(fileName)
                }
                newFileNameSuffix == fileNameSuffix
            }
            ?.collect(Collectors.toList<Any?>())
        if (CollectionUtils.isEmpty(duplicateFileNameList)) {
            return
        }
        if (duplicateFileNameList != null) {
            newFileName = StringBuilder(newFileNameWithoutSuffix)
                .append(CommonConstant.CN_LEFT_PARENTHESES_STR)
                .append(duplicateFileNameList.size)
                .append(CommonConstant.CN_RIGHT_PARENTHESES_STR)
                .append(newFileNameSuffix)
                .toString()
        }
        userFile.filename = newFileName
    }

    /**
     * 校验目标文件夹是否合法
     * 1、 不是选中的文件夹
     * 2、 不是选中文件夹的子文件夹
     *
     * @param targetParentId
     * @param fileIds
     * @return
     */
    private fun checkTargetParentIdAvailable(targetParentId: String, fileIds: List<String>): Boolean {
        if (fileIds.contains(targetParentId)) {
            return false
        }
        for (fileId in fileIds) {
            if (checkIsChildFolder(targetParentId, userFileRepository.findById(fileId).get())) {
                return false
            }
        }
        return true
    }

    /**
     * 批量复制文件
     *
     * @param fileIds
     * @param parentId
     * @param targetParentId
     * @param userId
     * @return
     */
    fun copy(fileIds: List<String>, parentId: String, targetParentId: String, userId: String): List<UserFile>? {
        if (!checkIsFolder(targetParentId, userId)) {
            throw BizException(msg = "请选择要复制到的文件夹")
        }
        if (!checkTargetParentIdAvailable(targetParentId, fileIds)) {
            throw BizException(msg = "要复制的文件中包含选中的目标文件夹,请重新选择")
        }
        doCopyUserFiles(fileIds, targetParentId, userId)
        return list(parentId, CommonConstant.ALL_FILE_TYPE, userId)
    }

    /**
     * 保存复制文件
     *
     * @param fileIds
     * @param targetParentId
     * @param userId
     * @return
     */
    private fun doCopyUserFiles(fileIds: List<String>, targetParentId: String, userId: String) {
        // 查询所有要被复制的文件信息
        val toBeCopiedFileInfoList: List<UserFile> =
            userFileRepository.findByIds(fileIds)
        val complementToBeCopiedFileInfoList =
            complementToBeCopiedFileInfoList(toBeCopiedFileInfoList.toMutableList(), targetParentId, userId)
        // 批量新增文件信息
        for (userFileInput in complementToBeCopiedFileInfoList) {
            userFileRepository.save(userFileInput)
        }
    }

    /**
     * 补全要复制的文件列表
     *
     * @param toBeCopiedFileInfoList
     * @param targetParentId
     * @param userId
     */
    private fun complementToBeCopiedFileInfoList(
        toBeCopiedFileInfoList: MutableList<UserFile>,
        targetParentId: String,
        userId: String
    ): List<UserFileInput> {
        val allChildUserFileList: MutableList<UserFileInput> = mutableListOf()
        toBeCopiedFileInfoList.stream().forEach { userFile: UserFile ->
            val userFileInput = UserFileInput(
                null,
                userFile.userId,
                targetParentId,
                userFile.realFileId,
                userFile.filename,
                userFile.folderFlag,
                userFile.fileSizeDesc,
                userFile.fileType,
                userFile.delFlag,
                userFile.createUser,
                userFile.createTime,
                userId,
                null,
            )
            handleDuplicateFileName(userFileInput)
            if (checkIsFolder(userFile)) {
                assembleAllChildUserFile(userFile, userFile.fileId, newFileId, userId)
            }
            allChildUserFileList.add(userFileInput)
        }
        return allChildUserFileList
    }

    /**
     * 查找并拼装子文件
     *
     * @param allChildUserFileList
     * @param parentUserFileId
     * @param newParentUserFileId
     * @param userId
     * @return
     */
    private fun assembleAllChildUserFile(
        allChildUserFileList: MutableList<UserFile>,
        parentUserFileId: String,
        newParentUserFileId: String,
        userId: String
    ) {
        val childUserFileList: List<UserFile> = userFileRepository.findFolderListByParentId(parentUserFileId) ?: return
        childUserFileList.stream().forEach { userFile: UserFile ->
            val fileId: String = userFile.getFileId()
            val newFileId: String = IdGenerator.nextId()
            userFile.setParentId(newParentUserFileId)
            userFile.setUserId(userId)
            userFile.setFileId(newFileId)
            userFile.setCreateUser(userId)
            userFile.setCreateTime(Date())
            userFile.setUpdateUser(userId)
            userFile.setUpdateTime(Date())
            allChildUserFileList.add(userFile)
            if (checkIsFolder(userFile)) {
                assembleAllChildUserFile(allChildUserFileList, fileId, newFileId, userId)
            }
        }
    }

    /**
     * 检查目标文件是不是子文件夹
     *
     * @param targetParentId
     * @param userFile
     * @return
     */
    private fun checkIsChildFolder(targetParentId: String, userFile: UserFile): Boolean {
        if (checkIsFolder(userFile)) {
            val allChildrenFile: List<UserFile> = listOf()
            findAllChildUserFile(allChildrenFile.toMutableList(), userFile.fileId)
            if (!CollectionUtils.isEmpty(allChildrenFile)) {
                val childFolderIdList =
                    allChildrenFile.stream().filter(this::checkIsFolder).map<Any>(UserFile::fileId).collect(
                        Collectors.toList()
                    )
                if (childFolderIdList.contains(targetParentId)) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 查询文件的所有子节点
     *
     * @param allChildUserFileList
     * @param parentId
     */
    private fun findAllChildUserFile(allChildUserFileList: MutableList<UserFile>, parentId: String) {
        val childUserFileList: List<UserFile> = userFileRepository.findFolderListByParentId(parentId) ?: return
        allChildUserFileList.addAll(childUserFileList)
        childUserFileList.stream()
            .filter { rPanUserFile: UserFile -> checkIsFolder(rPanUserFile) }
            .forEach { rPanUserFile: UserFile ->
                findAllChildUserFile(
                    allChildUserFileList,
                    rPanUserFile.fileId
                )
            }
    }

    /**
     * 拼装树节点
     * @param [userFile]
     * @return [FolderTreeNode]
     */
    private fun assembleFolderTreeNode(userFile: UserFile): FolderTreeNode {
        return FolderTreeNode(
            userFile.filename,
            userFile.fileId,
            listOf(),
            userFile.parentId,
        )
    }


    /**
     * 根据用户id和文件id查询对应的文件信息
     *
     * @param fileId
     * @param userId
     * @return
     */
    private fun getRPanUserFileByFileIdAndParentId(fileId: String, userId: String): UserFile {
        val userFileInfo = userFileRepository.findById(fileId).get()
        if (Objects.isNull(userFileInfo)) {
            throw BizException(msg = "该用户没有此文件")
        }
        if (userFileInfo.userId != userId) {
            throw BizException(msg = "该用户没有此文件")
        }
        if (CommonConstant.DelFlagEnum.YES.code == userFileInfo.delFlag) {
            throw BizException(msg = "该用户没有此文件")
        }
        return userFileInfo
    }


    /**
     * 文件下载
     *
     * @param fileId
     * @param response
     */
    fun download(fileId: String, response: HttpServletResponse) {
        if (checkIsFolder(fileId)) {
            throw BizException(msg = "不能选择文件夹下载")
        }
        val userFile = userFileRepository.findById(fileId).get()
        val file = fileService.getFileDetail(userFile.realFileId)
        doDownload(file.realPath, response, userFile.filename)
    }

    /**
     * 执行下载文件
     *
     * @param filePath
     * @param response
     * @param filename
     */
    private fun doDownload(filePath: String, response: HttpServletResponse, filename: String) {
        addCommonResponseHeader(response, CommonConstant.APPLICATION_OCTET_STREAM_STR)
        try {
            response.setHeader(
                CommonConstant.CONTENT_DISPOSITION_STR,
                CommonConstant.CONTENT_DISPOSITION_VALUE_PREFIX_STR + String(
                    filename.toByteArray(),
                    Charset.forName(CommonConstant.IOS_8859_1_STR)
                )
            )
        } catch (e: UnsupportedEncodingException) {
            log.error("下载文件失败", e)
        }
        read2OutputStream(filePath, response)
    }


    /**
     * 校验是不是文件夹
     *
     * @param fileId
     * @return
     */
    private fun checkIsFolder(fileId: String): Boolean {
        return checkIsFolder(userFileRepository.findById(fileId).get())
    }

    /**
     * 校验是不是文件夹
     *
     * @param fileId
     * @param userId
     * @return
     */
    private fun checkIsFolder(fileId: String, userId: String): Boolean {
        val findByFileIdAndUserId = userFileRepository.findByFileIdAndUserId(fileId, userId)
        if (findByFileIdAndUserId == null) {
            throw BizException(msg = "文件不存在")
        } else {
            return checkIsFolder(findByFileIdAndUserId)
        }
    }

    /**
     * 校验是不是文件夹
     * @param [userFile]
     * @return [Boolean]
     */
    private fun checkIsFolder(userFile: UserFile): Boolean {

        return CommonConstant.FolderFlagEnum.YES.code == userFile.folderFlag

    }


    /**
     * 上传真实文件
     *
     * @param file
     * @param userId
     * @param md5
     * @param size
     * @return
     */
    private fun uploadRealFile(file: MultipartFile, userId: String, md5: String, size: Long): File? {
        return fileService.save(file, userId, md5, size)
    }

    /**
     * 上传分片并自动合并并转移
     *
     * @param file
     * @param userId
     * @param md5
     * @param chunks
     * @param chunk
     * @param size
     * @param name
     * @return
     */
    private fun uploadRealFileWithChunk(
        file: MultipartFile,
        userId: String,
        md5: String,
        chunks: Int,
        chunk: Int,
        size: Long,
        name: String
    ): File? {
        return fileService.saveWithChunk(file, userId, md5, chunks, chunk, size, name)
    }

}
