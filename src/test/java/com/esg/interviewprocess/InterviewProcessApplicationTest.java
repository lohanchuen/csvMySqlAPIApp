package com.esg.interviewprocess;

import com.esg.interviewprocess.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class InterviewProcessApplicationTest
{
    @Mock
    private ApplicationContext appContext;

    @Mock
    private CustomerService service;

    @InjectMocks
    private InterviewProcessApplication app;


    @Test
    void runShouldCallLoadCustomersFromFileWhenArgsIsPresent() throws Exception
    {
        when(appContext.getBean(CustomerService.class)).thenReturn(service);
        String[] args = {"path"};

        app.run(args);

        verify(appContext, times(1)).getBean(CustomerService.class);
        verify(service, times(1)).loadCustomersFromFile("path");
    }

    @Test
    void runShouldNotCallLoadCustomersFromFileWhenArgsIsEmpty() throws Exception
    {
        String[] args = {""};

        app.run(args);

        verify(appContext, never()).getBean(CustomerService.class);
        verify(service, never()).loadCustomersFromFile("path");
    }

    @Test
    void runShouldNotCallLoadCustomersFromFileWhenNoArgs() throws Exception
    {
        app.run();

        verify(appContext, never()).getBean(CustomerService.class);
        verify(service, never()).loadCustomersFromFile("path");
    }

    @Test
    void restTemplateShouldReturnRestTemplate()
    {
        RestTemplate restTemplate = app.restTemplate();

        assertNotNull(restTemplate, "RestTemplate should not be null");
    }
}
