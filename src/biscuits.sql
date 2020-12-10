SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_auth_mapper
-- ----------------------------
DROP TABLE IF EXISTS `t_auth_mapper`;
CREATE TABLE `t_auth_mapper`  (
  `uuid` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `server_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `auth_uuid` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `mapper_uuid` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `auth_mapper_key`(`auth_uuid`, `mapper_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_auth_mapper
-- ----------------------------
INSERT INTO `t_auth_mapper` VALUES ('1011010', 'BISCUITS-SERVER', '1011020', '1011033');
INSERT INTO `t_auth_mapper` VALUES ('1011011', 'BISCUITS-SERVER', '1011021', '1011034');
INSERT INTO `t_auth_mapper` VALUES ('1011012', 'BISCUITS-SERVER', '1011020', '1011035');

-- ----------------------------
-- Table structure for t_authority
-- ----------------------------
DROP TABLE IF EXISTS `t_authority`;
CREATE TABLE `t_authority`  (
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `server_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `bundle_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `key_server_id`(`id`, `server_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_authority
-- ----------------------------
INSERT INTO `t_authority` VALUES ('1011020', 'BISCUITS-SERVER', 'order_manage', '管理', '管理订单数据的权限', 'order');
INSERT INTO `t_authority` VALUES ('1011021', 'BISCUITS-SERVER', 'order_read', '读取', '读取订单数据的权限', 'order');

-- ----------------------------
-- Table structure for t_bundle
-- ----------------------------
DROP TABLE IF EXISTS `t_bundle`;
CREATE TABLE `t_bundle`  (
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `bundle_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `bundle_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `server_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `with_auth` int(1) NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_bundle
-- ----------------------------
INSERT INTO `t_bundle` VALUES ('1011080', 'home', '主页', 'BISCUIT', 0);
INSERT INTO `t_bundle` VALUES ('1011081', 'order', '订单', 'BISCUIT', 1);
INSERT INTO `t_bundle` VALUES ('10110810', 'order', '订单', 'SKELETON-SERVER', 1);
INSERT INTO `t_bundle` VALUES ('10110811', 'remote', '远程调用', 'SKELETON-SERVER', 0);
INSERT INTO `t_bundle` VALUES ('1011082', 'remote', '远程调用', 'BISCUIT', 0);
INSERT INTO `t_bundle` VALUES ('1011083', 'home', '主页', 'BISCUITS-SERVER', 0);
INSERT INTO `t_bundle` VALUES ('1011084', 'order', '订单', 'BISCUITS-SERVER', 1);
INSERT INTO `t_bundle` VALUES ('1011085', 'remote', '远程调用', 'BISCUITS-SERVER', 0);
INSERT INTO `t_bundle` VALUES ('1011086', 'home', '主页', 'DEMO', 0);
INSERT INTO `t_bundle` VALUES ('1011087', 'order', '订单', 'DEMO', 1);
INSERT INTO `t_bundle` VALUES ('1011088', 'remote', '远程调用', 'DEMO', 0);
INSERT INTO `t_bundle` VALUES ('1011089', 'home', '主页', 'SKELETON-SERVER', 0);

-- ----------------------------
-- Table structure for t_bundle_group
-- ----------------------------
DROP TABLE IF EXISTS `t_bundle_group`;
CREATE TABLE `t_bundle_group`  (
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `group_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `sort` int(11) NULL DEFAULT NULL,
  `server_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_bundle_group_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_bundle_group_relation`;
CREATE TABLE `t_bundle_group_relation`  (
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `bundle_uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `group_uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `sort` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `key_sort`(`sort`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 193 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_department
-- ----------------------------
DROP TABLE IF EXISTS `t_department`;
CREATE TABLE `t_department`  (
  `UUID` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一标识',
  `PARENT_UUID` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '父节点标识',
  `NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `SPELL` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '拼音码',
  `SORT` int(6) NOT NULL DEFAULT 0 COMMENT '排序码',
  `ENABLE` int(1) NOT NULL DEFAULT 1 COMMENT '启用',
  `IS_QC` int(1) NOT NULL DEFAULT 0 COMMENT '是否质控科室',
  `IS_PURCHASE` int(1) NOT NULL DEFAULT 0 COMMENT '是否采购科室',
  `IS_CHILD` int(1) NOT NULL DEFAULT 0 COMMENT '是否含子节点',
  `SYSTEM_CODE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务系统对照',
  `LEADER` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分管领导'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_history
-- ----------------------------
DROP TABLE IF EXISTS `t_history`;
CREATE TABLE `t_history`  (
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `operate` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `operator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `operate_time` datetime(0) NULL DEFAULT NULL,
  `business_uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `operate_content` json NULL,
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_mapper
-- ----------------------------
DROP TABLE IF EXISTS `t_mapper`;
CREATE TABLE `t_mapper`  (
  `uuid` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `bundle_uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `request_method` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `action_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `server_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `bundle_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `key_mapper`(`action_id`, `server_id`, `bundle_id`, `request_method`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_mapper
-- ----------------------------
INSERT INTO `t_mapper` VALUES ('1011030', '1011083', 'POST', 'register', 'BISCUITS-SERVER', 'home');
INSERT INTO `t_mapper` VALUES ('1011031', '1011083', 'POST', 'login', 'BISCUITS-SERVER', 'home');
INSERT INTO `t_mapper` VALUES ('1011032', '1011083', 'GET', 'welcome', 'BISCUITS-SERVER', 'home');
INSERT INTO `t_mapper` VALUES ('1011033', '1011084', 'POST', 'audit', 'BISCUITS-SERVER', 'order');
INSERT INTO `t_mapper` VALUES ('1011034', '1011084', 'GET', 'list_order', 'BISCUITS-SERVER', 'order');
INSERT INTO `t_mapper` VALUES ('1011035', '1011084', 'POST', 'save', 'BISCUITS-SERVER', 'order');
INSERT INTO `t_mapper` VALUES ('1011036', '1011085', 'GET', 'bai_du_dom', 'BISCUITS-SERVER', 'remote');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一标识',
  `NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `SPELL` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '拼音码',
  `SORT` double NOT NULL COMMENT '排序码',
  `ENABLE` int(11) NOT NULL COMMENT '启用',
  `MEMO` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `SERVER_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子系统id',
  PRIMARY KEY (`UUID`) USING BTREE,
  UNIQUE INDEX `UNIQUE_SPELL`(`SPELL`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('001', '管理员', 'ADMIN', 0, 1, '管理员', 'biscuits-server');

-- ----------------------------
-- Table structure for t_role_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_role_auth`;
CREATE TABLE `t_role_auth`  (
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `role_uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `auth_uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `key_role_auth`(`role_uuid`, `auth_uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role_auth
-- ----------------------------
INSERT INTO `t_role_auth` VALUES ('001', '001', '1011020');
INSERT INTO `t_role_auth` VALUES ('002', '001', '1011021');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `UUID` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一标识',
  `DEPT_UUID` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门标识',
  `CODE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录名',
  `NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `PHONE` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '移动电话',
  `PASSWORD` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'e10adc3949ba59abbe56e057f20f883e' COMMENT '密码',
  `SORT` int(6) NULL DEFAULT 10 COMMENT '排序码',
  `ENABLE` int(1) NOT NULL DEFAULT 1 COMMENT '启用',
  `SYSTEM_CODE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务系统对照码',
  `MEMO` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `SIGN_PHOTO` longblob NULL COMMENT '签名图片',
  `LAST_PASSWORD_RESET_DATE` datetime(0) NULL DEFAULT NULL COMMENT '密码最后更新时间',
  `LAST_ROLE_MODIFY_DATE` datetime(0) NULL DEFAULT NULL COMMENT '角色最后分配时间',
  `IS_MANAGER` int(1) NOT NULL DEFAULT 0 COMMENT '是否单点登录管理员',
  PRIMARY KEY (`UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('001', NULL, 'ADMIN', '管理员', '119', 'e10adc3949ba59abbe56e057f20f883e', 10, 1, NULL, NULL, NULL, '2019-09-21 00:02:25', '2019-09-21 00:02:41', 1);

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
  `UUID` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一标识',
  `USER_UUID` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户标识',
  `ROLE_UUID` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色标识',
  PRIMARY KEY (`UUID`) USING BTREE,
  UNIQUE INDEX `UDX_USER_ROLE`(`USER_UUID`, `ROLE_UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES ('001', '001', '001');

-- ----------------------------
-- Table structure for tbl_order
-- ----------------------------
DROP TABLE IF EXISTS `tbl_order`;
CREATE TABLE `tbl_order`  (
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '1',
  `RELEVANT_BILL_UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `price` decimal(10, 2) NULL DEFAULT NULL,
  `day` date NULL DEFAULT NULL,
  `time` datetime(0) NULL DEFAULT NULL,
  `state` int(1) NULL DEFAULT 1,
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `sort` int(11) NULL DEFAULT 0,
  `photo` longblob NULL,
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbl_order
-- ----------------------------
INSERT INTO `tbl_order` VALUES ('10101', '10130', 'DEMO-1568995473402', 1.10, NULL, NULL, NULL, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101010', NULL, 'C-006', 50.25, '2019-09-21', '2019-09-21 00:10:39', 0, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101011', NULL, 'C-001', 500.50, '2019-09-24', '2019-09-21 00:10:39', NULL, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101012', '10130', 'DEMO-1568996507289', 1.10, NULL, NULL, NULL, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101013', '10130', 'DEMO-1568996804537', 1.10, NULL, NULL, NULL, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101015', NULL, 'C-006', 50.25, '2019-09-23', '2019-09-23 19:20:41', 0, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101016', NULL, 'C-001', 500.50, '2019-09-24', '2019-09-23 19:20:41', NULL, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101018', NULL, 'C-006', 50.25, '2019-09-23', '2019-09-23 19:21:15', 0, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101019', NULL, 'C-001', 500.50, '2019-09-24', '2019-09-23 19:21:15', NULL, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('10102', '10130', 'DEMO-1568995661235', 1.10, NULL, NULL, NULL, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101021', NULL, 'C-006', 50.25, '2019-09-23', '2019-09-23 19:24:08', 0, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101022', NULL, 'C-001', 500.50, '2019-09-24', '2019-09-23 19:24:08', NULL, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101024', NULL, 'C-006', 50.25, '2019-09-24', '2019-09-24 09:53:07', 0, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101025', NULL, 'C-001', 500.50, '2019-09-24', '2019-09-24 09:53:07', NULL, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101027', NULL, 'C-006', 50.25, '2019-09-24', '2019-09-24 10:15:30', 0, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101028', NULL, 'C-001', 500.50, '2019-09-24', '2019-09-24 10:15:30', NULL, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101030', NULL, 'C-006', 50.25, '2019-09-24', '2019-09-24 11:05:08', 0, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101031', NULL, 'C-001', 500.50, '2019-09-24', '2019-09-24 11:05:08', NULL, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101033', NULL, 'C-006', 50.25, '2019-09-24', '2019-09-24 11:10:19', 0, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101034', NULL, 'C-001', 500.50, '2019-09-24', '2019-09-24 11:10:19', NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('101035', '10130', 'DEMO-1569397824628', 1.10, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('101036', '10130', 'DEMO-1569397839986', 1.10, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('101037', '10130', 'DEMO-1569397892903', 1.10, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('101038', '10130', 'DEMO-1569398150136', 1.10, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('101039', '10130', 'DEMO-1569398163349', 1.10, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('10104', NULL, 'C-006', 50.25, '2019-09-21', '2019-09-21 00:08:22', 0, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101040', '10130', 'DEMO-1569398230141', 1.10, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('101041', '10130', 'DEMO-1569398230996', 1.10, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('101042', '10130', 'DEMO-1569398231728', 1.10, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('101043', '10130', 'DEMO-1569398232253', 1.10, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('101044', '10130', 'DEMO-1569398232744', 1.10, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('101045', '10130', 'DEMO-1569398233452', 1.10, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('101046', '10130', 'DEMO-1569398330462', 1.10, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('101047', '10130', 'DEMO-1570518213688', 1.10, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('101048', '10130', 'DEMO-1570851423305', 1.10, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('101049', '10130', 'DEMO-1570851510076', 1.10, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('10105', NULL, 'C-001', 500.50, '2019-09-24', '2019-09-21 00:08:22', NULL, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('101050', '10130', 'DEMO-1570851536152', 1.10, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('101051', '10130', 'DEMO-1570851714688', 1.10, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('101052', '10130', 'DEMO-1570854853681', 1.10, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('101053', '10130', 'DEMO-1570855606445', 1.10, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `tbl_order` VALUES ('10107', NULL, 'C-006', 50.25, '2019-09-21', '2019-09-21 00:10:10', 0, '002', 0, NULL);
INSERT INTO `tbl_order` VALUES ('10108', NULL, 'C-001', 500.50, '2019-09-24', '2019-09-21 00:10:11', NULL, '002', 0, NULL);

-- ----------------------------
-- Table structure for tbl_order_type
-- ----------------------------
DROP TABLE IF EXISTS `tbl_order_type`;
CREATE TABLE `tbl_order_type`  (
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbl_relevant_bill
-- ----------------------------
DROP TABLE IF EXISTS `tbl_relevant_bill`;
CREATE TABLE `tbl_relevant_bill`  (
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '1',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `AVAILABLE` int(1) NULL DEFAULT 1,
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbl_relevant_bill
-- ----------------------------
INSERT INTO `tbl_relevant_bill` VALUES ('10130', 'Hello-001', 0);

-- ----------------------------
-- Table structure for tbl_relevant_bill_detail
-- ----------------------------
DROP TABLE IF EXISTS `tbl_relevant_bill_detail`;
CREATE TABLE `tbl_relevant_bill_detail`  (
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `price` decimal(10, 2) NULL DEFAULT 0.00,
  `RELEVANT_BILL_UUID` int(32) NULL DEFAULT NULL,
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tbl_relevant_bill_detail
-- ----------------------------
INSERT INTO `tbl_relevant_bill_detail` VALUES ('001', '测试明细001', 14.00, 10130, '');
INSERT INTO `tbl_relevant_bill_detail` VALUES ('002', '测试明细002', 24.00, 10130, NULL);

SET FOREIGN_KEY_CHECKS = 1;
