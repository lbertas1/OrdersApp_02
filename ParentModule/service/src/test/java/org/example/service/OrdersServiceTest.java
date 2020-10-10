package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.persistence.model.Client;
import org.example.persistence.model.Product;
import org.example.service.extensions.ExtensionForOrders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(ExtensionForOrders.class)
@RequiredArgsConstructor
class OrdersServiceTest {

    private final OrdersService orderService;

    @Test
    public void getOrders() {
        assertNotNull(orderService.getCustomersWithProducts());
    }

    @Test
    public void designateClientWhoHasPaidTheMost() {
        Client clientWhoPaidTheMost = orderService
                .getCustomersWithProducts()
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors
                                .collectingAndThen(Collectors
                                                .mapping(clientMapEntry -> clientMapEntry
                                                                .getValue()
                                                                .entrySet()
                                                                .stream()
                                                                .mapToDouble(
                                                                        productIntegerEntry ->
                                                                                productIntegerEntry.getKey().getPrice() * productIntegerEntry.getValue())
                                                                .sum(),
                                                        Collectors.toList()),
                                        customerListDouble -> customerListDouble
                                                .stream()
                                                .mapToDouble(aDouble -> aDouble)
                                                .sum())
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow()
                .getKey();

        assertEquals(clientWhoPaidTheMost, orderService.designateClientWhoHasPaidTheMost());
    }

    @Test
    public void createStatementGroupingByAgeAndCategory() {
        Map<Integer, String> statementGroupingByAgeAndCategory = orderService.createStatementGroupingByAgeAndCategory();

        Set<Integer> ages = statementGroupingByAgeAndCategory.keySet();

        ages.forEach(age -> {
            List<Client> clientsWithGivenAge = orderService
                    .getCustomersWithProducts()
                    .keySet()
                    .stream()
                    .filter(client -> client.getAge() == age)
                    .collect(Collectors.toList());

            String category = orderService
                    .getCustomersWithProducts()
                    .entrySet()
                    .stream()
                    .filter(clientMapEntry -> clientsWithGivenAge.contains(clientMapEntry.getKey()))
                    .flatMap(clientMapEntry -> new ArrayList<>(clientMapEntry.getValue().keySet()).stream())
                    .collect(Collectors.groupingBy(
                            Product::getCategory,
                            Collectors.counting()
                    ))
                    .entrySet()
                    .stream()
                    .max(Comparator.comparingLong(Map.Entry::getValue))
                    .orElseThrow()
                    .getKey();

            assertEquals(category, statementGroupingByAgeAndCategory.get(age));
        });
    }

    @Test
    public void createStatementCountingAvgInCategory() {
        Map<String, Double> statementCountingAvgInCategory = orderService.createStatementCountingAvgInCategory();

        Set<String> categories = statementCountingAvgInCategory.keySet();

        categories.forEach(category -> {
            double averageForGivenCategory = orderService
                    .getCustomersWithProducts()
                    .values()
                    .stream()
                    .flatMap(productIntegerMap -> productIntegerMap.keySet().stream())
                    .filter(product -> product.getCategory().equals(category))
                    .mapToDouble(Product::getPrice)
                    .average()
                    .orElseThrow();

            assertEquals(averageForGivenCategory, statementCountingAvgInCategory.get(category));
        });
    }

    @Test
    public void createStatementCountingMaxInCategory() {
        Map<String, Product> statementCountingMaxInCategory = orderService.createStatementCountingMaxInCategory();

        Set<String> categories = statementCountingMaxInCategory.keySet();

        categories.forEach(category -> {
            Product finalProduct = orderService
                    .getCustomersWithProducts()
                    .values()
                    .stream()
                    .flatMap(productIntegerMap -> productIntegerMap.keySet().stream())
                    .filter(product -> product.getCategory().equals(category))
                    .max(Comparator.comparing(Product::getPrice))
                    .orElseThrow();

            assertEquals(finalProduct, statementCountingMaxInCategory.get(category));
        });
    }

    @Test
    public void createStatementCountingMinInCategory() {
        Map<String, Product> statementCountingMinInCategory = orderService.createStatementCountingMinInCategory();

        Set<String> categories = statementCountingMinInCategory.keySet();

        categories.forEach(category -> {
            Product finalProduct = orderService
                    .getCustomersWithProducts()
                    .values()
                    .stream()
                    .flatMap(productIntegerMap -> productIntegerMap.keySet().stream())
                    .filter(product -> product.getCategory().equals(category))
                    .min(Comparator.comparing(Product::getPrice))
                    .orElseThrow();

            assertEquals(finalProduct, statementCountingMinInCategory.get(category));
        });
    }

    // !!!!
    @Test
    public void getCategoryClientMap() {
        assertNotNull(orderService.getCategoryClientMap());
    }

    @Test
    public void calculateDebt() {
        Map<Client, Double> clientDoubleMap = orderService.calculateDebt();

        Set<Client> clients = clientDoubleMap.keySet();

        clients
                .forEach(client -> {
                    double sumForClient = orderService
                            .getCustomersWithProducts()
                            .entrySet()
                            .stream()
                            .filter(clientMapEntry -> clientMapEntry.getKey().equals(client))
                            .map(Map.Entry::getValue)
                            .mapToDouble(productIntegerMap -> productIntegerMap
                                    .entrySet()
                                    .stream()
                                    .mapToDouble(productIntegerEntry -> productIntegerEntry.getKey().getPrice() * productIntegerEntry.getValue())
                                    .sum())
                            .sum();

                    double clientDebt = client.getBalance() - sumForClient;

                    assertEquals(clientDebt, clientDoubleMap.get(client));
                });
    }
}