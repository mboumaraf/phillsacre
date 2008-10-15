-- phpMyAdmin SQL Dump
-- version 2.11.6
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 15, 2008 at 04:40 PM
-- Server version: 5.0.51
-- PHP Version: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `mail`
--

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
CREATE TABLE IF NOT EXISTS `accounts` (
  `id` int(11) NOT NULL auto_increment,
  `user_id` int(11) NOT NULL,
  `name` varchar(50) collate utf8_unicode_ci NOT NULL,
  `host_name` varchar(100) collate utf8_unicode_ci NOT NULL,
  `port` int(11) NOT NULL default '110',
  `username` varchar(50) collate utf8_unicode_ci NOT NULL,
  `password` varchar(50) collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- Table structure for table `attachments`
--

DROP TABLE IF EXISTS `attachments`;
CREATE TABLE IF NOT EXISTS `attachments` (
  `id` int(11) NOT NULL auto_increment,
  `message_id` int(11) NOT NULL,
  `name` varchar(100) collate utf8_unicode_ci NOT NULL,
  `mime_type` varchar(30) collate utf8_unicode_ci default NULL,
  `size` int(11) NOT NULL,
  `data` mediumblob NOT NULL,
  `type` enum('attachment','inline') collate utf8_unicode_ci NOT NULL default 'attachment',
  `contentid` varchar(100) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`id`),
  KEY `message_id` (`message_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=99 ;

-- --------------------------------------------------------

--
-- Table structure for table `folders`
--

DROP TABLE IF EXISTS `folders`;
CREATE TABLE IF NOT EXISTS `folders` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(50) collate utf8_unicode_ci NOT NULL,
  `user_id` int(11) NOT NULL,
  `parent_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `unique_folder_name` (`id`,`name`,`parent_id`),
  KEY `user_id` (`user_id`),
  KEY `parent_id` (`parent_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=22 ;

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
CREATE TABLE IF NOT EXISTS `messages` (
  `id` int(11) NOT NULL auto_increment,
  `account_id` int(11) NOT NULL,
  `server_index` int(11) default NULL,
  `folder_id` int(11) NOT NULL,
  `recipient` varchar(100) collate utf8_unicode_ci NOT NULL,
  `sender` varchar(100) collate utf8_unicode_ci default NULL,
  `received_date` datetime NOT NULL,
  `subject` varchar(200) collate utf8_unicode_ci NOT NULL,
  `text` text collate utf8_unicode_ci,
  `type` varchar(4) collate utf8_unicode_ci NOT NULL default 'text',
  `read` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `account_id` (`account_id`),
  KEY `folder_id` (`folder_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=77 ;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL auto_increment,
  `username` varchar(50) collate utf8_unicode_ci NOT NULL,
  `password` varchar(250) collate utf8_unicode_ci NOT NULL,
  `first_name` varchar(100) collate utf8_unicode_ci NOT NULL,
  `last_name` varchar(100) collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=2 ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `accounts`
--
ALTER TABLE `accounts`
  ADD CONSTRAINT `accounts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `attachments`
--
ALTER TABLE `attachments`
  ADD CONSTRAINT `attachments_ibfk_1` FOREIGN KEY (`message_id`) REFERENCES `messages` (`id`);

--
-- Constraints for table `folders`
--
ALTER TABLE `folders`
  ADD CONSTRAINT `folders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `folders_ibfk_2` FOREIGN KEY (`parent_id`) REFERENCES `folders` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
  ADD CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`folder_id`) REFERENCES `folders` (`id`);
