package com.example.demo.order.repository;

import com.example.demo.order.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
//    @Override
//    static List<Orders> findAll();
}
