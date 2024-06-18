package com.Ryoshi.RyoshiHub.popsauce.repository;

import com.Ryoshi.RyoshiHub.popsauce.entity.Setting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends CrudRepository<Setting, Long> {
}
