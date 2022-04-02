package net.pumbas.deadlinebot.authorization;

import net.pumbas.deadlinebot.App;
import net.pumbas.deadlinebot.authorization.discord.DiscordAuthorizationService;
import net.pumbas.deadlinebot.authorization.discord.UserData;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(App.API_V1)
public class AuthorizationRestController
{
    private final AuthorizationService authorizationService;
    private final DiscordAuthorizationService discordAuthorizationService;

    public AuthorizationRestController(
        AuthorizationService authorizationService,
        DiscordAuthorizationService discordAuthorizationService
    ) {
        this.authorizationService = authorizationService;
        this.discordAuthorizationService = discordAuthorizationService;
    }

    @GetMapping("/authorize/test")
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok("You've successfully connected :)");
    }

    @GetMapping("/authorize/reset")
    public RedirectView authorizeReset(HttpSession session) {
        this.authorizationService.updateAuthorizationState(session.getId(), AuthorizationState.UNAUTHORIZED);
        return new RedirectView("/authorization/authorize");
    }

    @GetMapping("/authorize/discord")
    public RedirectView authorizeDiscord(HttpSession session) {
        String authorizationUrl = this.discordAuthorizationService.getAuthorizationUrl(session);
        return new RedirectView(authorizationUrl);
    }

    @GetMapping("/authorize/discord/redirect")
    public RedirectView authorizeDiscordRedirect(
        @RequestParam(name = "state") String state,
        @RequestParam(name = "code") String code,
        HttpSession session
    ) {
        if (!AuthorizationService.toBase64(session.getId()).equals(state)) {
            // Clickjacked
            this.authorizationService.updateAuthorizationState(session.getId(), AuthorizationState.UNAUTHORIZED);
            return new RedirectView("/authorization/unauthorized?error=You were clickjacked!");
        }


        UserData userData = this.discordAuthorizationService.exchangeCode(session.getId(), code);
        if (!userData.isEmpty()) {
            this.authorizationService.updateAuthorizationState(session.getId(), AuthorizationState.AUTHORIZED_DISCORD);
            return new RedirectView("/authorization/authorize");
        }
        this.authorizationService.updateAuthorizationState(session.getId(), AuthorizationState.UNAUTHORIZED);
        return new RedirectView("/authorization/unauthorized?error=Couldn't fetch user data");
    }

    @GetMapping("/authorize/google")
    public RedirectView authorizeGoogle(@RequestParam("id") String discordId) {
        System.out.println("Authorizing: " + discordId);
        String authorizationUrl = this.authorizationService.getAuthorizationUrl(discordId);
        return new RedirectView(authorizationUrl);
    }

    @GetMapping("/authorize/google/redirect")
    public RedirectView authorizeToken(@RequestParam(name = "state") String discordId,
                                       @RequestParam(name = "code", required = false) String code,
                                       HttpSession session
    ) {
        if (code != null && discordId != null) {
            this.authorizationService.storeCredentials(code, discordId);
            this.authorizationService.updateAuthorizationState(session.getId(), AuthorizationState.AUTHORIZED_GOOGLE);
            return new RedirectView("/authorization/authorize?id=%s".formatted(discordId));
        }
        return new RedirectView("/authorization/unauthorized");
    }
}
