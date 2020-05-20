package com.csr.rjgcjys.service.serviceimpl;

import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.mapper.UserMapper;
import com.csr.rjgcjys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceimpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int insertUser(User user){
        return userMapper.insertUser(user);
    }

    @Override
    public List<User> findUserAll() {
        return userMapper.findUserAll();
    }

    @Override
    public List<User> findUserById(Integer uid) {
        return userMapper.findUserById(uid);
    }

    @Override
    public int updateUser(User user) {
       return userMapper.updateUser(user);
    }

    @Override
    public int deleteUser(Integer uid) {
        return userMapper.deleteUser(uid);
    }

    @Override
    public List<User> findUserByIdentity( String username,String password,String identity) {
        return userMapper.findUserByIdentity(username,password,identity);
    }

    @Override
    public int countStudents() {
        return userMapper.countStudents();
    }

    @Override
    public int countTeachers() {
        return userMapper.countTeachers();
    }

    @Override
    public int countDirectors() {
        return userMapper.countDirectors();
    }

    @Override
    public int countAdmins() {
        return userMapper.countAdmins();
    }

    @Override
    public User findUserByUid(Integer uid) {
        return userMapper.findUserByUid(uid);
    }


}
