package com.osayijoy.eventbooking.repository;


import com.osayijoy.eventbooking.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByNameContainingAndCategory(String name, String category);
}
