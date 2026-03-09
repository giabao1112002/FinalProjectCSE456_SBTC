package eiu.edu.vn.EquipmentBookingSystem.service;

import eiu.edu.vn.EquipmentBookingSystem.model.Account;
import eiu.edu.vn.EquipmentBookingSystem.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Optional<Account> getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Optional<Account> getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    // Đăng ký tài khoản mới
    public Account registerAccount(Account account) {
        if (accountRepository.existsByUsername(account.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        }
        // TODO: Hash password trước khi lưu
        account.setRole(Account.Role.USER);
        account.setActive(true);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    // Đăng nhập
    public Optional<Account> login(String username, String password) {
        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isPresent() && account.get().getPassword().equals(password) && account.get().getActive()) {
            return account;
        }
        return Optional.empty();
    }

    public Account saveAccount(Account account) {
        account.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    public void updateAccount(Long id, Account account) {
        Optional<Account> existingAccount = accountRepository.findById(id);
        if (existingAccount.isPresent()) {
            Account acc = existingAccount.get();
            acc.setFullName(account.getFullName());
            acc.setEmail(account.getEmail());
            acc.setRole(account.getRole());
            acc.setActive(account.getActive());
            acc.setUpdatedAt(LocalDateTime.now());
            accountRepository.save(acc);
        }
    }
}
