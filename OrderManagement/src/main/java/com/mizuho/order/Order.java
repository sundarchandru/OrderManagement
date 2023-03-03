package com.mizuho.order;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Order {


    private long id; // id of order
    private double price;
    private char side; // B "Bid" or O "Offer"
    private long size;

    public Order(long id, double price, char side, long size) {
        this.id = id;
        this.price = price;
        this.side = side;
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public char getSide() {
        return side;
    }

    public long getSize() {
        return size;
    }

    public Order withSize(long newSize) {
        return new Order(id, price, side, newSize);
    }

    public void setSize(long newSize) {
        size = newSize;
    }
    public void setId(long id) {
        this.id = id;
    }


}
