-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Mar 27, 2021 at 09:58 PM
-- Server version: 10.4.14-MariaDB-cll-lve
-- PHP Version: 7.2.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `u970414201_agadayurveda`
--

-- --------------------------------------------------------

--
-- Table structure for table `dailytip`
--

CREATE TABLE `dailytip` (
  `id` int(11) NOT NULL,
  `title` varchar(5000) NOT NULL,
  `description` varchar(10000) NOT NULL,
  `date` date NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `details`
--

CREATE TABLE `details` (
  `id` int(11) NOT NULL,
  `title` varchar(5000) NOT NULL,
  `description` varchar(10000) NOT NULL,
  `category` varchar(10000) DEFAULT NULL,
  `subcategory` varchar(1000) DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `details`
--

INSERT INTO `details` (`id`, `title`, `description`, `category`, `subcategory`, `timestamp`) VALUES
(1, 'Skin issue', 'Skin issue will be resolved by alovera', 'herb', 'plant', '2021-03-26 20:12:13'),
(2, 'Fever', 'Corona may cause high fever along with cough', 'disease', 'corona', '2021-03-27 08:29:00'),
(4, 'disease', 'corona', 'ss', 'sas', '2021-03-27 20:34:43');

-- --------------------------------------------------------

--
-- Table structure for table `subcategory`
--

CREATE TABLE `subcategory` (
  `id` int(255) NOT NULL,
  `subcategory` varchar(1000) NOT NULL,
  `category` varchar(1000) NOT NULL DEFAULT 'disease',
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `subcategory`
--

INSERT INTO `subcategory` (`id`, `subcategory`, `category`, `timestamp`) VALUES
(1, 'plant', 'herb', '2021-03-26 21:07:36'),
(2, 'corona', 'disease', '2021-03-27 08:09:37'),
(3, 'Cough', 'disease', '2021-03-27 20:57:00');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `dailytip`
--
ALTER TABLE `dailytip`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `details`
--
ALTER TABLE `details`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `subcategory`
--
ALTER TABLE `subcategory`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `dailytip`
--
ALTER TABLE `dailytip`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `details`
--
ALTER TABLE `details`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `subcategory`
--
ALTER TABLE `subcategory`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
