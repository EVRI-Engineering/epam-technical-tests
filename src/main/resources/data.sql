CREATE TABLE IF NOT EXISTS couriers
(
    ID      INTEGER PRIMARY KEY,
    FST_NME VARCHAR(64) NOT NULL,
    LST_NME VARCHAR(64) NOT NULL,
    ACTV    BOOLEAN     NOT NULL
);

MERGE  INTO couriers (ID, FST_NME, LST_NME, ACTV) KEY (ID)
VALUES (1, 'Ben', 'Askew', 1), (2, 'Svitlana', 'Ovcharenko', 1), (3, 'John', 'Doe', 0);