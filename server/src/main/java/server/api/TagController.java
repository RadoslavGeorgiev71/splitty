package server.api;

import commons.Debt;
import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private TagService tagService;

    /**
     * Constructor for tag controller
     * @param tagService - the tag service for the tag repository
     */
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Get all tags
     * @return all tags
     */
    @GetMapping(path = {"", "/"})
    public List<Tag> getAll() {
        return tagService.getAll();
    }

    /**
     * Save a new tag if it is valid and returns a response
     * @param tag - the debt to be saved
     * @return response entity with "ok" status message if it is successful,
     * "bad request" message otherwise
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Tag> add(@RequestBody Tag tag){
        Tag tagSaved = tagService.add(tag);
        if(tagSaved == null) {
            return ResponseEntity.badRequest().build();
        }
        else {
            return ResponseEntity.ok(tagSaved);
        }
    }

    /**
     * Deletes a tag by id if it exists
     * @param id - the id of the tag we search for
     * @return "Successful delete" response
     * if delete was successful, "Bad request" otherwise
     */
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        Tag deletedTag = tagService.delete(id);
        if (deletedTag == null) {
            return ResponseEntity.badRequest().build();
        }
        else {
            return ResponseEntity.ok().body("Successful delete");
        }
    }
}
