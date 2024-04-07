package server.Services;

import commons.Participant;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import server.Repositories.TestTagRepository;
import server.database.TagRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

}
