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

