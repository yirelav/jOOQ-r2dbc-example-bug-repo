package org.jooq.example.r2dbc.test;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.jooq.example.r2dbc.db.Tables.ENTITY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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

        ctx.deleteFrom(ENTITY).execute();
        ctx.insertInto(ENTITY)
                .defaultValues()
                .execute();
    }

    @Test
    public void test_tinyint() {
        var fetched = ctx
                .selectFrom(ENTITY)
                .fetchOne();

        log.info("fetched: \n{}", fetched);

        assertFalse(fetched.get(ENTITY.DELETED_TINYINT) != 0);
    }

    @Test
    public void test_bit() {
        var fetched = ctx
                .selectFrom(ENTITY)
                .fetchOne();

        log.info("fetched: \n{}", fetched);

        assertEquals(false, fetched.get(ENTITY.DELETED_BIT));
    }
}
