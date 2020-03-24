CREATE DATABASE  IF NOT EXISTS `who_played_best` ;
USE `who_played_best`;

--
-- Table structure for table `genre`
--

DROP TABLE IF EXISTS `genre`;

CREATE TABLE `genre` (
  `genre_id` int NOT NULL AUTO_INCREMENT,
  `genre` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`genre_id`),
  UNIQUE KEY `genre_id_UNIQUE` (`genre_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `performer`
--

DROP TABLE IF EXISTS `performer`;

CREATE TABLE `performer` (
  `performer_id` int NOT NULL AUTO_INCREMENT,
  `performer` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`performer_id`),
  UNIQUE KEY `performer_id_UNIQUE` (`performer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `song`
--

DROP TABLE IF EXISTS `song`;

CREATE TABLE `song` (
  `song_id` int NOT NULL AUTO_INCREMENT,
  `song_title` varchar(50) NOT NULL,
  `year` int NOT NULL,
  PRIMARY KEY (`song_id`),
  UNIQUE KEY `song_id_UNIQUE` (`song_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `song_genre`
--

DROP TABLE IF EXISTS `song_genre`;

CREATE TABLE `song_genre` (
  `song_id` int NOT NULL,
  `genre_id` int DEFAULT NULL,
  KEY `song_fk_idx` (`song_id`),
  KEY `genre_fk_idx` (`genre_id`),
  CONSTRAINT `genre_fk` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`genre_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `song_fk` FOREIGN KEY (`song_id`) REFERENCES `song` (`song_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `song_performer`
--

DROP TABLE IF EXISTS `song_performer`;

CREATE TABLE `song_performer` (
  `song_id` int NOT NULL,
  `performer_id` int NOT NULL,
  PRIMARY KEY (`song_id`,`performer_id`),
  KEY `performer_fk_idx` (`performer_id`),
  CONSTRAINT `perf_fk` FOREIGN KEY (`performer_id`) REFERENCES `performer` (`performer_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `song_perf_fk` FOREIGN KEY (`song_id`) REFERENCES `song` (`song_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `song_rating`
--

DROP TABLE IF EXISTS `song_rating`;

CREATE TABLE `song_rating` (
  `song_id` int NOT NULL,
  `rating` double NOT NULL,
  KEY `song_id_idx` (`song_id`),
  CONSTRAINT `song_rtg_fk` FOREIGN KEY (`song_id`) REFERENCES `song` (`song_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

