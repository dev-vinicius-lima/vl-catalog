package com.viniciuslima.dscatalog.repositories;

import com.viniciuslima.dscatalog.entities.Product;
import com.viniciuslima.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long noExistingId;
    private long countTotalProducts = 25L;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        noExistingId = 1000L;
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, repository.count());
    }

    @Test
    public void deleteShoulddeleteObjectWhenIdExists() {
        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnEmptyWhenIdDoesNotExist() {
        Optional<Product> result = repository.findById(noExistingId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findByIdShouldReturnNoEmptyWhenIdExists() {
        Optional<Product> result = repository.findById(existingId);

        Assertions.assertTrue(result.isPresent());
    }
}
