package com.company;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuDAO {

    private final List<MenuOrders> menuItems = new ArrayList<>();

    public MenuDAO() {
        menuItems.add(new MenuOrders("fish", 32.43, 100, "d"));
        menuItems.add(new MenuOrders("meat", 44.13, 100, "d"));
        menuItems.add(new MenuOrders("eggs", 10.78, 50, ""));
        menuItems.add(new MenuOrders("bear", 30.00, 900, ""));
        menuItems.add(new MenuOrders("tea", 20.20, 100, ""));
        menuItems.add(new MenuOrders("salad", 18.95, 50, "d"));
    }

    public void createMenu(EntityManager em) {
        em.getTransaction().begin();
        try{
            for (MenuOrders menuItem : menuItems) {

                em.persist(menuItem);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    public void selectOrdersForPrice(EntityManager em, Scanner scanner) {
        System.out.println("Enter minimum price: ");
        Double minPrice = Double.parseDouble(scanner.nextLine());
        System.out.println("Enter max price: ");
        Double maxPrice = Double.parseDouble(scanner.nextLine());

        try{
            Query query = em.createQuery("SELECT o FROM MenuOrders o WHERE o.price BETWEEN :minPrice AND :maxPrice");
            query.setParameter("minPrice", minPrice);
            query.setParameter("maxPrice", maxPrice);
            view(query);
        } catch (NoResultException e) {
            System.out.println("No orders found");
        }


    }

    public void addToMenu(EntityManager em, Scanner scanner) {
        System.out.println("Enter name of new dish: ");
        String name = scanner.nextLine();
        System.out.println("Enter price of new dish: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.println("Enter weight of new dish: ");
        int weight = Integer.parseInt(scanner.nextLine());
        System.out.println("If this dish contain discount enter d: ");
        String discount = scanner.nextLine();

        em.getTransaction().begin();
        try{
            MenuOrders mo = new MenuOrders(name, price, weight, discount);
            em.persist(mo);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    public void selectAll(EntityManager em) {
        try{
            Query query = em.createQuery("SELECT id FROM MenuOrders id");

            view(query);
        } catch (NoResultException e) {
            System.out.println("No orders found");
        }
    }

    public void selectOrdersForDiscount(EntityManager em) {
        try{
            Query query = em.createQuery("SELECT d FROM MenuOrders d WHERE d.discount = 'd'");

            view(query);
        } catch (NoResultException e) {
            System.out.println("No orders found");
        }
    }

    public void selectOrdersForWeight(EntityManager em, Scanner scanner) {
        int maxWeight = 0;
        MenuOrders mo = null;
        System.out.println("Enter name of dish which you want to choose: ");
        String name;
        StringBuilder order = new StringBuilder();
        try {
            while (maxWeight < 1000) {
                name = scanner.nextLine();
                Query query = em.createQuery("SELECT w FROM MenuOrders w WHERE name = :name");
                query.setParameter("name", name);
                mo = (MenuOrders) query.getSingleResult();
                maxWeight += mo.getWeight();
                order.append(mo.getName()).append(", ");
                System.out.println("Your order summary weight is: " + maxWeight);
                System.out.println("Your order contains: " + order.substring(0, order.lastIndexOf(",")));
            }
        } catch (NoResultException e) {
            System.out.println("No orders found");
        }
    }

    private void view(Query query) {
        List<MenuOrders> listOrdersWithDiscount = (List<MenuOrders>) query.getResultList();
        System.out.println("name\tprice\tweight\tdiscount");
        for (MenuOrders mo : listOrdersWithDiscount) {
            System.out.println(mo.getName() + "\t" + mo.getPrice() + "\t" + mo.getWeight() + "\t" + mo.getDiscount());
            System.out.println();
        }
    }

    public List<MenuOrders> getMenuItems() {
        return menuItems;
    }

}
