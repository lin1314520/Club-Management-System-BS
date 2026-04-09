-- 创建数据库
CREATE DATABASE IF NOT EXISTS `club_management` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `club_management`;

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
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`president_id`),
  UNIQUE KEY `uk_president_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社长';

-- 3. 系统用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '系统用户id',
  `username` varchar(50) NOT NULL COMMENT '用户名/学号',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `member_status` tinyint(1) DEFAULT '0' COMMENT '社团成员状态：0-普通用户，1-社团成员',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_user_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='普通用户';

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
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-已注销，1-正常',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '成立时间',
  PRIMARY KEY (`club_id`),
  KEY `idx_type_id` (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团信息';

-- 6. 建社申请表
DROP TABLE IF EXISTS `club_application`;
CREATE TABLE `club_application` (
  `application_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '建社申请id',
  `user_id` bigint(20) NOT NULL COMMENT '申请人ID',
  `club_name` varchar(100) NOT NULL COMMENT '申请创建的社团名称',
  `type_id` bigint(20) NOT NULL COMMENT '社团类型ID',
  `description` text COMMENT '申请理由及简介',
  `status` tinyint(1) DEFAULT '0' COMMENT '审批状态：0-审批中，1-已通过，2-已拒绝，3-已撤销',
  `apply_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `audit_time` datetime DEFAULT NULL COMMENT '审批时间',
  PRIMARY KEY (`application_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type_id` (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='建社申请';

-- 7. 社团成员(含入团申请)表
DROP TABLE IF EXISTS `club_member`;
CREATE TABLE `club_member` (
  `member_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '社团成员id',
  `club_id` bigint(20) NOT NULL COMMENT '社团ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `member_role` tinyint(1) DEFAULT '0' COMMENT '社内角色：0-普通成员，1-社长',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态：0-申请中，1-已加入，2-已拒绝，3-已退出',
  `apply_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `join_time` datetime DEFAULT NULL COMMENT '加入时间',
  PRIMARY KEY (`member_id`),
  KEY `idx_club_id` (`club_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团成员';

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
  `status` tinyint(1) DEFAULT '0' COMMENT '状态：0-未开始，1-进行中，2-已结束',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`activity_id`),
  KEY `idx_club_id` (`club_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动信息';

-- 9. 活动参与(报名)表
DROP TABLE IF EXISTS `activity_participant`;
CREATE TABLE `activity_participant` (
 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
 `activity_id` bigint(20) NOT NULL COMMENT '活动ID',
 `user_id` bigint(20) NOT NULL COMMENT '参与用户ID',
 `audit_status` tinyint(1) DEFAULT '0' COMMENT '审核状态：0-待审核，1-已通过，2-已拒绝',
 `sign_in_time` datetime DEFAULT NULL COMMENT '签到时间',
 `sign_out_time` datetime DEFAULT NULL COMMENT '签退时间',
 `apply_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
 PRIMARY KEY (`id`),
 KEY `idx_activity_id` (`activity_id`),
 KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动参与';

-- 10. 活动签到表
DROP TABLE IF EXISTS `activity_sign_in`;
CREATE TABLE `activity_sign_in` (
  `sign_in_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动签到id',
  `activity_id` bigint(20) NOT NULL COMMENT '活动ID',
  `user_id` bigint(20) NOT NULL COMMENT '签到用户ID',
  `sign_in_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
  PRIMARY KEY (`sign_in_id`),
  KEY `idx_activity_id` (`activity_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动签到';

-- 11. 活动签退表
DROP TABLE IF EXISTS `activity_sign_out`;
CREATE TABLE `activity_sign_out` (
  `sign_out_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动签退id',
  `activity_id` bigint(20) NOT NULL COMMENT '活动ID',
  `user_id` bigint(20) NOT NULL COMMENT '签退用户ID',
  `sign_out_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '签退时间',
  PRIMARY KEY (`sign_out_id`),
  KEY `idx_activity_id` (`activity_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动签退';

-- 12. 活动反馈(心得)表
DROP TABLE IF EXISTS `activity_feedback`;
CREATE TABLE `activity_feedback` (
  `feedback_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动反馈id',
  `activity_id` bigint(20) NOT NULL COMMENT '活动ID',
  `user_id` bigint(20) NOT NULL COMMENT '提交用户ID',
  `content` text NOT NULL COMMENT '心得体会内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  PRIMARY KEY (`feedback_id`),
  KEY `idx_activity_id` (`activity_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动反馈';

-- 13. 反馈回复表
DROP TABLE IF EXISTS `feedback_reply`;
CREATE TABLE `feedback_reply` (
  `reply_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '反馈回复id',
  `feedback_id` bigint(20) NOT NULL COMMENT '反馈ID',
  `user_id` bigint(20) NOT NULL COMMENT '回复用户ID(社长/管理员)',
  `content` text NOT NULL COMMENT '回复内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '回复时间',
  PRIMARY KEY (`reply_id`),
  KEY `idx_feedback_id` (`feedback_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='反馈回复';

-- 14. 活动推文表
DROP TABLE IF EXISTS `activity_tweet`;
CREATE TABLE `activity_tweet` (
  `tweet_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动推文id',
  `activity_id` bigint(20) NOT NULL COMMENT '活动ID',
  `publisher_id` bigint(20) NOT NULL COMMENT '发布人ID',
  `title` varchar(100) NOT NULL COMMENT '推文标题',
  `content` text NOT NULL COMMENT '推文内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`tweet_id`),
  KEY `idx_activity_id` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动推文';

-- 15. 通知信息表
DROP TABLE IF EXISTS `notification_info`;
CREATE TABLE `notification_info` (
  `notification_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '通知信息id',
  `club_id` bigint(20) DEFAULT NULL COMMENT '社团ID（为空则是系统管理员发布的全系统通知）',
  `title` varchar(100) NOT NULL COMMENT '通知标题',
  `content` text NOT NULL COMMENT '通知内容',
  `publisher_id` bigint(20) NOT NULL COMMENT '发布人ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`notification_id`),
  KEY `idx_club_id` (`club_id`),
  KEY `idx_publisher_id` (`publisher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知信息';

-- 16. 缴费通知表
DROP TABLE IF EXISTS `payment_notice`;
CREATE TABLE `payment_notice` (
  `notice_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '缴费通知id',
  `club_id` bigint(20) NOT NULL COMMENT '社团ID',
  `title` varchar(100) NOT NULL COMMENT '缴费标题(如：2023秋季会费)',
  `amount` decimal(10,2) NOT NULL COMMENT '缴费金额',
  `deadline` datetime NOT NULL COMMENT '截止时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`notice_id`),
  KEY `idx_club_id` (`club_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费通知';

-- 17. 缴费记录表
DROP TABLE IF EXISTS `payment_record`;
CREATE TABLE `payment_record` (
  `record_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '缴费记录id',
  `notice_id` bigint(20) NOT NULL COMMENT '缴费通知ID',
  `user_id` bigint(20) NOT NULL COMMENT '缴费用户ID',
  `amount` decimal(10,2) NOT NULL COMMENT '实际缴费金额',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态：0-未缴费，1-已缴费',
  `pay_time` datetime DEFAULT NULL COMMENT '缴费时间',
  PRIMARY KEY (`record_id`),
  KEY `idx_notice_id` (`notice_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费记录';

-- 18. 提现申请表
DROP TABLE IF EXISTS `withdrawal_record`;
CREATE TABLE `withdrawal_record` (
  `record_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '提现申请id',
  `club_id` bigint(20) NOT NULL COMMENT '社团ID',
  `applicant_id` bigint(20) NOT NULL COMMENT '申请人(社长)ID',
  `amount` decimal(10,2) NOT NULL COMMENT '提现金额',
  `reason` varchar(255) DEFAULT NULL COMMENT '提现事由',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：1-已完成',
  `apply_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '提现时间',
  PRIMARY KEY (`record_id`),
  KEY `idx_club_id` (`club_id`),
  KEY `idx_applicant_id` (`applicant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提现记录';
