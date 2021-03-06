--liquibase formatted sql
--changeset: transaction:create
CREATE TABLE IF NOT EXISTS TRANSACTION(
	ID BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	EMAIL_FROM VARCHAR(256) NOT NULL,
	EMAIL_TO VARCHAR(256) NOT NULL,
	TRANSFER_TYPE VARCHAR(256) NOT NULL,
	AMOUNT DOUBLE DEFAULT 0,
	TRANSACTION_DATETIME DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
);