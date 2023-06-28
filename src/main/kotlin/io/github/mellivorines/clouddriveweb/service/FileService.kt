package io.github.mellivorines.clouddriveweb.service

import io.github.mellivorines.clouddriveweb.config.CloudDrive
import io.github.mellivorines.clouddriveweb.dao.input.FileInfoInput
import io.github.mellivorines.clouddriveweb.dao.repository.FileInfoRepository
import io.github.mellivorines.clouddriveweb.utils.saveMultiFile
import jakarta.servlet.http.HttpServletRequest
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


/**
 * @Description:文件相关service
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/23
 */
@Service
class FileService(
    private val fileInfoRepository: FileInfoRepository,
    private val cloudDrive: CloudDrive
) {


    fun uploadFile(file: MultipartFile, request: HttpServletRequest): Any? {

        val md5Hex = DigestUtils.md5Hex(file.inputStream)
        val upload = saveMultiFile(cloudDrive.path, arrayOf(file))
        val userId = "da8a7dc3994e40ab869a38a1f7cc4431"
        val fileInfoInput =
            FileInfoInput(
                null,
                userId,
                md5Hex,
                null,
                file.size,
                file.originalFilename,
                null,
                null,
                null,
                null,
                0,
                1,
                1,
                0,
                null,
                2
            )
        return if (upload) {
            fileInfoRepository.save(fileInfoInput)
            "文件上传成功！"
        } else {
            "文件上传失败！"
        }
    }


    fun uploadFolder(folder: Array<MultipartFile>, request: HttpServletRequest): Any? {
        return saveMultiFile(cloudDrive.path, folder)
    }
}
