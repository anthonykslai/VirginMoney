package com.mkyong;

import com.mkyong.error.TransactionNotFoundException;
import com.mkyong.error.TransactionUnSupportedFieldPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class TransactionController {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private CollectTransaction collectTransaction;

    // Find
    @CrossOrigin
    @GetMapping("/transaction/{year}")
    List<Transaction> findAll(@PathVariable Long year) {
        if(year == 2020)
            return collectTransaction.findAllbyYear(year);
        else
            return (List<Transaction>) new TransactionNotFoundException(year);
    }

    // Save
    @CrossOrigin
    @PostMapping("/transaction")
    //return 201 instead of 200
    @ResponseStatus(HttpStatus.CREATED)
    Transaction newTransaction(@RequestBody Transaction newTransaction) {
        return repository.save(newTransaction);
    }

    @CrossOrigin
    @DeleteMapping("/transaction/{id}")
    void deleteTransaction(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
