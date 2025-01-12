package com.viniciuslima.dscatalog.tests;

import com.viniciuslima.dscatalog.dto.ProductDTO;
import com.viniciuslima.dscatalog.entities.Category;
import com.viniciuslima.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct() {
        Product product = new Product(1L, "Phone", "Description product", 800.0, "http://github.com/profile", Instant.now());
        product.getCategories().add((new Category(2L, "Electronics")));
        return product;
    }

    public static ProductDTO createProductDto() {
        return new ProductDTO(createProduct(), createProduct().getCategories());
    }

}
