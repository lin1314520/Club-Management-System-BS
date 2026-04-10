-- 创建数据库 (中文版 v2)
CREATE DATABASE IF NOT EXISTS `club_management_v2_cn` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `club_management_v2_cn`;

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
  `状态` tinyint(1) DEFAULT '1' COMMENT '状态',
  `出生日期` datetime DEFAULT NULL COMMENT '出生日期',
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
  `状态` tinyint(1) DEFAULT '1' COMMENT '状态',
  `出生日期` datetime DEFAULT NULL COMMENT '出生日期',
  `创建时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `更新时间` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`社长id`),
  UNIQUE KEY `uk_president_username` (`用户名`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社长';

-- 3. 社员表
DROP TABLE IF EXISTS `社员`;
CREATE TABLE `社员` (
  `社员id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '社员id',
  `用户名/学号` varchar(50) NOT NULL COMMENT '用户名/学号',
  `密码` varchar(100) NOT NULL COMMENT '密码',
  `真实姓名` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `手机号` varchar(20) DEFAULT NULL COMMENT '手机号',
  `邮箱` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `状态` tinyint(1) DEFAULT '1' COMMENT '状态',
  `社团成员状态` tinyint(1) DEFAULT '0' COMMENT '社团成员状态',
  `角色` int(11) DEFAULT '0' COMMENT '角色',
  `申请结果` int(11) DEFAULT '0' COMMENT '申请结果',
  `出生日期` date DEFAULT NULL COMMENT '出生日期',
  `创建时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `更新时间` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`社员id`),
  UNIQUE KEY `uk_user_username` (`用户名/学号`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社员';

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
  `状态` tinyint(1) DEFAULT '1' COMMENT '状态',
  `成立时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '成立时间',
  `更新时间` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`社团信息id`),
  KEY `idx_type_id` (`社团类型id`),
  CONSTRAINT `fk_club_info_type_id` FOREIGN KEY (`社团类型id`) REFERENCES `社团类型` (`社团类型id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团信息';

-- 6. 建社申请表
DROP TABLE IF EXISTS `建社申请`;
CREATE TABLE `建社申请` (
  `建社申请id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '建社申请id',
  `社员id` bigint(20) NOT NULL COMMENT '社员ID',
  `社团类型id` bigint(20) NOT NULL COMMENT '社团类型ID',
  `申请理由及简介` text COMMENT '申请理由及简介',
  `审批状态` tinyint(1) DEFAULT '0' COMMENT '审批状态',
  `申请结果` int(11) DEFAULT '0' COMMENT '申请结果',
  `申请时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `审批时间` datetime DEFAULT NULL COMMENT '审批时间',
  PRIMARY KEY (`建社申请id`),
  KEY `idx_user_id` (`社员id`),
  KEY `idx_type_id` (`社团类型id`),
  CONSTRAINT `fk_club_app_user_id` FOREIGN KEY (`社员id`) REFERENCES `社员` (`社员id`),
  CONSTRAINT `fk_club_app_type_id` FOREIGN KEY (`社团类型id`) REFERENCES `社团类型` (`社团类型id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='建社申请';

-- 7. 入社申请表
DROP TABLE IF EXISTS `入社申请`;
CREATE TABLE `入社申请` (
  `入社申请id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '入社申请id',
  `社团信息id` bigint(20) NOT NULL COMMENT '社团信息ID',
  `社员id` bigint(20) NOT NULL COMMENT '社员ID',
  `审核状态` varchar(100) DEFAULT '0' COMMENT '审核状态',
  `入社原因` text COMMENT '入社原因',
  `申请时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `审批时间` datetime DEFAULT NULL COMMENT '审批时间',
  `申请反馈` datetime DEFAULT NULL COMMENT '申请反馈',
  PRIMARY KEY (`入社申请id`),
  KEY `idx_club_id` (`社团信息id`),
  KEY `idx_user_id` (`社员id`),
  CONSTRAINT `fk_join_app_club_id` FOREIGN KEY (`社团信息id`) REFERENCES `社团信息` (`社团信息id`),
  CONSTRAINT `fk_join_app_user_id` FOREIGN KEY (`社员id`) REFERENCES `社员` (`社员id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入社申请';

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
  `状态` tinyint(1) DEFAULT '0' COMMENT '状态',
  `发布时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`活动信息id`),
  KEY `idx_club_id` (`社团信息id`),
  CONSTRAINT `fk_activity_info_club_id` FOREIGN KEY (`社团信息id`) REFERENCES `社团信息` (`社团信息id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动信息';

-- 9. 活动申请表
DROP TABLE IF EXISTS `活动申请`;
CREATE TABLE `活动申请` (
 `活动申请id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动申请id',
 `活动信息id` bigint(20) NOT NULL COMMENT '活动ID',
 `社员id` bigint(20) NOT NULL COMMENT '社员ID',
 `审核状态` tinyint(1) DEFAULT '0' COMMENT '审核状态',
 `申请理由` varchar(256) DEFAULT NULL COMMENT '申请理由',
 `申请反馈` varchar(256) DEFAULT NULL COMMENT '申请反馈',
 `报名时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
 `审核时间` datetime DEFAULT NULL COMMENT '审核时间',
 PRIMARY KEY (`活动申请id`),
 KEY `idx_activity_id` (`活动信息id`),
 KEY `idx_user_id` (`社员id`),
 CONSTRAINT `fk_act_app_act_id` FOREIGN KEY (`活动信息id`) REFERENCES `活动信息` (`活动信息id`),
 CONSTRAINT `fk_act_app_user_id` FOREIGN KEY (`社员id`) REFERENCES `社员` (`社员id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动申请';

-- 10. 活动签到表
DROP TABLE IF EXISTS `活动签到`;
CREATE TABLE `活动签到` (
  `活动签到id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动签到id',
  `活动信息id` bigint(20) NOT NULL COMMENT '活动ID',
  `签到发起状态` int(11) DEFAULT '0' COMMENT '签到发起状态',
  `签到发起时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '签到发起时间',
  PRIMARY KEY (`活动签到id`),
  KEY `idx_activity_id` (`活动信息id`),
  CONSTRAINT `fk_act_sign_in_act_id` FOREIGN KEY (`活动信息id`) REFERENCES `活动信息` (`活动信息id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动签到';

-- 11. 签到记录表
DROP TABLE IF EXISTS `签到记录`;
CREATE TABLE `签到记录` (
  `签到记录id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '签到记录id',
  `活动签到id` bigint(20) NOT NULL COMMENT '活动签到ID',
  `社员id` bigint(20) NOT NULL COMMENT '社员ID',
  `签到状态` int(11) DEFAULT '0' COMMENT '签到状态',
  `签到时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
  PRIMARY KEY (`签到记录id`),
  KEY `idx_sign_in_id` (`活动签到id`),
  KEY `idx_user_id` (`社员id`),
  CONSTRAINT `fk_sign_in_rec_sign_id` FOREIGN KEY (`活动签到id`) REFERENCES `活动签到` (`活动签到id`),
  CONSTRAINT `fk_sign_in_rec_user_id` FOREIGN KEY (`社员id`) REFERENCES `社员` (`社员id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='签到记录';

-- 12. 活动签退表
DROP TABLE IF EXISTS `活动签退`;
CREATE TABLE `活动签退` (
  `活动签退id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动签退id',
  `活动信息id` bigint(20) NOT NULL COMMENT '活动ID',
  `活动签退状态` int(11) DEFAULT '0' COMMENT '活动签退状态',
  `签退发起时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '签退发起时间',
  PRIMARY KEY (`活动签退id`),
  KEY `idx_activity_id` (`活动信息id`),
  CONSTRAINT `fk_act_sign_out_act_id` FOREIGN KEY (`活动信息id`) REFERENCES `活动信息` (`活动信息id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动签退';

-- 13. 签退记录表
DROP TABLE IF EXISTS `签退记录`;
CREATE TABLE `签退记录` (
  `签退记录id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '签退记录id',
  `活动签退id` bigint(20) NOT NULL COMMENT '活动签退ID',
  `社员id` bigint(20) NOT NULL COMMENT '社员ID',
  `签退状态` int(11) DEFAULT '0' COMMENT '签退状态',
  `签退时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '签退时间',
  PRIMARY KEY (`签退记录id`),
  KEY `idx_sign_out_id` (`活动签退id`),
  KEY `idx_user_id` (`社员id`),
  CONSTRAINT `fk_sign_out_rec_sign_id` FOREIGN KEY (`活动签退id`) REFERENCES `活动签退` (`活动签退id`),
  CONSTRAINT `fk_sign_out_rec_user_id` FOREIGN KEY (`社员id`) REFERENCES `社员` (`社员id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='签退记录';

-- 14. 活动反馈表
DROP TABLE IF EXISTS `活动反馈`;
CREATE TABLE `活动反馈` (
  `活动反馈id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动反馈id',
  `活动信息id` bigint(20) NOT NULL COMMENT '活动ID',
  `社员id` bigint(20) NOT NULL COMMENT '社员ID',
  `心得体会内容` text NOT NULL COMMENT '心得体会内容',
  `提交时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  PRIMARY KEY (`活动反馈id`),
  KEY `idx_activity_id` (`活动信息id`),
  KEY `idx_user_id` (`社员id`),
  CONSTRAINT `fk_act_feedback_act_id` FOREIGN KEY (`活动信息id`) REFERENCES `活动信息` (`活动信息id`),
  CONSTRAINT `fk_act_feedback_user_id` FOREIGN KEY (`社员id`) REFERENCES `社员` (`社员id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动反馈';

-- 15. 反馈回复表
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

-- 16. 活动推文表
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

-- 17. 通知信息表
DROP TABLE IF EXISTS `通知信息`;
CREATE TABLE `通知信息` (
  `通知信息id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '通知信息id',
  `社团信息id` bigint(20) DEFAULT NULL COMMENT '社团ID',
  `通知标题` varchar(100) NOT NULL COMMENT '通知标题',
  `通知内容` text NOT NULL COMMENT '通知内容',
  `发布人id` bigint(20) NOT NULL COMMENT '发布人ID',
  `发布时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`通知信息id`),
  KEY `idx_club_id` (`社团信息id`),
  CONSTRAINT `fk_notice_info_club_id` FOREIGN KEY (`社团信息id`) REFERENCES `社团信息` (`社团信息id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知信息';

-- 18. 缴费通知表
DROP TABLE IF EXISTS `缴费通知`;
CREATE TABLE `缴费通知` (
  `缴费通知id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '缴费通知id',
  `社团信息id` bigint(20) NOT NULL COMMENT '社团ID',
  `缴费标题` varchar(100) NOT NULL COMMENT '缴费标题',
  `缴费金额` decimal(10,2) NOT NULL COMMENT '缴费金额',
  `截止时间` datetime NOT NULL COMMENT '截止时间',
  `发布时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`缴费通知id`),
  KEY `idx_club_id` (`社团信息id`),
  CONSTRAINT `fk_payment_notice_club_id` FOREIGN KEY (`社团信息id`) REFERENCES `社团信息` (`社团信息id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费通知';

-- 19. 缴费记录表
DROP TABLE IF EXISTS `缴费记录`;
CREATE TABLE `缴费记录` (
  `缴费记录id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '缴费记录id',
  `缴费通知id` bigint(20) NOT NULL COMMENT '缴费通知ID',
  `社员id` bigint(20) NOT NULL COMMENT '缴费用户ID',
  `实际缴费金额` decimal(10,2) NOT NULL COMMENT '实际缴费金额',
  `状态` tinyint(1) DEFAULT '0' COMMENT '状态',
  `缴费时间` datetime DEFAULT NULL COMMENT '缴费时间',
  PRIMARY KEY (`缴费记录id`),
  KEY `idx_notice_id` (`缴费通知id`),
  KEY `idx_user_id` (`社员id`),
  CONSTRAINT `fk_payment_record_notice_id` FOREIGN KEY (`缴费通知id`) REFERENCES `缴费通知` (`缴费通知id`),
  CONSTRAINT `fk_payment_record_user_id` FOREIGN KEY (`社员id`) REFERENCES `社员` (`社员id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费记录';

-- 20. 提现记录表
DROP TABLE IF EXISTS `提现记录`;
CREATE TABLE `提现记录` (
  `提现记录id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '提现记录id',
  `社团信息id` bigint(20) NOT NULL COMMENT '社团ID',
  `社长id` bigint(20) NOT NULL COMMENT '社长ID',
  `提现金额` decimal(10,2) NOT NULL COMMENT '提现金额',
  `提现事由` varchar(255) DEFAULT NULL COMMENT '提现事由',
  `状态` tinyint(1) DEFAULT '1' COMMENT '状态',
  `提现时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '提现时间',
  PRIMARY KEY (`提现记录id`),
  KEY `idx_club_id` (`社团信息id`),
  KEY `idx_applicant_id` (`社长id`),
  CONSTRAINT `fk_withdrawal_club_id` FOREIGN KEY (`社团信息id`) REFERENCES `社团信息` (`社团信息id`),
  CONSTRAINT `fk_withdrawal_applicant_id` FOREIGN KEY (`社长id`) REFERENCES `社长` (`社长id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提现记录';
