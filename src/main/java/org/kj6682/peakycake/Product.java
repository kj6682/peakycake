package org.kj6682.peakycake;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.kj6682.commons.LocalDateDeserializer;
import org.kj6682.commons.LocalDateSerializer;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * Created by luigi on 13/07/2017.
 *
 * THE model (use this in a catalog)
 *
 */

@Data
@Entity
class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String cake;

    private String label;


    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate created;


    private String status;

    protected Product(){}

    public Product(String cake,
                   String label,
                   LocalDate since,
                   String status) {

        Assert.notNull(cake, "an order needs a product");
        Assert.notNull(label, "an order needs a label");
        Assert.notNull(since, "an order needs an origin date");
        Assert.notNull(status, "an order needs an state");

        this.cake = cake;
        this.label = label;
        this.created = since;
        this.status = status;
    }

}// :)