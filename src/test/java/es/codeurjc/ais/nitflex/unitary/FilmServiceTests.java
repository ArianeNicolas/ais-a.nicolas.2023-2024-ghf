package es.codeurjc.ais.nitflex.unitary;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import es.codeurjc.ais.nitflex.film.Film;
import es.codeurjc.ais.nitflex.film.FilmRepository;
import es.codeurjc.ais.nitflex.film.FilmService;
import es.codeurjc.ais.nitflex.notification.NotificationService;
import es.codeurjc.ais.nitflex.utils.UrlUtils;

class FilmServiceTests {
    
    @Test
    void saveWithValidUrl(){

        Film film = mock(Film.class);
        when(film.getTitle()).thenReturn("A Fantastic Film");

        FilmRepository filmRepository = mock(FilmRepository.class);
        when(filmRepository.save(film)).thenReturn(film);

        UrlUtils urlUtils = mock(UrlUtils.class);

        NotificationService notifService = mock(NotificationService.class);

        FilmService filmService = new FilmService(filmRepository, notifService, urlUtils);
        filmService.save(film);

        verify(notifService).notify("Film Event: Film with title="+film.getTitle()+" was created");
        verify(filmRepository).save(film);

    }

    @Test
    void saveWithInvalidUrl(){

        Film film = mock(Film.class);
        when(film.getTitle()).thenReturn("A Fantastic Film");
        when(film.getUrl()).thenReturn("anInvalidUrl");
        when(film.getReleaseYear()).thenReturn(2000);

        FilmRepository filmRepository = mock(FilmRepository.class);
        when(filmRepository.save(film)).thenReturn(film);

        UrlUtils urlUtils = mock(UrlUtils.class);
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "The url format is not valid")).when(urlUtils).checkValidImageURL("anInvalidUrl");
        
        NotificationService notifService = mock(NotificationService.class);

        FilmService filmService = new FilmService(filmRepository, notifService, urlUtils);
        assertThrows(ResponseStatusException.class, () -> filmService.save(film));

        verify(notifService, never()).notify(anyString());
        verify(filmRepository, never()).save(any());

    }

    @Test
    void invalidReleaseYear(){
        Film film = mock(Film.class);
        when(film.getTitle()).thenReturn("A Fantastic Film");
        when(film.getReleaseYear()).thenReturn(1200);

        FilmRepository filmRepository = mock(FilmRepository.class);
        when(filmRepository.save(film)).thenReturn(film);
        
        NotificationService notifService = mock(NotificationService.class);
        UrlUtils urlUtils = mock(UrlUtils.class);

        FilmService filmService = new FilmService(filmRepository, notifService, urlUtils);
        assertThrows(ResponseStatusException.class, () -> filmService.save(film));

        verify(filmRepository, never()).save(any());
        verify(notifService, never()).notify(anyString());
        verify(urlUtils, never()).checkValidImageURL(anyString());
    }

}
