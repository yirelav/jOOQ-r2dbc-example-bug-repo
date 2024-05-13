### Description

This example provides a simple demonstration of possible bug in jooq when using R2DBC driver.

### Installation

To install and run this example, simply check it out and follow these steps

1. Run the following command to start the database

```bash
$ docker-compose up -d
```

2. After the database is up and running, run

```bash
$ mvn clean install
```

3. See the output of the test

Currently, the test is failing with the following error:
```
[ERROR]   R2dbcTest.test:47 expected:<false> but was:<null>
```

Expected behavior is that the test should pass.