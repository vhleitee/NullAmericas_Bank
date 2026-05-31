-- Apaga o banco de dados completo se ele já existir e o recria do zero
DROP DATABASE IF EXISTS `nullamericas_bank`;
CREATE SCHEMA `nullamericas_bank`;
USE `nullamericas_bank`;

-- Criação da tabela login
DROP TABLE IF EXISTS `login`;
CREATE TABLE `login` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(128) DEFAULT NULL,
  `senha` varchar(128) DEFAULT NULL,
  `tipoUsuario` varchar(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Inserção dos dados
INSERT INTO `login` (`codigo`, `senha`, `tipoUsuario`) VALUES 
('funcionario1','1234','FUNCIONARIO'),
('usuario1','1234','USUARIO');