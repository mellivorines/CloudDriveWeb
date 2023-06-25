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

 Date: 26/06/2023 02:37:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_info
-- ----------------------------
DROP TABLE IF EXISTS `file_info`;
CREATE TABLE `file_info` (
  `file_info_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件ID',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `file_md5` varchar(32) DEFAULT NULL COMMENT 'md5值，第一次上传记录',
  `file_pid` varchar(10) DEFAULT NULL COMMENT '父级ID',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小',
  `file_name` varchar(200) DEFAULT NULL COMMENT '文件名称',
  `file_cover` varchar(100) DEFAULT NULL COMMENT '封面',
  `file_path` varchar(100) DEFAULT NULL COMMENT '文件路径',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `folder_type` tinyint(1) DEFAULT NULL COMMENT '0:文件 1:目录',
  `file_category` tinyint(1) DEFAULT NULL COMMENT '1:视频 2:音频  3:图片 4:文档 5:其他',
  `file_type` tinyint(1) DEFAULT NULL COMMENT ' 1:视频 2:音频  3:图片 4:pdf 5:doc 6:excel 7:txt 8:code 9:zip 10:其他',
  `status` tinyint(1) DEFAULT NULL COMMENT '0:转码中 1转码失败 2:转码成功',
  `recovery_time` datetime DEFAULT NULL COMMENT '回收站时间',
  `del_flag` tinyint(1) DEFAULT '2' COMMENT '删除标记 0:删除  1:回收站  2:正常',
  PRIMARY KEY (`file_info_id`) USING BTREE,
  KEY `idx_create_time` (`create_time`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_md5` (`file_md5`) USING BTREE,
  KEY `idx_file_pid` (`file_pid`),
  KEY `idx_del_flag` (`del_flag`),
  KEY `idx_recovery_time` (`recovery_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件信息';

-- ----------------------------
-- Records of file_info
-- ----------------------------
BEGIN;
INSERT INTO `file_info` (`file_info_id`, `user_id`, `file_md5`, `file_pid`, `file_size`, `file_name`, `file_cover`, `file_path`, `create_time`, `last_update_time`, `folder_type`, `file_category`, `file_type`, `status`, `recovery_time`, `del_flag`) VALUES ('0LZ8cAWtiM', '3178033358', '15d3b1d059bd580ea828cf264f6c7c3c', '0', 34974, 'WechatIMG361.jpeg', '202306/31780333580LZ8cAWtiM_.jpeg', '202306/31780333580LZ8cAWtiM.jpeg', '2023-06-10 15:09:27', '2023-06-10 15:12:25', 0, 3, 3, 2, '2023-06-10 15:12:10', 2);
INSERT INTO `file_info` (`file_info_id`, `user_id`, `file_md5`, `file_pid`, `file_size`, `file_name`, `file_cover`, `file_path`, `create_time`, `last_update_time`, `folder_type`, `file_category`, `file_type`, `status`, `recovery_time`, `del_flag`) VALUES ('CSdKA1WHcK', '3178033358', NULL, '0', NULL, 'mytest', NULL, NULL, '2023-06-10 15:13:44', '2023-06-10 15:13:44', 1, NULL, NULL, 2, NULL, 2);
INSERT INTO `file_info` (`file_info_id`, `user_id`, `file_md5`, `file_pid`, `file_size`, `file_name`, `file_cover`, `file_path`, `create_time`, `last_update_time`, `folder_type`, `file_category`, `file_type`, `status`, `recovery_time`, `del_flag`) VALUES ('P4jcIvjfSd', '3178033358', 'c447dee84adf50a6101a323902356d3f', '0', 60416, '附件6 中国红十字会志愿者登记表.doc', NULL, '202306/3178033358P4jcIvjfSd.doc', '2023-06-10 15:13:06', '2023-06-10 15:13:06', 0, 5, 10, 2, NULL, 2);
COMMIT;

-- ----------------------------
-- Table structure for file_share
-- ----------------------------
DROP TABLE IF EXISTS `file_share`;
CREATE TABLE `file_share` (
  `file_share_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分享ID',
  `file_info_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件ID',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `valid_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型 0:1天 1:7天 2:30天 3:永久有效',
  `expire_time` datetime DEFAULT NULL COMMENT '失效时间',
  `share_time` datetime DEFAULT NULL COMMENT '分享时间',
  `code` varchar(5) DEFAULT NULL COMMENT '提取码',
  `show_count` int DEFAULT '0' COMMENT '浏览次数',
  PRIMARY KEY (`file_share_id`) USING BTREE,
  KEY `idx_file_id` (`file_info_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_share_time` (`share_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='分享信息';

-- ----------------------------
-- Records of file_share
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `nick_name` varchar(20) DEFAULT NULL COMMENT '昵称',
  `user_name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名：可以使用邮箱或者电话号码',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `user_third_info_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户第三方信息ID',
  `join_time` datetime DEFAULT NULL COMMENT '加入时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `status` tinyint DEFAULT NULL COMMENT '0:禁用 1:正常',
  `use_space` bigint DEFAULT '0' COMMENT '使用空间单位byte',
  `total_space` bigint DEFAULT NULL COMMENT '总空间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `key_email` (`user_name`),
  UNIQUE KEY `key_nick_name` (`nick_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息';

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` (`user_id`, `nick_name`, `user_name`, `password`, `user_third_info_id`, `join_time`, `last_login_time`, `status`, `use_space`, `total_space`) VALUES ('da8a7dc3994e40ab869a38a1f7cc4431', 'linxi', 'lilinxi015@163.com', '654594Mi', NULL, '2023-06-25 22:59:49', '2023-06-25 22:59:49', 1, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for user_third_info
-- ----------------------------
DROP TABLE IF EXISTS `user_third_info`;
CREATE TABLE `user_third_info` (
  `user_third_info_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户第三方信息ID',
  `third_open_id` varchar(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '第三方OpenID',
  `third_avatar` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '第三方头像',
  `type` int DEFAULT NULL COMMENT '第三方信息类型：1QQ;2微信;',
  PRIMARY KEY (`user_third_info_id`),
  KEY `user_third_info_id` (`user_third_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户第三方信息';

-- ----------------------------
-- Records of user_third_info
-- ----------------------------
BEGIN;
INSERT INTO `user_third_info` (`user_third_info_id`, `third_open_id`, `third_avatar`, `type`) VALUES ('3178033357', 'asdas', 'dasda', NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
