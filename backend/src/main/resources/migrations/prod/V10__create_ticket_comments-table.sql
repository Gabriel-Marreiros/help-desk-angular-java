CREATE TABLE `ticket_comments` (
  `id` binary(16) NOT NULL,
  `comment` varchar(3000) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `edited` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `ticket_id` binary(16) NOT NULL,
  `user_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdoce3fj1osdn71h25dhfs160v` (`ticket_id`),
  KEY `FKqstmdduoeqr1bm2lj8r5tmhl2` (`user_id`),
  CONSTRAINT `FKdoce3fj1osdn71h25dhfs160v` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`),
  CONSTRAINT `FKqstmdduoeqr1bm2lj8r5tmhl2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
)