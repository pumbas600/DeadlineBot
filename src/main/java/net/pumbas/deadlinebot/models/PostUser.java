package net.pumbas.deadlinebot.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUser
{
    @JsonProperty("discord_id")
    private String discordId;

    @JsonProperty("calendar_id")
    private String calendarId;

    @JsonProperty("course_ids")
    private List<String> courseIds;
}
