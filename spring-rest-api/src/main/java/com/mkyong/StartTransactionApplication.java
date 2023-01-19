package com.mkyong;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

@SpringBootApplication
public class StartTransactionApplication {

    // start everything
    public static void main(String[] args) {
        SpringApplication.run(StartTransactionApplication.class, args);
    }

    // run this only on profile 'demo', avoid run this in test
    @Profile("demo")
    @Bean
    CommandLineRunner initDatabase(TransactionRepository repository) {
        DateTimeFormatter df = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("dd/MMM/yyyy")
                .toFormatter(Locale.ENGLISH);

        return args -> {
            repository.save(new Transaction(LocalDate.parse("01/Nov/2020", df), "Morrisons", "card", new BigDecimal("10.40"), "Groceries"));
            repository.save(new Transaction(LocalDate.parse("28/Oct/2020", df), "CYBG", "direct debit", new BigDecimal("600"), "MyMonthlyDD"));
            repository.save(new Transaction(LocalDate.parse("28/Oct/2020", df), "PureGym", "direct debit", new BigDecimal("40"), "MyMonthlyDD"));
            repository.save(new Transaction(LocalDate.parse("01/Oct/2020", df), "M&S", "card", new BigDecimal("5.99"), "Groceries"));
            repository.save(new Transaction(LocalDate.parse("30/Sep/2020", df), "McMillan", "internet", new BigDecimal("10"), ""));

        };
    }
}