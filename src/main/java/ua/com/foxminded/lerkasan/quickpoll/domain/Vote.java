package ua.com.foxminded.lerkasan.quickpoll.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Vote {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="option_id", referencedColumnName="id")
    @NotNull
    @ApiModelProperty(required = true)
    private Option option;
}
