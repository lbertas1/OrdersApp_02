package org.example.ui.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.JsonTransformer;
import org.example.service.OrdersService;

import static spark.Spark.*;

@RequiredArgsConstructor
public class Controller {

    private final OrdersService orders;

    public void initRoutes() {

        path("/get-orders", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                orders.print();
                return null;
            }), new JsonTransformer());
        });

        path("/client-who-paid-most", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                orders.designateClientWhoHasPaidTheMost();
                orders.designateClientWhoHasPaidTheMost();
                return null;
            }), new JsonTransformer());
        });

        path("/group-by-age-and-category", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                orders.createStatementGroupingByAgeAndCategory();
                return null;
            }), new JsonTransformer());
        });

        path("/count-average-in-category", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                orders.createStatementCountingAvgInCategory();
                return null;
            }), new JsonTransformer());
        });

        path("/category-client-map", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                orders.getCategoryClientMap();
                return null;
            }), new JsonTransformer());
        });

        path("/calculate-debt", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                orders.calculateDebt();
                return null;
            }), new JsonTransformer());
        });
    }
}
