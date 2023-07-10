package io.github.mellivorines.cloud.drive.web.model.tree

data class FolderTreeNode(
    var label: String,
    var id: String,
    var childrens: List<FolderTreeNode>,
    var parentId: String
)
