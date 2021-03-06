package com.mtesitoo.backend.service;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.model.Order;
import com.mtesitoo.backend.model.OrderProduct;
import com.mtesitoo.backend.model.OrderStatus;
import com.mtesitoo.backend.service.logic.ICallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by naily on 10/08/16.
 */
public class OrderDetailsResponse implements Response.Listener<String>, Response.ErrorListener {
    private ICallback  mCallback;
    private Order      mOrder;

    public OrderDetailsResponse(Order order, ICallback callback) {
        mCallback = callback;
        mOrder = order;
    }

    @Override
    public void onResponse(String response) {
        try {
            //todo naily how to handle responses from post?
            //hack for post messages. should handle it differently
            if (!response.isEmpty())
                parseResponse(response);

            if (mCallback != null)
                mCallback.onResult(mOrder);
        } catch (JSONException e) {
            if (mCallback != null)
                mCallback.onError(e);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mCallback.onError(error);
    }

    public void parseResponse(String response) throws JSONException {

        /**
         * Read detailed of a specific order. This should contain only products that have the same
         * status as the overall order.
         * Background info:
         * GET /api/v1/vendor/order returns multiple entries for an order that contains products
         * with different statuses. For e.g. assuming Order 12 has 5 products (2 shipped, 2 pending,
         * 1 cancelled), there will be 3 instances of Order 12 listed, one for each category.
         * When you want to see details of an Order that says "Pending",
         * GET /api/v1/vendor/order/{id} still lists all the 5 products. We are manually filtering
         * the result here so that we would get 2 products instead of 5.
         */

        JSONObject jsonOrder = new JSONObject(response);
        //TODO NAILY COMMENT OUT BEFORE SUBMISSION
        Log.d("OrderDetailsResponse", jsonOrder.toString(2));

        mOrder.setPaymentMethod(jsonOrder.getString("payment_method"));
        mOrder.setCustomerId(jsonOrder.getInt("customer_id"));
        mOrder.setEmailAddress(jsonOrder.getString("email"));
        mOrder.setCustomerTelephone(jsonOrder.getString("telephone"));
        //TODO naily Check with dAve - Do we populate shipping_address or payment_address
        mOrder.setDeliveryAddress(jsonOrder.getString("payment_address_1") + ", " +
                                  jsonOrder.getString("payment_address_2") + ", " +
                                  jsonOrder.getString("payment_postcode") + ", " +
                                  jsonOrder.getString("payment_city") + ", " +
                                  jsonOrder.getString("payment_country"));

        OrderStatus orderStatus = mOrder.getOrderStatus();

        JSONArray jsonProducts = jsonOrder.getJSONArray("products");
        ArrayList<OrderProduct> products = new ArrayList<>(jsonProducts.length());
        for (int i = 0; i < jsonProducts.length(); ++i)
        {
            JSONObject jsonProduct = jsonProducts.getJSONObject(i);
            OrderStatus productStatus = mapStatuses(jsonProduct.getInt("order_status_id"));
            // Only add products which matches the status of the order.
            if (productStatus == orderStatus) {
                products.add(new OrderProduct(
                        jsonProduct.getInt("order_product_id"),
                        mapStatuses(jsonProduct.getInt("order_status_id")),
                        jsonProduct.getString("name"),
                        jsonProduct.getString("model"),
                        jsonProduct.getInt("quantity"),
                        jsonProduct.getDouble("price")
                ));
            }
        }
        mOrder.setProducts(products);
    }

    private static Date FormatJsonDate(String dateString)
    {
        // Represents the current date format as specified by the string in the json definition file
        // e.g. "date_added":"2016-05-21 22:32:25"
        String dateStringFormat = "yyyy-MM-dd hh:mm:sss";

        Date date;

        try
        {
            date = new SimpleDateFormat(dateStringFormat).parse(dateString);
        }
        catch (java.text.ParseException e)
        {
            // If exception fires, make sure that the format specified by dateStringFormat matches
            // the format in the json definition file.
            // At time of writing (Jul-2016), this is what we get back: "date_added":"2016-05-08 22:41:05"
            Log.e("FormatHelper", "Error - " + e.getMessage());
            date = new Date();
        }

        return date;
    }

    private OrderStatus mapStatuses(int orderStatusId)
    {
        OrderStatus status;

        switch(orderStatusId) {
            case 1:
                status = OrderStatus.PENDING;
                break;
            case 3:
                status = OrderStatus.SHIPPED;
                break;
            case 5:
                status = OrderStatus.COMPLETE;
                break;
            case 7:
                status = OrderStatus.CANCELED;
                break;
            default: {
                Log.e("mapStatuses", "Status Id" + orderStatusId + "isn't supported");
                status = OrderStatus.ALL;
            }
        }

        return status;
    }
}
