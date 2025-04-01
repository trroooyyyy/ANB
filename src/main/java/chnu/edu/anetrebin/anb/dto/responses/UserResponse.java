package chnu.edu.anetrebin.anb.dto.responses;

import chnu.edu.anetrebin.anb.model.User;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserResponse(
        Long id,
        String login,
        String name,
        String surname,
        String email,
        String phone,
        LocalDate dateOfBirth
) {
    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .login(user.getLogin())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }
}

