package pe.edu.upc.billmind_backend.auth.utils;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RegisterRequest {
    private String name;

    private String last_name;

    private String phone;

    private String mail;

    private String password;
}
