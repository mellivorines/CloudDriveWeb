/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80031 (8.0.31)
 Source Host           : localhost:3306
 Source Schema         : CloudDrive

 Target Server Type    : MySQL
 Target Server Version : 80031 (8.0.31)
 File Encoding         : 65001

 Date: 02/07/2023 22:55:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cloud_drive_file
-- ----------------------------
DROP TABLE IF EXISTS `cloud_drive_file`;
CREATE TABLE `cloud_drive_file` (
  `file_id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '文件id',
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件名称',
  `real_path` varchar(700) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件物理路径',
  `file_size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件实际大小',
  `file_size_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件大小展示字符',
  `file_suffix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件后缀',
  `file_preview_content_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件预览的响应头Content-Type的值',
  `md5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件唯一标识',
  `create_user` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`file_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='物理文件信息表';

-- ----------------------------
-- Table structure for cloud_drive_share
-- ----------------------------
DROP TABLE IF EXISTS `cloud_drive_share`;
CREATE TABLE `cloud_drive_share` (
  `share_id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '分享id',
  `share_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '分享名称',
  `share_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '分享类型（0 有提取码）',
  `share_day_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '分享类型（0 永久有效；1 7天有效；2 30天有效）',
  `share_day` tinyint(1) NOT NULL DEFAULT '0' COMMENT '分享有效天数（永久有效为0）',
  `share_end_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分享结束时间',
  `share_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '分享链接地址',
  `share_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '分享提取码',
  `share_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '分享状态（0 正常；1 有文件被删除）',
  `create_user` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '分享创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`share_id`) USING BTREE,
  UNIQUE KEY `uk_create_user_time` (`create_user`,`create_time`) USING BTREE COMMENT '创建人、创建时间唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户分享表';

-- ----------------------------
-- Table structure for cloud_drive_share_file
-- ----------------------------
DROP TABLE IF EXISTS `cloud_drive_share_file`;
CREATE TABLE `cloud_drive_share_file` (
  `id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `share_id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '分享id',
  `file_id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '文件记录ID',
  `create_user` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '分享创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_share_id_file_id` (`share_id`,`file_id`) USING BTREE COMMENT '分享ID、文件ID联合唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户分享文件表';

-- ----------------------------
-- Table structure for cloud_drive_user
-- ----------------------------
DROP TABLE IF EXISTS `cloud_drive_user`;
CREATE TABLE `cloud_drive_user` (
  `user_id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '用户id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '密码',
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '随机盐值',
  `question` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '密保问题',
  `answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '密保答案',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `uk_username` (`username`) USING BTREE COMMENT '用户名唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户信息表';

-- ----------------------------
-- Table structure for cloud_drive_user_file
-- ----------------------------
DROP TABLE IF EXISTS `cloud_drive_user_file`;
CREATE TABLE `cloud_drive_user_file` (
  `file_id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '文件记录ID',
  `user_id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '用户ID',
  `parent_id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '上级文件夹ID,顶级文件夹为0',
  `real_file_id` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '真实文件id',
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件名',
  `folder_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否是文件夹 （0 否 1 是）',
  `file_size_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件大小展示字符',
  `file_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '文件类型（1 普通文件 2 压缩文件 3 excel 4 word 5 pdf 6 txt 7 图片 8 音频 9 视频 10 ppt 11 源码文件 12 csv）',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识（0 否 1 是）',
  `create_user` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`file_id`) USING BTREE,
  KEY `index_file_list` (`user_id`,`del_flag`,`parent_id`,`file_type`,`file_id`,`filename`,`folder_flag`,`file_size_desc`,`create_time`,`update_time`) USING BTREE COMMENT '查询文件列表索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户文件信息表';

-- ----------------------------
-- Table structure for cloud_drive_user_search_history
-- ----------------------------
DROP TABLE IF EXISTS `cloud_drive_user_search_history`;
CREATE TABLE `cloud_drive_user_search_history` (
  `id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `user_id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '用户id',
  `search_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '搜索文案',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_user_id_search_content_update_time` (`user_id`,`search_content`,`update_time`) USING BTREE COMMENT '用户id、搜索内容和更新时间唯一索引',
  UNIQUE KEY `uk_user_id_search_content` (`user_id`,`search_content`) USING BTREE COMMENT '用户id和搜索内容唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户搜索历史表';

SET FOREIGN_KEY_CHECKS = 1;
