-- Apaga o banco de dados completo se ele já existir e o recria do zero
DROP DATABASE IF EXISTS `nullamericas_bank`;
CREATE SCHEMA `nullamericas_bank`;
USE `nullamericas_bank`;

-- ==========================================
-- 1. CRIAÇÃO DA TABELA USERS
-- ==========================================
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `idUsers` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) DEFAULT NULL,
  `dataCadastro` datetime DEFAULT NULL,
  `pais` varchar(255) DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `cidade` varchar(255) DEFAULT NULL,
  `rua` varchar(600) DEFAULT NULL,
  `numero` int(11) DEFAULT NULL,
  `complemento` varchar(255) DEFAULT NULL,
  `cep` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idUsers`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
  CONSTRAINT `fk_user` FOREIGN KEY (`idUser`) REFERENCES `users` (`idUsers`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ==========================================
-- 3. INSERÇÃO DOS DADOS
-- ==========================================

-- Inserção dos dados tabela users
INSERT INTO `users` (`idUsers`, `nome`, `dataCadastro`, `pais`, `estado`, `cidade`, `rua`, `numero`, `complemento`, `cep`) VALUES 
(1, 'Vitor', '2026-06-05 21:02:00', 'Brasil', 'SC', 'Ascurra', 'teste', 1234, '321', '89138-000'),
(2, 'Franz', '2026-06-05 21:07:00', 'Brasil', 'SC', 'Ascurra', 'teste', 5489, '3453', '89138-000');

-- Inserção dos dados tabela login (incluindo a coluna idUser que faltava no cabeçalho do INSERT)
INSERT INTO `login` (`codigo`, `senha`, `tipoUsuario`, `idUser`) VALUES 
('franz', '1234', 'FUNCIONARIO', 2),
('vitor', '1234', 'USUARIO', 1);