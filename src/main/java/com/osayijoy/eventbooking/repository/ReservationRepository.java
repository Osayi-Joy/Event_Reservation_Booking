package com.osayijoy.eventbooking.repository;
/**
 * @author Joy Osayi
 * @createdOn Jun-28(Fri)-2024
 */
import com.osayijoy.eventbooking.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByEventId(Long eventId);
}

