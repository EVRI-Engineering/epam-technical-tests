CREATE TABLE IF NOT EXISTS couriers
(
    ID      INTEGER PRIMARY KEY,
    FST_NME VARCHAR(64) NOT NULL,
    LST_NME VARCHAR(64) NOT NULL,
    ACTV    BOOLEAN     NOT NULL
);

INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV)
VALUES (1, 'Ben', 'Askew', 1),
       (2, 'Alice', 'Johnson', 1),
       (3, 'John', 'Doe', 1),
       (4, 'Jane', 'Smith', 0),
       (5, 'Michael', 'Brown', 1),
       (6, 'Emily', 'Davis', 1),
       (7, 'Chris', 'Wilson', 0),
       (8, 'Jessica', 'Miller', 1),
       (9, 'David', 'Anderson', 1),
       (10, 'Sophia', 'Thomas', 0),
       (11, 'Daniel', 'Martinez', 1),
       (12, 'Olivia', 'Harris', 1),
       (13, 'Matthew', 'Clark', 0),
       (14, 'Emma', 'Lewis', 1),
       (15, 'Andrew', 'Walker', 1),
       (16, 'Isabella', 'Hall', 0),
       (17, 'James', 'Allen', 1),
       (18, 'Charlotte', 'Young', 1),
       (19, 'Benjamin', 'King', 0),
       (20, 'Lucas', 'Wright', 1),
       (21, 'Mia', 'Scott', 1);