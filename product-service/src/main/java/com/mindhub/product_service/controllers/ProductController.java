package com.mindhub.product_service.controllers;

import com.mindhub.product_service.Services.ProductService;
import com.mindhub.product_service.dtos.ProductRecord;
import com.mindhub.product_service.exceptions.ProductException;
import com.mindhub.product_service.dtos.ExistentProductsRecord;
import com.mindhub.product_service.dtos.NewProduct;
import com.mindhub.product_service.models.Product;
import com.mindhub.product_service.dtos.ProductQuantityRecord;
import com.mindhub.product_service.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/API/products")
public class ProductController {
    @Autowired
    ProductService productService;

    /**
     * Retrieve all products.
     * @return A {@link ResponseEntity} containing a set of {@link Product} objects.
     * @response 200 OK - List of all products.
     */
    @GetMapping("/public")
    public ResponseEntity<Set<ExistentProductsRecord>> getAllProducts() {
        Set<ExistentProductsRecord> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Retrieve a product by its ID.
     * @param id The ID of the product to retrieve.
     * @return A {@link ResponseEntity} containing the requested {@link Product}.
     * @throws ProductException If the product does not exist.
     * @response 200 OK - The requested product.
     * @response 404 NOT FOUND - If the product is not found.
     */
    @GetMapping("/public/{id}")
    public ResponseEntity<ProductRecord> getProductById(@PathVariable Long id) throws ProductException {
        ProductRecord product = productService.getDataProductById(id);
        return ResponseEntity.ok(product);
    }
    /**
     * Create a new product.
     * @param newProduct The {@link NewProduct} object containing product details.
     * @return A {@link ResponseEntity} containing the created {@link Product}.
     * @throws ProductException If the product cannot be created.
     * @response 200 OK - The created product.
     * @response 400 Bad gateway - If the input data its invalid
     */
    @PostMapping("/admin")
    public ResponseEntity<Product> createProduct(@RequestBody NewProduct newProduct) throws ProductException {
        Product product = productService.createProduct(newProduct);
        return ResponseEntity.ok(product);
    }

    /**
     * Update an existing product.
     * @param id The ID of the product to update.
     * @param newProduct The {@link NewProduct} object containing updated product details.
     * @return A {@link ResponseEntity} containing the updated {@link Product}.
     * @throws ProductException If the product cannot be updated or does not exist.
     * @response 200 OK - The updated product.
     * @response 400 Bad gateway - If the input data its invalid
     */
    @PutMapping("/admin/{id}")
    public ResponseEntity<ExistentProductsRecord> updateProduct(@PathVariable Long id, @RequestBody NewProduct newProduct) throws ProductException {
        ExistentProductsRecord updatedProduct = productService.updateProduct(id, newProduct);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Delete a product by its ID.
     * @param id The ID of the product to delete.
     * @return A {@link ResponseEntity} with no content.
     * @throws ProductException If the product does not exist or cannot be deleted.
     * @response 204 NO CONTENT - Product successfully deleted.
     * @response 404 NOT FOUND - If the product is not found.
     */
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) throws ProductException {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/private")
    public ResponseEntity<HashMap<Long, Integer>> existsProducts(@RequestBody List<ProductQuantityRecord> recordList){
        HashMap<Long, Integer> products = productService.getAllAvailableProducts(recordList);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/private/to-order")
    public ResponseEntity<String> existProduct(@RequestBody List<ProductQuantityRecord> quantityRecord) throws ProductException {
        productService.updateProductsQuantity(quantityRecord);
        return new ResponseEntity<String>(Constants.UPDATED_PDT, HttpStatus.OK);
    }


}
