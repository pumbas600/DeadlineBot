package net.pumbas.deadlinebot.authorization;

import net.pumbas.deadlinebot.App;
import net.pumbas.deadlinebot.authorization.discord.DiscordAuthorizationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(App.API_PREFIX)
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

    @GetMapping("/authorize")
    public RedirectView authorize(@RequestParam("id") String discordId) {
        System.out.println("Authorizing: " + discordId);
        String authorizationUrl = this.authorizationService.getAuthorizationUrl(discordId);
        return new RedirectView(authorizationUrl);
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
        this.authorizationService.updateAuthorizationState(session.getId(), AuthorizationState.AUTHORIZED_DISCORD);

        // Fetch discord id


        return new RedirectView();
    }

    @GetMapping("/authorize/token")
    public RedirectView authorizeToken(@RequestParam(name = "state") String discordId,
                                       @RequestParam(name = "code", required = false) String code,
                                       @RequestParam(name = "error", required = false) String error)
    {
        if (code != null && discordId != null) {
            this.authorizationService.storeCredentials(code, discordId);
            return new RedirectView("/authorization/authorized?id=%s".formatted(discordId));
        }
        return new RedirectView("/authorization/unauthorized");
    }
}
