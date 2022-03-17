package net.pumbas.deadlinebot.authorization;

import net.pumbas.deadlinebot.App;

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
    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping("/authorize")
    public String authorize(
        @RequestParam(name = "id", required = false) String discordId,
        HttpSession session,
        Model model
    ) {
        AuthorizationState authorizationState = this.authorizationService.getAuthorizationState(session.getId());
        model.addAttribute("authorizationState", authorizationState.name());
        model.addAttribute("authorizeDiscordUrl", App.API_PREFIX + "/authorize/discord");
        model.addAttribute("authorizeGoogleUrl", App.API_PREFIX + "/authorize/google");

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
