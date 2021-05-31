package ru.itis.javalab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.javalab.models.Event;
import ru.itis.javalab.models.User;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {

        @Query(value="SELECT id,added_time, event_name,user_id, prev_end_time as event_starts, event_starts as event_ends FROM (SELECT *, lag(event_ends) OVER (ORDER BY event_starts) as prev_end_time FROM event where user_id = :userId ORDER BY event_starts) s WHERE event_starts > prev_end_time and user_id = :userId order by event_starts",
        nativeQuery=true)
        List<Event> getFreeTime(@Param("userId") Long userId);

        @Query("select case when count(e) > 0 then true else false end from Event e where ((:eventStarts <= e.eventEnds and :eventEnds >= e.eventStarts) and e.user= :user) ")
        Boolean checkExistingEvent(@Param("eventStarts") LocalDateTime eventStarts,
                                 @Param("eventEnds") LocalDateTime eventEnds,
                                 @Param("user") User user);

}
