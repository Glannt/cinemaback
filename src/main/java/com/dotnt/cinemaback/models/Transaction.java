package com.dotnt.cinemaback.models;

import com.dotnt.cinemaback.constants.enums.EPaymentMethod;
import com.dotnt.cinemaback.constants.enums.ETransactionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Transaction")
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends AbstractEntity<UUID> {
    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private ETransactionStatus status; // PENDING, SUCCESS, FAILED, REFUNDED

    @Enumerated(EnumType.STRING)
    private EPaymentMethod paymentMethod; // CASH, CREDIT_CARD, MOMO, ZALO_PAY, etc.

    private LocalDateTime transactionTime;

}
