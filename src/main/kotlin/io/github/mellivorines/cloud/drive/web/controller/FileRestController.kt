//package io.github.mellivorines.cloud.drive.web.controller
//
//import io.github.mellivorines.cloud.drive.web.common.annotation.NeedLogin
//import io.github.mellivorines.cloud.drive.web.utils.UserIdUtil
//import io.swagger.v3.oas.annotations.Operation
//import io.swagger.v3.oas.annotations.tags.Tag
//import jakarta.servlet.http.HttpServletResponse
//import org.springframework.http.MediaType
//import org.springframework.validation.annotation.Validated
//import org.springframework.web.bind.annotation.*
//import javax.validation.constraints.NotBlank
//import javax.validation.constraints.NotNull
//
//
///**
// * @Description:
// *
// * @author lilinxi
// * @version 1.0.0
// * @since 2023/7/2
// */
//@RestController
//@Validated
//@Tag(name = "文件接口")
//@RequestMapping("/file")
//class FileRestController(private val userFileService: UserFileService) {
//
//    /**
//     * 获取文件列表
//     *
//     * @param parentId
//     * @param fileTypes
//     * @return
//     */
//    @Operation(summary = "获取文件列表")
//    @GetMapping("files")
//    @NeedLogin
//    fun list(
//        @RequestParam(value = "parentId", required = false) parentId: @NotNull(message = "父id不能为空") Long?,
//        @RequestParam(name = "fileTypes", required = false, defaultValue = "-1") fileTypes: String?
//    ): R<List<RPanUserFileVO?>?>? {
//        return R.data(iUserFileService.list(parentId, fileTypes, UserIdUtil.get()))
//    }
//
//    /**
//     * 新建文件夹
//     *
//     * @param createFolderPO
//     * @return
//     */
//    @ApiOperation(
//        value = "新建文件夹",
//        notes = "该接口提供了新建文件夹的功能",
//        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
//    )
//    @PostMapping("file/folder")
//    @NeedLogin
//    fun createFolder(@Validated @RequestBody createFolderPO: CreateFolderPO): R<List<RPanUserFileVO?>?>? {
//        return R.data(
//            iUserFileService.createFolder(
//                createFolderPO.getParentId(),
//                createFolderPO.getFolderName(),
//                UserIdUtil.get()
//            )
//        )
//    }
//
//    /**
//     * 文件重命名
//     *
//     * @param updateFileNamePO
//     * @return
//     */
//    @ApiOperation(
//        value = "文件重命名",
//        notes = "该接口提供了文件重命名的功能",
//        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
//    )
//    @PutMapping("file")
//    @NeedLogin
//    fun updateFilename(@Validated @RequestBody updateFileNamePO: UpdateFileNamePO): R<List<RPanUserFileVO?>?>? {
//        return R.data(
//            iUserFileService.updateFilename(
//                updateFileNamePO.getFileId(),
//                updateFileNamePO.getNewFilename(),
//                UserIdUtil.get()
//            )
//        )
//    }
//
//    /**
//     * 删除文件(批量)
//     *
//     * @param deletePO
//     * @return
//     */
//    @ApiOperation(
//        value = "删除文件(批量)",
//        notes = "该接口提供了删除文件(批量)的功能",
//        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
//    )
//    @DeleteMapping("file")
//    @NeedLogin
//    fun delete(@Validated @RequestBody deletePO: DeletePO): R<List<RPanUserFileVO?>?>? {
//        return R.data(iUserFileService.delete(deletePO.getParentId(), deletePO.getFileIds(), UserIdUtil.get()))
//    }
//
//    /**
//     * 上传文件
//     *
//     * @param fileUploadPO
//     * @return
//     */
//    @ApiOperation(
//        value = "上传文件",
//        notes = "该接口提供了上传文件的功能",
//        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
//    )
//    @PostMapping("file/upload")
//    @NeedLogin
//    fun upload(@Validated fileUploadPO: FileUploadPO): R<List<RPanUserFileVO?>?>? {
//        return if (fileUploadPO.isChunked()) {
//            R.data(
//                iUserFileService.uploadWithChunk(
//                    fileUploadPO.getFile(),
//                    fileUploadPO.getParentId(),
//                    UserIdUtil.get(),
//                    fileUploadPO.getMd5(),
//                    fileUploadPO.getChunks(),
//                    fileUploadPO.getChunk(),
//                    fileUploadPO.getSize(),
//                    fileUploadPO.getName()
//                )
//            )
//        } else R.data(
//            iUserFileService.upload(
//                fileUploadPO.getFile(),
//                fileUploadPO.getParentId(),
//                UserIdUtil.get(),
//                fileUploadPO.getMd5(),
//                fileUploadPO.getSize()
//            )
//        )
//    }
//
//    /**
//     * 秒传文件
//     *
//     * @param fileSecUploadPO
//     * @return
//     */
//    @ApiOperation(
//        value = "秒传文件",
//        notes = "该接口提供了秒传文件的功能",
//        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
//    )
//    @PostMapping("file/sec-upload")
//    @NeedLogin
//    fun secUpload(@Validated @RequestBody fileSecUploadPO: FileSecUploadPO): R? {
//        return if (iUserFileService.secUpload(
//                fileSecUploadPO.getParentId(),
//                fileSecUploadPO.getName(),
//                fileSecUploadPO.getMd5(),
//                UserIdUtil.get()
//            )
//        ) {
//            R.success()
//        } else R.fail("MD5不存在")
//    }
//
//    /**
//     * 下载文件(只支持单个下载)
//     *
//     * @param fileId
//     * @param response
//     */
//    @ApiOperation(
//        value = "下载文件(只支持单个下载)",
//        notes = "该接口提供了下载文件(只支持单个下载)的功能",
//        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
//        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
//    )
//    @GetMapping("file/download")
//    @NeedLogin
//    fun download(
//        @RequestParam(value = "fileId", required = false) fileId: @NotNull(message = "请选择要下载的文件") Long?,
//        response: HttpServletResponse?
//    ) {
//        iUserFileService.download(fileId, response, UserIdUtil.get())
//    }
//
//    /**
//     * 获取文件夹树
//     *
//     * @return
//     */
//    @ApiOperation(
//        value = "获取文件夹树",
//        notes = "该接口提供了获取文件夹树的功能",
//        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
//        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
//    )
//    @GetMapping("file/folder/tree")
//    @NeedLogin
//    fun getFolderTree(): R<List<FolderTreeNode?>?>? {
//        return R.data(iUserFileService.getFolderTree(UserIdUtil.get()))
//    }
//
//    /**
//     * 转移文件(批量)
//     *
//     * @param transferPO
//     * @return
//     */
//    @ApiOperation(
//        value = "转移文件(批量)",
//        notes = "该接口提供了转移文件(批量)的功能",
//        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
//    )
//    @PostMapping("file/transfer")
//    @NeedLogin
//    fun transfer(@Validated @RequestBody transferPO: TransferPO): R<List<RPanUserFileVO?>?>? {
//        return R.data(
//            iUserFileService.transfer(
//                transferPO.getFileIds(),
//                transferPO.getParentId(),
//                transferPO.getTargetParentId(),
//                UserIdUtil.get()
//            )
//        )
//    }
//
//    /**
//     * 复制文件(批量)
//     *
//     * @param copyPO
//     * @return
//     */
//    @ApiOperation(
//        value = "复制文件(批量)",
//        notes = "该接口提供了复制文件(批量)的功能",
//        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
//        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
//    )
//    @PostMapping("file/copy")
//    @NeedLogin
//    fun copy(@Validated @RequestBody copyPO: CopyPO): R<List<RPanUserFileVO?>?>? {
//        return R.data(
//            iUserFileService.copy(
//                copyPO.getFileIds(),
//                copyPO.getParentId(),
//                copyPO.getTargetParentId(),
//                UserIdUtil.get()
//            )
//        )
//    }
//
//    /**
//     * 通过文件名搜索文件列表
//     *
//     * @param keyword
//     * @param fileTypes
//     * @return
//     */
//    @ApiOperation(
//        value = "通过文件名搜索文件列表",
//        notes = "该接口提供了通过文件名搜索文件列表的功能",
//        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
//        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
//    )
//    @GetMapping("file/search")
//    @NeedLogin
//    fun search(
//        @RequestParam(value = "keyword", required = false) keyword: @NotBlank(message = "关键字不能为空") String?,
//        @RequestParam(name = "fileTypes", required = false, defaultValue = "-1") fileTypes: String?
//    ): R<List<RPanUserFileVO?>?>? {
//        return R.data(iUserFileService.search(keyword, fileTypes, UserIdUtil.get()))
//    }
//
//    /**
//     * 查询文件详情
//     *
//     * @param fileId
//     * @return
//     */
//    @ApiOperation(
//        value = "查询文件详情",
//        notes = "该接口提供了查询文件详情的功能",
//        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
//        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
//    )
//    @GetMapping("file")
//    @NeedLogin
//    fun detail(
//        @RequestParam(
//            value = "fileId",
//            required = false
//        ) fileId: @NotNull(message = "文件id不能为空") Long?
//    ): R<RPanUserFileVO?>? {
//        return R.data(iUserFileService.detail(fileId, UserIdUtil.get()))
//    }
//
//    /**
//     * 获取面包屑列表
//     *
//     * @return
//     */
//    @ApiOperation(
//        value = "获取面包屑列表",
//        notes = "该接口提供了获取面包屑列表的功能",
//        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
//        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
//    )
//    @GetMapping("file/breadcrumbs")
//    @NeedLogin
//    fun getBreadcrumbs(
//        @RequestParam(
//            value = "fileId",
//            required = false
//        ) fileId: @NotNull(message = "文件id不能为空") Long?
//    ): R<List<BreadcrumbVO?>?>? {
//        return R.data(iUserFileService.getBreadcrumbs(fileId, UserIdUtil.get()))
//    }
//
//    /**
//     * 预览单个文件
//     *
//     * @param fileId
//     * @return
//     */
//    @ApiOperation(
//        value = "预览单个文件",
//        notes = "该接口提供了预览单个文件的功能",
//        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
//        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
//    )
//    @GetMapping("preview")
//    @NeedLogin
//    fun preview(
//        @RequestParam(value = "fileId", required = false) fileId: @NotNull(message = "文件id不能为空") Long?,
//        response: HttpServletResponse?
//    ) {
//        iUserFileService.preview(fileId, response, UserIdUtil.get())
//    }
//}