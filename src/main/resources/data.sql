INSERT INTO product (name, price, image_url)
VALUES ('샘플 상품1', 10000, 'sample1.jpg');

INSERT INTO option (name, quantity, product_id)
VALUES ('옵션01', 10, 1);

INSERT INTO option (name, quantity, product_id)
VALUES ('옵션02', 10, 1);

INSERT INTO product (name, price, image_url)
VALUES ('샘플 상품2', 20000, 'sample2.jpg');

INSERT INTO option (name, quantity, product_id)
VALUES ('옵션01', 15, 2);

INSERT INTO member (name, email, password)
VALUES ('test1', 'test1@email.com', '1q2w3e4r5t');

INSERT INTO member (name, email, password)
VALUES ('test2', 'test2@email.com', '1q2w3e4r5t');

INSERT INTO wish (member_id, product_id, amount)
VALUES (1, 1, 10);

INSERT INTO wish (member_id, product_id, amount)
VALUES (1, 2, 15);
