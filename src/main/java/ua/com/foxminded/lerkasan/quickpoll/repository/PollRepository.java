package ua.com.foxminded.lerkasan.quickpoll.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.lerkasan.quickpoll.domain.Poll;
import ua.com.foxminded.lerkasan.quickpoll.exception.ResourceNotFoundException;

@Repository
public interface PollRepository extends CrudRepository<Poll, Long> {

    default Poll getPollById(Long pollId) {
        return findById(pollId)
                .orElseThrow(() -> new ResourceNotFoundException("The poll with id = " + pollId + " isn't found."));
    }
}
