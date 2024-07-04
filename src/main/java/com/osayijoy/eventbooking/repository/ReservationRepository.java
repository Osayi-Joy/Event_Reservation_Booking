package com.osayijoy.eventbooking.repository;
/**
 * @author Joy Osayi
 * @createdOn Jun-28(Fri)-2024
 */
import com.osayijoy.eventbooking.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    Page<Reservation> findByEventId(Long eventId, Pageable pageable);
    List<Reservation> findByEventId(Long eventId);
    Page<Reservation> findByUserEmail(String email, Pageable pageable);
    Optional<Reservation> findByEventIdAndUserEmail(Long eventId, String email);

}

