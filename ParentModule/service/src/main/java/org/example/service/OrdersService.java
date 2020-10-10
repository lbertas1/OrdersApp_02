package org.example.service;

import org.example.persistence.converters.JsonBaseConverter;
import org.example.persistence.exception.JsonConverterException;
import org.example.persistence.model.Client;
import org.example.persistence.model.Order;
import org.example.persistence.model.Product;
import org.example.service.dto.ClientWithProduct;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class OrdersService {

    private final Map<Client, Map<Product, Integer>> customersWithProducts;
    private ReadFromTxt readFromTxt;

    public OrdersService(String fileName) {
        this.customersWithProducts = init(fileName);
    }

    public OrdersService(String filename1, String filename2, String filename3) {
        readFromTxt = new ReadFromTxt(filename1, filename2, filename3);
        customersWithProducts = readFromTxt.getCustomersWithProducts();
    }

    public Map<Client, Map<Product, Integer>> init(String fileName) {
        return new JsonBaseConverter(fileName)
                .fromJson()
                .orElseThrow(() -> new JsonConverterException("cannot convert data from file"))
                .stream()
                .collect(Collectors.toMap(
                        Order::getClient,
                        order -> {
                            Map<Product, Integer> productIntegerMap = new LinkedHashMap<>();
                            order.getProducts().forEach(product -> {
                                productIntegerMap.merge(product, 1, Integer::sum);
                            });
                            return productIntegerMap;
                        }
                ));
    }

    public Map<Client, Map<Product, Integer>> getCustomersWithProducts() {
        return customersWithProducts;
    }

    public void print() {
        readFromTxt.getCustomersWithProducts().forEach((client, productIntegerMap) -> {
            System.out.println("CLIENT: " + client + "\n");
            productIntegerMap.forEach((product, integer) -> {
                System.out.println("PRODUCT: " + product + " in quantity: " + integer);
            });
        });
    }

    // 1
    public Client designateClientWhoHasPaidTheMost() {
        return customersWithProducts
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.collectingAndThen(Collectors.mapping(clientMapEntry -> clientMapEntry
                                        .getValue()
                                        .entrySet()
                                        .stream()
                                        .mapToDouble(productIntegerEntry -> productIntegerEntry.getKey().getPrice() * productIntegerEntry.getValue())
                                        .sum(),
                                Collectors.toList()),
                                doubles -> doubles)
                ))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        clientListEntry -> clientListEntry.getValue().get(0)
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow()
                .getKey();
    }

    // 2
    public Map<Integer, String> createStatementGroupingByAgeAndCategory() {
        return customersWithProducts
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(
                        e -> e.getKey().getAge(),
                        Collectors.collectingAndThen(
                                Collectors.flatMapping(e -> e.getValue().entrySet().stream().flatMap(ee -> Collections.nCopies(ee.getValue(), ee.getKey().getCategory()).stream()), Collectors.toList()),
                                categories -> categories
                                        .stream()
                                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                                        .entrySet()
                                        .stream()
                                        .max(Map.Entry.comparingByValue())
                                        .orElseThrow()
                                        .getKey()
                        )
                ));
    }

    // 3
    public Map<String, Double> createStatementCountingAvgInCategory() {

        return customersWithProducts
                .values()
                .stream()
                .flatMap(productIntegerMap -> productIntegerMap.keySet().stream())
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.averagingDouble(Product::getPrice)
                ));
    }

    public Map<String, Product> createStatementCountingMaxInCategory() {

        return customersWithProducts
                .values()
                .stream()
                .flatMap(productIntegerMap -> productIntegerMap.keySet().stream())
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingDouble(Product::getPrice)), Optional::orElseThrow)
                ));
    }

    public Map<String, Product> createStatementCountingMinInCategory() {

        return customersWithProducts
                .values()
                .stream()
                .flatMap(productIntegerMap -> productIntegerMap.keySet().stream())
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.collectingAndThen(Collectors.minBy(Comparator.comparingDouble(Product::getPrice)), Optional::orElseThrow)
                ));
    }

    // 4
    /*public Map<String, Client> getCategoryClientMap() {
        List<String> categories = customersWithProducts.values().stream()
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .map(Product::getCategory)
                .collect(Collectors.toList());

        Map<String, Client> categoryClientMap = new LinkedHashMap<>();

        categories.forEach(s -> {
            AtomicLong biggestCounterForCategory = new AtomicLong();
            customersWithProducts.forEach((client, productIntegerMap) -> {
                long counter = productIntegerMap.keySet().stream().filter(product -> product.getCategory().equalsIgnoreCase(s)).count();
                if (counter > biggestCounterForCategory.get()) {
                    biggestCounterForCategory.set(counter);
                    categoryClientMap.put(s, client);
                }
            });
            biggestCounterForCategory.set(0);
        });
        return categoryClientMap;
    }*/

    public Map<String, Client> getCategoryClientMap() {
        return customersWithProducts
                .entrySet()
                .stream()
                .flatMap(e -> e
                        .getValue()
                        .entrySet()
                        .stream()
                        .flatMap(ee -> Collections.nCopies(ee.getValue(), ee.getKey()).stream())
                        .map(product -> ClientWithProduct.builder()
                                .client(e.getKey())
                                .product(product)
                                .build()))
                .collect(Collectors.groupingBy(
                        cwp -> cwp.getProduct().getCategory(),
                        Collectors.collectingAndThen(
                                Collectors.groupingBy(ClientWithProduct::getClient, Collectors.toList()),
                                e -> e
                                        .entrySet()
                                        .stream()
                                        .max(Comparator.comparingInt(ee -> ee.getValue().size()))
                                        .orElseThrow()
                                        .getKey()
                        )
                ));
    }

    // 5
    public Map<Client, Double> calculateDebt() {
        return customersWithProducts
                .entrySet()
                .stream()
                .filter(clientMapEntry -> clientMapEntry.getKey().getBalance() < clientMapEntry
                        .getValue()
                        .entrySet()
                        .stream()
                        .mapToDouble(productIntegerEntry -> productIntegerEntry.getKey().getPrice() * productIntegerEntry.getValue())
                        .sum())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.collectingAndThen(Collectors.mapping(clientMapEntry -> clientMapEntry
                                        .getValue()
                                        .entrySet()
                                        .stream()
                                        .mapToDouble(productIntegerEntry -> productIntegerEntry.getKey().getPrice() * productIntegerEntry.getValue()), Collectors.summingDouble(DoubleStream::sum)),
                                aDouble -> aDouble
                        )))
                .entrySet()
                .stream()
                .peek(clientDoubleEntry -> clientDoubleEntry.setValue(clientDoubleEntry.getKey().getBalance() - clientDoubleEntry.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }
}
