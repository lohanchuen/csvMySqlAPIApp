package com.esg.interviewprocess.controller;

import com.esg.interviewprocess.dto.CustomerDto;
import com.esg.interviewprocess.exception.ObjectNotFoundException;
import com.esg.interviewprocess.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/customer")
@RestController
public class CustomerController
{
    @Autowired
    private CustomerService service;

    @GetMapping("/get")
    public CustomerDto getCustomer(int customerRef) throws ObjectNotFoundException
    {
        return service.getCustomer(customerRef);
    }

    @PostMapping("/save")
    public CustomerDto saveCustomer(@Valid @RequestBody CustomerDto customer)
    {
        return service.saveCustomer(customer);
    }
}
