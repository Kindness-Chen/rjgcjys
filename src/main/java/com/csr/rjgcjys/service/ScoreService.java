package com.csr.rjgcjys.service;

import com.csr.rjgcjys.entities.Score;
import com.csr.rjgcjys.entities.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScoreService {
    List<User>findStudentsAll();
    int insertStudentsScore(String username,String name,String sclass,Integer usualScore,Integer examScore,Integer finalScore,String tUsername);
    List<Score>findScoreByUsername(String username);
    List<Score>findStudentsScoreAll(String tUsername);
    Score findScoreByCid(Integer cid);
    int teachersUpdateScore(Score score);
    int deleteScoreByCid(Integer cid);
}
