package com.viniciuslima.dscatalog.resources;

import com.viniciuslima.dscatalog.dto.ProductDTO;
import com.viniciuslima.dscatalog.services.ProductService;
import com.viniciuslima.dscatalog.services.exceptions.ResourceNotFoundException;
import com.viniciuslima.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductResource.class)
public class ProductResourceTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;
    private long existingId;
    private long nonExistingId;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;
        productDTO = Factory.createProductDto();
        page = new PageImpl<>(List.of(productDTO));

        when(service.findAllPaged(any())).thenReturn(page);

        when(service.findById(existingId)).thenReturn(productDTO);
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockMvc.perform(get("/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        mockMvc.perform(get("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundExceptionWhenIdDoesNotExists() throws Exception {
        mockMvc.perform(get("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
