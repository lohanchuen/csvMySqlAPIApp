package com.esg.interviewprocess.service.impl;

import com.esg.interviewprocess.dto.CustomerDto;
import com.esg.interviewprocess.entity.Customer;
import com.esg.interviewprocess.exception.ObjectNotFoundException;
import com.esg.interviewprocess.mapper.CustomerMapper;
import com.esg.interviewprocess.repository.CustomerRepository;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest
{
    private static final String CUSTOMER_REF = "1223";
    private static final String PETER = "Peter";
    private static final String SLOUGH_AVENUE = "1725 Slough Avenue";
    private static final String SCRANTON_BUSINESS_PARK = "Scranton Business Park";
    private static final String PENNSYLVANIA = "Pennsylvania";
    private static final String LACKAWANNA = "Lackawanna";
    private static final String USA = "USA";
    private static final String POSTCODE = "18505";

    private static final String API_URL = "https://foo.api.com";

    @Spy
    @InjectMocks
    private CustomerServiceImpl serviceImpl;

    @Mock
    private CustomerRepository repository;

    @Mock
    private CustomerDto customerDto;

    @Mock
    private CustomerMapper mapper;

    @Mock
    private Customer customer;

    @Mock
    private BufferedReader bufferedReader;

    @Mock
    private CSVParser csvParser;

    @Mock
    private CSVRecord csvRecord;

    @Mock
    private RestTemplate restTemplate;

    @Captor
    private ArgumentCaptor<CustomerDto> customerCaptor;

    @BeforeEach
    public void init()
    {
        ReflectionTestUtils.setField(serviceImpl, "apiCustomerSaveUrl", API_URL);
    }

    @Test
    public void getCustomerShouldReturnCustomer() throws ObjectNotFoundException
    {
        when(repository.findById(anyInt())).thenReturn(Optional.of(customer));
        when(mapper.mapDto(customer)).thenReturn(customerDto);

        assertEquals(customerDto, serviceImpl.getCustomer(anyInt()));
    }

    @Test
    public void getCustomerShouldNotReturnCustomer()
    {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class, () ->
        {
            serviceImpl.getCustomer(anyInt());
        });

        Assertions.assertEquals("Object type: CustomerNot found", thrown.getMessage());
    }

    @Test
    void saveCustomer()
    {
        when(mapper.mapEntity(customerDto)).thenReturn(customer);
        when(repository.save(customer)).thenReturn(customer);
        when(mapper.mapDto(customer)).thenReturn(customerDto);

        assertEquals(customerDto, serviceImpl.saveCustomer(customerDto));
    }

    @Test
    void loadCustomersFromFileShouldLoadFileAndCallPostAPI() throws IOException
    {
        doReturn(bufferedReader).when(serviceImpl).getBufferedReader(anyString());
        doReturn(csvParser).when(serviceImpl).getCSVParser(bufferedReader);
        List<CSVRecord> recordList = Collections.singletonList(csvRecord);
        when(csvParser.getRecords()).thenReturn(recordList);
        mockCsvRecordValues();

        serviceImpl.loadCustomersFromFile("path");

        verify(restTemplate, times(1)).postForEntity(eq(API_URL), customerCaptor.capture(), eq(CustomerDto.class));
        assertCustomerData(customerCaptor.getValue());
    }

    private void mockCsvRecordValues()
    {
        when(csvRecord.get(CustomerServiceImpl.CUSTOMER_REF)).thenReturn(CUSTOMER_REF);
        when(csvRecord.get(CustomerServiceImpl.CUSTOMER_NAME)).thenReturn(PETER);
        when(csvRecord.get(CustomerServiceImpl.ADDRESS_LINE_1)).thenReturn(SLOUGH_AVENUE);
        when(csvRecord.get(CustomerServiceImpl.ADDRESS_LINE_2)).thenReturn(SCRANTON_BUSINESS_PARK);
        when(csvRecord.get(CustomerServiceImpl.TOWN)).thenReturn(PENNSYLVANIA);
        when(csvRecord.get(CustomerServiceImpl.COUNTY)).thenReturn(LACKAWANNA);
        when(csvRecord.get(CustomerServiceImpl.COUNTRY)).thenReturn(USA);
        when(csvRecord.get(CustomerServiceImpl.POSTCODE)).thenReturn(POSTCODE);
    }

    private void assertCustomerData(CustomerDto customer)
    {
        assertEquals(Integer.parseInt(CUSTOMER_REF), customer.getCustomerRef());
        assertEquals(PETER, customer.getName());
        assertEquals(SLOUGH_AVENUE, customer.getAddressLine1());
        assertEquals(SCRANTON_BUSINESS_PARK, customer.getAddressLine2());
        assertEquals(PENNSYLVANIA, customer.getTown());
        assertEquals(LACKAWANNA, customer.getCounty());
        assertEquals(USA, customer.getCountry());
        assertEquals(POSTCODE, customer.getPostCode());
    }
}
