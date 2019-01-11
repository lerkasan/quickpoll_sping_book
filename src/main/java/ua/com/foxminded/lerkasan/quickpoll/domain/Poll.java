package ua.com.foxminded.lerkasan.quickpoll.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class Poll {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String question;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="poll_id", referencedColumnName="id")
    @OrderBy
    private Set<Option> options;
}
