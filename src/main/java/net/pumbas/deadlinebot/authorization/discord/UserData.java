package net.pumbas.deadlinebot.authorization.discord;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserData
{
    public static final UserData EMPTY = new UserData("", "", "");

    private final String id;
    private final String username;
    private final String discriminator;

    public String getTag() {
        return this.username + "#" + this.discriminator;
    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }
}
