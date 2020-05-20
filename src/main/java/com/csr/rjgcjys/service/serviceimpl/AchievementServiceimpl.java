package com.csr.rjgcjys.service.serviceimpl;

import com.csr.rjgcjys.entities.Achievement;
import com.csr.rjgcjys.mapper.AchievementMapper;
import com.csr.rjgcjys.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("achievementService")
public class AchievementServiceimpl implements AchievementService {
    @Autowired
    private AchievementMapper achievementMapper;

    @Override
    public int insertAchievement(Achievement achievement) {
        return achievementMapper.insertAchievement(achievement);
    }

    @Override
    public List<Achievement> teachersLookAchievement(String username) {
        return achievementMapper.teachersLookAchievement(username);
    }

    @Override
    public Achievement findAchievementByZid(Integer zid) {
        return achievementMapper.findAchievementByZid(zid);
    }

    @Override
    public List<Achievement> findAchievementAll() {
        return achievementMapper.findAchievementAll();
    }

    @Override
    public int deleteAchievementByZid(Integer zid) {
        return achievementMapper.deleteAchievementByZid(zid);
    }

    @Override
    public int directorsUpdateAchievement(Achievement achievement) {
        return achievementMapper.directorsUpdateAchievement(achievement);
    }

    @Override
    public List<Achievement> directorsLookAchievementAll() {
        return achievementMapper.directorsLookAchievementAll();
    }
}
