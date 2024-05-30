utentiutentiutenti-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versione server:              10.4.32-MariaDB - mariadb.org binary distribution
-- S.O. server:                  Win64
-- HeidiSQL Versione:            12.2.0.6576
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dump della struttura del database abbonati1
CREATE DATABASE IF NOT EXISTS `abbonati1` /*!40100 DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci */;
USE `abbonati1`;

-- Dump della struttura di tabella abbonati1.acquisti
CREATE TABLE IF NOT EXISTS `acquisti` (
  `idAcquisto` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) DEFAULT NULL,
  `idProdotto` varchar(20) DEFAULT NULL,
  `data` date DEFAULT NULL,
  `costo` int(11) DEFAULT NULL,
  PRIMARY KEY (`idAcquisto`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Dump dei dati della tabella abbonati1.acquisti: ~3 rows (circa)
INSERT INTO `acquisti` (`idAcquisto`, `username`, `idProdotto`, `data`, `costo`) VALUES
	(1, 'marco', 'S09', '2024-03-28', 150),
	(2, 'luca', 'I12', '2024-03-25', 200),
	(3, 'marco', 'HPs15', '2024-02-12', 800);

-- Dump della struttura di tabella abbonati1.categoria
CREATE TABLE IF NOT EXISTS `categoria` (
  `idcategoria` varchar(50) NOT NULL,
  `descrizione` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idcategoria`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Dump dei dati della tabella abbonati1.categoria: ~2 rows (circa)
INSERT INTO `categoria` (`idcategoria`, `descrizione`) VALUES
	('PORT', 'Portatili'),
	('TEL', 'Telefonia');

-- Dump della struttura di tabella abbonati1.prodotto
CREATE TABLE IF NOT EXISTS `prodotto` (
  `id` varchar(20) NOT NULL,
  `descrizione` varchar(50) DEFAULT NULL,
  `prezzo` int(11) DEFAULT NULL,
  `categoria` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Dump dei dati della tabella abbonati1.prodotto: ~10 rows (circa)
INSERT INTO `prodotto` (`id`, `descrizione`, `prezzo`, `categoria`) VALUES
	('123', 'qweqe', 44, 'PORT'),
	('1234', '1234', 1234, 'PORT'),
	('12345', 'dsadsa', 3, 'PORT'),
	('dcvf', 'ererere', 5, 'PORT'),
	('dsdsad', 'dsasdsa', 44, 'PORT'),
	('eee', 'eee', 22, 'PORT'),
	('HPS15', 'HP Pavillon Ryzen 7', 800, 'PORT'),
	('I12', 'IPhone X', 400, 'TEL'),
	('NEW', 'Nuovo articolo', 300, 'PORT'),
	('qwqw', 'sadsa', 44, 'PORT'),
	('S09', 'Samsung Galaxy S9', 200, 'TEL');

-- Dump della struttura di tabella abbonati1.utenti
CREATE TABLE IF NOT EXISTS `utenti` (
  `username` varchar(30) NOT NULL,
  `password` varchar(256) NOT NULL,
  `cognome` varchar(30) DEFAULT NULL,
  `nome` varchar(30) DEFAULT NULL,
  `citta` varchar(30) DEFAULT NULL,
  `dataNascita` date DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `ruolo` varchar(50) DEFAULT 'USER',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Dump dei dati della tabella abbonati1.utenti: ~9 rows (circa)
INSERT INTO `utenti` (`username`, `password`, `cognome`, `nome`, `citta`, `dataNascita`, `email`, `ruolo`) VALUES
	('admin', '{bcrypt}$2y$10$Qs4vrW1.Bsjvp58utRsiEOuOCvqrbJau/rr1GJD2L8IAjNyMfddj6', 'Amministratore', 'Sito', 'Lecco', '1970-04-10', 'admin@gmail.com', 'ADMIN'),
	('federico', '{bcrypt}$2y$10$Qs4vrW1.Bsjvp58utRsiEOuOCvqrbJau/rr1GJD2L8IAjNyMfddj6', 'Neri', 'Federico', 'Milano', '2009-04-17', 'email@it', 'USER'),
	('luca', '{bcrypt}$2y$10$Qs4vrW1.Bsjvp58utRsiEOuOCvqrbJau/rr1GJD2L8IAjNyMfddj6', 'Rossi', 'Luca', 'Milano', '1995-12-05', 'email@it', 'USER'),
	('luca2', '{bcrypt}$2a$10$6GBth9mFVAjhiG79rXEaQeF0tHpd2zM4uIZnXzJss1GeMgBZUl2WG', NULL, NULL, NULL, NULL, 'asasa', NULL),
	('luca4', '{bcrypt}$2a$10$1tr3WCYymahAVvsIZDgrTeFG4au1INjC0SG5fczli/IWwI1I/3Erm', NULL, NULL, NULL, NULL, 'eeee', NULL),
	('luca5', '{bcrypt}$2a$10$ydmic1iDW30S8i1O20QMY.KT1uyq5AmmS9Cnh8NzD1WF5xuM00w5C', NULL, NULL, NULL, NULL, 'asas', NULL),
	('luca8', '{bcrypt}$2a$10$lgGEBaUtXG6XEA9Tyw4yc.DtW.JtoDr/ibheEBbcwW9X9JdZM7L56', NULL, NULL, NULL, NULL, 'aa', NULL),
	('marco', '{bcrypt}$2y$10$Qs4vrW1.Bsjvp58utRsiEOuOCvqrbJau/rr1GJD2L8IAjNyMfddj6', 'D\'Amato', 'marco', 'Lecco', '2010-04-03', 'email@it', 'USER'),
	('paolo', '{bcrypt}$2y$10$Qs4vrW1.Bsjvp58utRsiEOuOCvqrbJau/rr1GJD2L8IAjNyMfddj6', 'Bianchi', 'Paolo', 'Lecco', '2019-04-03', 'email@it', 'USER');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
