package org.example;

import java.util.Scanner;

import static java.lang.Thread.sleep;

public class App {

    public static final Scanner SCANNER = new Scanner(System.in);

    public static boolean func1(int a) throws InterruptedException {
        for (int i = 0; i < 100_000_000; i++) {
            sleep(1);
            if (i > a) {
                return false;
            }
        }

        return false;
    }

    public static boolean func2() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            sleep(10_000_000);
        }
        return false;
    }

    public static boolean func3(int b) throws InterruptedException {
        for (int i = 10; i > 3; i++) {
            sleep(1);
            if (i < b) {
                return false;
            }
        }
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n Inside main()");
        String typeOfShape;
        typeOfShape = SCANNER.next();

        int i = 0;

        for (; i < 10; i++) {
        }
        {
            for (int j = 1_000_000; j > 0; j--) {
                if (func1(i) || func2() || func3(j)) {
                    System.out.println("\n Inside if()");
                }
            }
        }
    }
}
