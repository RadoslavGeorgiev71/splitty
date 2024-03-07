package server.api;

import org.springframework.web.bind.annotation.*;
import server.PasswordGenerator;


@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private String previousPassword;

    @GetMapping(path = {"", "/"})
    public void generatePassword(){
        previousPassword = PasswordGenerator.generatePassword();
        System.out.println(previousPassword);
    }

    @PostMapping(path = { "", "/" })
    public boolean authenticatePassword(@RequestBody String password) {
        //logic to be implemented
        return true;
    }
}
