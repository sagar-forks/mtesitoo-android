package com.mtesitoo.backend.service.logic;

import com.mtesitoo.backend.model.Order;
import com.mtesitoo.backend.model.OrderStatus;


import java.util.List;

/**
 * Created by User on 26-04-2016
 */
public interface IOrderRequest {

    void getOrders(int sellerId, OrderStatus orderStatusId, ICallback<List<Order>> callback);

    void getDetailedOrders(Order order, ICallback callback);

    void submitOrder(Order order, ICallback<Order> callback);
}