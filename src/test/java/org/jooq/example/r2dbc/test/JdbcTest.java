package org.jooq.example.r2dbc.test;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.Source;
import org.jooq.impl.DSL;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.jooq.example.r2dbc.db.Tables.AUTHOR;
import static org.junit.Assert.assertEquals;

public class JdbcTest {

    private static final Logger log = LoggerFactory.getLogger(JdbcTest.class);

    private DSLContext ctx;

    @Before
    public void setup() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3310/test_schema",
                    "test",
                    "test"
            );

            ctx = DSL.using(
                    connection,
                    SQLDialect.MYSQL
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ctx.deleteFrom(AUTHOR).execute();
        ctx.insertInto(AUTHOR)
                .defaultValues()
                .execute();
    }

    @Test
    public void test() {
        Record1<Byte> fetched = ctx
                .select(AUTHOR.DELETED)
                .from(AUTHOR)
                .fetchOne();

        log.info("fetched: \n{}", fetched);

        assertEquals(false, fetched.value1() != 0);
    }
}
