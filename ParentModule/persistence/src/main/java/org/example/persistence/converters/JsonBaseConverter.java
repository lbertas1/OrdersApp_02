package org.example.persistence.converters;

import org.example.persistence.model.Order;

import java.util.List;

public class JsonBaseConverter extends JsonConverter<List<Order>> {
    public JsonBaseConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
