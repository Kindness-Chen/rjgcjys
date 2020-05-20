package com.csr.rjgcjys.service;

import com.csr.rjgcjys.entities.Achievement;


import java.util.List;

public interface AchievementService {
    int insertAchievement(Achievement achievement);
    List<Achievement> teachersLookAchievement(String username);
    Achievement findAchievementByZid(Integer zid);
    List<Achievement>findAchievementAll();
    int deleteAchievementByZid(Integer zid);
    int directorsUpdateAchievement(Achievement achievement);
    List<Achievement>directorsLookAchievementAll();
}
