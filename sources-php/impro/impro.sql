-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Mar 29 Septembre 2015 à 14:25
-- Version du serveur :  5.6.17
-- Version de PHP :  5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `impro`
--

-- --------------------------------------------------------

--
-- Structure de la table `imp_categorie`
--

DROP TABLE IF EXISTS `imp_categorie`;
CREATE TABLE IF NOT EXISTS `imp_categorie` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) NOT NULL,
  `type` varchar(15) NOT NULL,
  `path_folder` varchar(100) NOT NULL,
  `termine` tinyint(1) NOT NULL DEFAULT '0',
  `ordre` int(11) NOT NULL DEFAULT '99',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=13 ;

--
-- Contenu de la table `imp_categorie`
--

INSERT INTO `imp_categorie` (`id`, `nom`, `type`, `path_folder`, `termine`, `ordre`) VALUES
(1, 'Décor', 'PHOTO', 'décor', 0, 1),
(2, 'Paysage', 'PHOTO', 'paysage', 0, 2),
(3, 'Polaroïd', 'POLAROID', 'polaroid', 0, 3),
(4, 'Pop-up', 'PHOTO', 'popup', 0, 4),
(6, 'Truc muche', 'PHOTO', 'Truc muche', 0, 6),
(7, 'Bidule', 'PHOTO', 'Bidule', 0, 7),
(8, 'Choses et autres', 'PHOTO', 'choses', 0, 8),
(9, 'Autre catégorie', 'PHOTO', 'autres', 0, 9),
(10, 'Bizarrerie', 'PHOTO', 'bizarre', 0, 10),
(11, 'Mulen', 'PHOTO', 'mulen', 0, 11),
(12, 'Test', 'POLAROID', 'bla bla', 0, 5);

-- --------------------------------------------------------

--
-- Structure de la table `imp_statut`
--

DROP TABLE IF EXISTS `imp_statut`;
CREATE TABLE IF NOT EXISTS `imp_statut` (
  `champ` varchar(50) NOT NULL,
  `valeur` longtext,
  PRIMARY KEY (`champ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Contenu de la table `imp_statut`
--

INSERT INTO `imp_statut` (`champ`, `valeur`) VALUES
('block_masques', NULL),
('ecran', 'SALLE_ATTENTE'),
('id_categorie', NULL),
('integralite', NULL),
('photos_choisies', NULL),
('photo_courante', NULL),
('statut_diapo', NULL),
('type_ecran', NULL);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


CREATE TABLE IF NOT EXISTS `imp_remerciements` (
  `texte` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `imp_dates` (
  `id` int(11) NOT NULL,
  `date` date NOT NULL,
  `nom` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
