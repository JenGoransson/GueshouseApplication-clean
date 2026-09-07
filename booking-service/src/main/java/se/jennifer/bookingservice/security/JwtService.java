package se.jennifer.bookingservice.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final String secret =
            "uQ9m2rX4t8B1cL0pV7sE3kN9yF5hT2wZ6qR8bC1dM4xP7vJ0lH3fG9sK2tW5yA8"; //denna bör ligga i application.properties

    public String generateToken(String username){
        return Jwts.builder()
                .subject(username) //För vem
                .issuedAt(new Date()) //När skapades den
                .expiration(new Date(System.currentTimeMillis() + 600000)) //hur länge är tokenet giltlig
                .signWith(getSignInKey())  //signeras med vår hemliga nyckel så att ingen kan manipulera det.
                .compact(); //Gör om allt till en färdig textstring som skickas till klienten
    }

    // Plockar ut "subject" (användarnamnet) från tokenet.
    // Detta fungerar bara om tokenet är korrekt signerat med vår hemliga nyckel.
    // Om signaturen inte matchar → kastas ett exception.
    public String extractUsername(String token){
        return Jwts.parser()
                .verifyWith(getSignInKey())  //veriferiar signaturen
                .build()
                .parseSignedClaims(token)
                .getPayload() //läser payloaden(datan som ligger i signaturen)
                .getSubject();
    }

    // Kontrollerar om tokenet är giltigt.
    // Vi försöker extrahera användarnamnet — om det fungerar är tokenet OK.
    // Om något går fel (t.ex. fel signatur, utgånget token, korrupt token)
    // fångas felet och vi returnerar false.
    public boolean isTokenValid(String token){
        try {
            extractUsername(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    private SecretKey getSignInKey(){  //Metod som returnerar en secretKey
        byte [] bytes = secret.getBytes(StandardCharsets.UTF_8); //Gör om vår textstring till bytes
        return Keys.hmacShaKeyFor(bytes);  //Bygger lösenordet till rätt nyckelobjekt så den passar
    }

}

