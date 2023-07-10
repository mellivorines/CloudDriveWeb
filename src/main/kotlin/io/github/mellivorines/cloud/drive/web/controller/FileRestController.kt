package io.github.mellivorines.cloud.drive.web.controller

import io.github.mellivorines.cloud.drive.web.common.annotation.NeedLogin
import io.github.mellivorines.cloud.drive.web.dao.input.UserFileInput
import io.github.mellivorines.cloud.drive.web.model.ResultModel
import io.github.mellivorines.cloud.drive.web.model.fail
import io.github.mellivorines.cloud.drive.web.model.success
import io.github.mellivorines.cloud.drive.web.model.vo.FileUploadVO
import io.github.mellivorines.cloud.drive.web.model.vo.TransferAndCopyVO
import io.github.mellivorines.cloud.drive.web.service.UserFileService
import io.github.mellivorines.cloud.drive.web.utils.UserIdUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/2
 */
@RestController
@Validated
@Tag(name = "文件接口")
@RequestMapping("/file")
class FileRestController(private val userFileService: UserFileService) {

    /**
     * 获取文件列表
     *
     * @param parentId
     * @param fileTypes
     * @return
     */
    @Operation(summary = "获取文件列表")
    @GetMapping("files")
    @NeedLogin
    fun list(
        @RequestParam(value = "parentId", required = false) parentId: @NotNull(message = "父id不能为空") String,
        @RequestParam(value = "userId", required = false) userId: @NotNull(message = "用户id不能为空") String,
        @RequestParam(name = "fileTypes", required = false, defaultValue = "-1") fileTypes: String?
    ): ResultModel {
        return success(userFileService.list(parentId, fileTypes, userId))
    }

    /**
     * 新建文件夹
     *
     * @param createFolderPO
     * @return
     */
    @Operation(summary = "新建文件夹")
    @PostMapping("file/folder")
    @NeedLogin
    fun createFolder(@Validated @RequestBody saveUserFileInput: UserFileInput): ResultModel {
        return success(
            userFileService.createFolder(
                saveUserFileInput.parentId,
                saveUserFileInput.filename,
                saveUserFileInput.userId
            )
        )
    }

    /**
     * 文件重命名
     * @param [updateUserFileInput]
     * @return [ResultModel]返回结果
     */
    @Operation(summary = "文件重命名")
    @PutMapping("file")
    @NeedLogin
    fun updateFilename(@Validated @RequestBody updateUserFileInput: UserFileInput): ResultModel {
        return success(userFileService.updateFilename(updateUserFileInput))
    }

    /**
     * 删除文件(批量)
     *
     * @param deletePO
     * @return
     */
    @Operation(summary = "删除文件(批量)")
    @DeleteMapping("file")
    @NeedLogin
    fun delete(@Validated @RequestBody fileIds: List<String>, parentId: String, userId: String): ResultModel {
        return success(userFileService.delete(fileIds, parentId, userId))
    }

    /**
     * 上传文件
     *
     * @param fileUploadVO
     * @return
     */
    @Operation(summary = "上传文件")
    @PostMapping("file/upload")
    @NeedLogin
    fun upload(@Validated fileUploadVO: FileUploadVO): ResultModel {
        return if (fileUploadVO.isChunked()) {
            success(
                userFileService.uploadWithChunk(
                    fileUploadVO.file,
                    fileUploadVO.parentId,
                    fileUploadVO.userId,
                    fileUploadVO.md5,
                    fileUploadVO.chunks!!,
                    fileUploadVO.chunk!!,
                    fileUploadVO.size,
                    fileUploadVO.name
                )
            )
        } else success(
            userFileService.upload(
                fileUploadVO.file,
                fileUploadVO.parentId,
                fileUploadVO.userId,
                fileUploadVO.md5,
                fileUploadVO.size
            )
        )
    }

    /**
     * 秒传文件
     *
     * @param fileSecUploadPO
     * @return
     */
    @Operation(summary = "秒传文件")
    @PostMapping("file/sec-upload")
    @NeedLogin
    fun secUpload(@Validated @RequestBody fileSecUploadPO: FileUploadVO): ResultModel {
        return if (userFileService.secUpload(
                fileSecUploadPO.parentId,
                fileSecUploadPO.name,
                fileSecUploadPO.md5,
                fileSecUploadPO.userId
            )
        ) {
            success("上传成功")
        } else fail("MD5不存在")
    }

    /**
     * 下载文件(只支持单个下载)
     *
     * @param fileId
     * @param response
     */
    @Operation(summary = "下载文件(只支持单个下载)")
    @GetMapping("file/download")
    @NeedLogin
    fun download(
        @RequestParam(value = "fileId", required = false) fileId: String,
        @RequestParam(value = "userId", required = false) userId: String,
        response: HttpServletResponse
    ) {
        userFileService.download(fileId, response, userId)
    }

    /**
     * 获取文件夹树
     *
     * @return
     */
    @Operation(summary = "获取文件夹树")
    @GetMapping("file/folder/tree")
    @NeedLogin
    fun getFolderTree(
        @RequestParam(value = "userId", required = false) userId: String
    ): ResultModel {
        return success(userFileService.getFolderTree(userId))
    }

    /**
     * 转移文件(批量)
     *
     * @param transferAndCopyVO
     * @return
     */
    @Operation(summary = "转移文件(批量)")
    @PostMapping("file/transfer")
    @NeedLogin
    fun transfer(@Validated @RequestBody transferAndCopyVO: TransferAndCopyVO): ResultModel {
        return success(
            userFileService.transfer(
                transferAndCopyVO.fileIds,
                transferAndCopyVO.parentId,
                transferAndCopyVO.targetParentId,
                transferAndCopyVO.userId
            )
        )
    }

    /**
     * 复制文件(批量)
     *
     * @param copyVO
     * @return
     */
    @Operation(summary = "复制文件(批量)")
    @PostMapping("file/copy")
    @NeedLogin
    fun copy(@Validated @RequestBody copyVO: TransferAndCopyVO): ResultModel {
        return success(
            userFileService.copy(
                copyVO.fileIds,
                copyVO.parentId,
                copyVO.targetParentId,
                copyVO.userId,
            )
        )
    }

    /**
     * 通过文件名搜索文件列表
     *
     * @param keyword
     * @param fileTypes
     * @return
     */
    @Operation(summary = "通过文件名搜索文件列表")
    @GetMapping("file/search")
    @NeedLogin
    fun search(
        @RequestParam(value = "keyword", required = false) keyword: String,
        @RequestParam(value = "userId", required = false) userId: String,
        @RequestParam(name = "fileTypes", required = false, defaultValue = "-1") fileTypes: String
    ): ResultModel {
        return success(userFileService.search(keyword, fileTypes, userId))
    }

    /**
     * 查询文件详情
     *
     * @param fileId
     * @return
     */
    @Operation(summary = "查询文件详情")
    @GetMapping("file")
    @NeedLogin
    fun detail(
        @RequestParam(value = "fileId", required = false) fileId: String,
        @RequestParam(value = "userId", required = false) userId: String
    ): ResultModel {
        return success(userFileService.detail(fileId, userId))
    }

    /**
     * 获取面包屑列表
     *
     * @return
     */
    @Operation(summary = "获取面包屑列表")
    @GetMapping("file/breadcrumbs")
    @NeedLogin
    fun getBreadcrumbs(
        @RequestParam(value = "fileId", required = false) fileId: String,
        @RequestParam(value = "userId", required = false) userId: String
    ): ResultModel {
        return success(userFileService.getBreadcrumbs(fileId, userId))
    }

    /**
     * 预览单个文件
     *
     * @param fileId
     * @return
     */
    @Operation(summary = "预览单个文件")
    @GetMapping("preview")
    @NeedLogin
    fun preview(
        @RequestParam(value = "fileId", required = false) fileId: String,
        @RequestParam(value = "userId", required = false) userId: String,
        response: HttpServletResponse
    ) {
        userFileService.preview(fileId, response, userId)
    }
}
