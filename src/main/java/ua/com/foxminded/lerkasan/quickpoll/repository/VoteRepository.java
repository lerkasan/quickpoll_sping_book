package ua.com.foxminded.lerkasan.quickpoll.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.lerkasan.quickpoll.domain.Vote;

@Repository
public interface VoteRepository extends CrudRepository<Vote, Long> {

    @Query(value = "select v.* from Vote v, Option o where o.poll_id = ?1 and v.option_id = o.id", nativeQuery = true)
    Iterable<Vote> findByPoll(Long pollId);
}
