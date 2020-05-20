package com.csr.rjgcjys.service;

import com.csr.rjgcjys.entities.Mutual;
import com.csr.rjgcjys.entities.User;


import java.util.List;

public interface MutualService {
    List<User> findUserByIdentity(String username);
    Integer findSumBranch(String username);
    Integer findCountUsername(String username);
    List<Mutual> findCommonMutual(String username,String name1,String year);
    int insertMutual(Mutual mutual);
    List<Mutual>findMutualByUsername(String username);
    List<Mutual>directorsLookMutual();
    Mutual findMutualByMid(Integer mid);
    int directorsUpdateMutual(Mutual mutual);
    List<Mutual>findMutualByTotal(String username);
    int oneMutual(List<Mutual> mutuals);
    List<Mutual> findMutualByAll();
    List<Mutual>findDirectorHaveMutual();
    List<Mutual>findTeacherHaveMutual(String name1);
}
