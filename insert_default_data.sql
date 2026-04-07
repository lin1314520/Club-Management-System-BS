USE `club_management`;

-- 插入管理员 (密码统一为 123456)
INSERT INTO `sys_admin` (`username`, `password`, `real_name`, `phone`, `email`, `status`) VALUES 
('admin', '123456', '超级管理员', '13800000000', 'admin@example.com', 1);

-- 插入社长
INSERT INTO `sys_president` (`username`, `password`, `real_name`, `phone`, `email`, `status`) VALUES 
('president1', '123456', '张社长', '13800000001', 'president1@example.com', 1);

-- 插入普通用户
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `email`, `status`, `member_status`) VALUES 
('user1', '123456', '李四', '13800000002', 'user1@example.com', 1, 1);

-- 插入初始社团类型
INSERT INTO `club_type` (`type_name`, `description`) VALUES 
('学术科技类', '学术、科技相关的社团'),
('文化体育类', '文化、体育、艺术相关的社团');

-- 插入初始社团
INSERT INTO `club_info` (`club_name`, `type_id`, `description`, `status`) VALUES 
('计算机协会', (SELECT type_id FROM `club_type` WHERE `type_name` = '学术科技类'), '致力于计算机技术的学习与交流', 1);

-- 插入社团成员 (社长与普通成员)
-- 张社长作为社长 (member_role = 1) -> 指向 president 表的ID
INSERT INTO `club_member` (`club_id`, `user_id`, `member_role`, `status`, `join_time`) VALUES 
((SELECT club_id FROM `club_info` WHERE `club_name` = '计算机协会'), (SELECT president_id FROM `sys_president` WHERE `username` = 'president1'), 1, 1, NOW()),
((SELECT club_id FROM `club_info` WHERE `club_name` = '计算机协会'), (SELECT user_id FROM `sys_user` WHERE `username` = 'user1'), 0, 1, NOW());
