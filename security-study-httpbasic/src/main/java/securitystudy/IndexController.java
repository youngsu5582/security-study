package securitystudy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/login")
    public String login(){
        return "loginForm";
    }
    @GetMapping("/api/protected")
    public String protectedIndex(){
        return "protected";
    }
}
