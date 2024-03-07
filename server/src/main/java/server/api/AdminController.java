package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.AuthService;
import server.PasswordGenerator;


@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AuthService authService;
    private String previousPassword;

    @GetMapping(path = {"", "/"})
    public void generatePassword(){
        previousPassword = PasswordGenerator.generatePassword();
        System.out.println(previousPassword);
    }

    @PostMapping(path = { "", "/" })
    public boolean login(@RequestBody String password) {
        if(password.equals(previousPassword)){
            authService.addPassword(password);
            return true;
        }
        return false;
    }
}
