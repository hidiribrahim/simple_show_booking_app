package com.hidir.show.service;


import com.hidir.show.dto.ShowDto;
import com.hidir.show.entity.Show;
import com.hidir.show.exception.ShowDoesntExistException;
import com.hidir.show.repository.ShowRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowService {
    private final ModelMapper mapper;

    private final ShowRepository showRepository;

    public ShowService(ModelMapper mapper, ShowRepository showRepository) {
        this.mapper = mapper;
        this.showRepository = showRepository;
    }

    public List<ShowDto> getAllShows() {
        List<Show> shows = showRepository.findAll();
        return shows.stream().map(show->mapper.map(show,ShowDto.class)).collect(Collectors.toList());
    }

    public void save(ShowDto showDto) {

        Show show = mapper.map(showDto, Show.class);
        showRepository.save(show);
    }

    public ShowDto findShowById(int showNumber) {
        return showRepository.findById(showNumber)
                .map(showEntity->mapper.map(showEntity,ShowDto.class))
                .orElseThrow(() -> new ShowDoesntExistException("Show doesnt exist")) ;
    }

}
