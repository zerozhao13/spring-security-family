-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        10.5.4-MariaDB-1:10.5.4+maria~focal - mariadb.org binary distribution
-- 服务器操作系统:                      debian-linux-gnu
-- HeidiSQL 版本:                  11.0.0.5919
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 phoenix_user 的数据库结构
CREATE DATABASE IF NOT EXISTS `phoenix_user` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `phoenix_user`;

-- 导出  表 phoenix_user.open_id_role 结构
DROP TABLE IF EXISTS `open_id_role`;
CREATE TABLE IF NOT EXISTS `open_id_role` (
  `open_id` varchar(32) NOT NULL COMMENT '用户openid',
  `rid` smallint(5) unsigned NOT NULL COMMENT '角色编号',
  `create_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '更新时间',
  `version` tinyint(3) unsigned NOT NULL DEFAULT 1 COMMENT '数据版本',
  PRIMARY KEY (`open_id`,`rid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='openid与角色之间的关系';

-- 正在导出表  phoenix_user.open_id_role 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `open_id_role` DISABLE KEYS */;
INSERT IGNORE INTO `open_id_role` (`open_id`, `rid`, `create_time`, `update_time`, `version`) VALUES
	('8759cc97c9f69dbf139963d8e22a2d54', 1, '2020-08-08 09:33:43', '2020-08-08 09:33:43', 1);
/*!40000 ALTER TABLE `open_id_role` ENABLE KEYS */;

-- 导出  表 phoenix_user.permission 结构
DROP TABLE IF EXISTS `permission`;
CREATE TABLE IF NOT EXISTS `permission` (
  `pid` smallint(5) unsigned NOT NULL COMMENT '权限id',
  `permission` varchar(128) NOT NULL DEFAULT '' COMMENT '权限名',
  `resource` varchar(64) NOT NULL DEFAULT '' COMMENT '资源路径',
  `des` varchar(256) NOT NULL DEFAULT '' COMMENT '资源描述',
  `create_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '更新时间',
  `version` tinyint(3) unsigned NOT NULL DEFAULT 1 COMMENT '数据版本',
  PRIMARY KEY (`pid`),
  UNIQUE KEY `资源路径` (`resource`),
  KEY `权限名` (`permission`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源对应权限数据';

-- 正在导出表  phoenix_user.permission 的数据：~5 rows (大约)
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT IGNORE INTO `permission` (`pid`, `permission`, `resource`, `des`, `create_time`, `update_time`, `version`) VALUES
	(1, '查询用户列表', '/v1/users', '查询用户列表', '2020-08-08 00:00:00', '2020-08-08 00:00:00', 1),
	(2, '新增用户角色', '/v1/users/{openId}/roles/create', '新增用户角色', '2020-08-08 00:00:00', '2020-08-08 00:00:00', 1),
	(3, '查询权限列表', '/v1/permissions', '查询权限列表', '2020-08-08 00:00:00', '2020-08-08 00:00:00', 1),
	(4, '新增权限控制', '/v1/permissions/create', '新增权限控制', '2020-08-08 00:00:00', '2020-08-08 00:00:00', 1),
	(5, '新增角色权限关系', '/v1/permissions/roles/create', '新增角色权限关系', '2020-08-08 00:00:00', '2020-08-08 00:00:00', 1);
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;

-- 导出  表 phoenix_user.role_permission 结构
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE IF NOT EXISTS `role_permission` (
  `rid` smallint(5) unsigned NOT NULL COMMENT '角色id',
  `pid` smallint(5) unsigned NOT NULL DEFAULT 0 COMMENT '权限id',
  `create_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '更新时间',
  `version` tinyint(3) unsigned NOT NULL DEFAULT 1 COMMENT '数据版本',
  PRIMARY KEY (`rid`,`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限对应表';

-- 正在导出表  phoenix_user.role_permission 的数据：~5 rows (大约)
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
INSERT IGNORE INTO `role_permission` (`rid`, `pid`, `create_time`, `update_time`, `version`) VALUES
	(1, 1, '2020-08-08 06:56:30', '2020-08-08 06:56:30', 1),
	(1, 2, '2020-08-08 06:56:37', '2020-08-08 06:56:37', 1),
	(1, 3, '2020-08-08 06:56:41', '2020-08-08 06:56:41', 1),
	(1, 4, '2020-08-08 06:56:46', '2020-08-08 06:56:46', 1),
	(1, 5, '2020-08-08 06:56:51', '2020-08-08 06:56:51', 1);
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;

-- 导出  表 phoenix_user.user 结构
DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `uid` int(10) unsigned NOT NULL COMMENT '用户id',
  `username` varchar(32) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(64) NOT NULL DEFAULT '' COMMENT '用户密码',
  `expired` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '是否过期',
  `locked` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '是否锁定',
  `enabled` tinyint(1) unsigned NOT NULL DEFAULT 1 COMMENT '是否激活可用',
  `create_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '更新时间',
  `version` tinyint(3) unsigned NOT NULL DEFAULT 1 COMMENT '数据版本',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 正在导出表  phoenix_user.user 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT IGNORE INTO `user` (`uid`, `username`, `password`, `expired`, `locked`, `enabled`, `create_time`, `update_time`, `version`) VALUES
	(1, 'phoenix', '$2a$10$EKQ1I1ViVEyLD6bZq.9zwe2bhzQsvAjnlN3bKwVlSkD1Qt7k3xRl.', 1, 1, 1, '2020-08-08 17:31:24', '2020-08-08 17:31:24', 1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

-- 导出  表 phoenix_user.user_open_id 结构
DROP TABLE IF EXISTS `user_open_id`;
CREATE TABLE IF NOT EXISTS `user_open_id` (
  `open_id` varchar(32) NOT NULL COMMENT '用户openid',
  `uid` int(10) unsigned NOT NULL COMMENT '用户id',
  `app_id` int(10) unsigned NOT NULL COMMENT '应用id',
  `create_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '更新时间',
  `version` int(10) unsigned NOT NULL DEFAULT 1 COMMENT '数据版本号',
  PRIMARY KEY (`open_id`) USING BTREE,
  UNIQUE KEY `uid_appid` (`uid`,`app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户的openid对应关系';

-- 正在导出表  phoenix_user.user_open_id 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `user_open_id` DISABLE KEYS */;
INSERT IGNORE INTO `user_open_id` (`open_id`, `uid`, `app_id`, `create_time`, `update_time`, `version`) VALUES
	('8759cc97c9f69dbf139963d8e22a2d54', 1, 1, '2020-08-08 17:31:24', '2020-08-08 17:31:24', 1);
/*!40000 ALTER TABLE `user_open_id` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
