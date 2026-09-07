package se.jennifer.customerservice.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.jennifer.customerservice.dto.LoginRequest;
import se.jennifer.customerservice.error.BadRequest;
import se.jennifer.customerservice.model.Customer;
import se.jennifer.customerservice.repository.CustomerRepo;

@RestController
@RequestMapping("/customers")
public class LoginController {

    private final CustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder;

    public LoginController(CustomerRepo customerRepo, PasswordEncoder passwordEncoder) {
        this.customerRepo = customerRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public Customer login(@RequestBody LoginRequest request) {
        Customer customer = customerRepo
                .findByEmail(request.email())
                .orElseThrow(() -> new BadRequest("Invalid email or password"));

        boolean passwordMatches = passwordEncoder.matches(request.password(), customer.getPasswordHash());

        if (!passwordMatches) {
            throw new BadRequest("Invalid email or password");
        }

        return customer; // frontend får kundobjektet direkt
    }
}
