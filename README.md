# OrderManagement
Solution for various order management process during bidding process


Part A 

Following use cases has been addressed using Java language.

* OrderBook.java class has the solution for the given use cases
* OrderBookTest.java class has corresponding unit test cases 

• Given an Order, add it to the OrderBook (order additions are expected to occur extremely frequently)

• Given an order id, remove an Order from the OrderBook (order deletions are expected to occur at ap-
proximately 60% of the rate of order additions)

• Given an order id and a new size, modify an existing order in the book to use the new size (size modi-
cations do not eect time priority)
• Given a side and a level (an integer value >0) return the price for that level (where level 1 represents the
best price for a given side). For example, given side=B and level=2 return the second best bid price
• Given a side and a level return the total size available for that level
• Given a side return all the orders from that side of the book, in level- and time-order

Part B
Please suggest modications or additions to the Order and/or OrderBook classes to 
make them better suited to support real-life, latency-sensitive trading operations.


Here are a few suggestions:

    1.Add a timestamp field to the Order class to track the time the order was submitted. 
    This will allow the OrderBook to prioritize orders not just by price, but also by the 
    time they were submitted. It will also enable time-ordered queries, which are often 
    important in trading.

    2.Implement a more efficient data structure for the OrderBook. The current implementation 
    uses a map of queues, which is not very efficient for real-time trading operations. 
    A better data structure would be a binary heap or a balanced tree, which can provide efficient
    insertion, deletion, and search operations.

    3.Implement circuit breakers to prevent the OrderBook from being overwhelmed by a sudden influx of orders. 
    This can be done by setting limits on the number of orders that can be submitted per second or by monitoring 
    the overall load on the system and throttling incoming requests accordingly.

    4.Implement mechanisms for handling multiple trading venues and exchanges. 
    This will allow the OrderBook to handle orders from multiple sources and integrate with other
    trading systems. This can be done by implementing standard protocols for order submission and retrieval,
    such as FIX (Financial Information Exchange) or a RESTful API.

    5.Implement a mechanism for handling partial fills. When an order is partially filled, 
    it should remain in the OrderBook with the remaining size, and the timestamp should be updated to 
    reflect the time of the partial fill. This will allow the OrderBook to continue prioritizing the 
    order based on its remaining size and time priority.


