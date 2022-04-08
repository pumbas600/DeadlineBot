package net.pumbas.deadlinebot.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    private String id;

    private String name;

    @Field("owner_id")
    @JsonProperty("owner_id")
    private String ownerId;

    @Field("is_public")
    @JsonProperty("is_public")
    private boolean isPublic;

    public boolean trackableBy(String discordId) {
        return this.isPublic || this.isOwner(discordId);
    }

    public boolean isOwner(String discordId) {
        return this.ownerId.equals(discordId);
    }
}
