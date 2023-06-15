/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : book_db

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2017-11-05 16:05:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `username` varchar(20) NOT NULL default '',
  `password` varchar(32) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('a', 'a');

-- ----------------------------
-- Table structure for `t_book`
-- ----------------------------
DROP TABLE IF EXISTS `t_book`;
CREATE TABLE `t_book` (
  `barcode` varchar(20) NOT NULL COMMENT 'barcode',
  `bookName` varchar(20) NOT NULL COMMENT '图书名称',
  `bookTypeObj` int(11) NOT NULL COMMENT '图书所在类别',
  `price` float NOT NULL COMMENT '图书价格',
  `count` int(11) NOT NULL COMMENT '库存',
  `publishDate` varchar(20) default NULL COMMENT '出版日期',
  `publish` varchar(20) default NULL COMMENT '出版社',
  `bookPhoto` varchar(60) NOT NULL COMMENT '图书图片',
  `introduction` varchar(500) default NULL COMMENT '图书简介',
  PRIMARY KEY  (`barcode`),
  KEY `bookTypeObj` (`bookTypeObj`),
  CONSTRAINT `t_book_ibfk_1` FOREIGN KEY (`bookTypeObj`) REFERENCES `t_booktype` (`bookTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_book
-- ----------------------------
INSERT INTO `t_book` VALUES ('TS001', 'Bootstrap响应式网站开发', '1', '45.5', '14', '2017-11-14', '人民教育出版社', 'upload/9525cf5c-2f92-4fa5-963a-403c1360f344.jpg', '这是一本最新流行的图书，用的最新技术');
INSERT INTO `t_book` VALUES ('TS002', 'html5网站开发', '1', '40', '12', '2017-11-01', '四川大学出版社', 'upload/89753a5a-3e34-440e-8f40-5d8bad96cb0f.jpg', '一本好书，就能成就你');
INSERT INTO `t_book` VALUES ('TS003', '中国近代史', '2', '42.5', '22', '2017-11-02', '人民教育出版社', 'upload/fc88fc60-7a06-45b4-9fa4-61602031e7f2.jpg', '讲解了中国近代的很多历史信息');
INSERT INTO `t_book` VALUES ('TS004', 'SpringMVC开发详解', '1', '58', '12', '2017-11-06', '四川大学出版社', 'upload/d7a3a7ae-48f0-4037-9768-5d1298d6a71d.jpg', '双鱼林设计大神助理飞跃');

-- ----------------------------
-- Table structure for `t_booktype`
-- ----------------------------
DROP TABLE IF EXISTS `t_booktype`;
CREATE TABLE `t_booktype` (
  `bookTypeId` int(11) NOT NULL auto_increment COMMENT '图书类别',
  `bookTypeName` varchar(18) NOT NULL COMMENT '类别名称',
  `days` int(11) NOT NULL COMMENT '可借阅天数',
  PRIMARY KEY  (`bookTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_booktype
-- ----------------------------
INSERT INTO `t_booktype` VALUES ('1', '计算机类', '30');
INSERT INTO `t_booktype` VALUES ('2', '历史类', '25');

-- ----------------------------
-- Table structure for `t_loaninfo`
-- ----------------------------
DROP TABLE IF EXISTS `t_loaninfo`;
CREATE TABLE `t_loaninfo` (
  `loadId` int(11) NOT NULL auto_increment COMMENT '借阅编号',
  `book` varchar(20) NOT NULL COMMENT '图书对象',
  `reader` varchar(20) NOT NULL COMMENT '读者对象',
  `borrowDate` varchar(20) default NULL COMMENT '借阅时间',
  `returnDate` varchar(20) default NULL COMMENT '归还时间',
  PRIMARY KEY  (`loadId`),
  KEY `book` (`book`),
  KEY `reader` (`reader`),
  CONSTRAINT `t_loaninfo_ibfk_2` FOREIGN KEY (`reader`) REFERENCES `t_reader` (`readerNo`),
  CONSTRAINT `t_loaninfo_ibfk_1` FOREIGN KEY (`book`) REFERENCES `t_book` (`barcode`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_loaninfo
-- ----------------------------
INSERT INTO `t_loaninfo` VALUES ('1', 'TS001', 'DZ001', '2017-11-02 06:09:31', '2017-11-15 06:09:57');
INSERT INTO `t_loaninfo` VALUES ('2', 'TS002', 'DZ002', '2017-11-01 15:49:37', '2017-11-08 15:51:13');
INSERT INTO `t_loaninfo` VALUES ('3', 'TS003', 'DZ001', '2017-11-03 15:52:16', '2017-11-16 15:52:18');

-- ----------------------------
-- Table structure for `t_reader`
-- ----------------------------
DROP TABLE IF EXISTS `t_reader`;
CREATE TABLE `t_reader` (
  `readerNo` varchar(20) NOT NULL COMMENT 'readerNo',
  `readerTypeObj` int(11) NOT NULL COMMENT '读者类型',
  `readerName` varchar(20) default NULL COMMENT '姓名',
  `sex` varchar(2) default NULL COMMENT '性别',
  `birthday` varchar(20) default NULL COMMENT '读者生日',
  `telephone` varchar(20) default NULL COMMENT '联系电话',
  `email` varchar(50) default NULL COMMENT '联系Email',
  `qq` varchar(20) default NULL COMMENT '联系qq',
  `address` varchar(80) default NULL COMMENT '读者地址',
  `photo` varchar(60) NOT NULL COMMENT '读者头像',
  PRIMARY KEY  (`readerNo`),
  KEY `readerTypeObj` (`readerTypeObj`),
  CONSTRAINT `t_reader_ibfk_1` FOREIGN KEY (`readerTypeObj`) REFERENCES `t_readertype` (`readerTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_reader
-- ----------------------------
INSERT INTO `t_reader` VALUES ('DZ001', '1', '双鱼林', '男', '2017-11-01', '13539432439', 'dashen@163.com', '359184434', '四川成都', 'upload/f07b2ce3-7fd0-467d-a88b-4a01f750e803.jpg');
INSERT INTO `t_reader` VALUES ('DZ002', '1', '王小花', '女', '2017-11-01', '13349382343', 'xiaohua@163.com', '239583443', '四川达州', 'upload/443ebd2f-c50c-4f24-8594-83beacf11e59.jpg');

-- ----------------------------
-- Table structure for `t_readertype`
-- ----------------------------
DROP TABLE IF EXISTS `t_readertype`;
CREATE TABLE `t_readertype` (
  `readerTypeId` int(11) NOT NULL auto_increment COMMENT '读者类型编号',
  `readerTypeName` varchar(20) NOT NULL COMMENT '读者类型',
  `number` int(11) NOT NULL COMMENT '可借阅数目',
  PRIMARY KEY  (`readerTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_readertype
-- ----------------------------
INSERT INTO `t_readertype` VALUES ('1', '学生类', '3');
INSERT INTO `t_readertype` VALUES ('2', '教师类', '5');
