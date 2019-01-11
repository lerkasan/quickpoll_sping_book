package ua.com.foxminded.lerkasan.quickpoll.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.com.foxminded.lerkasan.quickpoll.domain.Vote;
import ua.com.foxminded.lerkasan.quickpoll.repository.VoteRepository;

import java.net.URI;

@RestController
public class VoteController {

    @Autowired
    private VoteRepository voteRepository;

    @PostMapping("/api/polls/{pollId}/votes")
    public ResponseEntity createVote(@PathVariable Long pollId, @RequestBody Vote vote) {
        // TODO: add Poll field as a ManyToOne connection to Vote entity and implement validation that option_id in vote corresponds to one of set<option_id>  values in this poll
        Vote createdVote = voteRepository.save(vote);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(createdVote.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdVote);
    }

    @GetMapping("/api/polls/{pollId}/votes")
    public ResponseEntity<Iterable<Vote>> getAllVotes(@PathVariable Long pollId) {
        Iterable<Vote> votes = voteRepository.findByPoll(pollId);
        return new ResponseEntity<>(votes, HttpStatus.OK);
    }
}
