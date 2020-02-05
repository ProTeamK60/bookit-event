package se.knowit.bookitevent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OptionDTO {
    private Integer optionId;
    private String optionType;
    private String title;
    private String queryString;
    private Boolean required;
}
