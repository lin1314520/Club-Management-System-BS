-- 创建数据库
CREATE DATABASE IF NOT EXISTS `club_management_cn` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `club_management_cn`;

SET NAMES utf8mb4;

-- 1. 系统管理员表
DROP TABLE IF EXISTS `系统管理员`;
CREATE TABLE `系统管理员` (
  `系统管理员id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '系统管理员id',
  `用户名` varchar(50) NOT NULL COMMENT '用户名',
  `密码` varchar(100) NOT NULL COMMENT '密码',
  `真实姓名` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `手机号` varchar(20) DEFAULT NULL COMMENT '手机号',
  `邮箱` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `状态` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `创建时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `更新时间` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`系统管理员id`),
  UNIQUE KEY `uk_admin_username` (`用户名`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统管理员';

-- 2. 社长表
DROP TABLE IF EXISTS `社长`;
CREATE TABLE `社长` (
  `社长id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '社长id',
  `用户名` varchar(50) NOT NULL COMMENT '用户名',
  `密码` varchar(100) NOT NULL COMMENT '密码',
  `真实姓名` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `手机号` varchar(20) DEFAULT NULL COMMENT '手机号',
  `邮箱` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `状态` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `创建时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `更新时间` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`社长id`),
  UNIQUE KEY `uk_president_username` (`用户名`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社长';

-- 3. 系统用户表
DROP TABLE IF EXISTS `普通用户`;
CREATE TABLE `普通用户` (
  `普通用户id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '系统用户id',
  `用户名/学号` varchar(50) NOT NULL COMMENT '用户名/学号',
  `密码` varchar(100) NOT NULL COMMENT '密码',
  `真实姓名` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `手机号` varchar(20) DEFAULT NULL COMMENT '手机号',
  `邮箱` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `状态` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `社团成员状态` tinyint(1) DEFAULT '0' COMMENT '社团成员状态：0-普通用户，1-社团成员',
  `出生日期` date DEFAULT NULL COMMENT '出生日期',
  `创建时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `更新时间` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`普通用户id`),
  UNIQUE KEY `uk_user_username` (`用户名/学号`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='普通用户';

-- 4. 社团类型表
DROP TABLE IF EXISTS `社团类型`;
CREATE TABLE `社团类型` (
  `社团类型id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '社团类型id',
  `类型名称` varchar(50) NOT NULL COMMENT '类型名称',
  `类型描述` varchar(255) DEFAULT NULL COMMENT '类型描述',
  `创建时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`社团类型id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团类型';

-- 5. 社团信息表
DROP TABLE IF EXISTS `社团信息`;
CREATE TABLE `社团信息` (
  `社团信息id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '社团信息id',
  `社团名称` varchar(100) NOT NULL COMMENT '社团名称',
  `社团类型id` bigint(20) NOT NULL COMMENT '社团类型ID',
  `社团简介` text COMMENT '社团简介',
  `状态` tinyint(1) DEFAULT '1' COMMENT '状态：0-已注销，1-正常',
  `成立时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '成立时间',
  PRIMARY KEY (`社团信息id`),
  KEY `idx_type_id` (`社团类型id`),
  CONSTRAINT `fk_club_info_type_id` FOREIGN KEY (`社团类型id`) REFERENCES `社团类型` (`社团类型id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团信息';

-- 6. 建社申请表
DROP TABLE IF EXISTS `建社申请`;
CREATE TABLE `建社申请` (
  `建社申请id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '建社申请id',
  `普通用户id` bigint(20) NOT NULL COMMENT '申请人ID',
  `申请创建的社团名称` varchar(100) NOT NULL COMMENT '申请创建的社团名称',
  `社团类型id` bigint(20) NOT NULL COMMENT '社团类型ID',
  `申请理由及简介` text COMMENT '申请理由及简介',
  `审批状态` tinyint(1) DEFAULT '0' COMMENT '审批状态：0-审批中，1-已通过，2-已拒绝，3-已撤销',
  `申请时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `审批时间` datetime DEFAULT NULL COMMENT '审批时间',
  PRIMARY KEY (`建社申请id`),
  KEY `idx_user_id` (`普通用户id`),
  KEY `idx_type_id` (`社团类型id`),
  CONSTRAINT `fk_club_app_user_id` FOREIGN KEY (`普通用户id`) REFERENCES `普通用户` (`普通用户id`),
  CONSTRAINT `fk_club_app_type_id` FOREIGN KEY (`社团类型id`) REFERENCES `社团类型` (`社团类型id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='建社申请';

-- 7. 社团成员(含入团申请)表
DROP TABLE IF EXISTS `社团成员`;
CREATE TABLE `社团成员` (
  `社团成员id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '社团成员id',
  `社团信息id` bigint(20) NOT NULL COMMENT '社团ID',
  `普通用户id` bigint(20) NOT NULL COMMENT '用户ID',
  `社内角色` tinyint(1) DEFAULT '0' COMMENT '社内角色：0-普通成员，1-社长',
  `状态` tinyint(1) DEFAULT '0' COMMENT '状态：0-申请中，1-已加入，2-已拒绝，3-已退出',
  `申请时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `加入时间` datetime DEFAULT NULL COMMENT '加入时间',
  PRIMARY KEY (`社团成员id`),
  KEY `idx_club_id` (`社团信息id`),
  KEY `idx_user_id` (`普通用户id`),
  CONSTRAINT `fk_club_member_club_id` FOREIGN KEY (`社团信息id`) REFERENCES `社团信息` (`社团信息id`),
  CONSTRAINT `fk_club_member_user_id` FOREIGN KEY (`普通用户id`) REFERENCES `普通用户` (`普通用户id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团成员';

-- 8. 活动信息表
DROP TABLE IF EXISTS `活动信息`;
CREATE TABLE `活动信息` (
  `活动信息id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动信息id',
  `社团信息id` bigint(20) NOT NULL COMMENT '所属社团ID',
  `活动标题` varchar(100) NOT NULL COMMENT '活动标题',
  `活动详情` text COMMENT '活动详情',
  `活动地点` varchar(255) DEFAULT NULL COMMENT '活动地点',
  `开始时间` datetime NOT NULL COMMENT '开始时间',
  `结束时间` datetime NOT NULL COMMENT '结束时间',
  `状态` tinyint(1) DEFAULT '0' COMMENT '状态：0-未开始，1-进行中，2-已结束',
  `发布时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`活动信息id`),
  KEY `idx_club_id` (`社团信息id`),
  CONSTRAINT `fk_activity_info_club_id` FOREIGN KEY (`社团信息id`) REFERENCES `社团信息` (`社团信息id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动信息';

-- 9. 活动参与(报名)表
DROP TABLE IF EXISTS `活动参与`;
CREATE TABLE `活动参与` (
 `活动参与id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
 `活动信息id` bigint(20) NOT NULL COMMENT '活动ID',
 `普通用户id` bigint(20) NOT NULL COMMENT '参与用户ID',
 `审核状态` tinyint(1) DEFAULT '0' COMMENT '审核状态：0-待审核，1-已通过，2-已拒绝',
 `签到时间` datetime DEFAULT NULL COMMENT '签到时间',
 `签退时间` datetime DEFAULT NULL COMMENT '签退时间',
 `报名时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
 PRIMARY KEY (`活动参与id`),
 KEY `idx_activity_id` (`活动信息id`),
 KEY `idx_user_id` (`普通用户id`),
 CONSTRAINT `fk_act_participant_act_id` FOREIGN KEY (`活动信息id`) REFERENCES `活动信息` (`活动信息id`),
 CONSTRAINT `fk_act_participant_user_id` FOREIGN KEY (`普通用户id`) REFERENCES `普通用户` (`普通用户id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动参与';

-- 10. 活动签到表
DROP TABLE IF EXISTS `活动签到`;
CREATE TABLE `活动签到` (
  `活动签到id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动签到id',
  `活动信息id` bigint(20) NOT NULL COMMENT '活动ID',
  `普通用户id` bigint(20) NOT NULL COMMENT '签到用户ID',
  `签到时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
  PRIMARY KEY (`活动签到id`),
  KEY `idx_activity_id` (`活动信息id`),
  KEY `idx_user_id` (`普通用户id`),
  CONSTRAINT `fk_act_sign_in_act_id` FOREIGN KEY (`活动信息id`) REFERENCES `活动信息` (`活动信息id`),
  CONSTRAINT `fk_act_sign_in_user_id` FOREIGN KEY (`普通用户id`) REFERENCES `普通用户` (`普通用户id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动签到';

-- 11. 活动签退表
DROP TABLE IF EXISTS `活动签退`;
CREATE TABLE `活动签退` (
  `活动签退id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动签退id',
  `活动信息id` bigint(20) NOT NULL COMMENT '活动ID',
  `普通用户id` bigint(20) NOT NULL COMMENT '签退用户ID',
  `签退时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '签退时间',
  PRIMARY KEY (`活动签退id`),
  KEY `idx_activity_id` (`活动信息id`),
  KEY `idx_user_id` (`普通用户id`),
  CONSTRAINT `fk_act_sign_out_act_id` FOREIGN KEY (`活动信息id`) REFERENCES `活动信息` (`活动信息id`),
  CONSTRAINT `fk_act_sign_out_user_id` FOREIGN KEY (`普通用户id`) REFERENCES `普通用户` (`普通用户id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动签退';

-- 12. 活动反馈(心得)表
DROP TABLE IF EXISTS `活动反馈`;
CREATE TABLE `活动反馈` (
  `活动反馈id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动反馈id',
  `活动信息id` bigint(20) NOT NULL COMMENT '活动ID',
  `普通用户id` bigint(20) NOT NULL COMMENT '提交用户ID',
  `心得体会内容` text NOT NULL COMMENT '心得体会内容',
  `提交时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  PRIMARY KEY (`活动反馈id`),
  KEY `idx_activity_id` (`活动信息id`),
  KEY `idx_user_id` (`普通用户id`),
  CONSTRAINT `fk_act_feedback_act_id` FOREIGN KEY (`活动信息id`) REFERENCES `活动信息` (`活动信息id`),
  CONSTRAINT `fk_act_feedback_user_id` FOREIGN KEY (`普通用户id`) REFERENCES `普通用户` (`普通用户id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动反馈';

-- 13. 反馈回复表
DROP TABLE IF EXISTS `反馈回复`;
CREATE TABLE `反馈回复` (
  `反馈回复id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '反馈回复id',
  `活动反馈id` bigint(20) NOT NULL COMMENT '反馈ID',
  `回复用户id` bigint(20) NOT NULL COMMENT '回复用户ID(社长/管理员)',
  `回复内容` text NOT NULL COMMENT '回复内容',
  `回复时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '回复时间',
  PRIMARY KEY (`反馈回复id`),
  KEY `idx_feedback_id` (`活动反馈id`),
  CONSTRAINT `fk_feedback_reply_fb_id` FOREIGN KEY (`活动反馈id`) REFERENCES `活动反馈` (`活动反馈id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='反馈回复';

-- 14. 活动推文表
DROP TABLE IF EXISTS `活动推文`;
CREATE TABLE `活动推文` (
  `活动推文id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动推文id',
  `活动信息id` bigint(20) NOT NULL COMMENT '活动ID',
  `发布人id` bigint(20) NOT NULL COMMENT '发布人ID',
  `推文标题` varchar(100) NOT NULL COMMENT '推文标题',
  `推文内容` text NOT NULL COMMENT '推文内容',
  `发布时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`活动推文id`),
  KEY `idx_activity_id` (`活动信息id`),
  CONSTRAINT `fk_act_tweet_act_id` FOREIGN KEY (`活动信息id`) REFERENCES `活动信息` (`活动信息id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动推文';

-- 15. 通知信息表
DROP TABLE IF EXISTS `通知信息`;
CREATE TABLE `通知信息` (
  `通知信息id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '通知信息id',
  `社团信息id` bigint(20) DEFAULT NULL COMMENT '社团ID（为空则是系统管理员发布的全系统通知）',
  `通知标题` varchar(100) NOT NULL COMMENT '通知标题',
  `通知内容` text NOT NULL COMMENT '通知内容',
  `发布人id` bigint(20) NOT NULL COMMENT '发布人ID',
  `发布时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`通知信息id`),
  KEY `idx_club_id` (`社团信息id`),
  KEY `idx_publisher_id` (`发布人id`),
  CONSTRAINT `fk_notice_info_club_id` FOREIGN KEY (`社团信息id`) REFERENCES `社团信息` (`社团信息id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知信息';

-- 16. 缴费通知表
DROP TABLE IF EXISTS `缴费通知`;
CREATE TABLE `缴费通知` (
  `缴费通知id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '缴费通知id',
  `社团信息id` bigint(20) NOT NULL COMMENT '社团ID',
  `缴费标题` varchar(100) NOT NULL COMMENT '缴费标题(如：2023秋季会费)',
  `缴费金额` decimal(10,2) NOT NULL COMMENT '缴费金额',
  `截止时间` datetime NOT NULL COMMENT '截止时间',
  `发布时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`缴费通知id`),
  KEY `idx_club_id` (`社团信息id`),
  CONSTRAINT `fk_payment_notice_club_id` FOREIGN KEY (`社团信息id`) REFERENCES `社团信息` (`社团信息id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费通知';

-- 17. 缴费记录表
DROP TABLE IF EXISTS `缴费记录`;
CREATE TABLE `缴费记录` (
  `缴费记录id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '缴费记录id',
  `缴费通知id` bigint(20) NOT NULL COMMENT '缴费通知ID',
  `普通用户id` bigint(20) NOT NULL COMMENT '缴费用户ID',
  `实际缴费金额` decimal(10,2) NOT NULL COMMENT '实际缴费金额',
  `状态` tinyint(1) DEFAULT '0' COMMENT '状态：0-未缴费，1-已缴费',
  `缴费时间` datetime DEFAULT NULL COMMENT '缴费时间',
  PRIMARY KEY (`缴费记录id`),
  KEY `idx_notice_id` (`缴费通知id`),
  KEY `idx_user_id` (`普通用户id`),
  CONSTRAINT `fk_payment_record_notice_id` FOREIGN KEY (`缴费通知id`) REFERENCES `缴费通知` (`缴费通知id`),
  CONSTRAINT `fk_payment_record_user_id` FOREIGN KEY (`普通用户id`) REFERENCES `普通用户` (`普通用户id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费记录';

-- 18. 提现记录表
DROP TABLE IF EXISTS `提现记录`;
CREATE TABLE `提现记录` (
  `提现记录id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '提现申请id',
  `社团信息id` bigint(20) NOT NULL COMMENT '社团ID',
  `社长id` bigint(20) NOT NULL COMMENT '申请人(社长)ID',
  `提现金额` decimal(10,2) NOT NULL COMMENT '提现金额',
  `提现事由` varchar(255) DEFAULT NULL COMMENT '提现事由',
  `状态` tinyint(1) DEFAULT '1' COMMENT '状态：1-已完成',
  `提现时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '提现时间',
  PRIMARY KEY (`提现记录id`),
  KEY `idx_club_id` (`社团信息id`),
  KEY `idx_applicant_id` (`社长id`),
  CONSTRAINT `fk_withdrawal_club_id` FOREIGN KEY (`社团信息id`) REFERENCES `社团信息` (`社团信息id`),
  CONSTRAINT `fk_withdrawal_applicant_id` FOREIGN KEY (`社长id`) REFERENCES `社长` (`社长id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提现记录';
