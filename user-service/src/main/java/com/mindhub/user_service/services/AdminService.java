package com.mindhub.user_service.services;

import com.mindhub.user_service.dtos.*;
import com.mindhub.user_service.exceptions.UserException;

import java.util.Set;

public interface AdminService {
    Set<OrderDTO> getAllOrders();
    Set<ExistentProductsRecord> getAllProducts();
    void deleteProduct(Long id);
    void deleteOrder(Long id);
    ExistentProductsRecord updateProduct(Long id, NewProduct newProduct);
}
