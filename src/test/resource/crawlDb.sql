/*
Navicat MySQL Data Transfer

Source Server         : crawDb
Source Server Version : 50540
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50540
File Encoding         : 65001

Date: 2015-11-24 15:31:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for crawllist小说采集入口表
-- ----------------------------
DROP TABLE IF EXISTS `crawllist`;
CREATE TABLE `crawllist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `url` varchar(100) NOT NULL COMMENT '采集URL',
  `state` enum('1','0') NOT NULL COMMENT '采集状态',
  `info` varchar(100) DEFAULT NULL COMMENT '采集描述',
  `frequency` int(11) DEFAULT '60' COMMENT '采集频率',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='小说采集入口表';

-- ----------------------------
-- Table structure for novelchapter小说章节信息表
-- ----------------------------
DROP TABLE IF EXISTS `novelchapter`;
CREATE TABLE `novelchapter` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `url` varchar(100) NOT NULL COMMENT '阅读页URL',
  `title` varchar(50) DEFAULT NULL COMMENT '章节标题',
  `wordcount` int(11) DEFAULT NULL COMMENT '字数统计',
  `chapterid` int(11) DEFAULT NULL COMMENT '章节排序',
  `chaptertime` bigint(20) DEFAULT NULL COMMENT '章节时间',
  `createtime` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `state` enum('1','0') NOT NULL COMMENT '采集状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='小说章节信息表';

-- ----------------------------
-- Table structure for novelchapterdetail小说章节详细信息表
-- ----------------------------
DROP TABLE IF EXISTS `novelchapterdetail`;
CREATE TABLE `novelchapterdetail` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `url` varchar(100) NOT NULL COMMENT '阅读页URL',
  `title` varchar(50) DEFAULT NULL COMMENT '章节标题',
  `wordcount` int(11) DEFAULT NULL COMMENT '字数统计',
  `chapterid` int(11) DEFAULT NULL COMMENT '章节排序',
  `content` text COMMENT '正文',
  `chaptertime` bigint(20) DEFAULT NULL COMMENT '章节时间',
  `createtime` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updatetime` bigint(20) DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='小说章节详细信息表';

-- ----------------------------
-- Table structure for novelinfo小说简介信息表
-- ----------------------------
DROP TABLE IF EXISTS `novelinfo`;
CREATE TABLE `novelinfo` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `url` varchar(100) NOT NULL COMMENT '简介页URL',
  `NAME` varchar(50) DEFAULT NULL COMMENT '小说名',
  `author` varchar(30) DEFAULT NULL COMMENT '作者名',
  `description` text COMMENT '小说简介',
  `type` varchar(20) DEFAULT NULL COMMENT '分类',
  `lastchapter` varchar(100) DEFAULT NULL COMMENT '最新章节名',
  `chaptercount` int(11) DEFAULT NULL COMMENT '章节数',
  `chapterlisturl` varchar(100) DEFAULT NULL COMMENT '章节列表页URL',
  `wordcount` int(11) DEFAULT NULL COMMENT '字数统计',
  `keywords` varchar(100) DEFAULT NULL COMMENT '关键字',
  `createtime` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updatetime` bigint(20) DEFAULT NULL COMMENT '最后更新时间',
  `state` enum('1','0') DEFAULT NULL COMMENT '采集状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='小说简介信息表';
