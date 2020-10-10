package org.example.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.persistence.model.Client;
import org.example.persistence.model.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientWithProduct {
    private Client client;
    private Product product;
}
