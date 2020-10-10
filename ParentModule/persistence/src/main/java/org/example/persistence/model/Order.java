package org.example.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    private Client client;
    private List<Product> products;

    @Override
    public String toString() {
        return String.join(
                "CLIENT: " + client,
                "PRODUCT: " + products + "\n");
    }
}
