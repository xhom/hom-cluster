SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for local_user
-- ----------------------------
DROP TABLE IF EXISTS `local_user`;
CREATE TABLE `local_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮件地址',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `create_dt` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_dt` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `last_login_dt` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of local_user
-- 123456 - $2a$10$GAFesTx89j8cEEOHDKdT0uTT3lxF.8C2uSFg.RH0T0.BVaEgYtJeW
-- 123abc - $2a$10$vs2tzE3AfnhgQ/gOTL1KbOIGDXZyov8hQQqMu2uch0d21jCi.EbZ6
-- abc123 - $2a$10$xdCFPmhIqnCqAP.UIKzFFe.AXRp6QhTiwjAk6LY9dHkJI.RxplGny
-- ----------------------------
INSERT INTO `local_user` VALUES (1, 'zhangsan', '$2a$10$GAFesTx89j8cEEOHDKdT0uTT3lxF.8C2uSFg.RH0T0.BVaEgYtJeW', 'zs@qq.com', '17723119808', '2023-05-25 15:52:41', '2023-05-25 15:52:46', NULL);
INSERT INTO `local_user` VALUES (2, 'lisi', '$2a$10$vs2tzE3AfnhgQ/gOTL1KbOIGDXZyov8hQQqMu2uch0d21jCi.EbZ6', 'ls@qq.com', '19923248871', '2023-05-25 15:53:21', '2023-05-25 15:53:23', NULL);
INSERT INTO `local_user` VALUES (3, 'wangwu', '$2a$10$xdCFPmhIqnCqAP.UIKzFFe.AXRp6QhTiwjAk6LY9dHkJI.RxplGny', 'ww@qq.com', '13811229065', '2023-05-25 15:55:21', '2023-05-25 15:55:23', NULL);

SET FOREIGN_KEY_CHECKS = 1;
