package hello.security.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.function.Supplier;

@Entity
@Data
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;
    private LocalDateTime createDate;

    private String provider;
    private String providerId;

    @Builder
    public User(String username,
                String password,
                String email,
                String role,
                LocalDateTime createDate,
                String provider,
                String providerId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.createDate = createDate;
        this.provider = provider;
        this.providerId = providerId;
    }

    public User(){}


    public static User createUser(String username, String email, String password, String role, String provider, String providerId) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setProvider(provider);
        user.setProviderId(providerId);

        return user;

    }
}


