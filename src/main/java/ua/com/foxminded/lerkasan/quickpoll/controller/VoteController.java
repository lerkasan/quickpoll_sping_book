package ua.com.foxminded.lerkasan.quickpoll.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;
import ua.com.foxminded.lerkasan.quickpoll.domain.Poll;
import ua.com.foxminded.lerkasan.quickpoll.domain.Vote;
import ua.com.foxminded.lerkasan.quickpoll.exception.ResourceNotFoundException;
import ua.com.foxminded.lerkasan.quickpoll.repository.PollRepository;
import ua.com.foxminded.lerkasan.quickpoll.repository.VoteRepository;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/polls/{pollId}/votes")
public class VoteController {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PollRepository pollRepository;

    @ApiIgnore
    @ModelAttribute
    private Poll lookupPollById(@PathVariable(required = false) Long pollId) {
        Poll poll = new Poll();
        if (pollId != null) {
            poll = pollRepository.getPollById(pollId);
        }
        return poll;
    }

    @ApiOperation(value = "Vote in a a poll with given pollId")
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createVote(@ModelAttribute Poll poll, @Valid @RequestBody Vote vote) {
        if (! poll.getOptions().contains(vote.getOption())) {
            throw new ResourceNotFoundException("Vote option doesn't match the question options");
        }
        Vote createdVote = voteRepository.save(vote);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(createdVote.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdVote);
    }

    @ApiOperation(value = "List all votes in a poll with given pollId")
    @GetMapping
    public ResponseEntity<Iterable<Vote>> getAllVotes(@ModelAttribute Poll poll) {
        Iterable<Vote> votes = voteRepository.findByPoll(poll.getId());
        return new ResponseEntity<>(votes, HttpStatus.OK);
    }

    @ApiOperation(value = "Show info about a vote with given voteId")
    @GetMapping("/{voteId}")
    public ResponseEntity<Vote> getVote(@PathVariable Long voteId) {
        Vote vote = voteRepository.getVoteById(voteId);
        return new ResponseEntity<>(vote, HttpStatus.OK);
    }
}
