package se.jennifer.customerservice.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.jennifer.customerservice.dto.LoginRequest;
import se.jennifer.customerservice.dto.LoginResponse;
import se.jennifer.customerservice.error.BadRequest;
import se.jennifer.customerservice.model.Customer;
import se.jennifer.customerservice.repository.CustomerRepo;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final CustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwTService jwTService;

    public LoginController(CustomerRepo customerRepo, PasswordEncoder passwordEncoder, JwTService jwTService) {
        this.customerRepo = customerRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwTService = jwTService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        Customer customer = customerRepo
                .findByEmail(request.email()).orElseThrow(() -> new BadRequest("Invalid email or passowrd"));

        boolean passwordMatches = passwordEncoder.matches(request.password(), customer.getPasswordHash());

        if (!passwordMatches) {
            throw new BadRequest("Invalid email or password");
        }
        String token = jwTService.generateToken(customer.getId(),customer.getEmail());

        return new LoginResponse(token);
    }
}
