package com.csr.rjgcjys.service.serviceimpl;

import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.entities.Word;
import com.csr.rjgcjys.mapper.WordMapper;
import com.csr.rjgcjys.service.WordService;
import org.apache.ibatis.annotations.Arg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("wordService")
public class WordServiceimpl implements WordService {

    @Autowired
    private WordMapper wordMapper;

    @Override
    public int insertWord(String username, String name, String sclass, String subject, String teacher, String fileName,String extName, String url) {
        return wordMapper.insertWord(username,name,sclass,subject,teacher,fileName,extName,url);
    }

    @Override
    public Word findWordBySid(Integer sid) {
        return wordMapper.findWordBySid(sid);
    }

    @Override
    public List<Word> findWordAll() {
        return wordMapper.findWordAll();
    }

    @Override
    public List<User> findUserByName() {
        return wordMapper.findUserByName();
    }

    @Override
    public List<Word> findWordByUserName(String username) {
        return wordMapper.findWordByUserName(username);
    }

    @Override
    public List<Word> findWordByName(String teacher) {
        return wordMapper.findWordByName(teacher);
    }

    @Override
    public int deleteWordBySid(Integer sid) {
        return wordMapper.deleteWordBySid(sid);
    }

    @Override
    public User findUserTelephone(String username) {
        return wordMapper.findUserTelephone(username);
    }

    @Override
    public int updateWordBySid(Word word) {
        return wordMapper.updateWordBySid(word);
    }

    @Override
    public List<Word> findWordByScore(String teacher) {
        return wordMapper.findWordByScore(teacher);
    }

    @Override
    public Integer countWord(String username) {
        return wordMapper.countWord(username);
    }
}
