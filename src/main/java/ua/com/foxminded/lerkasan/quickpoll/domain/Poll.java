package ua.com.foxminded.lerkasan.quickpoll.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Data
public class Poll {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @ApiModelProperty(required = true)
    private String question;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="poll_id", referencedColumnName="id")
    @OrderBy
    @NotEmpty
    @Size(min = 2, max = 6)
    @ApiModelProperty(required = true)
    private Set<Option> options;
}
