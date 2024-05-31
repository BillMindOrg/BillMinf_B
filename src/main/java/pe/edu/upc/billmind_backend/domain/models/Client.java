package pe.edu.upc.billmind_backend.domain.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pe.edu.upc.billmind_backend.auth.entities.ClientRole;
import pe.edu.upc.billmind_backend.auth.entities.RefreshToken;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Client")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String last_name;

    @Column(nullable = false, unique = true)
    @Email(message = "Email should be valid")
    private String mail;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, unique = true)
    @Size(min = 8, message = "Password should have at least 8 characters")
    private String password;

    @OneToOne(mappedBy = "client")
    private RefreshToken refreshToken;

    @Enumerated(EnumType.STRING)
    private ClientRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return mail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
