package org.example.ui;

import lombok.RequiredArgsConstructor;
import org.example.service.OrdersService;
import org.example.ui.data.UserData;
import org.example.persistence.model.Client;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class MenuService {
    private final OrdersService ordersService;

    private int chooseOption() {
        System.out.println("Menu:");
        System.out.println("1. Show orders");
        System.out.println("2. Client who paid the most");
        System.out.println("3. Get age and categories most often bought in this age");
        System.out.println("4. Get cheapest and most expensive product and average cost for product in category");
        System.out.println("5. Get customer who most often bought products in given category");
        System.out.println("6. Get clients debt");
        System.out.println("0. End of app");
        return UserData.getInt("Choose option:");
    }

    public void menu() {

        int option;
        do {
            option = chooseOption();
            switch (option) {
                case 1 -> option1();
                case 2 -> option2();
                case 3 -> option3();
                case 4 -> option4();
                case 5 -> option5();
                case 6 -> option6();
                case 0 -> {
                    System.out.println("Have a nice day!");
                    return;
                }
                default -> System.out.println("No such option");
            }

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);
    }

    private void option1() {
        ordersService.print();
    }

    private void option2() {
        System.out.println(ordersService.designateClientWhoHasPaidTheMost());
    }

    private void option3() {
        ordersService.createStatementGroupingByAgeAndCategory().forEach((integer, s) -> System.out.println("AGE: " + integer + " " + " CATEGORY: " + s));
    }

    private void option4() {
        ordersService.createStatementCountingAvgInCategory();
    }

    private void option5() {
        ordersService.getCategoryClientMap().forEach((s, client) -> System.out.println("CATEGORY: " + s + " CLIENT: " + client));
    }

    private void option6() {
        ordersService.calculateDebt().forEach((client, aDouble) -> {
            System.out.println("CLIENT: " + client + " HIS DEBT: " + aDouble);
        });
    }
}
