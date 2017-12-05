-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 05, 2017 at 01:53 PM
-- Server version: 10.1.25-MariaDB
-- PHP Version: 5.6.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sdp`
--

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `product_id` int(11) NOT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `quantity` varchar(255) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `shop_id` int(11) NOT NULL,
  `BuyTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`product_id`, `product_name`, `quantity`, `price`, `shop_id`, `BuyTime`) VALUES
(67, 'Pencil Battery', '26', 150, 32, '2017-11-05 07:50:50'),
(68, 'Ispahani Tea Bag', '45', 150, 32, '2017-11-05 07:51:59'),
(75, 'Rice', '41', 5846, 32, '2017-11-07 23:07:59'),
(76, 'Popcorn', '4', 1000, 32, '2017-11-07 23:09:51'),
(77, 'gh', '80', 80, 32, '2017-11-07 23:10:32'),
(79, 'j', '3', 12, 32, '2017-11-08 09:49:29'),
(80, 'Biscuit', '8', 15, 32, '2017-11-08 12:16:05'),
(81, 'Rice', '41', 10, 32, '2017-11-08 12:21:33'),
(82, 'ruchi', '20', 29, 32, '2017-11-08 12:23:03'),
(83, 'popcorn', '32', 5, 47, '2017-11-25 02:01:55'),
(84, 'Yy', '1', 8, 47, '2017-11-25 02:02:16'),
(85, 'Chokochoko', '58', 5, 32, '2017-11-28 21:50:34'),
(86, 'B', '30', 3, 32, '2017-11-28 21:50:58');

-- --------------------------------------------------------

--
-- Table structure for table `registration`
--

CREATE TABLE `registration` (
  `res_id` int(11) NOT NULL,
  `Email` varchar(255) CHARACTER SET utf8 NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `registration`
--

INSERT INTO `registration` (`res_id`, `Email`, `password`) VALUES
(8, 'Mehadi', '88'),
(120, 'Hasan', '90'),
(121, 'Das', '32'),
(122, 'Meraj', '99');

-- --------------------------------------------------------

--
-- Table structure for table `sell`
--

CREATE TABLE `sell` (
  `sell_id` int(22) NOT NULL,
  `product_name` varchar(50) NOT NULL,
  `sell_quantity` int(11) NOT NULL,
  `total_sell_price` int(30) NOT NULL,
  `sell_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `shop_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sell`
--

INSERT INTO `sell` (`sell_id`, `product_name`, `sell_quantity`, `total_sell_price`, `sell_date`, `shop_id`) VALUES
(33, 'Pencil Battery', 3, 90, '2017-11-24 17:49:11', 32),
(36, 'Pencil Battery', 2, 6, '2017-11-24 19:34:52', 32),
(37, 'Ispahani Tea Bag', 5, 150, '2017-11-24 19:38:48', 32),
(38, 'Popcorn', 4, 4000, '2017-11-24 19:41:30', 32),
(39, 'Pencil Battery', 2, 132, '2017-11-24 19:59:02', 32),
(40, 'Yy', 2, 16, '2017-11-24 20:02:36', 47),
(41, 'Rice', 5, 250, '2017-11-29 05:47:39', 32),
(42, 'Popcorn', 2, 10, '2017-11-29 05:51:26', 32);

-- --------------------------------------------------------

--
-- Table structure for table `shop`
--

CREATE TABLE `shop` (
  `shop_id` int(11) NOT NULL,
  `shop_name` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `res_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `shop`
--

INSERT INTO `shop` (`shop_id`, `shop_name`, `category`, `res_id`) VALUES
(32, 'Medicinecorner', 'Medicine', 8),
(47, 'Das-store', 'medicine', 121),
(48, 'Medicine corner', 'Medicine', 122);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`product_id`),
  ADD KEY `shop_id` (`shop_id`);

--
-- Indexes for table `registration`
--
ALTER TABLE `registration`
  ADD PRIMARY KEY (`res_id`);

--
-- Indexes for table `sell`
--
ALTER TABLE `sell`
  ADD PRIMARY KEY (`sell_id`),
  ADD KEY `shop_id` (`shop_id`);

--
-- Indexes for table `shop`
--
ALTER TABLE `shop`
  ADD PRIMARY KEY (`shop_id`),
  ADD KEY `res_id` (`res_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=87;
--
-- AUTO_INCREMENT for table `registration`
--
ALTER TABLE `registration`
  MODIFY `res_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=123;
--
-- AUTO_INCREMENT for table `sell`
--
ALTER TABLE `sell`
  MODIFY `sell_id` int(22) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;
--
-- AUTO_INCREMENT for table `shop`
--
ALTER TABLE `shop`
  MODIFY `shop_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `product`
--
ALTER TABLE `product`
  ADD CONSTRAINT `product_ibfk_1` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`shop_id`);

--
-- Constraints for table `sell`
--
ALTER TABLE `sell`
  ADD CONSTRAINT `sell_ibfk_1` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`shop_id`);

--
-- Constraints for table `shop`
--
ALTER TABLE `shop`
  ADD CONSTRAINT `shop_ibfk_1` FOREIGN KEY (`res_id`) REFERENCES `registration` (`res_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
