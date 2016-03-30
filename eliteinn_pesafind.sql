-- phpMyAdmin SQL Dump
-- version 4.0.10.7
-- http://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: Mar 30, 2016 at 02:30 PM
-- Server version: 5.5.45-cll-lve
-- PHP Version: 5.4.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `eliteinn_pesafind`
--

-- --------------------------------------------------------

--
-- Table structure for table `atms`
--

CREATE TABLE IF NOT EXISTS `atms` (
  `atmid` int(11) NOT NULL AUTO_INCREMENT,
  `atmtype` varchar(50) NOT NULL,
  `building` varchar(50) NOT NULL,
  `bankid` int(11) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  PRIMARY KEY (`atmid`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=20 ;

--
-- Dumping data for table `atms`
--

INSERT INTO `atms` (`atmid`, `atmtype`, `building`, `bankid`, `latitude`, `longitude`) VALUES
(1, 'Visa', 'Ground Floor, Tyme Arcade', 1, -1.39367, 36.6829512),
(2, 'Visa', 'Mbagathi Rd', 1, -1.3929793, 36.6829508),
(3, 'Visa', ' Gataka Rd, Ongata Rongai', 2, -1.392634, 36.6829506),
(4, 'Visa', 'Prestige Plaza, Ngong Rd, Nairobi', 2, -1.3919434, 36.6829502),
(14, 'Visa', 'Taj Mal Towers', 4, -1.3455367, 36.7258366),
(13, 'Master Card', 'Mombasa Road', 3, -1.2904137, 36.7808914),
(8, 'Visa', 'Kenyatta Hospital Estate', 1, -1.3070154, 36.8088763),
(9, 'Kenswitch', 'Mbagathi Road', 1, -1.3070154, 36.8088763),
(10, 'Master Card', 'Golf Course, Nairobi', 1, -1.3070154, 36.8088763),
(11, 'Visa', 'Kenyatta Market', 2, -1.3065863, 36.8089621),
(12, 'Master Card', 'Exchange, Nairobi', 2, -1.3065863, 36.8089621),
(15, 'Master Card', 'Tuskys Business Centre', 4, -1.3455367, 36.7258366),
(16, 'Visa', 'Maziwa, Nairobi', 4, -1.3455367, 36.7258366),
(17, 'Master Card', 'Buruburu Fairlane Building', 4, -1.3033193, 36.7785367),
(18, 'Visa', 'Nairobi West Estate', 5, -1.3679851, 36.6837627),
(19, 'Visa', 'Mombasa Rd, Mlolongo', 5, -1.3195879, 36.7562036);

-- --------------------------------------------------------

--
-- Table structure for table `banks`
--

CREATE TABLE IF NOT EXISTS `banks` (
  `bankid` int(11) NOT NULL AUTO_INCREMENT,
  `bankname` varchar(50) NOT NULL,
  `bankurl` varchar(100) NOT NULL,
  `branches` int(11) NOT NULL,
  `imageurl` varchar(100) NOT NULL,
  `about` varchar(255) NOT NULL,
  PRIMARY KEY (`bankid`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `banks`
--

INSERT INTO `banks` (`bankid`, `bankname`, `bankurl`, `branches`, `imageurl`, `about`) VALUES
(1, 'Barclays Bank Kenya', 'http://eliteinnovations.biz/pesafind/images/barclays.jpg', 120, 'http://eliteinnovations.biz/pesafind/images/barclaysimg.jpg', 'Barclays Bank is a large financial institution in Kenya, with an estimated asset base in excess of US$2.329 billion (KES:200.975 billion), as of March 2014. '),
(2, 'Equity Bank', 'http://eliteinnovations.biz/pesafind/images/equity.jpg', 135, 'http://eliteinnovations.biz/pesafind/images/equityimg.jpg', 'Equity Bank Kenya Limited was incorporated in 2014 as a result of the corporate restructure of Equity Group Holdings Limited. Prior to November 2014, Equity Group Holdings Limited operated both as a licensed bank and a holding company for its subsidiaries'),
(3, 'NIC Bank', 'http://eliteinnovations.biz/pesafind/images/nic.jpg', 24, 'http://eliteinnovations.biz/pesafind/images/nicimg.jpg', 'NIC Bank is a large financial services provider in East Africa. Headquartered in Nairobi, Kenya, the bank owns subsidiary companies in Kenya, Uganda Tanzania'),
(4, 'Standard Chartered', 'http://eliteinnovations.biz/pesafind/images/standard.jpg', 34, 'http://eliteinnovations.biz/pesafind/images/standardimg.jpg', 'Standard Chartered Kenya is a large financial services provider in Kenya. As of December 2013, the bank''s total assets were valued at about US$2.539 billion (KES:220.39 billion), with shareholders'' equity of about US$417.1 million (KES:36.2 billion)'),
(5, 'Kenya Commercial Bank', 'http://eliteinnovations.biz/pesafind/images/kcb.jpg', 177, 'http://eliteinnovations.biz/pesafind/images/kcbimg.jpg', 'KCB Bank Kenya was among the three largest commercial banks in Kenya with assets of more than US$2.65 billion (KES:223 billion). The other two large Kenyan commercial banks are Barclays Bank Kenya and Standard Chartered Bank Kenya.');

-- --------------------------------------------------------

--
-- Table structure for table `mpesa`
--

CREATE TABLE IF NOT EXISTS `mpesa` (
  `mpesaid` int(11) NOT NULL AUTO_INCREMENT,
  `mpesaname` varchar(50) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `building` varchar(100) NOT NULL,
  `contact` varchar(50) NOT NULL,
  PRIMARY KEY (`mpesaid`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `mpesa`
--

INSERT INTO `mpesa` (`mpesaid`, `mpesaname`, `latitude`, `longitude`, `building`, `contact`) VALUES
(1, 'M-Pesa Vester Ltd', -1.3922547, 35.6592652, 'Rongai', '0721 830060'),
(2, 'M-Pesa Family Bank', -1.2064096, 36.9116053, 'Githurai 45 Roundabout', '020 4272100'),
(3, 'M-Pesa Chevaic', -1.3071122, 36.8089167, 'Near Strathmore University', '020 4272100'),
(4, 'M-Pesa Equity Bank Ltd ', -1.3071122, 36.8089167, 'Enterprise Rd', '020 4272100'),
(5, 'M-Pesa Ardhi Sacco Society Ltd', -1.3071122, 36.8089167, 'Opposite NHIF Building', '020 4272100'),
(6, 'M-Pesa Makro Communication', -1.3427372, 36.7292789, 'Mitumba Village', '020 4272100'),
(7, 'M-Pesa Mwanjeko Investment Ltd', -1.2920659, 36.7869273, 'Gachororo, Juja', '020 4272100'),
(8, 'M-Pesa Tai Communication', -1.3070916, 36.7963887, 'Mbagathi Way', '020 4272100'),
(9, ' M-Pesa Rupaca', -1.3070916, 36.7963887, 'Mbagathi Road', '020 2744000'),
(10, 'M-Pesa Ligate communication(Yes)', -1.3070916, 36.7963887, 'Akila Estate 1', '020 4272100');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
