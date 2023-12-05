package com.esg.interviewprocess.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class CustomerDto
{
    @NotNull(message = "The customerRef is required.")
    @Min(value = 1, message = "Reference must be greater than or equal to 1.")
    public int customerRef;

    @NotBlank(message = "The Name is required.")
    public String name;
    public String addressLine1;
    public String addressLine2;
    public String town;
    public String county;
    public String country;
    public String postCode;
}
