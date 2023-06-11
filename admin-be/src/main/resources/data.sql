/* cascaded Yz+F#?l=#y;B/d`  eh^G+rsB;br}=25 */
INSERT INTO USERS(ID, EMAIL, IMAGE_URL, NAME, PASSWORD, LOGIN_TOKEN, EMAIL_VERIFICATION_TOKEN, EMAIL_VERIFIED, LOGIN_ATTEMPTS, DELETED, DTYPE) VALUES
    ('e3661c31-d1a4-47ab-94b6-1c6500dccf24', 'admin@homeguard.com', NULL, 'Admin', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', '$2a$10$.99MTclb7Fkrh5wFZJVCcuEGJbOi1egHWQNKuvvCUglEr5K5IGaeG', '$2a$10$f2N3JUSrF/WGfZzB6o61TuWdsv7bivCVSRZwGqlBe5rCR4vr2VR0O', true, 0, false, 'Admin');

/* cascaded QAHt3K5y}wSIpy` Rgx>m--in=Yc%UZ */
INSERT INTO USERS(ID, EMAIL, IMAGE_URL, NAME, PASSWORD, LOGIN_TOKEN, EMAIL_VERIFICATION_TOKEN, EMAIL_VERIFIED, LOGIN_ATTEMPTS, DELETED, DTYPE) VALUES
    ('e3661c31-d1a4-47ab-94b6-1c6500dccf25', 'pera@gmail.com', NULL, 'Pera', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', '$2a$10$ohLW4CKCTrZ0OKagfg6gGuaqGVeibHtnrVPrqs3Qp6qUwha/PgHwS', '$2a$10$kwqrklIUNeqDKC9I68QqpeHMhVIK5dD4TJ301d4i/dZC3gScCGpc.', true, 0, false, 'Tenant');

/* cascaded  5=?.:EWTdOl)O<+ 4<)kYW-]Qz8+u|K */
INSERT INTO USERS(ID, EMAIL, IMAGE_URL, NAME, PASSWORD, LOGIN_TOKEN, EMAIL_VERIFICATION_TOKEN, EMAIL_VERIFIED, LOGIN_ATTEMPTS, DELETED, DTYPE) VALUES
    ('e3661c31-d1a4-47ab-94b6-1c6500dccf26', 'mika@gmail.com', NULL, 'Mika', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', '$2a$10$u5scz/HdiNRNVIZz2KNyce/Tm7.q6.SdlaslHZLfWTstd6Kb.xgiq', '$2a$10$aPnVG2/OEnl4VAc.Y7znHOnE8J6SfjfULYE4EkDAGMlMjeLuxgkuW', true, 0, false, 'Landlord');


INSERT INTO CSR(ID, COMMON_NAME, COUNTRY, GIVEN_NAME, ORGANIZATION, ORGANIZATIONAL_UNIT, SURNAME, EMAIL, CREATION_DATE, STATUS) VALUES
    ('e3661c31-d1a4-47ab-94b6-1c6500dccf27', 'admin', 'RS', 'Mika', 'HomeGuard', 'HomeGuard', 'Mikic', 'mika@gmail.com', '2023-03-31 14:30:00',0),
    ('e3661c31-d1a4-47ab-94b6-1c6500dccf28', 'pera', 'RS', 'Pera', 'HomeGuard', 'HomeGuard', 'Peric', 'pera@gmail.com', '2023-03-31 14:30:00',0);


INSERT INTO ROLE (name) VALUES ('ROLE_ADMIN');
INSERT INTO ROLE (name) VALUES ('ROLE_LANDLORD');
INSERT INTO ROLE (name) VALUES ('ROLE_TENANT');

INSERT INTO USER_ROLE (user_id, role_id) VALUES ('e3661c31-d1a4-47ab-94b6-1c6500dccf24', 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES ('e3661c31-d1a4-47ab-94b6-1c6500dccf26', 2);
INSERT INTO USER_ROLE (user_id, role_id) VALUES ('e3661c31-d1a4-47ab-94b6-1c6500dccf25', 3);

INSERT INTO PRIVILEGE (name) VALUES ('CERTIFICATE_MANAGEMENT');

INSERT INTO PRIVILEGE (name) VALUES ('READ_CSR');
INSERT INTO PRIVILEGE (name) VALUES ('WRITE_CSR');

INSERT INTO PRIVILEGE (name) VALUES ('READ_USER');
INSERT INTO PRIVILEGE (name) VALUES ('WRITE_USER');
INSERT INTO PRIVILEGE (name) VALUES ('SEARCH_USER');

INSERT INTO PRIVILEGE (name) VALUES ('READ_REAL_ESTATE');
INSERT INTO PRIVILEGE (name) VALUES ('WRITE_REAL_ESTATE');

INSERT INTO PRIVILEGE (name) VALUES ('READ_PROFILE');

INSERT INTO PRIVILEGE (name) VALUES ('READ_MESSAGES');

INSERT INTO PRIVILEGE (name) VALUES ('READ_REPORTS');


INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (1, 1);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (1, 2);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (1, 3);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (1, 4);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (1, 5);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (1, 6);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (1, 7);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (1, 8);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (1, 9);

/* Landlord */
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (2, 3);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (2, 4);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (2, 7);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (2, 9);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (2, 10);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (2, 11);

/* Tenant */
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (3, 3);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (3, 4);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (3, 7);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (3, 9);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (3, 10);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (3, 11);
