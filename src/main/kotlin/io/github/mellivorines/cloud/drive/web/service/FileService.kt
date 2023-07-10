package io.github.mellivorines.cloud.drive.web.service

import io.github.mellivorines.cloud.drive.web.dao.entity.File
import io.github.mellivorines.cloud.drive.web.dao.input.FileInput
import io.github.mellivorines.cloud.drive.web.dao.repository.FileRepository
import io.github.mellivorines.cloud.drive.web.exception.BizException
import io.github.mellivorines.cloud.drive.web.storage.core.StorageManager
import io.github.mellivorines.cloud.drive.web.utils.FileUtil
import lombok.extern.slf4j.Slf4j
import org.apache.commons.lang3.ObjectUtils
import org.apache.commons.lang3.StringUtils
import org.reflections.Reflections.log
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/3
 */
@Service
@Slf4j
class FileService(
    private val storageManager: StorageManager,
    private val fileRepository: FileRepository
) {

    /**
     * 保存物理文件
     *
     * @param file
     * @param userId
     * @param md5
     * @param size
     * @return
     */
    fun save(file: MultipartFile, userId: String, md5: String, size: Long): File? {
        val fileInput =
            file.originalFilename?.let { uploadFile(file)?.let { it1 -> assembleRPanFile(it1, userId, md5, size, it) } }
        if (fileInput != null) {
            saveFileInfo(fileInput)
        }
        return fileInput?.toEntity() ?: throw BizException(msg = "保存物理文件失败！")
    }

    /**
     * 保存分片文件
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
    fun saveWithChunk(
        file: MultipartFile,
        userId: String,
        md5: String,
        chunks: Int,
        chunk: Int,
        size: Long,
        name: String
    ): File? {
        val filePath = uploadFileWithChunk(file, md5, chunks, chunk, size, name)
        if (StringUtils.isNoneBlank(filePath)) {
            val fileInput = filePath?.let { assembleRPanFile(it, userId, md5, size, name) }
            if (fileInput != null) {
                saveFileInfo(fileInput)
            }
            return fileInput?.toEntity() ?: throw BizException(msg = "保存分片文件！")
        }
        return null
    }

    /**
     * 删除物理文件
     *
     * @param fileIds
     * @return
     */
    fun delete(fileIds: List<String>) {
        // TODO 集成MQ 优化成异步消息操作 加上重试机制
        deletePhysicalFiles(fileIds)
        deleteFileInfos(fileIds)
    }

    /**
     * 获取实体文件详情
     *
     * @param realFileId
     * @return
     */
    fun getFileDetail(realFileId: String): File {
        val file: File = fileRepository.findById(realFileId).get()
        if (ObjectUtils.isEmpty(file)) {
            throw BizException(msg = "实体文件不存在")
        }
        return file
    }

    fun getFileListByMd5(md5: String): List<File>? {
        return fileRepository.findByMd5(md5)
    }

    /**
     * 保存物理文件信息
     *
     * @param fileInput
     */
    private fun saveFileInfo(fileInput: FileInput) {
        fileRepository.save(fileInput)
    }


    /**
     * 上传物理文件
     *
     * @param file
     */
    private fun uploadFile(file: MultipartFile): String? {
        return try {
            file.originalFilename?.let { storageManager.store(file.inputStream, file.size, it) }
        } catch (e: IOException) {
            log.error("上传失败", e)
            throw BizException(msg = "上传失败")
        }
    }

    /**
     * 拼装物理文件信息
     *
     * @param filePath
     * @param userId
     * @param md5
     * @param size
     * @param name
     * @return
     */
    private fun assembleRPanFile(filePath: String, userId: String, md5: String, size: Long, name: String): FileInput? {
        val suffix: String = FileUtil.getFileSuffix(name)
        val newFileName: String = FileUtil.getFilename(filePath)
        val fileInput = FileUtil.getContentType(filePath)?.let {
            FileInput(
                fileId = null,
                filename = newFileName,
                realPath = filePath,
                fileSize = size.toString(),
                fileSizeDesc = FileUtil.getFileSizeDesc(size),
                fileSuffix = suffix,
                filePreviewContentType = it,
                md5 = md5,
                createUser = userId,
                createTime = null
            )
        }
        return fileInput
    }

    /**
     * 删除物理文件信息
     *
     * @param fileIds
     * @return
     */
    private fun deleteFileInfos(fileIds: List<String>) {
        fileRepository.deleteByIds(fileIds)
    }

    /**
     * 批量删除物理文件
     *
     * @param fileIds
     * @return
     */
    private fun deletePhysicalFiles(fileIds: List<String>) {
        val fileList =
            fileRepository.findByIds(fileIds)
        fileList.stream().map<Any>(File::realPath).forEach { path: Any? ->
            try {
                storageManager.delete(path.toString())
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 上传分片文件
     *
     * @param file
     * @param md5
     * @param chunks
     * @param chunk
     * @param size
     * @param name
     * @return 分片文件是否都上传完毕
     */
    private fun uploadFileWithChunk(
        file: MultipartFile,
        md5: String,
        chunks: Int,
        chunk: Int,
        size: Long,
        name: String
    ): String? {
        return try {
            storageManager.storeWithChunk(file.inputStream, md5, chunks, chunk, size, file.size, name)
        } catch (e: IOException) {
            e.printStackTrace()
            throw BizException(msg = "上传失败!")
        }
    }


}
