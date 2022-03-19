package net.pumbas.deadlinebot.authorization.discord;

import com.google.api.client.util.Key;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserData
{
    public static final UserData EMPTY = new UserData("", "", "");

    @Key
    private String id;

    @Key
    private String username;

    @Key
    private String discriminator;

    public String getTag() {
        return this.username + "#" + this.discriminator;
    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }
}
