package com.example.CodeTest.controller;

import com.example.CodeTest.model.Account;
import com.example.CodeTest.model.MessageType;
import com.example.CodeTest.model.Origin;
import com.example.CodeTest.model.Transaction;
import com.example.CodeTest.repository.AccountRepository;
import com.example.CodeTest.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;

    @PutMapping("/payments/{id}")
    @Transactional
    public ResponseEntity<Transaction> updateTransaction(@PathVariable("id") UUID id, @RequestBody Transaction transaction) {
        try {
            Optional<Transaction> transactionData = transactionRepository.findById(id);
            if (transactionData.isPresent()&&transaction.getMessageType().equals(MessageType.ADJUSTMENT)) {
                Optional<Account> accountData = accountRepository.findById(transaction.getAccountId());
                if (accountData.isPresent()) {
                    Transaction _transaction = transactionData.get();

                        Double commissionRate = transaction.getOrigin().equals(Origin.VISA) ?0.01 :0.02;
                        Double commissionRateBefore = _transaction.getOrigin().equals(Origin.VISA) ? 0.01: 0.02;

                        Account _account = accountData.get();
                        BigDecimal balance=  _account.getBalance();
                        balance=balance.add(_transaction.getAmount().add(_transaction.getAmount().multiply(BigDecimal.valueOf(commissionRateBefore))));
                        int res = balance.compareTo(transaction.getAmount());
                        if( res==0||res==1) {
                            balance= balance.subtract(transaction.getAmount().add(transaction.getAmount().multiply(BigDecimal.valueOf(commissionRate))));
                            _account.setBalance(balance);
                            accountRepository.save(_account);

                            _transaction.setMessageType(transaction.getMessageType());
                            _transaction.setAccountId(transaction.getAccountId());
                            _transaction.setOrigin(transaction.getOrigin());
                            _transaction.setAmount(transaction.getAmount());
                            return new ResponseEntity<>(transactionRepository.save(_transaction), HttpStatus.OK);
                     }
                    else {
                        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
                    }
                }
                else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/payments")
    @Transactional
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        try {
            if (transaction.getMessageType().equals(MessageType.PAYMENT)) {
                Optional<Account> accountData = accountRepository.findById(transaction.getAccountId());
                Account _account = accountData.get();
                int res = _account.getBalance().compareTo(transaction.getAmount());
                if (res == 0 || res == 1) {
                    Double commissionRate = transaction.getOrigin().equals(Origin.VISA) ? 0.01 : 0.02;
                    BigDecimal balance = _account.getBalance().subtract(transaction.getAmount().add(transaction.getAmount().multiply(BigDecimal.valueOf(commissionRate))));
                    _account.setBalance(balance);
                    accountRepository.save(_account);

                    Transaction _transaction = transactionRepository
                            .save(new Transaction(transaction.getMessageType(), transaction.getAccountId(), transaction.getOrigin(), transaction.getAmount()));

                    return new ResponseEntity<>(_transaction, HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
                }
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/payments")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        try {
            List<Transaction> transactions = new ArrayList<Transaction>();

            transactionRepository.findAll().forEach(transactions::add);

            if (transactions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
