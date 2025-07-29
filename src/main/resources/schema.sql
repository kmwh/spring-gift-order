CREATE TABLE product (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  price INT NOT NULL,
  image_url VARCHAR(500) NOT NULL
);

CREATE TABLE option (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  quantity INT NOT NULL,
  product_id BIGINT NOT NULL,
  CONSTRAINT uk_product_option_name UNIQUE (product_id, name)
);

ALTER TABLE option
ADD CONSTRAINT fk_option_product_id
FOREIGN KEY (product_id)
REFERENCES product(id)
ON DELETE CASCADE;

CREATE TABLE member (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  email VARCHAR(255),
  password VARCHAR(255),
  provider VARCHAR(255),
  social_id BIGINT
);

CREATE TABLE wish (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  member_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  amount INT NOT NULL
);

ALTER TABLE wish
ADD CONSTRAINT fk_wish_product_id
FOREIGN KEY (product_id)
REFERENCES product(id)
ON DELETE CASCADE;

ALTER TABLE wish
ADD CONSTRAINT fk_wish_member_id
FOREIGN KEY (member_id)
REFERENCES member(id)
ON DELETE CASCADE;

CREATE TABLE kakao_token (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  access_token VARCHAR(255) NOT NULL,
  refresh_token VARCHAR(255) NOT NULL,
  access_token_expires_at DATETIME NOT NULL,
  refresh_token_expires_at DATETIME NOT NULL,
  member_id BIGINT
);

ALTER TABLE kakao_token
ADD CONSTRAINT fk_token_member_id
FOREIGN KEY (member_id)
REFERENCES member (id);

CREATE TABLE orders (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  option_id BIGINT,
  quantity INT NOT NULL,
  order_date_time TIMESTAMP NOT NULL,
  message VARCHAR(255)
);

ALTER TABLE orders
ADD CONSTRAINT fk_order_option_id
FOREIGN KEY (option_id)
REFERENCES option (id);
