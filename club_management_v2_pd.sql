-- 创建数据库 (英文版 v2)
CREATE DATABASE IF NOT EXISTS `club_management_v2` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `club_management_v2`;

SET NAMES utf8mb4;

-- 1. 系统管理员表
DROP TABLE IF EXISTS `sys_admin`;
CREATE TABLE `sys_admin` (
  `admin_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '系统管理员id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `birthday` datetime DEFAULT NULL COMMENT '出生日期',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `uk_admin_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统管理员';

-- 2. 社长表
DROP TABLE IF EXISTS `sys_president`;
CREATE TABLE `sys_president` (
  `president_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '社长id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `birthday` datetime DEFAULT NULL COMMENT '出生日期',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`president_id`),
  UNIQUE KEY `uk_president_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社长';

-- 3. 社员表 (原普通用户)
DROP TABLE IF EXISTS `club_member_user`;
CREATE TABLE `club_member_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '社员id',
  `username` varchar(50) NOT NULL COMMENT '用户名/学号',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `member_status` tinyint(1) DEFAULT '0' COMMENT '社团成员状态',
  `role` int(11) DEFAULT '0' COMMENT '角色',
  `apply_result` int(11) DEFAULT '0' COMMENT '申请结果',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_user_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社员';

-- 4. 社团类型表
DROP TABLE IF EXISTS `club_type`;
CREATE TABLE `club_type` (
  `type_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '社团类型id',
  `type_name` varchar(50) NOT NULL COMMENT '类型名称',
  `description` varchar(255) DEFAULT NULL COMMENT '类型描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团类型';

-- 5. 社团信息表
DROP TABLE IF EXISTS `club_info`;
CREATE TABLE `club_info` (
  `club_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '社团信息id',
  `club_name` varchar(100) NOT NULL COMMENT '社团名称',
  `type_id` bigint(20) NOT NULL COMMENT '社团类型ID',
  `description` text COMMENT '社团简介',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '成立时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`club_id`),
  KEY `idx_type_id` (`type_id`),
  CONSTRAINT `fk_club_info_type_id` FOREIGN KEY (`type_id`) REFERENCES `club_type` (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团信息';

-- 6. 建社申请表
DROP TABLE IF EXISTS `club_application`;
CREATE TABLE `club_application` (
  `application_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '建社申请id',
  `user_id` bigint(20) NOT NULL COMMENT '社员ID',
  `type_id` bigint(20) NOT NULL COMMENT '社团类型ID',
  `description` text COMMENT '申请理由及简介',
  `audit_status` tinyint(1) DEFAULT '0' COMMENT '审批状态',
  `apply_result` int(11) DEFAULT '0' COMMENT '申请结果',
  `apply_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `audit_time` datetime DEFAULT NULL COMMENT '审批时间',
  PRIMARY KEY (`application_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type_id` (`type_id`),
  CONSTRAINT `fk_club_app_user_id` FOREIGN KEY (`user_id`) REFERENCES `club_member_user` (`user_id`),
  CONSTRAINT `fk_club_app_type_id` FOREIGN KEY (`type_id`) REFERENCES `club_type` (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='建社申请';

-- 7. 社团成员表现态 (保留/新增，用于记录已加入的成员)
DROP TABLE IF EXISTS `club_member`;
CREATE TABLE `club_member` (
  `member_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '社团成员id',
  `club_id` bigint(20) NOT NULL COMMENT '社团信息ID',
  `user_id` bigint(20) NOT NULL COMMENT '社员ID',
  `member_role` tinyint(1) DEFAULT '0' COMMENT '社内角色：0-普通成员，1-社长等',
  `join_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：1-正常，0-已退出',
  PRIMARY KEY (`member_id`),
  KEY `idx_club_id` (`club_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_club_member_club_id` FOREIGN KEY (`club_id`) REFERENCES `club_info` (`club_id`),
  CONSTRAINT `fk_club_member_user_id` FOREIGN KEY (`user_id`) REFERENCES `club_member_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团成员';

-- 7.1 入社申请表 (中间表，记录申请过程)
DROP TABLE IF EXISTS `join_club_application`;
CREATE TABLE `join_club_application` (
  `join_app_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '入社申请id',
  `club_id` bigint(20) NOT NULL COMMENT '社团信息ID',
  `user_id` bigint(20) NOT NULL COMMENT '社员ID',
  `audit_status` varchar(100) DEFAULT '0' COMMENT '审核状态',
  `join_reason` text COMMENT '入社原因',
  `apply_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `audit_time` datetime DEFAULT NULL COMMENT '审批时间',
  `feedback` datetime DEFAULT NULL COMMENT '申请反馈',
  PRIMARY KEY (`join_app_id`),
  KEY `idx_club_id` (`club_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_join_app_club_id` FOREIGN KEY (`club_id`) REFERENCES `club_info` (`club_id`),
  CONSTRAINT `fk_join_app_user_id` FOREIGN KEY (`user_id`) REFERENCES `club_member_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入社申请';

-- 8. 活动信息表
DROP TABLE IF EXISTS `activity_info`;
CREATE TABLE `activity_info` (
  `activity_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动信息id',
  `club_id` bigint(20) NOT NULL COMMENT '所属社团ID',
  `title` varchar(100) NOT NULL COMMENT '活动标题',
  `content` text COMMENT '活动详情',
  `location` varchar(255) DEFAULT NULL COMMENT '活动地点',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`activity_id`),
  KEY `idx_club_id` (`club_id`),
  CONSTRAINT `fk_activity_info_club_id` FOREIGN KEY (`club_id`) REFERENCES `club_info` (`club_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动信息';

-- 9. 活动申请表 (原活动参与)
DROP TABLE IF EXISTS `activity_application`;
CREATE TABLE `activity_application` (
 `apply_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动申请id',
 `activity_id` bigint(20) NOT NULL COMMENT '活动ID',
 `user_id` bigint(20) NOT NULL COMMENT '社员ID',
 `audit_status` tinyint(1) DEFAULT '0' COMMENT '审核状态',
 `apply_reason` varchar(256) DEFAULT NULL COMMENT '申请理由',
 `feedback` varchar(256) DEFAULT NULL COMMENT '申请反馈',
 `apply_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
 `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
 PRIMARY KEY (`apply_id`),
 KEY `idx_activity_id` (`activity_id`),
 KEY `idx_user_id` (`user_id`),
 CONSTRAINT `fk_act_app_act_id` FOREIGN KEY (`activity_id`) REFERENCES `activity_info` (`activity_id`),
 CONSTRAINT `fk_act_app_user_id` FOREIGN KEY (`user_id`) REFERENCES `club_member_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动申请';

-- 10. 活动签到表 (发起)
DROP TABLE IF EXISTS `activity_sign_in`;
CREATE TABLE `activity_sign_in` (
  `sign_in_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动签到id',
  `activity_id` bigint(20) NOT NULL COMMENT '活动ID',
  `sign_in_status` int(11) DEFAULT '0' COMMENT '签到发起状态',
  `launch_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '签到发起时间',
  PRIMARY KEY (`sign_in_id`),
  KEY `idx_activity_id` (`activity_id`),
  CONSTRAINT `fk_act_sign_in_act_id` FOREIGN KEY (`activity_id`) REFERENCES `activity_info` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动签到';

-- 11. 签到记录表 (中间表)
DROP TABLE IF EXISTS `sign_in_record`;
CREATE TABLE `sign_in_record` (
  `record_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '签到记录id',
  `sign_in_id` bigint(20) NOT NULL COMMENT '活动签到ID',
  `user_id` bigint(20) NOT NULL COMMENT '社员ID',
  `status` int(11) DEFAULT '0' COMMENT '签到状态',
  `sign_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
  PRIMARY KEY (`record_id`),
  KEY `idx_sign_in_id` (`sign_in_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_sign_in_rec_sign_id` FOREIGN KEY (`sign_in_id`) REFERENCES `activity_sign_in` (`sign_in_id`),
  CONSTRAINT `fk_sign_in_rec_user_id` FOREIGN KEY (`user_id`) REFERENCES `club_member_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='签到记录';

-- 12. 活动签退表 (发起)
DROP TABLE IF EXISTS `activity_sign_out`;
CREATE TABLE `activity_sign_out` (
  `sign_out_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动签退id',
  `activity_id` bigint(20) NOT NULL COMMENT '活动ID',
  `sign_out_status` int(11) DEFAULT '0' COMMENT '活动签退状态',
  `launch_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '签退发起时间',
  PRIMARY KEY (`sign_out_id`),
  KEY `idx_activity_id` (`activity_id`),
  CONSTRAINT `fk_act_sign_out_act_id` FOREIGN KEY (`activity_id`) REFERENCES `activity_info` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动签退';

-- 13. 签退记录表 (中间表)
DROP TABLE IF EXISTS `sign_out_record`;
CREATE TABLE `sign_out_record` (
  `record_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '签退记录id',
  `sign_out_id` bigint(20) NOT NULL COMMENT '活动签退ID',
  `user_id` bigint(20) NOT NULL COMMENT '社员ID',
  `status` int(11) DEFAULT '0' COMMENT '签退状态',
  `sign_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '签退时间',
  PRIMARY KEY (`record_id`),
  KEY `idx_sign_out_id` (`sign_out_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_sign_out_rec_sign_id` FOREIGN KEY (`sign_out_id`) REFERENCES `activity_sign_out` (`sign_out_id`),
  CONSTRAINT `fk_sign_out_rec_user_id` FOREIGN KEY (`user_id`) REFERENCES `club_member_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='签退记录';

-- 14. 活动反馈表
DROP TABLE IF EXISTS `activity_feedback`;
CREATE TABLE `activity_feedback` (
  `feedback_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动反馈id',
  `activity_id` bigint(20) NOT NULL COMMENT '活动ID',
  `user_id` bigint(20) NOT NULL COMMENT '社员ID',
  `content` text NOT NULL COMMENT '心得体会内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  PRIMARY KEY (`feedback_id`),
  KEY `idx_activity_id` (`activity_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_act_feedback_act_id` FOREIGN KEY (`activity_id`) REFERENCES `activity_info` (`activity_id`),
  CONSTRAINT `fk_act_feedback_user_id` FOREIGN KEY (`user_id`) REFERENCES `club_member_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动反馈';

-- 15. 反馈回复表
DROP TABLE IF EXISTS `feedback_reply`;
CREATE TABLE `feedback_reply` (
  `reply_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '反馈回复id',
  `feedback_id` bigint(20) NOT NULL COMMENT '反馈ID',
  `user_id` bigint(20) NOT NULL COMMENT '回复用户ID(社长/管理员)',
  `content` text NOT NULL COMMENT '回复内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '回复时间',
  PRIMARY KEY (`reply_id`),
  KEY `idx_feedback_id` (`feedback_id`),
  CONSTRAINT `fk_feedback_reply_fb_id` FOREIGN KEY (`feedback_id`) REFERENCES `activity_feedback` (`feedback_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='反馈回复';

-- 16. 活动推文表
DROP TABLE IF EXISTS `activity_tweet`;
CREATE TABLE `activity_tweet` (
  `tweet_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动推文id',
  `activity_id` bigint(20) NOT NULL COMMENT '活动ID',
  `publisher_id` bigint(20) NOT NULL COMMENT '发布人ID',
  `title` varchar(100) NOT NULL COMMENT '推文标题',
  `content` text NOT NULL COMMENT '推文内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`tweet_id`),
  KEY `idx_activity_id` (`activity_id`),
  CONSTRAINT `fk_act_tweet_act_id` FOREIGN KEY (`activity_id`) REFERENCES `activity_info` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动推文';

-- 17. 通知信息表
DROP TABLE IF EXISTS `notification_info`;
CREATE TABLE `notification_info` (
  `notification_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '通知信息id',
  `club_id` bigint(20) DEFAULT NULL COMMENT '社团ID',
  `title` varchar(100) NOT NULL COMMENT '通知标题',
  `content` text NOT NULL COMMENT '通知内容',
  `publisher_id` bigint(20) NOT NULL COMMENT '发布人ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`notification_id`),
  KEY `idx_club_id` (`club_id`),
  KEY `idx_publisher_id` (`publisher_id`),
  CONSTRAINT `fk_notice_info_club_id` FOREIGN KEY (`club_id`) REFERENCES `club_info` (`club_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知信息';

-- 18. 缴费通知表
DROP TABLE IF EXISTS `payment_notice`;
CREATE TABLE `payment_notice` (
  `notice_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '缴费通知id',
  `club_id` bigint(20) NOT NULL COMMENT '社团ID',
  `title` varchar(100) NOT NULL COMMENT '缴费标题',
  `amount` decimal(10,2) NOT NULL COMMENT '缴费金额',
  `deadline` datetime NOT NULL COMMENT '截止时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`notice_id`),
  KEY `idx_club_id` (`club_id`),
  CONSTRAINT `fk_payment_notice_club_id` FOREIGN KEY (`club_id`) REFERENCES `club_info` (`club_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费通知';

-- 19. 缴费记录表
DROP TABLE IF EXISTS `payment_record`;
CREATE TABLE `payment_record` (
  `record_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '缴费记录id',
  `notice_id` bigint(20) NOT NULL COMMENT '缴费通知ID',
  `user_id` bigint(20) NOT NULL COMMENT '缴费用户ID',
  `amount` decimal(10,2) NOT NULL COMMENT '实际缴费金额',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态',
  `pay_time` datetime DEFAULT NULL COMMENT '缴费时间',
  PRIMARY KEY (`record_id`),
  KEY `idx_notice_id` (`notice_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_payment_record_notice_id` FOREIGN KEY (`notice_id`) REFERENCES `payment_notice` (`notice_id`),
  CONSTRAINT `fk_payment_record_user_id` FOREIGN KEY (`user_id`) REFERENCES `club_member_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费记录';

-- 20. 提现记录表
DROP TABLE IF EXISTS `withdrawal_record`;
CREATE TABLE `withdrawal_record` (
  `record_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '提现记录id',
  `club_id` bigint(20) NOT NULL COMMENT '社团ID',
  `applicant_id` bigint(20) NOT NULL COMMENT '社长ID',
  `amount` decimal(10,2) NOT NULL COMMENT '提现金额',
  `reason` varchar(255) DEFAULT NULL COMMENT '提现事由',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `apply_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '提现时间',
  PRIMARY KEY (`record_id`),
  KEY `idx_club_id` (`club_id`),
  KEY `idx_applicant_id` (`applicant_id`),
  CONSTRAINT `fk_withdrawal_club_id` FOREIGN KEY (`club_id`) REFERENCES `club_info` (`club_id`),
  CONSTRAINT `fk_withdrawal_applicant_id` FOREIGN KEY (`applicant_id`) REFERENCES `sys_president` (`president_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提现记录';
