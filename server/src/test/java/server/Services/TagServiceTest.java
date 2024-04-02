package server.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import server.Repositories.TestTagRepository;
import server.database.TagRepository;

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

}
