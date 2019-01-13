package ua.com.foxminded.lerkasan.quickpoll.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class VoteResult {

    private Long totalVotes;
    private Collection<OptionCount> results;
}
