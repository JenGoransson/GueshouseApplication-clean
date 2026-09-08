package se.jennifer.bookingservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnRoomsSuccessfully() throws Exception {

        mockMvc.perform(get("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateBookingSuccessfully() throws Exception {

        String bookingJson = """
            {
                "customerId": 1,
                "roomId": 101,
                "startDate": "2026-12-01",
                "endDate": "2026-12-05"
            }
        """;

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnConflictWhenRoomIsDoubleBooked() throws Exception {

        String doubleBookingJson = """
            {
                "customerId": 2,
                "roomId": 101,
                "startDate": "2026-12-01",
                "endDate": "2026-12-05"
            }
        """;

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(doubleBookingJson))
                .andExpect(status().isConflict());
    }
}