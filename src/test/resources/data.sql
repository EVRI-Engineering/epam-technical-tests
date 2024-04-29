CREATE TABLE
IF NOT EXISTS couriers
( ID INTEGER PRIMARY KEY, FST_NME VARCHAR(64) NOT NULL, LST_NME VARCHAR(64) NOT NULL, ACTV BOOLEAN NOT NULL );

INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV)
VALUES (3, 'test first name 3', 'test last name 3', 1);

INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV)
VALUES (4, 'test first name 4', 'test last name 4', 0);

INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV)
VALUES (5, 'test first name 5', 'test last name 5', 1);