package com.mindhub.product_service.controllers;

import com.mindhub.product_service.Services.ProductService;
import com.mindhub.product_service.exceptions.ProductException;
import com.mindhub.product_service.models.NewProduct;
import com.mindhub.product_service.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/API/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<Set<Product>> getAllProducts() {
        Set<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) throws ProductException {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody NewProduct newProduct) throws ProductException {
        Product product = productService.createProduct(newProduct);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody NewProduct newProduct) throws ProductException {
        Product updatedProduct = productService.updateProduct(id, newProduct);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) throws ProductException {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

}
