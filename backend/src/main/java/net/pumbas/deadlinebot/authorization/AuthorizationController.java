package net.pumbas.deadlinebot.authorization;

import net.pumbas.deadlinebot.App;
import net.pumbas.deadlinebot.authorization.discord.DiscordAuthorizationService;
import net.pumbas.deadlinebot.authorization.discord.UserData;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("authorization")
public class AuthorizationController
{
    private final DiscordAuthorizationService discordAuthorizationService;
    private final AuthorizationService authorizationService;

    public AuthorizationController(DiscordAuthorizationService discordAuthorizationService,
                                   AuthorizationService authorizationService
    ) {
        this.discordAuthorizationService = discordAuthorizationService;
        this.authorizationService = authorizationService;
    }

    @GetMapping("/authorize")
    public String authorize(
        HttpSession session,
        Model model
    ) {
        UserData userData = this.discordAuthorizationService.getUserData(session.getId());

        AuthorizationState authorizationState = this.authorizationService.getAuthorizationState(session.getId());
        model.addAttribute("authorizationState", authorizationState.ordinal());
        model.addAttribute("authorizeDiscordUrl", App.API_PREFIX + "/authorize/discord");
        model.addAttribute("authorizeResetUrl", App.API_PREFIX + "/authorize/reset");
        model.addAttribute("authorizeGoogleUrl", App.API_PREFIX + "/authorize/google?id=" + userData.getId());
        model.addAttribute("discordId", userData.getId());
        model.addAttribute("discordTag", userData.getTag());

        return "authorize";
    }

    @GetMapping("/authorized")
    public String authorized(Model model, @RequestParam(name = "id") String discordId) {
        model.addAttribute("discordId", discordId);
        return "authorized";
    }

    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "unauthorized";
    }
}
