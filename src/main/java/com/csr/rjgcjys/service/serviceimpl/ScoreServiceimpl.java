package com.csr.rjgcjys.service.serviceimpl;

import com.csr.rjgcjys.entities.Score;
import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.mapper.ScoreMapper;
import com.csr.rjgcjys.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("scoreService")
public class ScoreServiceimpl implements ScoreService{
    @Autowired
    private ScoreMapper scoreMapper;
    @Override
    public List<User> findStudentsAll() {
        return scoreMapper.findStudentsAll();
    }

    @Override
    public int insertStudentsScore(String username, String name, String sclass, Integer usualScore, Integer examScore, Integer finalScore,String tUsername) {
        return scoreMapper.insertTeachersScore(username, name, sclass, usualScore, examScore, finalScore,tUsername);
    }

    @Override
    public List<Score> findScoreByUsername(String username) {
        return scoreMapper.findScoreByUsername(username);
    }

    @Override
    public List<Score> findStudentsScoreAll(String tUsername) {
        return scoreMapper.findStudentsScoreAll(tUsername);
    }

    @Override
    public Score findScoreByCid(Integer cid) {
        return scoreMapper.findScoreByCid(cid);
    }

    @Override
    public int teachersUpdateScore(Score score) {
        return scoreMapper.teachersUpdateScore(score);
    }

    @Override
    public int deleteScoreByCid(Integer cid) {
        return scoreMapper.deleteScoreByCid(cid);
    }


}
