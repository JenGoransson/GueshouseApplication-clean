package se.jennifer.bookingservice.dto;

public record CustomerDto(
        Long id,
        String firstname,
        String lastname,
        String email
) {}

