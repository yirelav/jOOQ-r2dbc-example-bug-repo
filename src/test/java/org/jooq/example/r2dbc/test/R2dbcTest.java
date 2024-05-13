package org.jooq.example.r2dbc.test;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.impl.DSL;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;

import static org.jooq.example.r2dbc.db.Tables.AUTHOR;
import static org.junit.Assert.assertEquals;

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

        Flux
                .from(ctx.deleteFrom(AUTHOR))
                .thenMany(ctx.insertInto(AUTHOR).defaultValues())
                .blockFirst();
    }

    @Test
    public void test() {
        Record1<Byte> fetched = Flux
                .from(ctx.select(AUTHOR.DELETED).from(AUTHOR))
                .log()
                .blockFirst();

        assertEquals(false, fetched.value1() != 0);
    }
}
