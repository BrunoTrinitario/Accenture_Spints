package com.mindhub.product_service.Services.imp;

import com.mindhub.product_service.Repositories.ProductRepository;
import com.mindhub.product_service.Services.ProductService;
import com.mindhub.product_service.exceptions.ProductException;
import com.mindhub.product_service.models.NewProduct;
import com.mindhub.product_service.models.Product;
import com.mindhub.product_service.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Set<Product> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        Set<Product> productSet = productList.stream().map(product -> product).collect(Collectors.toSet());
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
    public Product updateProduct(Long id, NewProduct newProduct) throws ProductException {
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

        return product;
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
}
