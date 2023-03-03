package com.mizuho.order;


import lombok.EqualsAndHashCode;

import java.util.*;

@EqualsAndHashCode
public class OrderBook {
    private TreeMap<Double, PriorityQueue<Order>> bids;
    private Map<Double, PriorityQueue<Order>> offers;
    Map<Long, Order> orderMap;
    private long orderIdCounter = 0;

    public OrderBook() {
        bids = new TreeMap<>(Comparator.reverseOrder());
        offers = new TreeMap<>();
        orderMap = new HashMap<>();
    }

    public void addOrder(Order order) {
        order.setId(++orderIdCounter);
        PriorityQueue<Order> ordersAtPrice = (order.getSide() == 'B') ? bids.computeIfAbsent(order.getPrice(), k -> new PriorityQueue<>(Comparator.comparing(Order::getId))) : offers.computeIfAbsent(order.getPrice(), k -> new PriorityQueue<>(Comparator.comparing(Order::getId)));
        ordersAtPrice.offer(order);
        orderMap.put(order.getId(), order);
    }

    public Order getBestBid() {
        if (bids.isEmpty()) {
            return null;
        }
        Double bestBidPrice = bids.keySet().iterator().next();
        PriorityQueue<Order> bestBids = bids.get(bestBidPrice);
        if (bestBids.isEmpty()) {
            return null;
        }
        return bestBids.peek();
    }

    public Double getPriceForLevel(char side, int level) {
        if (side == 'B') {
            SortedMap<Double, PriorityQueue<Order>> bidsDescending = bids.descendingMap();
            int currentLevel = 1;
            for (Map.Entry<Double, PriorityQueue<Order>> entry : bidsDescending.entrySet()) {
                if (currentLevel + entry.getValue().size() > level) {
                    return entry.getKey();
                } else {
                    currentLevel += entry.getValue().size();
                }
            }
        } else if (side == 'O') {
            SortedMap<Double, PriorityQueue<Order>> offersAscending = (SortedMap<Double, PriorityQueue<Order>>) offers;

            int currentLevel = 1;
            for (Map.Entry<Double, PriorityQueue<Order>> entry : offersAscending.entrySet()) {
                if (currentLevel + entry.getValue().size() > level) {
                    return entry.getKey();
                } else {
                    currentLevel += entry.getValue().size();
                }
            }
        }
        return null;
    }

    public void removeOrder(long orderId) {
        Order order = orderMap.remove(orderId);
        if (order != null) {
            if (order.getSide() == 'B') {
                PriorityQueue<Order> bids = this.bids.get(order.getPrice());
                if (bids != null) {
                    bids.remove(order);
                    if (bids.isEmpty()) {
                        this.bids.remove(order.getPrice());
                    }
                }
            } else if (order.getSide() == 'O') {
                PriorityQueue<Order> offers = this.offers.get(order.getPrice());
                if (offers != null) {
                    offers.remove(order);
                    if (offers.isEmpty()) {
                        this.offers.remove(order.getPrice());
                    }
                }
            }
        }
    }

    public void modifyOrder(long orderId, long newSize) {
        Order order = orderMap.get(orderId);
        if (order == null) {
            return;
        }
        Order newOrder = order.withSize(newSize);
        orderMap.put(orderId, newOrder);
        PriorityQueue<Order> ordersAtPrice = (order.getSide() == 'B') ? bids.get(order.getPrice()) : offers.get(order.getPrice());
        ordersAtPrice.remove(order);
        ordersAtPrice.add(newOrder);
    }

    public Order getOrderById(long orderId) {
        return orderMap.getOrDefault(orderId, null);
    }

    public Long getSizeForLevel2(char side, int level) {
        if (side == 'B') {
            SortedMap<Double, PriorityQueue<Order>> bidsDescending = bids.descendingMap();
            int currentLevel = 1;
            for (Map.Entry<Double, PriorityQueue<Order>> entry : bidsDescending.entrySet()) {
                if (currentLevel + entry.getValue().size() > level) {
                    int numOrdersAtLevel = level - currentLevel;
                    long size = 0;
                    for (Order order : entry.getValue()) {
                        if (numOrdersAtLevel == 0) {
                            break;
                        }
                        size += order.getSize();
                        numOrdersAtLevel--;
                    }
                    return size;
                } else {
                    currentLevel += entry.getValue().size();
                }
            }
        } else if (side == 'O') {
            SortedMap<Double, PriorityQueue<Order>> offersAscending = (SortedMap<Double, PriorityQueue<Order>>) offers;

            int currentLevel = 1;
            for (Map.Entry<Double, PriorityQueue<Order>> entry : offersAscending.entrySet()) {
                if (currentLevel + entry.getValue().size() > level) {
                    int numOrdersAtLevel = level - currentLevel;
                    long size = 0;
                    for (Order order : entry.getValue()) {
                        if (numOrdersAtLevel == 0) {
                            break;
                        }
                        size += order.getSize();
                        numOrdersAtLevel--;
                    }
                    return size;
                } else {
                    currentLevel += entry.getValue().size();
                }
            }
        }
        return null;
    }

    public Long getSizeForLevel(char side, int level) {
        if (side == 'B') {
            SortedMap<Double, PriorityQueue<Order>> bidsDescending = bids.descendingMap();
            int currentLevel = 1;
            for (Map.Entry<Double, PriorityQueue<Order>> entry : bidsDescending.entrySet()) {
                if (currentLevel + entry.getValue().size() > level) {
                    long size = 0;
                    for (Order order : entry.getValue()) {
                        size += order.getSize();
                    }
                    return size;
                } else {
                    currentLevel += entry.getValue().size();
                }
            }
        } else if (side == 'O') {
            SortedMap<Double, PriorityQueue<Order>> offersAscending = (SortedMap<Double, PriorityQueue<Order>>) offers;

            int currentLevel = 1;
            for (Map.Entry<Double, PriorityQueue<Order>> entry : offersAscending.entrySet()) {
                if (currentLevel + entry.getValue().size() > level) {
                    long size = 0;
                    for (Order order : entry.getValue()) {
                        size += order.getSize();
                    }
                    return size;
                } else {
                    currentLevel += entry.getValue().size();
                }
            }
        }
        return null;
    }


    public List<Order> getOrdersBySide(char side) {
        List<Order> orders = new ArrayList<>();
        if (side == 'B') {
            SortedMap<Double, PriorityQueue<Order>> bidsDescending = bids.descendingMap();
            for (Map.Entry<Double, PriorityQueue<Order>> entry : bidsDescending.entrySet()) {
                PriorityQueue<Order> ordersAtLevel = entry.getValue();
                while (!ordersAtLevel.isEmpty()) {
                    orders.add(ordersAtLevel.poll());
                }
            }
        } else if (side == 'O') {
            SortedMap<Double, PriorityQueue<Order>> offersAscending = (SortedMap<Double, PriorityQueue<Order>>) offers;
            for (Map.Entry<Double, PriorityQueue<Order>> entry : offersAscending.entrySet()) {
                PriorityQueue<Order> ordersAtLevel = entry.getValue();
                while (!ordersAtLevel.isEmpty()) {
                    orders.add(ordersAtLevel.poll());
                }
            }
        }
        return orders;
    }
}





