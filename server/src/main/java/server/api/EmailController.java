package server.api;


import commons.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.EmailService;
import server.Services.EventService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/email")
public class EmailController {

    private EmailService emailService;
    private EventService eventService;

    @Value("${server.url}")
    private String serverUrl;

    /**
     * Constructor for EmailController
     * @param emailService to use
     * @param eventService to ue
     */
    public EmailController(EmailService emailService, EventService eventService) {
        this.emailService = emailService;
        this.eventService = eventService;
    }


    /**
     * Method to send the invite code to a list of
     * participant's emails
     * @param inviteCode code to join event
     * @param emails list of emails to send
     * @param creatorName the name of the person who sends the emails
     * @return response
     */
    @PostMapping(path = {"/{inviteID}"})
    public ResponseEntity<?> sendInvites(@PathVariable("inviteID") String inviteCode,
                                         @RequestBody List<String> emails,
                                         @RequestParam String creatorName,
                                         @RequestParam String creatorEmail){
        Optional<Event> eventOptional = eventService.findByInviteCode(inviteCode);
        if(!emails.isEmpty() && eventOptional.isPresent()){
            for(String email : emails){
                String body = "";
                if(creatorName.equals("null")){
                    body += "You are invited to " + eventOptional.get().getTitle() + " event. " +
                            "You can connect to this event using the following code: " + inviteCode+
                            ". The server URL is: " + serverUrl;
                }
                else{
                    body += creatorName + " invited you to " + eventOptional.get().getTitle()
                            + " event." +
                            " You can connect to this event using the following code: "
                            + inviteCode + ". The server URL is: " + serverUrl;;
                }
                emailService.sendEmail(email, eventOptional.get().getTitle()
                                + " ( " + inviteCode + " ) - Splitty",
                        body, creatorEmail);
            }
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
    }


//    /**
//     * Method to send the invite code to a list of
//     * participant's emails
//     * @param inviteCode code to join event
//     * @param emails list of emails to send
//     * @param creatorName the name of the person who sends the emails
//     * @return response
//     */
//    @PostMapping(path = {"/"})
//    public ResponseEntity<?> sendDefault(@RequestBody String email){
//        Optional<Event> eventOptional = eventService.findByInviteCode(inviteCode);
//        if(!emails.isEmpty() && eventOptional.isPresent()){
//            for(String email : emails){
//                String body = "";
//                if(creatorName.equals("null")){
//                    body += "You are invited to " + eventOptional.get().getTitle() + " event. " +
//                            "You can connect to this event using the following code: " + inviteCode+
//                            ". The server URL is: " + serverUrl;
//                }
//                else{
//                    body += creatorName + " invited you to " + eventOptional.get().getTitle()
//                            + " event." +
//                            " You can connect to this event using the following code: "
//                            + inviteCode + ". The server URL is: " + serverUrl;;
//                }
//                if(creatorEmail.equals("null")){ //If the creators email is not set
//                    creatorEmail = "";
//                }
//                emailService.sendEmail(email, eventOptional.get().getTitle()
//                                + " ( " + inviteCode + " ) - Splitty",
//                        body, creatorName);
//            }
//            return ResponseEntity.ok(HttpStatus.OK);
//        }
//        return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
//    }

    /**
     * Method to send the invite code to a list of
     * participant's emails
     * @param inviteCode code to join event
     * @param emails list of emails to send
     * @param creatorName the name of the person who sends the emails
     * @return response
     */
    @PostMapping(path = {"/"})
    public ResponseEntity<?> sendRemainder(@PathVariable("creatorEmail") String inviteCode,
                                         @RequestBody List<String> emails,
                                         @RequestParam String creatorName, @RequestParam String creatorEmail){
        Optional<Event> eventOptional = eventService.findByInviteCode(inviteCode);
        if(!emails.isEmpty() && eventOptional.isPresent()){
            for(String email : emails){
                String body = "";
                if(creatorName.equals("null")){
                    body += "You are invited to " + eventOptional.get().getTitle() + " event. " +
                            "You can connect to this event using the following code: " + inviteCode+
                            ". The server URL is: " + serverUrl;
                }
                else{
                    body += creatorName + " invited you to " + eventOptional.get().getTitle()
                            + " event." +
                            " You can connect to this event using the following code: "
                            + inviteCode + ". The server URL is: " + serverUrl;;
                }
                emailService.sendEmail(email, eventOptional.get().getTitle()
                                + " ( " + inviteCode + " ) - Splitty",
                        body, creatorEmail);
            }
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
    }


    //TODO Send payment reminder method

}