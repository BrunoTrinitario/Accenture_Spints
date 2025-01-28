package com.mindhub.product_service.dtos;

public record ProductRecord(Long id,String name,String description, Double price, Integer quantity) {
}
