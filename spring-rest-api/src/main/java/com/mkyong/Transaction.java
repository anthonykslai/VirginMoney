package com.mkyong;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDate transDate;
    private String vendor;
    private String type;
    private BigDecimal price;
    private String category;

    // avoid this "No default constructor for entity"
    public Transaction() {
    }

    public Transaction(LocalDate transDate, String vendor, String type, BigDecimal price, String category) {
        this.transDate = transDate;
        this.vendor = vendor;
        this.type = type;
        this.price = price;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTransDate() {
        return transDate;
    }

    public void setTransDate(LocalDate transDate) {
        this.transDate = transDate;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
