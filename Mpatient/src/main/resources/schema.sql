DROP TABLE IF EXISTS patient;

CREATE TABLE IF NOT EXISTS patient (
    id                  BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    lastname            VARCHAR(100) NOT NULL,
    firstname           VARCHAR(100) NOT NULL,
    dateofbirth         DATE,
    gender              VARCHAR(1),
    address             VARCHAR(255),
    phone               VARCHAR(50),
    CONSTRAINT uc_patient_unique UNIQUE (lastname, firstname, dateofbirth, gender)
);