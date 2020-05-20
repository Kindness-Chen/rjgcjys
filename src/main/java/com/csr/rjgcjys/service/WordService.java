package com.csr.rjgcjys.service;

import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.entities.Word;

import java.util.List;

public interface WordService {
    int insertWord(String username,String name,String sclass,String subject,String teacher,String fileName,String extName,String url);
    Word findWordBySid(Integer sid);
    List<Word>findWordAll();
    List<User>findUserByName();
    List<Word>findWordByUserName(String username);
    List<Word>findWordByName(String teacher);
    int deleteWordBySid(Integer sid);
    User findUserTelephone(String username);
    int updateWordBySid(Word word);
    List<Word>findWordByScore(String teacher);
    Integer countWord(String username);
}
