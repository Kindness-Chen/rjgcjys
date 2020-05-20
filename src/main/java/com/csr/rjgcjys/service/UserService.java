package com.csr.rjgcjys.service;

import com.csr.rjgcjys.entities.User;

import java.util.List;

public interface UserService {
    int insertUser(User user);
    List<User> findUserAll();
    List<User> findUserById(Integer uid);
    int updateUser(User user);
    int deleteUser(Integer uid);
    List<User> findUserByIdentity(String username,String password,String identity);
    int countStudents();
    int countTeachers();
    int countDirectors();
    int countAdmins();
    User findUserByUid(Integer uid);
}
