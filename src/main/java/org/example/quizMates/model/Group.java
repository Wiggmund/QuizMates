package org.example.quizMates.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
public class Group {
    private Long id;
    private String studentId;
    private String name;
  //  private List<Student> students;
}
