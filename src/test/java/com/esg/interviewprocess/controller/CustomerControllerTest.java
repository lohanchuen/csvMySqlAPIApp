package com.esg.interviewprocess.controller;

import com.esg.interviewprocess.dto.CustomerDto;
import com.esg.interviewprocess.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class CustomerControllerTest
{
    private static final String CUSTOMER_REF = "customerRef";
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private CustomerController controller;

    @Mock
    private CustomerService service;

    @Mock
    private CustomerDto customer;


    @BeforeEach
    public void setup()
    {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getCustomerShouldReturnCustomer() throws Exception
    {
        when(service.getCustomer(anyInt())).thenReturn(customer);

        mockMvc.perform(get("/customer/get")
                        .param(CUSTOMER_REF, Integer.toString(anyInt()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customer)));
    }

    @Test
    void saveCustomerShouldCallRepositoryToSaveCustomer() throws Exception
    {
        CustomerDto newCustomer = new CustomerDto();
        newCustomer.setCustomerRef(1234);
        newCustomer.setName("Name");

        when(service.saveCustomer(any(CustomerDto.class))).thenReturn(newCustomer);

        mockMvc.perform(post("/customer/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCustomer)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(newCustomer)));

        verify(service, times(1)).saveCustomer(any(CustomerDto.class));
    }

    @Test
    void saveCustomerShouldNotCallRepositoryIfRequestInvalid() throws Exception
    {
        CustomerDto newCustomer = new CustomerDto();
        newCustomer.setCustomerRef(1234);

        mockMvc.perform(post("/customer/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCustomer)))
                .andExpect(status().isBadRequest());

        verify(service, never()).saveCustomer(any(CustomerDto.class));
    }
}
