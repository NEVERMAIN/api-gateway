/*
 Navicat Premium Dump SQL

 Source Server         : 192.168.198.138_13306
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : 192.168.198.138:13306
 Source Schema         : api_gateway

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 08/08/2025 10:12:08
*/

create database api_gateway default character set utf8mb4 collate utf8mb4_0900_ai_ci;
use api_gateway;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for application_interface
-- ----------------------------
DROP TABLE IF EXISTS `application_interface`;
CREATE TABLE `application_interface`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `system_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用唯一标识',
  `interface_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '系统接口唯一标识',
  `interface_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '服务接口名称',
  `interface_version` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口版本',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统接口' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of application_interface
-- ----------------------------
INSERT INTO `application_interface` VALUES (1, 'api-gateway-test', 'io.github.NEVERMAIN.gateway.rpc.IActivityBooth', '网关测试接口', '1.0.0', '2025-07-01 17:17:40', '2025-07-25 22:01:48');

-- ----------------------------
-- Table structure for application_interface_method
-- ----------------------------
DROP TABLE IF EXISTS `application_interface_method`;
CREATE TABLE `application_interface_method`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `system_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用唯一标识',
  `interface_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '系统接口唯一标识',
  `method_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '系统接口方法唯一标识',
  `method_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '服务方法名称',
  `parameter_type` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '参数类型(RPC 限定单参数注册): new String[]{\"java.lang.String\"}、new String[]{\"io.github.NEVERMAIN.gateway.rpc.dto.XReq\"}',
  `uri` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网关接口',
  `auth` int NULL DEFAULT NULL COMMENT '0-不需要鉴权 1-需要鉴权',
  `http_command_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口类型；GET、POST、PUT、DELETE',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统接口方法详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of application_interface_method
-- ----------------------------
INSERT INTO `application_interface_method` VALUES (2, 'api-gateway-test', 'io.github.NEVERMAIN.gateway.rpc.IActivityBooth', 'sayHi', '测试方法', 'java.lang.String', '/wg/activity/sayHi', 0, 'GET', '2025-07-01 17:24:26', '2025-07-01 17:24:26');
INSERT INTO `application_interface_method` VALUES (3, 'api-gateway-test', 'io.github.NEVERMAIN.gateway.rpc.IActivityBooth', 'insert', '插入方法', 'io.github.NEVERMAIN.gateway.rpc.dto.XReq', '/wg/activity/insert', 1, 'POST', '2025-07-01 17:24:27', '2025-07-02 16:57:44');

-- ----------------------------
-- Table structure for application_system
-- ----------------------------
DROP TABLE IF EXISTS `application_system`;
CREATE TABLE `application_system`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `system_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '系统ID',
  `system_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用名称',
  `system_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '系统类型：RPC、HTTP',
  `system_registry` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '系统注册中心：ZK、ETCD、NACOS',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '应用系统' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of application_system
-- ----------------------------
INSERT INTO `application_system` VALUES (1, 'api-gateway-test', '网关测试系统', 'RPC', 'zookeeper://192.168.198.138:2181', '2025-07-01 17:17:15', '2025-07-25 21:59:20');

-- ----------------------------
-- Table structure for gateway_distribution
-- ----------------------------
DROP TABLE IF EXISTS `gateway_distribution`;
CREATE TABLE `gateway_distribution`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `group_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分组ID',
  `gateway_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网关ID',
  `system_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '系统唯一标识',
  `system_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '系统名称',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '网关分配' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gateway_distribution
-- ----------------------------
INSERT INTO `gateway_distribution` VALUES (1, '10001', 'api-gateway-g1', 'api-gateway-test', '测试系统', '2025-07-02 16:55:38', '2025-07-02 16:55:49');

-- ----------------------------
-- Table structure for gateway_server
-- ----------------------------
DROP TABLE IF EXISTS `gateway_server`;
CREATE TABLE `gateway_server`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `group_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分组ID',
  `group_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分组名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '网关服务' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gateway_server
-- ----------------------------
INSERT INTO `gateway_server` VALUES (1, '10001', '电商支付网关');

-- ----------------------------
-- Table structure for gateway_server_detail
-- ----------------------------
DROP TABLE IF EXISTS `gateway_server_detail`;
CREATE TABLE `gateway_server_detail`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `group_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '网关服务分组',
  `gateway_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网关ID',
  `gateway_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网关名称',
  `gateway_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网关地址',
  `status` int NULL DEFAULT NULL COMMENT '服务状态：0-不可用、1-可使用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `un_idx_group_id_group_address`(`gateway_id` ASC, `gateway_address` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '网关服务明细' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gateway_server_detail
-- ----------------------------
INSERT INTO `gateway_server_detail` VALUES (10, '10001', 'api-gateway-g1', '电商配送网关', '192.168.198.138:7397', 1, '2025-07-26 00:22:21', '2025-08-07 00:51:52');

-- ----------------------------
-- Table structure for http_statement
-- ----------------------------
DROP TABLE IF EXISTS `http_statement`;
CREATE TABLE `http_statement`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `application` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '应用名称',
  `interface_name` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '服务接口；RPC、其他',
  `method_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT ' 服务方法；RPC#method',
  `parameter_type` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '参数类型(RPC 限定单参数注册)；new String[]{\"java.lang.String\"}、new String[]{\"cn.bugstack.gateway.rpc.dto.XReq\"}',
  `uri` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '网关接口',
  `http_command_type` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '接口类型；GET、POST、PUT、DELETE',
  `auth` int NOT NULL DEFAULT 0 COMMENT 'true = 1是、false = 0否',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of http_statement
-- ----------------------------
INSERT INTO `http_statement` VALUES (1, 'api-gateway-test', 'io.github.NEVERMAIN.gateway.rpc.IActivityBooth', 'sayHi', 'java.lang.String', '/wg/activity/sayHi', 'GET', 0, '2022-10-22 15:30:00', '2022-10-22 15:30:00');
INSERT INTO `http_statement` VALUES (2, 'api-gateway-test', 'io.github.NEVERMAIN.gateway.rpc.IActivityBooth', 'insert', 'io.github.NEVERMAIN.gateway.rpc.dto.XReq', '/wg/activity/insert', 'POST', 1, '2022-10-22 15:30:00', '2022-10-22 15:30:00');

SET FOREIGN_KEY_CHECKS = 1;
