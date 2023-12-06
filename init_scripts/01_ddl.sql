CREATE TABLE users (
  id       BIGINT       NOT NULL,
  username VARCHAR(20)  NOT NULL,
  password VARCHAR(120) NOT NULL,
  email    VARCHAR(50)  NOT NULL,
  CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
  ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
  ADD CONSTRAINT uc_users_username UNIQUE (username);