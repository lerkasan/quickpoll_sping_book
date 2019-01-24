package ua.com.foxminded.lerkasan.quickpoll.v2.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;
import ua.com.foxminded.lerkasan.quickpoll.domain.Poll;
import ua.com.foxminded.lerkasan.quickpoll.domain.Vote;
import ua.com.foxminded.lerkasan.quickpoll.dto.error.ErrorDetails;
import ua.com.foxminded.lerkasan.quickpoll.repository.PollRepository;
import ua.com.foxminded.lerkasan.quickpoll.repository.VoteRepository;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController("voteControllerV2")
@RequestMapping("/api/v2/polls/{pollId}/votes")
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

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Vote in a poll with given pollId", notes = "The URL of the created vote is returned in Location header", response = Vote.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Vote was accepted gracefully", response = Vote.class),
            @ApiResponse(code = 400, message = "Bad request", response = ErrorDetails.class)
    })
    public ResponseEntity createVote(@ModelAttribute Poll poll, @Valid @RequestBody Vote vote) {
        if (! poll.getOptions().contains(vote.getOption())) {
            throw new IllegalArgumentException("Vote option doesn't match the question options");
        }
        Vote createdVote = voteRepository.save(vote);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(createdVote.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdVote);
    }

    @GetMapping
    @ApiOperation(value = "List all votes in a poll with given pollId", response = Vote.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Votes in a poll are shown gracefully", response = Vote.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Not found", response = ErrorDetails.class)
    })
    public ResponseEntity<Page<Vote>> getAllVotes(@ModelAttribute Poll poll, Pageable pageable) {
        Page<Vote> votes = voteRepository.findByPoll(poll.getId(), pageable);
        return new ResponseEntity<>(votes, HttpStatus.OK);
    }

    @GetMapping("/{voteId}")
    @ApiOperation(value = "Show info about a vote with given voteId", response = Vote.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Vote info is shown gracefully", response = Vote.class),
            @ApiResponse(code = 404, message = "Not found", response = ErrorDetails.class)
    })
    public ResponseEntity<Vote> getVote(@PathVariable Long voteId) {
        Vote vote = voteRepository.getVoteById(voteId);
        return new ResponseEntity<>(vote, HttpStatus.OK);
    }
}
