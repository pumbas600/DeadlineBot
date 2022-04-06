package net.pumbas.deadlinebot.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Document("users")
public class User
{
    @Id
    private String discordId;

    @Field("calendar_id")
    private String calendarId;

    @DocumentReference
    private List<Course> courses;
}
