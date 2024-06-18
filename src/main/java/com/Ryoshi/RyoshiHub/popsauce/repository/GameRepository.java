package com.Ryoshi.RyoshiHub.popsauce.repository;

import com.Ryoshi.RyoshiHub.popsauce.entity.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
    Optional<Game> findByCode(String code);

    List<Game> findAllByStarted(Boolean started);

}
