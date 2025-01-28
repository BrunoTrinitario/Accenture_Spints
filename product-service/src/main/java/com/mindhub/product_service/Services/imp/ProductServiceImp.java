package com.mindhub.product_service.Services.imp;

import com.mindhub.product_service.Repositories.ProductRepository;
import com.mindhub.product_service.Services.ProductService;
import com.mindhub.product_service.exceptions.ProductException;
import com.mindhub.product_service.dtos.ExistentProductsRecord;
import com.mindhub.product_service.dtos.NewProduct;
import com.mindhub.product_service.models.Product;
import com.mindhub.product_service.dtos.ProductQuantityRecord;
import com.mindhub.product_service.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Set<ExistentProductsRecord> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        Set<ExistentProductsRecord> productSet = productList.stream().map(product -> new ExistentProductsRecord(product.getId(),product.getPrice(),product.getStock())).collect(Collectors.toSet());
        return productSet;
    }

    @Override
    public Product getProductById(Long id) throws ProductException {
        Product product = productRepository.findById(id).orElseThrow(()->new ProductException(Constants.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));
        return product;
    }

    @Override
    public Product createProduct(NewProduct newProduct) throws ProductException {
        validateName(newProduct.name());
        validatePrice(newProduct.price());
        validateStock(newProduct.stock());
        Product product = new Product(newProduct.name(), newProduct.description(), newProduct.price(), newProduct.stock());
        product = productRepository.save(product);
        return product;
    }

    @Override
    public ExistentProductsRecord updateProduct(Long id, NewProduct newProduct) throws ProductException {
        validatePrice(newProduct.price());
        validateStock(newProduct.stock());
        Product product = productRepository.findById(id).orElseThrow(()->new ProductException(Constants.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (newProduct.description()!=null && !newProduct.description().isBlank())
            product.setDescription(newProduct.description());
        if (newProduct.price()!=null){
            product.setPrice(newProduct.price());
        }
        if (newProduct.stock()!=null){
            product.setStock(newProduct.stock());
        }
        if (newProduct.name()!=null && !newProduct.name().equals(product.getName())){
            validateName(newProduct.name());
            product.setName(newProduct.name());
        }
        product = productRepository.save(product);

        return new ExistentProductsRecord(product.getId(), product.getPrice(), product.getStock());
    }

    @Override
    public void deleteProductById(Long id) throws ProductException {
        if (productRepository.existsById(id)){
            productRepository.deleteById(id);
        }else{
            throw new ProductException(Constants.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public boolean existsProductById(Long id) {
        return productRepository.existsById(id);
    }

    @Override
    public boolean existsProductByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public Long getIdByName(String name) throws ProductException {
        Product product = productRepository.findByName(name).orElseThrow(()->new ProductException(Constants.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));
        return product.getId();
    }

    @Override
    public HashMap<Long, Integer> getAllAvailableProducts(List<ProductQuantityRecord> productQuantityRecordList){
        HashMap<Long, Integer> availableProductMap = new HashMap<>();

        productQuantityRecordList.forEach( product -> {
            try{
                Product aux = getProductById(product.id());
                availableProductMap.put(aux.getId(), aux.getStock());
            }catch (ProductException e){

            }
        });
        return availableProductMap;
    }

    @Override
    public ExistentProductsRecord getOneAvailableProduct(ProductQuantityRecord quantityRecord){
        try {
            Product product = getProductById(quantityRecord.id());
            if (product.getStock()>= quantityRecord.quantity()){
                product.setStock(product.getStock()-quantityRecord.quantity());
                productRepository.save(product);
                return new ExistentProductsRecord(product.getId(), product.getPrice(), quantityRecord.quantity());
            }else{
                return new ExistentProductsRecord(product.getId(), null, quantityRecord.quantity());
            }
        } catch (ProductException e) {
            return null;
        }
    }

    private void validateName(String name) throws ProductException {
        if (existsProductByName(name)){
            throw new ProductException(Constants.PRODUCT_EXISTS,HttpStatus.CONFLICT);
        }else{
            if (name!=null && name.isBlank()){
                throw new ProductException(Constants.INVALID_NAME);
            }
        }
    }

    private void validatePrice(Double price) throws ProductException {
        if (price!=null && price<0){
            throw new ProductException(Constants.INVALID_PRICE);
        }
    }

    private void validateStock(Integer stock) throws ProductException {
        if (stock!=null && stock<0){
            throw new ProductException(Constants.INVALID_STOCK);
        }
    }

    public void updateProductsQuantity(List<ProductQuantityRecord> quantityRecord){
        quantityRecord.forEach(product ->{
            try {
                updateProductQuantity(product.id(), product.quantity());
            } catch (ProductException e) {
            }
        });
    }

    @Override
    public void updateProductQuantity(Long idProduct, Integer quantity) throws ProductException {
        Product product = getProductById(idProduct);
        if (product.getStock()+quantity<0){
            throw new ProductException(Constants.NEGATIVE_STOCK, HttpStatus.NOT_ACCEPTABLE);
        }

        product.setStock(product.getStock()+quantity);
        productRepository.save(product);

    }
}
