-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versione server:              10.3.15-MariaDB - mariadb.org binary distribution
-- S.O. server:                  Win64
-- HeidiSQL Versione:            10.1.0.5464
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dump della struttura del database real_estate
CREATE DATABASE IF NOT EXISTS `real_estate` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `real_estate`;

-- Dump della struttura di tabella real_estate.neighborhood
CREATE TABLE IF NOT EXISTS `neighborhood` (
  `Neighborhood` varchar(22) CHARACTER SET utf8 DEFAULT NULL,
  `Postal_Code` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- L’esportazione dei dati non era selezionata.
-- Dump della struttura di tabella real_estate.properties
CREATE TABLE IF NOT EXISTS `properties` (
  `Address` varchar(25) CHARACTER SET utf8 DEFAULT NULL,
  `Zillow_Address` varchar(49) CHARACTER SET utf8 DEFAULT NULL,
  `Sale_Date` varchar(18) CHARACTER SET utf8 DEFAULT NULL,
  `Opening_Bid` int(11) DEFAULT NULL,
  `Sale_Price_bid_price` varchar(9) CHARACTER SET utf8 DEFAULT NULL,
  `Book_Writ` varchar(9) CHARACTER SET utf8 DEFAULT NULL,
  `OPA` int(11) DEFAULT NULL,
  `Postal_Code` int(11) DEFAULT NULL,
  `Attorney` varchar(56) CHARACTER SET utf8 DEFAULT NULL,
  `Ward` int(11) DEFAULT NULL,
  `Seller` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `Buyer` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `Sheriff_Cost` decimal(6,2) DEFAULT NULL,
  `Advertising` decimal(6,2) DEFAULT NULL,
  `Other` int(11) DEFAULT NULL,
  `Record_Deed` int(11) DEFAULT NULL,
  `Water` decimal(7,2) DEFAULT NULL,
  `PGW` decimal(7,2) DEFAULT NULL,
  `Avg_Walk_Transit_score` decimal(4,2) DEFAULT NULL,
  `Violent_Crime_Rate` decimal(3,2) DEFAULT NULL,
  `School_Score` decimal(4,2) DEFAULT NULL,
  `Zillow_Estimate` decimal(8,2) DEFAULT NULL,
  `Rent_Estimate` decimal(6,2) DEFAULT NULL,
  `taxAssessment` decimal(8,2) DEFAULT NULL,
  `yearBuilt` int(11) DEFAULT NULL,
  `finished_SqFt` int(11) DEFAULT NULL,
  `bathrooms` varchar(5) CHARACTER SET utf8 DEFAULT NULL,
  `bedrooms` varchar(5) CHARACTER SET utf8 DEFAULT NULL,
  `PropType` varchar(15) CHARACTER SET utf8 DEFAULT NULL,
  `Average_comps` decimal(8,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- L’esportazione dei dati non era selezionata.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
