package com.esg.interviewprocess.service.impl;

import com.esg.interviewprocess.dto.CustomerDto;
import com.esg.interviewprocess.entity.Customer;
import com.esg.interviewprocess.exception.ObjectNotFoundException;
import com.esg.interviewprocess.mapper.CustomerMapper;
import com.esg.interviewprocess.repository.CustomerRepository;
import com.esg.interviewprocess.service.CustomerService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Service
public class CustomerServiceImpl implements CustomerService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
    protected static final String CUSTOMER_REF = "Customer Ref";
    protected static final String CUSTOMER_NAME = "Customer Name";
    protected static final String ADDRESS_LINE_1 = "Address Line 1";
    protected static final String ADDRESS_LINE_2 = "Address Line 2";
    protected static final String TOWN = "Town";
    protected static final String COUNTY = "County";
    protected static final String COUNTRY = "Country";
    protected static final String POSTCODE = "Postcode";

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CustomerMapper mapper;

    @Value("${api.customer.save.url}")
    private String apiCustomerSaveUrl;


    @Override
    public CustomerDto getCustomer(int customerRef) throws ObjectNotFoundException
    {
        return repository.findById(customerRef)
                .map(customer -> mapper.mapDto(customer))
                .orElseThrow(() -> new ObjectNotFoundException("Customer"));
    }

    @Override
    public CustomerDto saveCustomer(CustomerDto customer)
    {
        Customer customerEntity = mapper.mapEntity(customer);
        Customer customerSaved = repository.save(customerEntity);
        CustomerDto returnDto = mapper.mapDto(customerSaved);

        return returnDto;
    }

    @Override
    public void loadCustomersFromFile(String filePath)
    {
        Iterable<CSVRecord> csvRecords = readCsvRecords(filePath);
        if (csvRecords != null)
        {
            for (CSVRecord csvRecord : csvRecords)
            {
                CustomerDto customer = parseCustomer(csvRecord);
                try
                {
                    restTemplate.postForEntity(apiCustomerSaveUrl, customer, CustomerDto.class);
                } catch (Exception e)
                {
                    LOGGER.warn("Customer {} not created", customer.getCustomerRef());
                    e.printStackTrace();
                }
            }
        }
    }

    protected Iterable<CSVRecord> readCsvRecords(String filePath)
    {
        try (BufferedReader reader = getBufferedReader(filePath))
        {
            CSVParser csvParser = getCSVParser(reader);
            return csvParser.getRecords();
        } catch (IOException e)
        {
            LOGGER.error("File {} not found", filePath, e);
        }
        return null;
    }

    protected CSVParser getCSVParser(BufferedReader reader) throws IOException
    {
        return new CSVParser(reader, CSVFormat.EXCEL.withHeader());
    }

    protected BufferedReader getBufferedReader(String filePath) throws FileNotFoundException
    {
        return new BufferedReader(new FileReader(filePath));
    }

    protected CustomerDto parseCustomer(CSVRecord csvRecord)
    {
        return new CustomerDto(
                Integer.parseInt(csvRecord.get(CUSTOMER_REF)),
                csvRecord.get(CUSTOMER_NAME),
                csvRecord.get(ADDRESS_LINE_1),
                csvRecord.get(ADDRESS_LINE_2),
                csvRecord.get(TOWN),
                csvRecord.get(COUNTY),
                csvRecord.get(COUNTRY),
                csvRecord.get(POSTCODE)
        );
    }
}
