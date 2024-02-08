CREATE TABLE IF NOT EXISTS couriers
(
    ID INTEGER PRIMARY KEY,
    FST_NME VARCHAR(64) NOT NULL,
    LST_NME VARCHAR(64) NOT NULL,
    ACTV    BOOLEAN     NOT NULL
);

INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV)
VALUES (1, 'Ben', 'Askew', 1), (2, 'Tom', 'Ford', 0), (3, 'Don', 'Pero', 1), (4, 'Mary', 'Poppins', 0);