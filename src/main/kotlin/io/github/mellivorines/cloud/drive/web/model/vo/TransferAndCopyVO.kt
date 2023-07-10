package io.github.mellivorines.cloud.drive.web.model.vo

data class TransferAndCopyVO(
    var fileIds: List<String>,
    var parentId: String,
    var userId: String,
    var targetParentId: String
)
