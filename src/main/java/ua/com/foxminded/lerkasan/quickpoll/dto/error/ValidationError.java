package ua.com.foxminded.lerkasan.quickpoll.dto.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationError {

    @JsonIgnore
    private String field;

    private String code;

    private String message;
}
