package com.microbank.accountservice.controller;

import com.microbank.accountservice.model.Account;
import com.microbank.accountservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;


    @GetMapping("/getAllAccount")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Account>> getAll() {
        List<Account> accounts = accountRepository.findAll();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/createAccount")

    public ResponseEntity<Account> create(@RequestBody Account acc, @RequestHeader("X-User-Id") Long userId) {

        acc.setUserId(userId);
        Account saved = accountRepository.save(acc);
        return ResponseEntity.ok(saved);
    }



    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Account>> getByUserId(@PathVariable Long userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable Long id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }




    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        accountRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
