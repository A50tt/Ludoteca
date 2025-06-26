package com.ccsw.tutorial.author;

import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.game.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorGameHelperService {

    @Autowired
    GameService gameService;

    List<Game> findGamesByAuthor(Long id) {
        return gameService.findGamesByAuthor(id);
    }
}
