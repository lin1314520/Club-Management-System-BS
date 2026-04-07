-- 数据库概念模型 (中文列名版)
CREATE DATABASE IF NOT EXISTS `club_management_conceptual` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `club_management_conceptual`;

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
  PRIMARY KEY (`系统管理员id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统管理员';

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
  PRIMARY KEY (`社长id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社长';

DROP TABLE IF EXISTS `普通用户`;
CREATE TABLE `普通用户` (
  `普通用户id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '普通用户id',
  `用户名/学号` varchar(50) NOT NULL COMMENT '用户名/学号',
  `密码` varchar(100) NOT NULL COMMENT '密码',
  `真实姓名` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `手机号` varchar(20) DEFAULT NULL COMMENT '手机号',
  `邮箱` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `状态` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `社团成员状态` tinyint(1) DEFAULT '0' COMMENT '社团成员状态：0-普通用户，1-社团成员',
  `创建时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `更新时间` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`普通用户id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='普通用户';

DROP TABLE IF EXISTS `社团类型`;
CREATE TABLE `社团类型` (
  `社团类型id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '社团类型id',
  `类型名称` varchar(50) NOT NULL COMMENT '类型名称',
  `类型描述` varchar(255) DEFAULT NULL COMMENT '类型描述',
  `创建时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`社团类型id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团类型';

DROP TABLE IF EXISTS `社团信息`;
CREATE TABLE `社团信息` (
  `社团信息id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '社团信息id',
  `社团名称` varchar(100) NOT NULL COMMENT '社团名称',
  `社团简介` text COMMENT '社团简介',
  `状态` tinyint(1) DEFAULT '1' COMMENT '状态：0-已注销，1-正常',
  `成立时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '成立时间',
  PRIMARY KEY (`社团信息id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团信息';

DROP TABLE IF EXISTS `建社申请`;
CREATE TABLE `建社申请` (
  `建社申请id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '建社申请id',
  `申请创建的社团名称` varchar(100) NOT NULL COMMENT '申请创建的社团名称',
  `申请理由及简介` text COMMENT '申请理由及简介',
  `审批状态` tinyint(1) DEFAULT '0' COMMENT '审批状态：0-审批中，1-已通过，2-已拒绝，3-已撤销',
  `申请时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `审批时间` datetime DEFAULT NULL COMMENT '审批时间',
  PRIMARY KEY (`建社申请id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='建社申请';

DROP TABLE IF EXISTS `社团成员`;
CREATE TABLE `社团成员` (
  `社团成员id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '社团成员id',
  `社内角色` tinyint(1) DEFAULT '0' COMMENT '社内角色：0-普通成员，1-社长',
  `状态` tinyint(1) DEFAULT '0' COMMENT '状态：0-申请中，1-已加入，2-已拒绝，3-已退出',
  `申请时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `加入时间` datetime DEFAULT NULL COMMENT '加入时间',
  PRIMARY KEY (`社团成员id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团成员';

DROP TABLE IF EXISTS `活动信息`;
CREATE TABLE `活动信息` (
  `活动信息id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动信息id',
  `活动标题` varchar(100) NOT NULL COMMENT '活动标题',
  `活动详情` text COMMENT '活动详情',
  `活动地点` varchar(255) DEFAULT NULL COMMENT '活动地点',
  `开始时间` datetime NOT NULL COMMENT '开始时间',
  `结束时间` datetime NOT NULL COMMENT '结束时间',
  `状态` tinyint(1) DEFAULT '0' COMMENT '状态：0-未开始，1-进行中，2-已结束',
  `发布时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`活动信息id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动信息';

DROP TABLE IF EXISTS `活动参与`;
CREATE TABLE `活动参与` (
  `活动参与id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动参与id',
  `审核状态` tinyint(1) DEFAULT '0' COMMENT '审核状态：0-待审核，1-已通过，2-已拒绝',
  `报名时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
  `签到时间` datetime DEFAULT NULL COMMENT '签到时间',
  `签退时间` datetime DEFAULT NULL COMMENT '签退时间',
  PRIMARY KEY (`活动参与id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动参与';

DROP TABLE IF EXISTS `活动签到`;
CREATE TABLE `活动签到` (
  `活动签到id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动签到id',
  `签到时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
  PRIMARY KEY (`活动签到id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动签到';

DROP TABLE IF EXISTS `活动签退`;
CREATE TABLE `活动签退` (
  `活动签退id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动签退id',
  `签退时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '签退时间',
  PRIMARY KEY (`活动签退id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动签退';

DROP TABLE IF EXISTS `活动反馈`;
CREATE TABLE `活动反馈` (
  `活动反馈id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动反馈id',
  `心得体会内容` text NOT NULL COMMENT '心得体会内容',
  `提交时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  PRIMARY KEY (`活动反馈id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动反馈';

DROP TABLE IF EXISTS `反馈回复`;
CREATE TABLE `反馈回复` (
  `反馈回复id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '反馈回复id',
  `回复内容` text NOT NULL COMMENT '回复内容',
  `回复时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '回复时间',
  PRIMARY KEY (`反馈回复id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='反馈回复';

DROP TABLE IF EXISTS `活动推文`;
CREATE TABLE `活动推文` (
  `活动推文id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动推文id',
  `推文标题` varchar(100) NOT NULL COMMENT '推文标题',
  `推文内容` text NOT NULL COMMENT '推文内容',
  `发布时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`活动推文id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动推文';

DROP TABLE IF EXISTS `通知信息`;
CREATE TABLE `通知信息` (
  `通知信息id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '通知信息id',
  `通知标题` varchar(100) NOT NULL COMMENT '通知标题',
  `通知内容` text NOT NULL COMMENT '通知内容',
  `发布时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`通知信息id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知信息';

DROP TABLE IF EXISTS `缴费通知`;
CREATE TABLE `缴费通知` (
  `缴费通知id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '缴费通知id',
  `缴费标题` varchar(100) NOT NULL COMMENT '缴费标题(如：2023秋季会费)',
  `缴费金额` decimal(10,2) NOT NULL COMMENT '缴费金额',
  `截止时间` datetime NOT NULL COMMENT '截止时间',
  `发布时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`缴费通知id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费通知';

DROP TABLE IF EXISTS `缴费记录`;
CREATE TABLE `缴费记录` (
  `缴费记录id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '缴费记录id',
  `实际缴费金额` decimal(10,2) NOT NULL COMMENT '实际缴费金额',
  `状态` tinyint(1) DEFAULT '0' COMMENT '状态：0-未缴费，1-已缴费',
  `缴费时间` datetime DEFAULT NULL COMMENT '缴费时间',
  PRIMARY KEY (`缴费记录id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费记录';

DROP TABLE IF EXISTS `提现记录`;
CREATE TABLE `提现记录` (
  `提现记录id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '提现记录id',
  `提现金额` decimal(10,2) NOT NULL COMMENT '提现金额',
  `提现事由` varchar(255) DEFAULT NULL COMMENT '提现事由',
  `状态` tinyint(1) DEFAULT '1' COMMENT '状态：1-已完成',
  `提现时间` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '提现时间',
  PRIMARY KEY (`提现记录id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提现记录';
