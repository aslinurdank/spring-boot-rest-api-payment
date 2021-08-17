package com.example.CodeTest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "Transactions")
@RequiredArgsConstructor
@EqualsAndHashCode(of="transactionId")
public class Transaction {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID transactionId;
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
    private Long accountId;
    @Enumerated(EnumType.STRING)
    private Origin origin;
    private BigDecimal amount;

    public Transaction(MessageType messageType, Long accountId, Origin origin, BigDecimal amount) {
        this.messageType = messageType;
        this.accountId = accountId;
        this.origin = origin;
        this.amount = amount;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Origin getOrigin() {
        return origin;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }
}
