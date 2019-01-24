package ua.com.foxminded.lerkasan.quickpoll.v1.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController("pollControllerV1") // to avoid exception rg.springframework.context.annotation.ConflictingBeanDefinitionException: Annotation-specified bean name 'pollController' for bean class [ua.com.foxminded.lerkasan.quickpoll.v2.controller.PollController] conflicts with existing, non-compatible bean definition of same name and class [ua.com.foxminded.lerkasan.quickpoll.v1.controller.PollController]
@RequestMapping(path = "/api/v1/polls")
public class PollController {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    @GetMapping
    @ApiOperation(value = "List all polls", response = Poll.class, responseContainer = "List")
    @ApiResponse(code = 200, message = "Polls were listed gracefully", response = Poll.class, responseContainer = "List")
    public ResponseEntity<Iterable<Poll>> getAllPolls() {
        Iterable<Poll> allPolls = pollRepository.findAll();
        return new ResponseEntity<>(allPolls, HttpStatus.OK);
    }


    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a new poll", notes = "The URL of the created poll is returned in Location header", response = Poll.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Poll was created gracefully", response = Poll.class),
            @ApiResponse(code = 400, message = "Bad request", response = ErrorDetails.class)
    })
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

    @GetMapping(path = "/{pollId}")
    @ApiOperation(value = "Get info about a poll with given pollId", response = Poll.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Poll info is shown gracefully", response = Poll.class),
            @ApiResponse(code = 404, message = "Not found", response = ErrorDetails.class)
    })
    public ResponseEntity getPoll(@ModelAttribute Poll poll) {
        return ResponseEntity.ok().body(poll);
    }

    @PutMapping(path = "/{pollId}", consumes = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update a poll with given pollId", response = Poll.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Poll was updated gracefully", response = Poll.class),
            @ApiResponse(code = 400, message = "Bad request", response = ErrorDetails.class),
            @ApiResponse(code = 404, message = "Not found", response = ErrorDetails.class)
    })
    public ResponseEntity updatePoll(@ModelAttribute Poll oldPoll, @Valid @RequestBody Poll poll) {
        poll.setId(oldPoll.getId());
        Poll updatedPoll = pollRepository.save(poll);
        return ResponseEntity.ok().body(updatedPoll);
    }

    @DeleteMapping(path = "/{pollId}")
    @ApiOperation(value = "Delete a poll with given pollId", response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Poll was deleted gracefully", response = Void.class),
            @ApiResponse(code = 404, message = "Not found", response = ErrorDetails.class)
    })
    public ResponseEntity deletePoll(@ModelAttribute Poll poll) {
        Iterable<Vote> votes = voteRepository.findByPoll(poll.getId());
        votes.forEach(vote -> voteRepository.delete(vote)); // Is there any way to achieve this by Cascade.REMOVE?
        pollRepository.delete(poll);
        return ResponseEntity.noContent().build();
    }
}
