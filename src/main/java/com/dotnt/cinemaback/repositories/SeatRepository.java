package com.dotnt.cinemaback.repositories;

import com.dotnt.cinemaback.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SeatRepository extends JpaRepository<Seat, UUID> {
    @Query("SELECT s FROM Seat s WHERE CONCAT(s.row, s.number) IN :seatCodes")
    List<Seat> findByRowAndNumberIn(@Param("seatCodes") List<String> seatCodes);

    @Query(value = """
                SELECT DISTINCT s.* FROM seat s
                JOIN booked_seat bs ON s.id = bs.seat_id
                JOIN ticket t ON bs.ticket_id = t.id
                JOIN show_time st ON t.showtime_id = st.id
                WHERE st.end_time < :now AND s.status = '2'
            """, nativeQuery = true)
    List<Seat> findOccupiedSeatsNative(@Param("now") LocalDateTime now);
}