package com.mindhub.product_service.Services;

import com.mindhub.product_service.exceptions.ProductException;
import com.mindhub.product_service.models.ExistentProductsRecord;
import com.mindhub.product_service.models.NewProduct;
import com.mindhub.product_service.models.Product;
import com.mindhub.product_service.models.ProductQuantityRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface ProductService {
    Set<Product> getAllProducts();
    Product getProductById(Long id) throws ProductException;
    Product createProduct(NewProduct newProduct) throws ProductException;
    Product updateProduct(Long id, NewProduct newProduct) throws ProductException;
    void deleteProductById(Long id) throws ProductException;
    boolean existsProductById(Long id);
    boolean existsProductByName(String name);
    Long getIdByName(String name) throws ProductException;
    List<ExistentProductsRecord> getAllAvailableProducts(List<ProductQuantityRecord> productQuantityRecordList);
}
