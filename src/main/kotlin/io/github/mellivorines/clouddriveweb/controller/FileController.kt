package io.github.mellivorines.clouddriveweb.controller

import ch.qos.logback.core.util.FileUtil
import io.github.mellivorines.clouddriveweb.model.ResultModel
import io.github.mellivorines.clouddriveweb.model.success
import io.github.mellivorines.clouddriveweb.service.FileService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


/**
 * @Description:文件相关Controller
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/10
 */
@Tag(name = "文件管理")
@RestController
@RequestMapping("/file")
@Transactional
class FileController(private val fileService: FileService) {

    @Operation(summary = "文件上传")
    @PostMapping("uploadFile")
    fun uploadFile(@RequestParam("file") file: MultipartFile, request: HttpServletRequest): ResultModel {
        return success(fileService.uploadFile(file,request))
    }


    @Operation(summary = "文件夹上传")
    @PostMapping("uploadFolder")
    fun uploadFolder(folder: Array<MultipartFile>,request: HttpServletRequest): ResultModel {
        return success(fileService.uploadFolder(folder,request))
    }
}
