CREATE TABLE entity
(
    id              INT        NOT NULL AUTO_INCREMENT,
    deleted_tinyint tinyint(1) NOT NULL DEFAULT false,
    deleted_bit     bit(1)     NOT NULL DEFAULT 0,

    CONSTRAINT pk_entity PRIMARY KEY (id)
);
