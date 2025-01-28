package com.mindhub.order_service.repositories;

import com.mindhub.order_service.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

    void deleteById(Long id);
}
