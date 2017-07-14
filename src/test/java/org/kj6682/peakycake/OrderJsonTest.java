package org.kj6682.peakycake;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by luigi on 12/07/2017.
 * <p>
 * TDD - use this test to define the ORDER model
 */
@RunWith(SpringRunner.class)
@JsonTest
public class OrderJsonTest {

    Order order;
    @Autowired
    private JacksonTester<Order> json;

    @Before
    public void setup() {
        order = new Order("north",
                "peaky cake",
                "peaky cake customizable message",
                LocalDate.of(2017, 7, 13),
                LocalDate.of(2017, 8, 7),
                "NEW");

    }

    @Test
    public void serialise() throws Exception {

        System.out.println(this.json.write(order));
        assertThat(this.json.write(order)).isEqualTo(ResourceUtils.getFile("classpath:order.json"));
    }

    @Test
    public void deserialise() throws Exception {

        File file = ResourceUtils.getFile("classpath:order.json");
        String jsonObject = new String(Files.readAllBytes(file.toPath()));
        Order newOrder = this.json.parse(jsonObject).getObject();
        assertThat(newOrder.equals(order));

    }

}
