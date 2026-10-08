-- Public initialization: schema only, no original users or business records.

-- Create and select your database before import.

SET NAMES utf8mb4;

SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE IF NOT EXISTS `admin`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `collect`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pkId` int NOT NULL COMMENT '外键',
  `date` datetime NOT NULL COMMENT '收藏时间',
  `userId` int NOT NULL COMMENT '收藏人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `comment`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '评论主键',
  `info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论信息',
  `pkId` int NOT NULL COMMENT '被评论内容',
  `time` datetime NOT NULL COMMENT '评论时间',
  `parentId` int NULL DEFAULT 0 COMMENT '父',
  `reply` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回复内容',
  `replyTime` datetime NULL DEFAULT NULL,
  `userId` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `confess`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表白',
  `info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '内容（json string)',
  `connect` int NOT NULL COMMENT '0手机1qq2微信3其他',
  `elseConnect` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '其他',
  `images` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片',
  `type` int NOT NULL COMMENT '（0表白1闲置2寻物3吐槽）',
  `time` datetime NOT NULL COMMENT '发布时间',
  `userId` int NOT NULL COMMENT '发布人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `like_table`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '点赞主键',
  `pkId` int NOT NULL COMMENT '外键',
  `userId` int NOT NULL COMMENT '点赞本人',
  `time` datetime NOT NULL COMMENT '点赞时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作内容',
  `methods` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求的方法',
  `classname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类方法',
  `user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作用户',
  `params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '参数',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '成功或者失败',
  `time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `sensitive`  (
  `id` int NOT NULL COMMENT '敏感词主键',
  `info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '敏感词内容',
  `status` int NOT NULL COMMENT '0用1不用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录账号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录密码',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS=1;
