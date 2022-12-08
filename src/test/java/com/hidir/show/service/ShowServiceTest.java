package com.hidir.show.service;

import com.hidir.show.dto.ShowDto;
import com.hidir.show.entity.Show;
import com.hidir.show.exception.ShowDoesntExistException;
import com.hidir.show.repository.ShowRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShowServiceTest {

    @InjectMocks
    ShowService showService;

    @Mock
    ShowRepository showRepository;

    @Spy
    ModelMapper mapper = new ModelMapper();

    @Test
    void getAllShows() {
        List<Show> shows = List.of(new Show(1,"the show",1,10,2),new Show(2,"the second show",2,10,2));
        when(showRepository.findAll()).thenReturn(shows);
        List<ShowDto> toAssert = showService.getAllShows();
        assertEquals(2,toAssert.size());
    }


    @Test
    void shouldFindShowById() {
        Show show = new Show(1,"the show",1,10,2);
        when(showRepository.findById(1)).thenReturn(Optional.of(show));
        ShowDto showDto = showService.findShowById(1);
        assertEquals(show.getShowName(),showDto.getShowName());
        assertEquals(show.getRowCount(),showDto.getRowCount());
        assertEquals(show.getSeatsPerRow(),showDto.getSeatsPerRow());
        assertEquals(show.getCancellationWindow(),showDto.getCancellationWindow());

    }

    @Test
    void givenNonExistantShowId_throwShowDoesntExist() {
        Show show = new Show(1,"the show",1,10,2);
        when(showRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ShowDoesntExistException.class, ()->showService.findShowById(1));
    }


    @Test
    void save() {
        ShowDto showDto = new ShowDto(1,"the show",1,10,2);
        showService.save(showDto);
        verify(showRepository,times(1)).save(any(Show.class));
    }
}