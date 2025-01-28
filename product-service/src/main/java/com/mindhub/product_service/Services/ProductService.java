package com.mindhub.product_service.Services;

import com.mindhub.product_service.dtos.ProductRecord;
import com.mindhub.product_service.exceptions.ProductException;
import com.mindhub.product_service.dtos.ExistentProductsRecord;
import com.mindhub.product_service.dtos.NewProduct;
import com.mindhub.product_service.models.Product;
import com.mindhub.product_service.dtos.ProductQuantityRecord;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public interface ProductService {
    Set<ExistentProductsRecord> getAllProducts();
    Product getProductById(Long id) throws ProductException;
    ProductRecord getDataProductById(Long id) throws ProductException;
    Product createProduct(NewProduct newProduct) throws ProductException;
    ExistentProductsRecord updateProduct(Long id, NewProduct newProduct) throws ProductException;
    void deleteProductById(Long id) throws ProductException;
    boolean existsProductById(Long id);
    boolean existsProductByName(String name);
    Long getIdByName(String name) throws ProductException;
    HashMap<Long, Integer> getAllAvailableProducts(List<ProductQuantityRecord> productQuantityRecordList);
    ExistentProductsRecord getOneAvailableProduct(ProductQuantityRecord quantityRecord) throws ProductException;
    public void updateProductsQuantity(List<ProductQuantityRecord> quantityRecord);
    void updateProductQuantity(Long idProduct, Integer quantity) throws ProductException;
}
