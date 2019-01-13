package ua.com.foxminded.lerkasan.quickpoll.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.com.foxminded.lerkasan.quickpoll.domain.Vote;
import ua.com.foxminded.lerkasan.quickpoll.dto.OptionCount;
import ua.com.foxminded.lerkasan.quickpoll.dto.VoteResult;
import ua.com.foxminded.lerkasan.quickpoll.repository.PollRepository;
import ua.com.foxminded.lerkasan.quickpoll.repository.VoteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class ComputeResultController {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PollRepository pollRepository;

    @ApiOperation(value = "View results of a poll with given pollId")
    @GetMapping("/api/computeresult")
    public ResponseEntity computeResult(@RequestParam Long pollId) {
        pollRepository.getPollById(pollId); // just to check existence of a poll with pollId
        VoteResult voteResult = new VoteResult();
        Iterable<Vote> allVotes = voteRepository.findByPoll(pollId);
        Map<Long, Long> countedResults = StreamSupport.stream(allVotes.spliterator(), false)
                .collect(Collectors.groupingBy(vote -> vote.getOption().getId(),
                        Collectors.counting()));
        List<OptionCount> optionCounts = new ArrayList<>();
        countedResults.forEach((k,v) -> optionCounts.add(new OptionCount(k,v)));
        Long totalVotes = countedResults.values().stream().reduce(0L, Long::sum);
        voteResult.setResults(optionCounts);
        voteResult.setTotalVotes(totalVotes);
        return ResponseEntity.ok(voteResult);
    }
}
