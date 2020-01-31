DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `user_id` varchar(36) NOT NULL,
  `first_name` varchar(64) NOT NULL,
  `last_name` varchar(64) NOT NULL,
  `email_address` varchar(64) NOT NULL,
  `password` varchar(64) NOT NULL,
  `account_created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `account_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `bill`;
CREATE TABLE `bill` (
  `bill_id` varchar(36) NOT NULL,
  `created_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `owner_id` varchar(36) NOT NULL,
  `vendor` varchar(64) NOT NULL,
  `bill_date` date NOT NULL,
  `due_date` date NOT NULL,
  `amount_due` double(6,2) NOT NULL,
  `categories` varchar(256) NOT NULL,
  `payment_status` ENUM('paid','due','past_due','no_payment_required') NOT NULL,
  PRIMARY KEY (`bill_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

INSERT INTO bill
VALUES (
  "02105ec4-97c1-4b16-8e1a-b181a0bfe4c4",
  "2020-01-29 20:07:33",
  "2020-01-29 20:07:33",
  "e8ffcae7-d7fb-403a-be39-41d99aa9b4fc",
  "Northeastern University",
  "2021-05-05",
  "2021-04-11",
  "7000.51",
  "[college, tuition, spring2020]",
  "paid"
);

