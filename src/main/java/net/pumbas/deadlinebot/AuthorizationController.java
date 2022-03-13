package net.pumbas.deadlinebot;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/v1")
public class AuthorizationController
{
    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping("/authorize/test")
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok("You've successfully connected :)");
    }

    @GetMapping("/authorize")
    public RedirectView authorize(@RequestParam("id") String userId) {
        String authorizationUrl = this.authorizationService.getAuthorizationUrl(userId);
        return new RedirectView(authorizationUrl);
    }

    @GetMapping("/authorize/token")
    public RedirectView authorizeToken(@RequestParam(name = "state", defaultValue = "user") String userId,
                                       @RequestParam(name = "code", required = false) String code,
                                       @RequestParam(name = "error", required = false) String error)
    {
        if (code != null) {
            this.authorizationService.storeCredentials(code, userId);
            return new RedirectView("/api/v1/authorized");
        }
        else
            return new RedirectView("/api/v1/unauthorized");
    }

    @GetMapping("/authorized")
    public String authorized() {
        return "Thanks for authorizing :)";
    }

    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "You didn't authorize the bot to use your google calendar :(";
    }
}
