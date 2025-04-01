package chnu.edu.anetrebin.anb.service.user;

import chnu.edu.anetrebin.anb.dto.requests.UserRequest;
import chnu.edu.anetrebin.anb.dto.responses.UserResponse;

import java.util.List;

public interface UserService {
    void registerUser(UserRequest userRequest);

    void deleteUser(Long id);

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long id, UserRequest userRequest);

    List<UserResponse> getAllUsers();
}
