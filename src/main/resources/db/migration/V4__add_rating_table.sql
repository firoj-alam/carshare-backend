-- V4: Rating table

CREATE TABLE `rating` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `booking_id` bigint NOT NULL,
  `rater_id` bigint NOT NULL,
  `rater_role` varchar(20) NOT NULL,
  `ratee_id` bigint NOT NULL,
  `stars` int NOT NULL,
  `comment` varchar(500) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rating_booking_rater_role` (`booking_id`, `rater_role`),
  KEY `idx_rating_ratee_id` (`ratee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;