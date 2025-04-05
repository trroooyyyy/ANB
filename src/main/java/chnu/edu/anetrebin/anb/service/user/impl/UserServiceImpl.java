package chnu.edu.anetrebin.anb.service.user.impl;

import chnu.edu.anetrebin.anb.dto.requests.AccountRequest;
import chnu.edu.anetrebin.anb.dto.requests.UserRequest;
import chnu.edu.anetrebin.anb.dto.responses.UserResponse;
import chnu.edu.anetrebin.anb.exceptions.account.AccountCreationException;
import chnu.edu.anetrebin.anb.exceptions.user.UserNotFoundException;
import chnu.edu.anetrebin.anb.exceptions.user.UserAlreadyExists;
import chnu.edu.anetrebin.anb.model.Account;
import chnu.edu.anetrebin.anb.model.User;
import chnu.edu.anetrebin.anb.repository.AccountRepository;
import chnu.edu.anetrebin.anb.repository.UserRepository;
import chnu.edu.anetrebin.anb.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static chnu.edu.anetrebin.anb.model.Account.generateAccountNumber;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public void registerUser(UserRequest userRequest) {
        if (repository.existsByLogin(userRequest.login())) {
            throw new UserAlreadyExists("User with this login already exists.");
        }

        if (userRequest.email() != null && repository.existsByEmail(userRequest.email())) {
            throw new UserAlreadyExists("User with this email already exists.");
        }

        if (userRequest.phone() != null && repository.existsByPhone(userRequest.phone())) {
            throw new UserAlreadyExists("User with this phone number already exists.");
        }

        User user = User.builder()
                .login(userRequest.login())
                .password(userRequest.password())
                .name(userRequest.name())
                .surname(userRequest.surname())
                .dateOfBirth(userRequest.dateOfBirth())
                .email(userRequest.email())
                .phone(userRequest.phone())
                .build();

        repository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponse getUserById(Long id) {
        return UserResponse.toResponse(repository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponse> getAllUsers() {
        return repository.findAllByOrderByIdAsc().stream().map(UserResponse::toResponse).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (repository.existsByLogin(request.login()) && !user.getLogin().equals(request.login())) {
            throw new UserAlreadyExists("User with this login already exists.");
        }

        if (request.email() != null && repository.existsByEmail(request.email()) && !user.getEmail().equals(request.email())) {
            throw new UserAlreadyExists("User with this email already exists.");
        }

        if (request.phone() != null && repository.existsByPhone(request.phone()) && !user.getPhone().equals(request.phone())) {
            throw new UserAlreadyExists("User with this phone number already exists.");
        }

        user.setLogin(request.login());
        user.setPassword(request.password());
        user.setName(request.name());
        user.setSurname(request.surname());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setDateOfBirth(request.dateOfBirth());

        return UserResponse.toResponse(repository.save(user));
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        repository.delete(user);
    }

    @Transactional
    public void createAccount(Long userId, AccountRequest request) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        String accountNumber;
        int attempts = 0;
        do {
            accountNumber = generateAccountNumber(request.currency());
            if (++attempts > 5) {
                throw new AccountCreationException("Failed to generate unique account number");
            }
        } while (accountRepository.existsByAccountNumber(accountNumber));

        Account account = Account.builder()
                .accountName(request.accountName())
                .accountNumber(accountNumber)
                .currency(request.currency())
                .user(user)
                .build();

        user.addAccount(account);
        accountRepository.save(account);
    }
}
