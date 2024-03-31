package server.Services;

import commons.Debt;
import commons.Tag;
import org.springframework.stereotype.Service;
import server.database.TagRepository;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepo;

    /**
     * Constructor for TagService
     * @param tagRepo - the repository for tags
     */
    public TagService(TagRepository tagRepo) {
        this.tagRepo = tagRepo;
    }

    /**
     * Get all tags
     * @return all tags
     */
    public List<Tag> getAll() {
        return tagRepo.findAll();
    }

    /**
     * Save a new tag if it is valid and returns a response
     * @param tag - the debt to be saved
     * @return the debt if the debt was added,
     * null otherwise
     */
    public Tag add(Tag tag) {
        if (tag.getType() == null || tag.getColor() == null) {
            return null;
        }
        return tagRepo.save(tag);
    }

    /**
     * Deletes a tag by given id
     * @param id - the id of the tag we delete
     * @return the tag if it was deleted successfully,
     * null otherwise
     */
    public Tag delete(long id) {
        if (!tagRepo.existsById(id)) {
            return null;
        }
        Tag tag = tagRepo.findById(id).get();
        tagRepo.deleteById(id);
        return tag;
    }
}
