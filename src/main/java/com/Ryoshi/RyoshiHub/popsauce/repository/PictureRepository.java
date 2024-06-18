package com.Ryoshi.RyoshiHub.popsauce.repository;

import com.Ryoshi.RyoshiHub.popsauce.entity.Picture;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureRepository extends CrudRepository<Picture, Long> {

    List<Picture> findAllByCategory(String category);

}
