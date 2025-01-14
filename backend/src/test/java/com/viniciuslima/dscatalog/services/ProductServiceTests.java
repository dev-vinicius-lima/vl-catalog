package com.viniciuslima.dscatalog.services;


import com.viniciuslima.dscatalog.repositories.ProductRepository;
import com.viniciuslima.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    public ProductService service;

    @Mock
    public ProductRepository repository;

    public long existingId;
    public long nonExistingId;

    @BeforeEach
    void setUp() {
        existingId = 1;
        nonExistingId = 10000;

        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.doNothing().when(repository).deleteById(existingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> service.delete(existingId));

        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);

    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingId));

        Mockito.verify(repository, Mockito.times(1)).existsById(nonExistingId);
    }

}
