package net.pumbas.deadlinebot;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class OAuthAuthorizationController
{
    @GetMapping("/authorize/test")
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok("You've successfully connected :)");
    }

    @PostMapping("/authorize")
    public ResponseEntity<String> addAuthorization(@RequestBody String content) {
        return ResponseEntity.badRequest().body("Received: \n" + content);
    }
}
