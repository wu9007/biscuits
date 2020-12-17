SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbl_order
-- ----------------------------
DROP TABLE IF EXISTS `tbl_order`;
CREATE TABLE `tbl_order`  (
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '1',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `cost` decimal(10, 2) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_actor
-- ----------------------------
DROP TABLE IF EXISTS `t_actor`;
CREATE TABLE `t_actor`  (
  `UUID` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一标识',
  `SERVICE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务标识',
  `BUNDLE_ID` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模块标识',
  `ACTOR_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参与者标识',
  `NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参与者名称',
  `DESCRIPTION` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参与者描述',
  `SORT` int(11) NOT NULL DEFAULT 10 COMMENT '排序码',
  PRIMARY KEY (`UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_app_version
-- ----------------------------
DROP TABLE IF EXISTS `t_app_version`;
CREATE TABLE `t_app_version`  (
  `UUID` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '数据编号',
  `VERSION` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '版本号',
  `URL` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '下载地址',
  `PATH` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'apk保存路径',
  `MANDATORY` tinyint(1) NULL DEFAULT NULL COMMENT '是否强制升级',
  `DESCRIPTION` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '升级描述',
  `APP_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app名称',
  `PUBLISH_TIME` datetime(0) NULL DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'apk版本管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_authority
-- ----------------------------
DROP TABLE IF EXISTS `t_authority`;
CREATE TABLE `t_authority`  (
  `UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `SERVICE_ID` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `ID` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `DESCRIPTION` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `BUNDLE_ID` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`UUID`) USING BTREE,
  UNIQUE INDEX `key_server_id`(`ID`, `SERVICE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_bill_type
-- ----------------------------
DROP TABLE IF EXISTS `t_bill_type`;
CREATE TABLE `t_bill_type`  (
  `UUID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CODE` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SERVICE_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`UUID`) USING BTREE,
  UNIQUE INDEX `code_service`(`CODE`, `SERVICE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_bundle
-- ----------------------------
DROP TABLE IF EXISTS `t_bundle`;
CREATE TABLE `t_bundle`  (
  `UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `BUNDLE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `BUNDLE_NAME` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `SERVICE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '服务ID',
  `WITH_AUTH` int(1) NULL DEFAULT NULL COMMENT '访问该bundle是否需要权限',
  `SORT` double(11, 2) NULL DEFAULT 0.00 COMMENT '排序码',
  `TARGET_CLIENT` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '客户端',
  `IMG` longblob NULL COMMENT '图标',
  PRIMARY KEY (`UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

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
  `IS_COLLECT_BLOOD` int(1) NOT NULL DEFAULT 0 COMMENT '是否采血科室',
  `SYSTEM_CODE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务系统对照',
  `LEADER` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分管领导',
  `STATION_UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '血站标识',
  PRIMARY KEY (`UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_department_class
-- ----------------------------
DROP TABLE IF EXISTS `t_department_class`;
CREATE TABLE `t_department_class`  (
  `UUID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SPELL` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SORT` double(10, 2) NULL DEFAULT NULL,
  `ENABLE` tinyint(1) NULL DEFAULT 1,
  PRIMARY KEY (`UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_department_class_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_department_class_relation`;
CREATE TABLE `t_department_class_relation`  (
  `UUID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DEPARTMENT_UUID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASS_UUID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_device_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_device_auth`;
CREATE TABLE `t_device_auth`  (
  `UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一标识',
  `TYPE_UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型',
  `DEVICE_SN` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备序列号',
  `AUTHORISATION_CODE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备授权码',
  `SERVICE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属服务标识',
  PRIMARY KEY (`UUID`) USING BTREE,
  UNIQUE INDEX `UK_DEVICE_AUTHORISATION`(`TYPE_UUID`, `DEVICE_SN`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_group
-- ----------------------------
DROP TABLE IF EXISTS `t_group`;
CREATE TABLE `t_group`  (
  `UUID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `GROUP_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分组ID',
  `GROUP_NAME` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分组名称',
  `SORT` double(11, 2) NULL DEFAULT NULL COMMENT '排序码',
  `SERVICE_ID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务ID',
  `ADDITIONAL_SERVICE_ID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附加服务ID，该分组可包含附加服务的功能点',
  `PARENT_UUID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父节点',
  `IMG` longblob NULL COMMENT '图标',
  PRIMARY KEY (`UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_group_bundle
-- ----------------------------
DROP TABLE IF EXISTS `t_group_bundle`;
CREATE TABLE `t_group_bundle`  (
  `UUID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `GROUP_UUID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `BUNDLE_UUID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SORT` decimal(11, 2) NULL DEFAULT 0.00,
  PRIMARY KEY (`UUID`) USING BTREE,
  UNIQUE INDEX `index_group_bundle`(`GROUP_UUID`, `BUNDLE_UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_history
-- ----------------------------
DROP TABLE IF EXISTS `t_history`;
CREATE TABLE `t_history`  (
  `UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `OPERATE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `OPERATOR` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `OPERATE_TIME` datetime(0) NULL DEFAULT NULL,
  `BUSINESS_UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `OPERATE_CONTENT` json NULL,
  PRIMARY KEY (`UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_id_rule
-- ----------------------------
DROP TABLE IF EXISTS `t_id_rule`;
CREATE TABLE `t_id_rule`  (
  `UUID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `SERVICE_ID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务编码',
  `CLIENT_TYPE` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端类型',
  `BILL_TYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单据类型，关联 T_BILL_TYPE',
  `BILL_CODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单据编码',
  `DIFF_BY_DEPT` tinyint(1) NULL DEFAULT 0 COMMENT '是否区分业务部门',
  `DATE_FORMATTER` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日期格式',
  `SERIAL_LENGTH` tinyint(1) NULL DEFAULT 5 COMMENT '序号长度',
  `RESET_RULE` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '序号清零规则',
  PRIMARY KEY (`UUID`) USING BTREE,
  UNIQUE INDEX `unique_key`(`BILL_TYPE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_mapper
-- ----------------------------
DROP TABLE IF EXISTS `t_mapper`;
CREATE TABLE `t_mapper`  (
  `UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `BUNDLE_UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `REQUEST_METHOD` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `ACTION_ID` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `SERVICE_ID` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `BUNDLE_ID` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `AUTH_ID` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`UUID`) USING BTREE,
  UNIQUE INDEX `key_mapper`(`ACTION_ID`, `SERVICE_ID`, `BUNDLE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_microservice
-- ----------------------------
DROP TABLE IF EXISTS `t_microservice`;
CREATE TABLE `t_microservice`  (
  `UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一标识',
  `SERVICE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微服务ID',
  `VIEW_SERVICE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '页面服务',
  `SORT` double(11, 2) NULL DEFAULT NULL COMMENT '排序码',
  `ENABLE` tinyint(1) NOT NULL COMMENT '启用（0-启用，1-未启用）',
  `IMG` longblob NULL COMMENT '图标',
  `IP` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'localhost' COMMENT '服务器ip',
  `PORT` int(5) NOT NULL DEFAULT 8080 COMMENT '端口号',
  `NAME` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微服务名称',
  PRIMARY KEY (`UUID`) USING BTREE,
  UNIQUE INDEX `server`(`SERVICE_ID`) USING BTREE COMMENT '服务器唯一'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '服务器设置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_post
-- ----------------------------
DROP TABLE IF EXISTS `t_post`;
CREATE TABLE `t_post`  (
  `UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一标识',
  `NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `SPELL` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '拼音码',
  `SORT` double NOT NULL COMMENT '排序码',
  `ENABLE` int(11) NOT NULL COMMENT '启用',
  `MEMO` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `SERVICE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子系统ID',
  PRIMARY KEY (`UUID`) USING BTREE,
  UNIQUE INDEX `index_name`(`NAME`, `SERVICE_ID`) USING BTREE,
  UNIQUE INDEX `index_spell`(`SPELL`, `SERVICE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_post_actor
-- ----------------------------
DROP TABLE IF EXISTS `t_post_actor`;
CREATE TABLE `t_post_actor`  (
  `UUID` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一标识',
  `POST_UUID` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位标识',
  `ACTOR_UUID` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参与者标识',
  PRIMARY KEY (`UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_post_user
-- ----------------------------
DROP TABLE IF EXISTS `t_post_user`;
CREATE TABLE `t_post_user`  (
  `UUID` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一标识',
  `POST_UUID` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位标识',
  `USER_UUID` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参与者标识',
  PRIMARY KEY (`UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_preferences
-- ----------------------------
DROP TABLE IF EXISTS `t_preferences`;
CREATE TABLE `t_preferences`  (
  `UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '                           ',
  `ID` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数编号',
  `DESCRIPTION` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '参数描述',
  `VALUE` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数值',
  `NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数名称',
  `STATION_CODE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '站编码',
  `SERVICE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务编号',
  `BUNDLE_ID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '功能点编号',
  `REGEX` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '正则表达式',
  `ALERT` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提示信息',
  PRIMARY KEY (`UUID`) USING BTREE,
  UNIQUE INDEX `IDX_UK_PREFERENCE_ID_BUNDLE`(`ID`, `BUNDLE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '首选项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_process
-- ----------------------------
DROP TABLE IF EXISTS `t_process`;
CREATE TABLE `t_process`  (
  `UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `PROCESS_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `DATA_UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `CURRENT_NODE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `CUSTOMIZED_PROCESSES` varchar(320) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

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
  `SERVICE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子系统id',
  `DEPARTMENT_UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`UUID`) USING BTREE,
  UNIQUE INDEX `UNIQUE_SPELL`(`SPELL`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_role_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_role_auth`;
CREATE TABLE `t_role_auth`  (
  `UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `ROLE_UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `AUTH_UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`UUID`) USING BTREE,
  UNIQUE INDEX `key_role_auth`(`ROLE_UUID`, `AUTH_UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_station
-- ----------------------------
DROP TABLE IF EXISTS `t_station`;
CREATE TABLE `t_station`  (
  `UUID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '血站名称',
  `CODE` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标准编码',
  `SPELL` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '拼音码',
  `SORT` int(6) NOT NULL COMMENT '排序码',
  `ENABLE` int(1) NOT NULL COMMENT '是否启用',
  `PARENT_UUID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '上级血站标识，只针对分站有效。',
  `CLASS_UUID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '血站级别标识',
  PRIMARY KEY (`UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '血站表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_station_class
-- ----------------------------
DROP TABLE IF EXISTS `t_station_class`;
CREATE TABLE `t_station_class`  (
  `UUID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '血站级别名称',
  `CODE` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标准编码',
  `SPELL` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拼音码',
  `SORT` int(6) NOT NULL COMMENT '排序码',
  `ENABLE` int(1) NOT NULL COMMENT '是否启用',
  PRIMARY KEY (`UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '血站级别字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_table_name_id
-- ----------------------------
DROP TABLE IF EXISTS `t_table_name_id`;
CREATE TABLE `t_table_name_id`  (
  `UUID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `SERVICE_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务',
  `TABLE_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表名',
  `TABLE_ID` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表ID',
  `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`UUID`) USING BTREE,
  UNIQUE INDEX `index_tableId`(`TABLE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_td_style
-- ----------------------------
DROP TABLE IF EXISTS `t_td_style`;
CREATE TABLE `t_td_style`  (
  `UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一标识',
  `STATION_UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '血站标识',
  `SERVICE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '服务ID',
  `NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名称',
  `TYPE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `VERSION` int(32) NULL DEFAULT NULL COMMENT '版本号',
  `SORT` double NOT NULL COMMENT '排序码',
  `MEMO` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `JSON` json NULL COMMENT '样式串',
  `IMG` longblob NULL COMMENT '背景',
  `ENABLE` int(1) NULL DEFAULT NULL COMMENT '启用',
  PRIMARY KEY (`UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `UUID` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一标识',
  `DEPT_UUID` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门标识',
  `AVATAR` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录名',
  `STAFF_ID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工号/编号',
  `NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `SPELL` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拼音码',
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

SET FOREIGN_KEY_CHECKS = 1;
