package com.mindhub.homebanking.models;

import jakarta.persistence.*;
@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private Integer payments;
    private int paymentsMade = 0;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Loan loan;

    public ClientLoan() {
    }

    public ClientLoan(Double amount, Integer payments, int paymentsMade) {
        this.amount = amount;
        this.payments = payments;
        this.paymentsMade = paymentsMade;
    }

    public Long getId() {
        return id;
    }


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public int getPaymentsMade() {
        return paymentsMade;
    }

    public void setPaymentsMade(int paymentsMade) {
        this.paymentsMade = paymentsMade;
    }
}
