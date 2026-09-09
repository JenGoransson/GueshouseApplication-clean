package se.jennifer.bookingservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.jennifer.bookingservice.booking.client.CustomerClient;
import se.jennifer.bookingservice.dto.CustomerDto;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerClient customerClient;

    @Test
    void shouldReturnRoomsSuccessfully() throws Exception {

        mockMvc.perform(get("/rooms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateBookingSuccessfully() throws Exception {
    when(customerClient
            .getCustomerById(anyLong(), isNull()))
            .thenReturn(new CustomerDto(1L,
                    "Marcus", "Viklund", "marcus.viklund@gmail.com", "123456789"));
        String bookingJson = """
            {
                "customerId": 1,
                "roomId": 1,
                "startDate": "2026-12-01",
                "endDate": "2026-12-05"
            }
        """;

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnConflictWhenRoomIsDoubleBooked() throws Exception {
        when(customerClient
                .getCustomerById(anyLong(), isNull()))
                .thenReturn(new CustomerDto(2L,
                        "test", "customer", "test.customer@gmail.com", "987654321"));
        String firstBookingJson = """
                {
                "customerId": 2,
                "roomId": 1,
                "startDate": "2026-12-01",
                "endDate": "2026-12-05"
                }
                """;
        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firstBookingJson)).andExpect(status().isCreated());

        String doubleBookingJson = """
            {
                "customerId": 2,
                "roomId": 1,
                "startDate": "2026-12-01",
                "endDate": "2026-12-05"
            }
        """;

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(doubleBookingJson))
                .andExpect(status().isConflict());
    }
}