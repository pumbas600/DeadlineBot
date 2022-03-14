package net.pumbas.deadlinebot.authorization;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("authorization")
public class AuthorizationMvcController
{
    @GetMapping("/authorized")
    public String authorized(Model model, @RequestParam(name= "id") String discordId) {
        model.addAttribute("discordId", discordId);
        return "authorized";
    }

    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "unauthorized";
    }
}
