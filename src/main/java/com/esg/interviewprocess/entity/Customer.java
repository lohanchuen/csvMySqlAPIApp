package com.esg.interviewprocess.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "customers")
public class Customer
{
    @Id
    @NonNull
    private int customerRef;

    @NonNull
    private String name;
    private String addressLine1;
    private String addressLine2;
    private String town;
    private String county;
    private String country;
    private String postCode;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerRef == customer.customerRef && Objects.equals(name, customer.name) && Objects.equals(addressLine1, customer.addressLine1) && Objects.equals(addressLine2, customer.addressLine2) && Objects.equals(town, customer.town) && Objects.equals(county, customer.county) && Objects.equals(country, customer.country) && Objects.equals(postCode, customer.postCode);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(customerRef, name, addressLine1, addressLine2, town, county, country, postCode);
    }
}
