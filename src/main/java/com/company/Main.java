package com.company;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class Main {
    static EntityManagerFactory emf;
    static EntityManager em;
    static MenuDAO menu = new MenuDAO();

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        try{
            emf = Persistence.createEntityManagerFactory("JPAOrder");
            em = emf.createEntityManager();

            menu.createMenu(em);

            try{
                menu.selectAll(em);
                while(true) {
                    System.out.println("To add new order enter 1:");
                    System.out.println("To choose order for prices enter 2:");
                    System.out.println("To choose order with discount enter 3:");
                    System.out.println("To choose order for weight enter 4:");
                    String s = scanner.nextLine();
                    switch (s) {
                        case "1" :
                            menu.addToMenu(em, scanner);
                            break;
                        case "2" :
                            menu.selectOrdersForPrice(em, scanner);
                            break;
                        case "3" :
                            menu.selectOrdersForDiscount(em);
                            break;
                        case "4":
                            menu.selectOrdersForWeight(em, scanner);
                            break;
                        default: return;
                    }
                }
            } finally {
                scanner.close();
                em.close();
                emf.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
