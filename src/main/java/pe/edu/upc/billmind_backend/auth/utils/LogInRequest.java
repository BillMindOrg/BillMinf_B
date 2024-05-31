package pe.edu.upc.billmind_backend.auth.utils;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogInRequest {
    private String mail;

    private String password;
}
