package io.github.mellivorines.cloud.drive.web.model.tree

import io.github.mellivorines.cloud.drive.web.dao.entity.UserFile

data class FolderTreeNode(
    var label:String,
    var id:String,
    var childrens:List<FolderTreeNode>,
    var parentId:String
) {
}
