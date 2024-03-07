package server.api;

import commons.Quote;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;

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
