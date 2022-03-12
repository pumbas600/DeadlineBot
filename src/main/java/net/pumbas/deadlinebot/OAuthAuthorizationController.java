package net.pumbas.deadlinebot;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class OAuthAuthorizationController
{
    @GetMapping("/authorize/test")
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok("You've successfully connected :)");
    }

    @PostMapping("/authorize/token")
    public ResponseEntity<String> authorizeToken(@RequestParam(name = "code", required = false) String code,
                                                 @RequestParam(name = "error", required = false) String error)
    {
        if (code != null)
            return ResponseEntity.ok().body("Received code: %s".formatted(code));
        else
            return ResponseEntity.badRequest().body("You didn't authorize the bot to use your google calendar :(");
    }
}
