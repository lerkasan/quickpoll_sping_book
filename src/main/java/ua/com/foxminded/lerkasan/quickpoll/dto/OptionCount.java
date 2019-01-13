package ua.com.foxminded.lerkasan.quickpoll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OptionCount {

    private Long optionId;
    private Long count;
}
