package ua.com.foxminded.lerkasan.quickpoll.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.lerkasan.quickpoll.domain.Option;

@Repository
public interface OptionRepository extends PagingAndSortingRepository<Option, Long> {
}
