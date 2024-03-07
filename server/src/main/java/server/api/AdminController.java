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

    /**
     * Generates a password and prints it in the server terminal and
     * adds the generated password to the list of authorized passwords
     * in authService.
     */
    @GetMapping(path = {"", "/"})
    public void generatePassword(){
        String password = PasswordGenerator.generatePassword();
        authService.addPassword(password);
        System.out.println(password);
    }

    /**
     * Returns boolean on whether login was successful or not
     * @param password The password to be used in login attempt
     * @return Boolean value of login success
     */
    @PostMapping(path = { "", "/" })
    public boolean login(@RequestBody String password) {
        return authService.authenticate(password);
    }
}
