package com.driver;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {
    Map<String, Order> orderMap;
    Map<String, DeliveryPartner> partnerMap;
    Map<String, List<String>> partnerOrderMap;
    Map<String , String> orderPartnerPair;

    public OrderRepository(){
        this.orderMap = new HashMap<>();
        this.partnerMap = new HashMap<>();
        this.partnerOrderMap = new HashMap<>();
        this.orderPartnerPair = new HashMap<>();

    }
    public void addOrder(Order order) throws Exception{
        orderMap.put(order.getId(), order);
    }

    public void addPartner(String id){
        partnerMap.put(id, new DeliveryPartner(id));
    }

    public void assignPartner(String orderId, String partnerId){
        DeliveryPartner partner = partnerMap.get(orderId);
        Order order = orderMap.get(orderId);

        List<String> orderList = partnerOrderMap.getOrDefault(partnerId, new ArrayList<>());
        orderList.add(orderId);
        partnerOrderMap.put(partnerId, orderList);
        orderPartnerPair.put(orderId, partnerId);

        int numberOfOrders = partnerOrderMap.get(partnerId).size();
        partner.setNumberOfOrders(numberOfOrders);
    }

    public Order getOrderById(String orderId){
        Order order = orderMap.getOrDefault(orderId, null);
        return order;
    }

    public DeliveryPartner getPartnerById(String partnerId){
        return partnerMap.getOrDefault(partnerId, null);
    }

    public Integer getOrderCountByPartnerId(String parnetId){
        int count = partnerMap.get(parnetId).getNumberOfOrders();
        return count;
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        return partnerOrderMap.getOrDefault(partnerId, new ArrayList<>());
    }

    public List<String> getAllOrders(){
        return new ArrayList<>(orderMap.keySet());
    }

    public Integer getCountOfUnassignedOrders(){
        int unAssigned = 0;
        for(String orderId : orderMap.keySet()){
            if(!orderPartnerPair.containsKey(orderId)){
                unAssigned++;
            }
        }
        return unAssigned;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(int currTime, String partnerId){
        List<String> orderList = partnerOrderMap.get(partnerId);
        int remaining = 0;
        for(String o : orderList){
            Order order = orderMap.get(o);
            if(order.getDeliveryTime() > currTime) remaining++;
        }
        return remaining;
    }

    public Integer getLastDeliveryTimeByPartnerId(String partnerId){
        int lastTime = 0;
        for(String o : partnerOrderMap.get(partnerId)){
            Order order = orderMap.get(o);
            lastTime = Math.max(lastTime, order.getDeliveryTime());
        }
        return lastTime;
    }

    public void deletePartnerById(String partnerId){
        //getting orders related to partner
        List<String> orderList = partnerOrderMap.get(partnerId);
        for(String order : orderList){
            orderPartnerPair.remove(order);
        }
        partnerOrderMap.remove(partnerId);
        partnerMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId){
        orderMap.remove(orderId);
        if(orderPartnerPair.containsKey(orderId)){
            String partner = orderPartnerPair.get(orderId);

            List<String> orderList = partnerOrderMap.get(partner);
            orderList.remove(orderId);
            partnerOrderMap.put(partner,orderList);

            DeliveryPartner deliveryPartner = partnerMap.get(partner);
            deliveryPartner.setNumberOfOrders(partnerOrderMap.get(partner).size());

            orderPartnerPair.remove(orderId);
        }
    }
}
