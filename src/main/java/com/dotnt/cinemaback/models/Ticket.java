package com.dotnt.cinemaback.models;

import com.dotnt.cinemaback.constants.enums.ETicket;
import com.dotnt.cinemaback.constants.enums.ProjectionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "Ticket")
@Table(name = "ticket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket extends AbstractEntity<UUID> {

    private String bookingReference;
    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

//    @ManyToOne
//    @JoinColumn(name = "customer_id")
//    private Customer customer;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookedSeat> bookedSeats = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "showtime_id")
    private ShowTime showtime;

    private LocalDateTime bookingDate;

    @Enumerated(EnumType.STRING)
    private ProjectionType type; // 2D, 3D, 4DX

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ETicket status; // BOOKED, CANCELLED, CHECKED_IN

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private Transaction transactions;

}
