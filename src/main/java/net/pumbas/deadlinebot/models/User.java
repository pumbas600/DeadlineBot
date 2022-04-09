package net.pumbas.deadlinebot.models;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    @JsonProperty("discord_id")
    private String discordId;

    @Field("calendar_id")
    @JsonProperty("calendar_id")
    private String calendarId;

    @DocumentReference
    private List<Course> courses;
}
