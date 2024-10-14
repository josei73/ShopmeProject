package com.shopme.common.entity.site.service;


import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.site.Checkout.CheckoutInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutService {
    private static final int DIM_DIVISOR = 139;


    public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate) {
        CheckoutInfo checkoutInfo = new CheckoutInfo();
        float productTotal = calculateProductTotal(cartItems);
        float productCost = calculateProductCost(cartItems);
        float shippingCost = calculateShippingCost(cartItems, shippingRate);
        float paymentTotal = shippingCost + productTotal;

        checkoutInfo.setProductCost(productCost);
        checkoutInfo.setProductTotal(productTotal);
        checkoutInfo.setShippingCostTotal(shippingCost);
        checkoutInfo.setPaymentTotal(paymentTotal);

        checkoutInfo.setDeliverDays(shippingRate.getDays());
        checkoutInfo.setCodSupported(shippingRate.isCodSupported());


        return checkoutInfo;

    }

    /* Berechnung dimensional Weight (DIM) = Package's Lenght X Width X Height / DIM_DIVISOR
    Final Weight = DIM
        Final Weight = Products Weight if Products Weight > DIM
     Shipping Cost = Final Weight X Shipping Rate

     Units:
        Dimensions : inches
        weights : pounds

     DIM DIVISOR = 139 cubics inches per Pound (used by FedEX)


     */
    private float calculateShippingCost(List<CartItem> cartItems, ShippingRate shippingRate) {
        float shippingCostTotal = 0.0f;

        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
            float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
            float shippingCost = finalWeight * item.getQuantity() * shippingRate.getRate();
           // Muss gesetzt werden damit beim Checkout sehen kann wie viel die Transportkosten für die jeweiligen Products sind
            item.setShippingCost(shippingCost);

            shippingCostTotal += shippingCost;
        }


        return shippingCostTotal;
    }

    private float calculateProductTotal(List<CartItem> cartItems) {
        float total = 0.0f;
        // Subtoal ist discount preis falls reduziert ist
        for (CartItem item : cartItems)
            total += item.getSubtotal();
        return total;
    }

    private float calculateProductCost(List<CartItem> cartItems) {
        float cost = 0.0f;
        for (CartItem item : cartItems)
            cost += item.getQuantity() * item.getProduct().getCost();
        return cost;
    }
}
