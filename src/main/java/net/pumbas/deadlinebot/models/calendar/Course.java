package net.pumbas.deadlinebot.models.calendar;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Document("courses")
public class Course
{
    @Id
    private String courseId;

    private String courseName;
    private boolean isPublic;
}
