package chnu.edu.anetrebin.anb.service.account.impl;

import chnu.edu.anetrebin.anb.dto.requests.AccountRequest;
import chnu.edu.anetrebin.anb.dto.responses.AccountResponse;
import chnu.edu.anetrebin.anb.exceptions.account.AccountNotFoundException;
import chnu.edu.anetrebin.anb.model.Account;
import chnu.edu.anetrebin.anb.repository.AccountRepository;
import chnu.edu.anetrebin.anb.service.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository repository;

    @Override
    public void deleteAccount(Long id) {
        Account account = repository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
        repository.delete(account);
    }

    @Transactional(readOnly = true)
    @Override
    public AccountResponse getAccountById(Long id) {
        return AccountResponse.toResponse(repository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id)));
    }

    @Transactional
    @Override
    public AccountResponse updateAccount(Long id, AccountRequest accountRequest) {
        Account account = repository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));

        account.setAccountName(accountRequest.accountName());
        account.setCurrency(accountRequest.currency());

        // TODO: If currency has changed - request for CurrencyExchange
        return AccountResponse.toResponse(repository.save(account));

    }

    @Transactional(readOnly = true)
    @Override
    public List<AccountResponse> getAllAccounts() {
        return repository.findAllByOrderByIdAsc().stream().map(AccountResponse::toResponse).collect(Collectors.toList());
    }
}
