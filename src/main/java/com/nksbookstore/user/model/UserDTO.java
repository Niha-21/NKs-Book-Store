package com.nksbookstore.user.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    
    private Long id;
    
    @NotBlank
    private String username;

    private String email;

    @NotBlank
    private String password;

}
