package com.esg.interviewprocess.service;

import com.esg.interviewprocess.dto.CustomerDto;
import com.esg.interviewprocess.exception.ObjectNotFoundException;

public interface CustomerService
{
    CustomerDto getCustomer(int customerRef) throws ObjectNotFoundException;

    CustomerDto saveCustomer(CustomerDto customer);

    void loadCustomersFromFile(String filePath);
}
