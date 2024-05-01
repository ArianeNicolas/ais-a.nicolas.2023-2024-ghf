package es.codeurjc.ais.nitflex.integration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import es.codeurjc.ais.nitflex.utils.UrlUtils;

class UrlUtilsTests {

    @Test
    void invalidUrl(){
        UrlUtils urlUtils = new UrlUtils();
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> urlUtils.checkValidImageURL("this-is-not-an-url"));
        assertEquals(HttpStatus.BAD_REQUEST+" \"The url format is not valid\"", ex.getMessage());
    }

    @Test
    void notAnImage(){
        UrlUtils urlUtils = new UrlUtils();
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> urlUtils.checkValidImageURL("https://www.themoviedb.org/image.png"));
        assertEquals(HttpStatus.BAD_REQUEST+" \"Url resource does not exists\"", ex.getMessage());
    }
    
}
