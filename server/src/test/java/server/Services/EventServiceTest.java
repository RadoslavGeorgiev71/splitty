package server.Services;

import commons.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.EventRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventServiceTest {
    @Mock
    private EventRepository eventRepo;

    @InjectMocks
    private EventService sut;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testEventServiceConstructor() {
        assertNotNull(sut);
    }

    @Test
    void testFindAll() {
        Event event = new Event();
        event.setInviteCode("123");
        when(eventRepo.findAll()).thenReturn(Arrays.asList(event));
        List<Event> events = sut.findAll();
        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals(event, events.get(0));
    }

    @Test
    void testCreateEvent() {
        Event event = new Event();
        event.setInviteCode("123");
        when(eventRepo.save(any(Event.class))).thenReturn(event);
        Event result = sut.create(event);
        assertNotNull(result);
        assertEquals("123", result.getInviteCode());
        verify(eventRepo, times(1)).save(event);
    }

    @Test
    void testUpdateNonExistentEvent() {
        Event updatedEvent = new Event();
        updatedEvent.setInviteCode("1234");
        when(eventRepo.findById(anyLong())).thenReturn(Optional.empty());
        Event result = sut.update(1L, updatedEvent);
        assertNull(result);
        verify(eventRepo, times(0)).save(any(Event.class));
    }

    @Test
    void testFindByInviteCodeAndCreateEvent() {
        Event event = new Event();
        event.setInviteCode("123");
        when(eventRepo.findByInviteCode("123")).thenReturn(Optional.of(event));
        Optional<Event> foundEvent = sut.findByInviteCode("123");
        assertTrue(foundEvent.isPresent());
        assertEquals(event, foundEvent.get());
    }

    @Test
    void testUpdateEvent() {
        Event event = new Event();
        event.setInviteCode("123");
        when(eventRepo.findById(anyLong())).thenReturn(Optional.of(event));
        when(eventRepo.save(any(Event.class))).thenReturn(event);
        Event updatedEvent = new Event();
        updatedEvent.setInviteCode("1234");
        Event result = sut.update(1L, updatedEvent);
        assertNotNull(result);
        assertEquals("1234", result.getInviteCode());
    }

    @Test
    void testFindExistsById(){
        Event event = new Event();
        event.setInviteCode("Whatever");
        when(eventRepo.findById(anyLong())).thenReturn(Optional.of(event));
        when(eventRepo.existsById(anyLong())).thenReturn(true);
        Optional<Event> foundEvent = sut.findById(1L);
        assertTrue(foundEvent.isPresent());
        assertEquals(event, foundEvent.get());
        assertTrue(sut.existsById(1L));
    }

//    @Test
//    void testDeleteEvent(){
//        Event event = new Event();
//        event.setInviteCode("123");
//        doNothing().when(eventRepo).deleteById(anyLong());
//        sut.deleteById(1L);
//        verify(eventRepo, times(1)).deleteById(1L);
//    }

    @Test
    void testFlush() {
        sut.flush();
        verify(eventRepo, times(1)).flush();
    }
}