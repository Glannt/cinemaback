package com.dotnt.cinemaback.repositories;

import com.dotnt.cinemaback.models.BookedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookedSeatRepository extends JpaRepository<BookedSeat, UUID> {
    // Custom query to find booked seats by hall ID
    @Query(value = """
            SELECT bs.*
            FROM booked_seat bs
            JOIN seat s ON bs.seat_id = s.id
            JOIN hall_seat hs ON s.hall_seat_id = hs.id
            JOIN hall h ON hs.hall_id = h.id
            WHERE h.id = :hallId""", nativeQuery = true)
    List<BookedSeat> findAllBySeat_Hall_Id(@Param("hallId") UUID hallId);

    @Query(value = """
            SELECT bs.*
            FROM booked_seat bs
            JOIN ticket t ON bs.ticket_id = t.id
            JOIN show_time st ON t.showtime_id = st.id
            WHERE st.id = :showtimeId""", nativeQuery = true)
    List<BookedSeat> findAllByShowtimeId(UUID showtimeId);
}
