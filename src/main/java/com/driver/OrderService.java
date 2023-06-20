package com.driver;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService{
    @Autowired
    OrderRepository repository;
    public void addOrder(Order order) throws Exception {
        repository.addOrder(order);
    }
    public void addPartner(String partnerId){
        repository.addPartner(partnerId);
    }
    public void assignPartner(String orderId, String partnerId){
        repository.assignPartner(orderId, partnerId);
    }
    public Order getOrderById(String orderId){ return repository.getOrderById(orderId);}
    public DeliveryPartner getPartnerById(String partnerId){ return repository.getPartnerById(partnerId);}
    public Integer getOrderCountByPartnerId(String partnerId){ return repository.getOrderCountByPartnerId(partnerId);}
    public List<String> getOrdersByPartnerId(String partnerId){
        return repository.getOrdersByPartnerId(partnerId);
    }
    public List<String> getAllOrders(){return repository.getAllOrders();}
    public Integer getCountOfUnassignedOrders(){return repository.getCountOfUnassignedOrders();}

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){
        String[]str = time.split(":");
        int currTime = Integer.parseInt(str[0])*60 + Integer.parseInt(str[1]);
        return repository.getOrdersLeftAfterGivenTimeByPartnerId(currTime, partnerId);
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId){
        int time = repository.getLastDeliveryTimeByPartnerId(partnerId);

        String HH = "";
        String MM = "";

        int hour = time / 60;
        int minutes = time % 60;

        HH += ((hour > 9) ? hour : "0"+hour);
        MM += ((minutes > 9) ? minutes : "0"+minutes);

        return HH +":"+MM;
    }
    public void deletePartnerById(String partnerId){
        repository.deletePartnerById(partnerId);
    }
    public void deleteOrderById(String orderId){
        repository.deleteOrderById(orderId);
    }
}
