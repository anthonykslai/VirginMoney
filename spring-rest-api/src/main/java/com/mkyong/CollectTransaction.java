package com.mkyong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CollectTransaction {

    @Autowired
    private TransactionRepository repository;

    private List<Transaction> transactionList = new ArrayList<Transaction>();

    public List<Transaction> findAllbyYear(long year){
        List<Transaction> result = new ArrayList<Transaction>();

        transactionList = repository.findAll();

        for(Transaction transaction: transactionList){
            if (transaction.getTransDate().getYear() == year){
                result.add(transaction);
            }
        }
        return result;
    }
}
