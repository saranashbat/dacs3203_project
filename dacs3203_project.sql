-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 18, 2025 at 01:31 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dacs3203_project`
--

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `username` varchar(200) NOT NULL,
  `hashedpass` varchar(64) NOT NULL,
  `salt` varchar(200) NOT NULL,
  `role` varchar(100) NOT NULL,
  `position` varchar(200) NOT NULL,
  `salary` double NOT NULL,
  `project` varchar(200) NOT NULL,
  `vacationdays` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`username`, `hashedpass`, `salt`, `role`, `position`, `salary`, `project`, `vacationdays`) VALUES
('employee#1', '443955AEC5BE62B25939694A64BE629990AE41B4A0952C5F20072445F3F70B62', '/8kmujUPrPmx+NnSVW6VSjfI2YI=', 'employee', 'Assistant', 25000, '', 10),
('employee#2', '78841368805E128BD8C98C123B3DE8F430374274714FD437878F715663D74DF2', 'WV8SWSIptEf+2sLryh6mUY7waKs=', 'employee', 'Director', 32000, '', 10),
('hrmanager', '75718E95F2C3B64706143ECB679F5FD1A1C3DFD8AA4EB5F5CEE619F63249888F', 'L5ljskQNF+KmDSXErTjlzRx2+cs=', 'hrmanager', '', 0, '', 0);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
