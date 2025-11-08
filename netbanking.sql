-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 08, 2025 at 08:49 AM
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
-- Database: `netbanking`
--

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `id` int(11) NOT NULL,
  `cif_number` varchar(11) NOT NULL,
  `type` varchar(10) NOT NULL,
  `category` varchar(50) DEFAULT 'Transfer',
  `amount` decimal(10,2) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`id`, `cif_number`, `type`, `category`, `amount`, `description`, `date`) VALUES
(1, '12345678901', 'debit', 'Money Transfer', 100.00, NULL, '2025-11-07 17:54:38'),
(2, '98765432109', 'credit', 'Money Received', 100.00, NULL, '2025-11-07 17:54:38'),
(3, '12345678901', 'debit', 'Money Transfer', 50.00, NULL, '2025-11-07 17:55:01'),
(4, '98765432109', 'credit', 'Money Received', 50.00, NULL, '2025-11-07 17:55:01'),
(5, '12345678901', 'debit', 'Money Transfer', 50.00, NULL, '2025-11-07 17:55:16'),
(6, '98765432109', 'credit', 'Money Received', 50.00, NULL, '2025-11-07 17:55:16'),
(7, '12345678901', 'debit', 'Money Transfer', 89.00, NULL, '2025-11-07 17:55:27'),
(8, '98765432109', 'credit', 'Money Received', 89.00, NULL, '2025-11-07 17:55:27'),
(9, '12345678901', 'debit', 'Money Transfer', 89.00, NULL, '2025-11-07 17:55:42'),
(10, '98765432109', 'credit', 'Money Received', 89.00, NULL, '2025-11-07 17:55:42'),
(11, '12345678901', 'debit', 'Money Transfer', 56.00, NULL, '2025-11-07 17:55:50'),
(12, '98765432109', 'credit', 'Money Received', 56.00, NULL, '2025-11-07 17:55:50'),
(13, '12345678901', 'debit', 'Money Transfer', 20.00, NULL, '2025-11-08 04:03:12'),
(14, '98765432109', 'credit', 'Money Received', 20.00, NULL, '2025-11-08 04:03:12'),
(15, '98765432109', 'debit', 'Money Transfer', 500.00, NULL, '2025-11-08 04:23:04'),
(16, '12345678901', 'credit', 'Money Received', 500.00, NULL, '2025-11-08 04:23:04'),
(17, '12345678901', 'debit', 'Money Transfer', 50.00, NULL, '2025-11-08 06:27:55'),
(18, '98765432109', 'credit', 'Money Received', 50.00, NULL, '2025-11-08 06:27:55'),
(19, '98765432109', 'debit', 'Money Transfer', 60.00, NULL, '2025-11-08 07:27:21'),
(20, '12345678901', 'credit', 'Money Received', 60.00, NULL, '2025-11-08 07:27:21'),
(21, '98765432109', 'debit', 'Money Transfer', 80.00, NULL, '2025-11-08 07:29:34'),
(22, '12345678901', 'credit', 'Money Received', 80.00, NULL, '2025-11-08 07:29:34');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `account_number` char(16) NOT NULL,
  `cif_number` char(11) NOT NULL,
  `branch_code` char(5) NOT NULL,
  `country` varchar(50) NOT NULL,
  `mobile_number` varchar(20) NOT NULL,
  `password` varchar(50) NOT NULL,
  `balance` decimal(10,2) DEFAULT 0.00,
  `transaction_pin` char(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `account_number`, `cif_number`, `branch_code`, `country`, `mobile_number`, `password`, `balance`, `transaction_pin`) VALUES
(1, '1234567890123456', '12345678901', '12345', 'India', '9087654321', '12345', 9836.00, '1234'),
(2, '9876543210987654', '98765432109', '12345', 'India', '9080614554', '12345', 10164.00, '4321'),
(6, '7894561237894561', '78945612378', '78945', 'India', '7894561237', '78945', 0.00, '7894'),
(7, '4561237891234567', '45612378906', '48569', 'India', '9632587410', '12345', 0.00, '1234');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_category` (`category`),
  ADD KEY `idx_cif_category` (`cif_number`,`category`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `account_number` (`account_number`),
  ADD UNIQUE KEY `cif_number` (`cif_number`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
