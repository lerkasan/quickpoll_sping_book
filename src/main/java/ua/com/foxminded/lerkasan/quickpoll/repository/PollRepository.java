package ua.com.foxminded.lerkasan.quickpoll.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.lerkasan.quickpoll.domain.Poll;

@Repository
public interface PollRepository extends CrudRepository<Poll, Long> {
}
