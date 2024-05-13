CREATE TABLE author
(
    id      INT     NOT NULL AUTO_INCREMENT,
    deleted boolean NOT NULL DEFAULT false,

    CONSTRAINT pk_author PRIMARY KEY (id)
);
