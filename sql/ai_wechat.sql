/*

记录问答日志
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;


DROP DATABASE IF EXISTS `ai_wechat`;
CREATE DATABASE `ai_wechat` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

-- ----------------------------
--  Table structure for `QA`
-- ----------------------------
DROP TABLE IF EXISTS `QA`;
CREATE TABLE `QA` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键自增主键',
  `question` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '问题',
  `answer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '答案',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '用户名',
  `session_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT 'session id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
