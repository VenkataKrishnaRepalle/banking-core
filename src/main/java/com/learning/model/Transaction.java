package com.learning.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {

    private UUID uuid;

    private UUID customerUuid;

    private TransactionType transactionType;

    private double transactionAmount;

    private Instant transactionDate;
}
