package org.jooq.example.r2dbc.test;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;

import static org.jooq.example.r2dbc.db.Tables.ENTITY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class R2dbcTest {

    private DSLContext ctx;

    @Before
    public void setup() {
        ConnectionFactory connectionFactory = ConnectionFactories.get(
                ConnectionFactoryOptions
                        .parse("r2dbc:mysql://localhost:3310/test_schema")
                        .mutate()
                        .option(ConnectionFactoryOptions.USER, "test")
                        .option(ConnectionFactoryOptions.PASSWORD, "test")
                        .build()
        );

        ctx = DSL.using(connectionFactory);

        Flux.from(ctx.deleteFrom(ENTITY))
                .thenMany(ctx.insertInto(ENTITY).defaultValues())
                .blockFirst();
    }

    @Test
    public void test_tinyint() {
        var fetched = Flux.from(ctx.selectFrom(ENTITY))
                .log()
                .blockFirst();

        assertFalse(fetched.get(ENTITY.DELETED_TINYINT) != 0);
    }

    @Test
    public void test_bit() {
        var fetched = Flux.from(ctx.selectFrom(ENTITY))
                .log()
                .blockFirst();

        assertEquals(false, fetched.get(ENTITY.DELETED_BIT));
    }
}
