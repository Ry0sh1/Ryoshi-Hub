package com.Ryoshi.RyoshiHub.popsauce.service;

import com.Ryoshi.RyoshiHub.popsauce.entity.Setting;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SettingService {

    private final HashMap<Long, Setting> settingMap;
    private Long currentId;

    public SettingService() {
        this.settingMap = new HashMap<>();
        this.currentId = 0L;
    }

    public void save(Setting setting) {
        setting.setId(currentId);
        currentId++;
        settingMap.put(setting.getId(), setting);
    }

    public void deleteById(Long id) {
        settingMap.remove(id);
    }

}
