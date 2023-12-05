# csvMySqlAPIApp

Loading a database having as source data a csv file

## Versions used to develop this application

**mvn --version**  Apache Maven 3.5.4

**java -version**  openjdk version "17.0.2" 2022-01-18

## To run the application:

Run:
```
mvn clean install
```

Add the file's path as argument.
CSV file path on this example location is: **src/main/resources/customers.csv**
```
mvn spring-boot:run -D"spring-boot.run.arguments=src/main/resources/customers.csv"
```
## Test endpoints
### Get customer by customer's reference:
http://localhost:8090/customer/get?customerRef=3

### Save Customer into the database:
http://localhost:8090/customer/save

Example of json to use:
```
{
    "customerRef": 1,
    "name": "Michael Scott",
    "addressLine1": "1725 Slough Avenue",
    "addressLine2": "Scranton Business Park",
    "town": "Pennsylvania",
    "county": "Lackawanna",
    "country": "USA",
    "postCode": "18505"
}
```
### Save Customer into the database with wrong format:
http://localhost:8090/customer/save

Example of json to use:
```
{
    "addressLine1": "1727 Slough Avenue",
    "addressLine2": "Scranton Business Park",
    "town": "Pennsylvania",
    "county": "Lackawanna",
    "country": "USA",
    "postCode": "18505"
}
```
Output:
```
{
    "errors": [
        "The Name is required.",
        "Reference must be greater than or equal to 1."
    ]
}
```
