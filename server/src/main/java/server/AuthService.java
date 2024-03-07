package server;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

    private List<String> authorizedPasswords;

    public AuthService() {
        this.authorizedPasswords = new ArrayList<>();
    }
    public void addPassword(String password){
        authorizedPasswords.add(password);
    }
    public boolean authenticate(String password) {
        return authorizedPasswords.contains(password);
    }
}
