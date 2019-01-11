package ua.com.foxminded.lerkasan.quickpoll.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.com.foxminded.lerkasan.quickpoll.domain.Poll;
import ua.com.foxminded.lerkasan.quickpoll.exception.ResourceNotFoundException;
import ua.com.foxminded.lerkasan.quickpoll.repository.PollRepository;

import java.net.URI;

@RestController
@RequestMapping(path = "/api/polls")
public class PollController {

    @Autowired
    private PollRepository pollRepository;

    @GetMapping
    public ResponseEntity<Iterable<Poll>> getAllPolls() {
        Iterable<Poll> allPolls = pollRepository.findAll();
        return new ResponseEntity<>(allPolls, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity createPoll(@RequestBody Poll poll) {
        Poll createdPoll = pollRepository.save(poll);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(createdPoll.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdPoll);
    }

    @GetMapping(path = "/{pollId}")
    public ResponseEntity getPoll(@PathVariable long pollId) {
        Poll poll = pollRepository.findById(pollId).orElseThrow(ResourceNotFoundException::new);
        return ResponseEntity.ok().body(poll);
    }

    @PutMapping(path = "/{pollId}")
    public ResponseEntity updatePoll(@PathVariable long pollId, @RequestBody Poll poll) {
        poll.setId(pollId);
        Poll updatedPoll = pollRepository.save(poll);
        return ResponseEntity.ok().body(updatedPoll);
    }

    @DeleteMapping(path = "/{pollId}")
    public ResponseEntity deletePoll(@PathVariable long pollId) {
        pollRepository.deleteById(pollId);
        return ResponseEntity.noContent().build();
    }
}
