package se.knowit.bookitevent.model;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;

import lombok.Data;

@Data
@Entity
@AllArgsConstructor
public class Option {

  private Integer optionId;
  private String optionType;
  private String title;
  private String queryString;
}
