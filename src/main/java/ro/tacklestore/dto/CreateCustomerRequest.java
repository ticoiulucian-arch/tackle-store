package ro.tacklestore.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateCustomerRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank @Email
    private String email;

    private String phone;
    private String address;
    private String city;
    private String postalCode;
    private String country;
}

