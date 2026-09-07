package se.jennifer.customerservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class BookingClient {

    private final RestClient restClient;

    public BookingClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://localhost:8081").build();
    }

    public boolean hasActiveBookings(Long customerId) {

        try {
            Boolean resuilt = restClient.get().uri(
                    "/bookings/customer/{customerId/active}", customerId).retrieve().body(Boolean.class);

            return Boolean.TRUE.equals(resuilt);
        } catch (Exception e) {
            throw new RuntimeException("Booking service is not available, please try again later");
        }
    }
}
