package se.jennifer.customerservice.dto;

public record CreateCustomerRequest(
        String firstname,
        String lastname,
        String email,
        String phone,
        String password
) {}

