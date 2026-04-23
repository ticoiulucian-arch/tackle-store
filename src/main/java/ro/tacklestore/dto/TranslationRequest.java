package ro.tacklestore.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TranslationRequest {
    private String name;
    private String description;
}

