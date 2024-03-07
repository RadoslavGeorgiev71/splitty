package server.api;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private String previousPassword;

    @GetMapping(path = {"", "/"})
    public void generatePassword(){

    }

    @PostMapping(path = { "", "/" })
    public boolean authenticatePassword(@RequestBody String password) {

        return true;
    }
}
