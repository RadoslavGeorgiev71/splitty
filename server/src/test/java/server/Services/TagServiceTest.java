package server.Services;

import commons.Participant;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import server.Repositories.TestTagRepository;
import server.database.TagRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TagServiceTest {
    @Mock
    private TagRepository tagRepo;
    private TagService sut;

    @BeforeEach
    void TagServiceSetUp() {
        tagRepo = new TestTagRepository();
        sut = new TagService(tagRepo);
    }

    @Test
    void testTagServiceConstructor() {
        assertNotNull(sut);
    }

    @Test
    void testCreateTag() {
        Tag t1 = new Tag("Food", "Green");
        Tag savedTag = sut.add(t1);
        assertEquals(t1, savedTag);
        List<Tag> tags = tagRepo.findAll();
        assertEquals(tags, List.of(t1));
    }

}
