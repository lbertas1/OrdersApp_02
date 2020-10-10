package org.example.service;

import org.example.persistence.model.Client;
import org.example.persistence.model.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReadFromTxt {

    private final Map<Client, Map<Product, Integer>> customersWithProducts;

    public ReadFromTxt(String fileName, String fileName2, String fileName3){
        customersWithProducts = new LinkedHashMap<>();
        getDataFromFile(fileName);
        getDataFromFile(fileName2);
        getDataFromFile(fileName3);
    }

    public Map<Client, Map<Product, Integer>> getCustomersWithProducts() {
        return customersWithProducts;
    }

    private void getDataFromFile(String fileName) {
        List<String> rowsFromFile = null;
        try {
            rowsFromFile = Files.readAllLines(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert rowsFromFile != null;
        rowsFromFile.forEach(s -> {
            Client client = getClient(s);
            Product product1 = getFirstProduct(s);
            Product product2 = getSecondProduct(s);
            addClient(client, product1, product2);
        });
    }

    private Client getClient(String s) {
        String[] division = s.split(" ");
        String[] clientArray = division[0].split(";");
        return new Client(clientArray[0], clientArray[1], Integer.parseInt(clientArray[2]), Double.parseDouble(clientArray[3]));
    }

    private Product getFirstProduct(String row) {
        String[] division = row.split(" ");
        String[] firstProduct = division[1].split(";");
        firstProduct[0] = firstProduct[0].replace("[", "");
        return new Product(firstProduct[0], firstProduct[1], Double.parseDouble(firstProduct[2]));
    }

    private Product getSecondProduct(String row) {
        String[] division = row.split(" ");
        String[] secondProduct = division[2].split(";");
        secondProduct[2] = secondProduct[2].replace("]", "");
        return new Product(secondProduct[0], secondProduct[1], Double.parseDouble(secondProduct[2]));
    }

    private void addClient(Client client, Product product1, Product product2) {
        if (!customersWithProducts.containsKey(client)) {
            Map<Product, Integer> productMap = new LinkedHashMap<>();
            productMap.putIfAbsent(product1, 1);
            productMap.putIfAbsent(product2, 1);

            customersWithProducts.put(client, productMap);
        } else {
            Map<Product, Integer> productIntegerMap = customersWithProducts.get(client);
            addProduct(productIntegerMap, product1);
            addProduct(productIntegerMap, product2);
        }
    }

    private void addProduct(Map<Product, Integer> productIntegerMap, Product product) {
        if (!productIntegerMap.containsKey(product)) productIntegerMap.put(product, 1);
        else {
            int quantity = productIntegerMap.get(product);
            quantity = quantity + 1;
            productIntegerMap.put(product, quantity);
        }
    }
}
