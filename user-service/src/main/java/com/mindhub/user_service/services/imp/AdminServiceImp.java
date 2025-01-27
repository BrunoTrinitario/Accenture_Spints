package com.mindhub.user_service.services.imp;

import com.mindhub.user_service.dtos.ExistentProductsRecord;
import com.mindhub.user_service.dtos.NewProduct;
import com.mindhub.user_service.dtos.OrderDTO;
import com.mindhub.user_service.exceptions.ServicesException;
import com.mindhub.user_service.services.AdminService;
import com.mindhub.user_service.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

@Service
public class AdminServiceImp implements AdminService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ORDERS_PATH}")
    private String orderPath;

    @Value("${PRODUCTS_PATH}")
    private String productPath;

    @Override
    public Set<OrderDTO> getAllOrders() {
        try{
            Set<OrderDTO> orders = restTemplate.getForObject(orderPath,Set.class);
            return orders;
        } catch (RestClientException e) {
            throw new ServicesException(Constants.UNAVAILABLE_ORDER_SERVICE, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @Override
    public Set<ExistentProductsRecord> getAllProducts() {
        try{
            Set<ExistentProductsRecord> products = restTemplate.getForObject(productPath,Set.class);
            return products;
        } catch (RestClientException e) {
            throw new ServicesException(Constants.UNAVAILABLE_PRODUCT_SERVICE, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }


    @Override
    public void deleteProduct(Long id) {
        try{
            String uri = productPath + "/" + id;
            restTemplate.delete(uri);
        } catch (RestClientException e) {
            throw new ServicesException(Constants.UNAVAILABLE_PRODUCT_SERVICE, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @Override
    public void deleteOrder(Long id) {
        try{
            String uri = orderPath + "/" + id;
            restTemplate.delete(uri);
        } catch (RestClientException e) {
            throw new ServicesException(Constants.UNAVAILABLE_PRODUCT_SERVICE, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @Override
    public ExistentProductsRecord updateProduct(Long id, NewProduct newProduct) {
        try{
            String uri = productPath + "/" + id;
            ParameterizedTypeReference<ExistentProductsRecord> responseType = new ParameterizedTypeReference<>() {};
            HttpEntity<NewProduct> httpEntity = new HttpEntity<>(newProduct);
            ResponseEntity<ExistentProductsRecord> responseEntity = restTemplate.exchange(productPath, HttpMethod.PUT ,httpEntity, responseType);
            return responseEntity.getBody();
        } catch (RestClientException e) {
            if (e instanceof HttpStatusCodeException){
                HttpStatusCodeException aux = (HttpStatusCodeException)e;
                throw new ServicesException(Constants.PDT_NOT_FOUND, (HttpStatus) aux.getStatusCode());
            }else{
                throw new ServicesException(Constants.UNAVAILABLE_PRODUCT_SERVICE, HttpStatus.SERVICE_UNAVAILABLE);
            }

        }
    }
}
