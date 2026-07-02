-- 测试种子数据：为 example-mybatis 集成测试提供基础行
INSERT INTO `user_auth_phone` (`id`, `tenant`, `user_id`, `phone`, `pwd`, `version`, `deleted`, `creator`, `ctime`, `updater`, `utime`)
VALUES
    (1, 1, 1001, '13800000001', 'pwd1', 0, 0, 'tester', NOW(), 'tester', NOW()),
    (2, 1, 1002, '13800000002', 'pwd2', 0, 0, 'tester', NOW(), 'tester', NOW()),
    (3, 1, 1003, '13800000003', 'pwd3', 0, 0, 'tester', NOW(), 'tester', NOW()),
    (4, 1, 1004, '13800000004', 'pwd4', 0, 0, 'tester', NOW(), 'tester', NOW()),
    (5, 1, 1005, '13800000005', 'pwd5', 0, 0, 'tester', NOW(), 'tester', NOW()),
    (6, 1, 1006, '13800000006', 'pwd6', 0, 0, 'tester', NOW(), 'tester', NOW()),
    (7, 1, 1007, '13800000007', 'pwd7', 0, 0, 'tester', NOW(), 'tester', NOW()),
    (8, 1, 1008, '13800000008', 'pwd8', 0, 0, 'tester', NOW(), 'tester', NOW()),
    (9, 1, 1009, '13800000009', 'pwd9', 0, 0, 'tester', NOW(), 'tester', NOW());
