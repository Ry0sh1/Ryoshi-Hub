package com.Ryoshi.RyoshiHub.popsauce.repository;

import com.Ryoshi.RyoshiHub.popsauce.entity.Game;
import com.Ryoshi.RyoshiHub.popsauce.entity.GamePicture;
import com.Ryoshi.RyoshiHub.popsauce.entity.Picture;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GamePictureRepository extends CrudRepository<GamePicture, Long> {

    List<GamePicture> findAllByGame(Game game);
    Optional<GamePicture> findByGameAndPlace(Game game, int place);
    Optional<GamePicture> findByGameAndPicture(Game game, Picture picture);

    int findMaxPlaceByGame(Game games);

}
