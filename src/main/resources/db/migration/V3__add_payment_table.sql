-- V3: Payment table for completed bookings

CREATE TABLE `payment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `booking_id` bigint NOT NULL,
  `car_id` bigint NOT NULL,
  `owner_id` bigint NOT NULL,
  `driver_id` bigint NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `commission_rate` decimal(5,2) NOT NULL,
  `commission_amount` decimal(10,2) NOT NULL,
  `owner_earning_amount` decimal(10,2) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_payment_booking_id` (`booking_id`),
  KEY `idx_payment_owner_id` (`owner_id`),
  KEY `idx_payment_driver_id` (`driver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;