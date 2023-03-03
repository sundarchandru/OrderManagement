package com.mizuho.order;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OrderBookTest {

    @Test
    public void testAddOrder() {
        OrderBook orderBook = new OrderBook();
        orderBook.addOrder(new Order(0, 100.0, 'B', 10));
        orderBook.addOrder(new Order(0, 99.0, 'B', 5));
        orderBook.addOrder(new Order(0, 101.0, 'O', 15));
        Order bestBid = orderBook.getBestBid();
        assertEquals(100.0, bestBid.getPrice(), 0.0);
    }

    @Test
    public void testRemoveOrder() {
        OrderBook orderBook = new OrderBook();
        // Add some orders to the book
        Order order1 = new Order(1, 100.0, 'B', 10);
        Order order2 = new Order(2, 99.0, 'B', 5);
        Order order3 = new Order(3, 101.0, 'O', 15);
        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        // Remove an existing order
        orderBook.removeOrder(order1.getId());
        assertEquals(null, orderBook.getOrderById(order1.getId()));
        // Remove a non-existent order
        orderBook.removeOrder(4);
        assertNull(orderBook.getOrderById(4));
    }


    @Test
    public void testModifyOrder() {
        OrderBook orderBook = new OrderBook();

        // Add an order to the book
        Order order1 = new Order(1, 100.0, 'B', 10);
        orderBook.addOrder(order1);
        // Modify the size of the order
        orderBook.modifyOrder(1, 8);

        // Retrieve the modified order by ID
        Order modifiedOrder = orderBook.getOrderById(1);

        // Check that the order has the expected size
        assertEquals(8, modifiedOrder.getSize());
    }

    @Test
    public void testGetPriceForLevel() {
        OrderBook orderBook = new OrderBook();
        orderBook.addOrder(new Order(1, 10.0, 'B', 100));
        orderBook.addOrder(new Order(2, 11.0, 'B', 200));
        orderBook.addOrder(new Order(3, 12.0, 'B', 50));
        orderBook.addOrder(new Order(4, 13.0, 'O', 150));
        orderBook.addOrder(new Order(5, 14.0, 'O', 75));
        orderBook.addOrder(new Order(6, 15.0, 'O', 125));

        assertEquals(10.0, orderBook.getPriceForLevel('B', 1), 0.0001);
        assertEquals(11.0, orderBook.getPriceForLevel('B', 2), 0.0001);
        assertEquals(12.0, orderBook.getPriceForLevel('B', 3), 0.0001);
        assertEquals(13.0, orderBook.getPriceForLevel('O', 1), 0.0001);
        assertEquals(14.0, orderBook.getPriceForLevel('O', 2), 0.0001);
        assertEquals(15.0, orderBook.getPriceForLevel('O', 3), 0.0001);
        assertNull(orderBook.getPriceForLevel('B', 4));
        assertNull(orderBook.getPriceForLevel('O', 4));
    }

    @Test
    public void testGetSizeForLevel() {
        OrderBook orderBook = new OrderBook();
        orderBook.addOrder(new Order(1, 10.0, 'B', 10));
        orderBook.addOrder(new Order(2, 11.0, 'B', 5));
        orderBook.addOrder(new Order(3, 12.0, 'B', 20));
        orderBook.addOrder(new Order(4, 13.0, 'O', 15));
        orderBook.addOrder(new Order(5, 14.0, 'O', 20));

        Long size = orderBook.getSizeForLevel('B', 1);
        assertEquals(10L, size.longValue());

        size = orderBook.getSizeForLevel('B', 2);
        assertEquals(5L, size.longValue());

        size = orderBook.getSizeForLevel('B', 3);
        assertEquals(20L, size.longValue());

        size = orderBook.getSizeForLevel('O', 1);
        assertEquals(15L, size.longValue());

        size = orderBook.getSizeForLevel('O', 2);
        assertEquals(20L, size.longValue());

        size = orderBook.getSizeForLevel('O', 3);
        assertNull(size);
    }

    @Test
    public void testGetOrdersBySide() {
        OrderBook orderBook = new OrderBook();

        Order order1 = new Order(1, 10.0, 'B', 5);
        Order order2 = new Order(2, 11.0, 'B', 10);
        Order order3 = new Order(3, 9.0, 'O', 15);
        Order order4 = new Order(4, 8.0, 'O', 20);

        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        orderBook.addOrder(order4);

        List<Order> bids = orderBook.getOrdersBySide('B');
        assertEquals(2, bids.size());
        assertEquals(order1, bids.get(0));
        assertEquals(order2, bids.get(1));

        List<Order> offers = orderBook.getOrdersBySide('O');
        assertEquals(2, offers.size());
        assertEquals(order4, offers.get(0));
        assertEquals(order3, offers.get(1));
    }


}
