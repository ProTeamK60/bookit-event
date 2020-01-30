package se.knowit.bookitevent.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Option {
    
    private Integer optionId;
    private String optionType;
    private String title;
    private String queryString;
}
