-- Apaga o banco de dados completo se ele já existir e o recria do zero
DROP DATABASE IF EXISTS `nullamericas_bank`;
CREATE SCHEMA `nullamericas_bank`;
USE `nullamericas_bank`;

-- ==========================================
-- 1. CRIAÇÃO DA TABELA USERS
-- ==========================================
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) DEFAULT NULL,
  `dataCadastro` datetime DEFAULT current_timestamp(),
  `pais` varchar(255) DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `cidade` varchar(255) DEFAULT NULL,
  `rua` varchar(600) DEFAULT NULL,
  `numero` int(11) DEFAULT NULL,
  `complemento` varchar(255) DEFAULT NULL,
  `cep` varchar(255) DEFAULT NULL,
  `documento` varchar(14) DEFAULT NULL,
  `tipoDocumento` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ==========================================
-- 2. CRIAÇÃO DA TABELA LOGIN
-- ==========================================
DROP TABLE IF EXISTS `login`;
CREATE TABLE `login` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(128) DEFAULT NULL,
  `senha` varchar(128) DEFAULT NULL,
  `tipoUsuario` varchar(11) NOT NULL,
  `idUser` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user` (`idUser`),
  CONSTRAINT `fk_user` FOREIGN KEY (`idUser`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ==========================================
-- 3. CRIAÇÃO DA TABELA CONTA
-- ==========================================
DROP TABLE IF EXISTS `conta`;
CREATE TABLE `conta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idUser` int(11) DEFAULT NULL,
  `dataCadastro` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `idx_idUser` (`idUser`),
  CONSTRAINT `fk_conta_user` FOREIGN KEY (`idUser`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ==========================================
-- 4. CRIAÇÃO DA TABELA TRANSACAO
-- ==========================================
DROP TABLE IF EXISTS `transacao`;
CREATE TABLE `transacao` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idConta` int(11) DEFAULT NULL,
  `idContaCorrespondente` int(11) DEFAULT NULL,
  `dataTransacao` datetime DEFAULT current_timestamp(),
  `valor` double DEFAULT NULL,
  `tipoTransacao` varchar(22) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indx_idConta` (`idConta`),
  KEY `indx_idContaCorrespondente` (`idContaCorrespondente`),
  CONSTRAINT `fk_idConta` FOREIGN KEY (`idConta`) REFERENCES `conta` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_idContaCorrespondente` FOREIGN KEY (`idContaCorrespondente`) REFERENCES `conta` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- ==========================================
-- 5. INSERÇÃO DOS DADOS (FICTÍCIOS)
-- ==========================================

-- Inserção na tabela users
INSERT INTO `users` (`id`, `nome`, `pais`, `estado`, `cidade`, `rua`, `numero`, `complemento`, `cep`, `documento`, `tipoDocumento`) VALUES 
(1, 'Vitor', 'Brasil', 'SC', 'Ascurra', 'teste', 1234, '321', '89138-000', '63447156007', 'CPF'),
(2, 'Franz', 'Brasil', 'SC', 'Ascurra', 'teste', 5489, '3453', '89138-000', '23914430000114', 'CNPJ');

-- Inserção na tabela login
INSERT INTO `login` (`codigo`, `senha`, `tipoUsuario`, `idUser`) VALUES 
('franz', '1234', 'FUNCIONARIO', 2),
('vitor', '1234', 'USUARIO', 1);

-- Inserção na tabela conta
INSERT INTO `conta` (`id`, `idUser`, `dataCadastro`) VALUES 
(1, 1, '2026-06-05 21:05:00'),
(2, 2, '2026-06-05 21:10:00');

-- Inserção na tabela transacao
INSERT INTO `transacao` (`idConta`, `idContaCorrespondente`, `valor`, `tipoTransacao`) VALUES 
(1, 2, 150.00, 'DEPOSITO'),
(2, 1, 50.50, 'SAQUE');