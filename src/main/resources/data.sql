CREATE TABLE couriers
(
    ID      INTEGER PRIMARY KEY,
    FST_NME VARCHAR(64) NOT NULL,
    LST_NME VARCHAR(64) NOT NULL,
    ACTV    BOOLEAN     NOT NULL
);

INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV)
VALUES (
    1, 'Ben', 'Askew', 1
),
(
    2, 'Sam', 'Jones', 0
),
(
    3, 'Dan', 'Garcia', 1
),
(
    4, 'Jane', 'Brown', 0
),
(
    5, 'Alice', 'Smith', 1
);