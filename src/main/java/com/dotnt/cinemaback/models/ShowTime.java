package com.dotnt.cinemaback.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "ShowTime")
@Table(name = "show_time")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowTime extends AbstractEntity<UUID> {

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall;         // Hall nào chiếu

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;       // Chiếu phim nào

    @Column(nullable = false)
    private LocalDateTime startTime;  // Thời gian bắt đầu

    @Column(nullable = false)
    private LocalDateTime endTime;    // Thời gian kết thúc (có thể tính từ duration movie)

    private Double ticketPrice;       // Giá vé (nếu muốn set riêng cho suất này)

    private String status;            // Ví dụ: ACTIVE, CANCELLED
}
