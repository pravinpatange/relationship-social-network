package io.relationship.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import jakarta.validation.constraints.Size;

public class UserDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponse {
        private Long          id;
        private String        username;
        private String        email;
        private String        displayName;
        private String        profilePictureUrl;
        private String        bio;
        private String        location;
        private String        website;
        private UserEntity.AccountType accountType;
        private LocalDateTime createdAt;
    }



    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProfileRequest {

        @Size(max = 100, message = "Display name must be at most 100 characters")
        private String displayName;

        @Size(max = 500, message = "Bio must be at most 500 characters")
        private String bio;

        @Size(max = 100, message = "Location must be at most 100 characters")
        private String location;

        @Size(max = 255, message = "Website must be at most 255 characters")
        private String website;

        private String      profilePictureUrl;
        private UserEntity.AccountType accountType;
    }
}
