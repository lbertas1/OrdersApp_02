package org.example.ui;

import org.example.service.OrdersService;
import org.example.ui.controller.Controller;

import static spark.Spark.initExceptionHandler;
import static spark.Spark.port;

public class App {
    public static void main(String[] args) throws NoSuchFieldException {

        final String FILENAME_PATH1 = "C:/IntelijProjects/Z10_T01_MultimoduleApp/ParentModule/org.example.ui/src/main/resources/mapyTask1_1.txt";
        final String FILENAME_PATH2 = "C:/IntelijProjects/Z10_T01_MultimoduleApp/ParentModule/org.example.ui/src/main/resources/mapyTask1_2.txt";
        final String FILENAME_PATH3 = "C:/IntelijProjects/Z10_T01_MultimoduleApp/ParentModule/org.example.ui/src/main/resources/mapyTask1_3.txt";
        final String FILENAME_PATH34_JSON = "C:/IntelijProjects/Z10_T01_MultimoduleApp/ParentModule/org.example.ui/src/main/resources/orders.json";
        var ordersService = new OrdersService(FILENAME_PATH34_JSON);
        var menuService = new MenuService(ordersService);
        menuService.menu();

        port(8090);
        initExceptionHandler(e -> System.out.println(e.getMessage()));

        Controller controller = new Controller(ordersService);
        controller.initRoutes();
    }
}
