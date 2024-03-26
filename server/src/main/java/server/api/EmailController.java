package server.api;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.EmailService;

import java.util.List;


@RestController
@RequestMapping("/api/email")
public class EmailController {

    private EmailService emailService;


    /**
     * Constructor for EmailController
     * @param emailService to use
     */
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }


    /**
     * Method to send the invite code to a list of
     * participant's emails
     * @param inviteCode code to join event
     * @param emails list of emails to send
     * @return response
     */
    @PostMapping(path = {"/{inviteID}"})
    public ResponseEntity<?> sendInvites(@PathVariable("inviteID") String inviteCode,
                                         @RequestBody List<String> emails){
        if(!emails.isEmpty()){
            for(String email : emails){
                emailService.sendEmail(email, "Invite code",
                        "The invite code to join the event is " + inviteCode);
            }
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
    }


    //TODO Send payment reminder method

}