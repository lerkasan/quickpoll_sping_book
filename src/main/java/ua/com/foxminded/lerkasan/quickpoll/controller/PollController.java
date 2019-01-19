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
import ua.com.foxminded.lerkasan.quickpoll.repository.PollRepository;
import ua.com.foxminded.lerkasan.quickpoll.repository.VoteRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/polls")
public class PollController {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    @ApiOperation(value = "List all polls")
    @GetMapping
    public ResponseEntity<Iterable<Poll>> getAllPolls() {
        Iterable<Poll> allPolls = pollRepository.findAll();
        return new ResponseEntity<>(allPolls, HttpStatus.OK);
    }

    @ApiOperation(value = "Create a new poll")
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createPoll(@Valid @RequestBody Poll poll) {
        Poll createdPoll = pollRepository.save(poll);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(createdPoll.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdPoll);
    }

    @ApiIgnore
    @ModelAttribute
    private Poll lookupPollById(@PathVariable(required = false) Long pollId) {
        Poll poll = new Poll();
        if (pollId != null) {
            poll = pollRepository.getPollById(pollId);
        }
        return poll;
    }

    @ApiOperation(value = "Get info about a poll with given pollId")
    @GetMapping(path = "/{pollId}")
    public ResponseEntity getPoll(@ModelAttribute Poll poll) {
        return ResponseEntity.ok().body(poll);
    }

    @ApiOperation(value = "Update a poll with given pollId")
    @PutMapping(path = "/{pollId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity updatePoll(@ModelAttribute Poll oldPoll, @Valid @RequestBody Poll poll) {
        poll.setId(oldPoll.getId());
        Poll updatedPoll = pollRepository.save(poll);
        return ResponseEntity.ok().body(updatedPoll);
    }

    @ApiOperation(value = "Delete a poll with given pollId")
    @DeleteMapping(path = "/{pollId}")
    public ResponseEntity deletePoll(@ModelAttribute Poll poll) {
        Iterable<Vote> votes = voteRepository.findByPoll(poll.getId());
        votes.forEach(vote -> voteRepository.delete(vote)); // Is there any way to achieve this by Cascade.REMOVE?
        pollRepository.delete(poll);
        return ResponseEntity.noContent().build();
    }
}
