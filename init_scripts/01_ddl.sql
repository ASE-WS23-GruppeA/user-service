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

INSERT INTO users (id, username, password, email)
VALUES (100, 'mock', '{bcrypt}$2a$10$GoJ3RFhYbW03u1i3MJHi0uuLY/CNi7LBeru5jMCc7vd6jrpAKf93C', 'mock@mock.com');
