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

INSERT INTO `users` VALUES 
(6,'Daniel Vorcaro','2026-06-17 20:48:39','Brasil','DF','Brasilia','Lula da Silva',1313,'2222','89168000','11111111111','CPF'),
(7,'Luiz Inacio Lula da Silva','2026-06-17 20:52:26','BR','DF','Brasilia','',0,'','0','22222222222','CPF'),
(8,'Alexandre de Morais','2026-06-17 20:53:45','','','','',0,'','','33333333333','CPF'),
(9,'Everton Pereira da Cruz','2026-06-17 21:03:33','Brasil','SC','Blumenau','',0,'','','44444444444','CPF');

INSERT INTO `login` VALUES 
(7,'vorcaro','123','FUNCIONARIO',6),
(8,'lula','123','USUARIO',7),
(9,'alexandre','123','USUARIO',8),
(10,'everton','123','USUARIO',9);

INSERT INTO `conta` VALUES 
(8,7,'2026-06-17 20:56:37'),
(9,8,'2026-06-17 20:56:44'),
(10,9,'2026-06-17 21:04:00');

INSERT INTO `transacao` VALUES 
(69,8,8,'2026-06-17 20:57:07',150000,'DEPOSITO'),
(70,8,9,'2026-06-17 21:01:00',-131000,'TRANSFERENCIA_ENVIADA'),
(71,9,8,'2026-06-17 21:01:00',131000,'TRANSFERENCIA_RECEBIDA'),
(72,8,10,'2026-06-17 21:04:47',-1313.13,'TRANSFERENCIA_ENVIADA'),
(73,10,8,'2026-06-17 21:04:47',1313.13,'TRANSFERENCIA_RECEBIDA');
