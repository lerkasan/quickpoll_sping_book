package ua.com.foxminded.lerkasan.quickpoll.dto.error;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ErrorDetails {

    private String title;
    private int status;
    private String detail;
    private LocalDateTime timestamp;
    private String exceptionName;
    private Map<String, List<ValidationError>> errors = new HashMap<>();
}
