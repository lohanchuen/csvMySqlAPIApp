package com.esg.interviewprocess.mapper;

import com.esg.interviewprocess.dto.CustomerDto;
import com.esg.interviewprocess.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper
{
    CustomerDto mapDto(Customer customer);

    Customer mapEntity(CustomerDto customer);
}
