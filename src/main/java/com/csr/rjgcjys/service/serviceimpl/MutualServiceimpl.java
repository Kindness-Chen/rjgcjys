package com.csr.rjgcjys.service.serviceimpl;

import com.csr.rjgcjys.entities.Mutual;
import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.mapper.MutualMapper;
import com.csr.rjgcjys.service.MutualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("mutualService")
public class MutualServiceimpl implements MutualService {
    @Autowired
    private MutualMapper mutualMapper;
    @Override
    public List<User> findUserByIdentity(String username) {
        return mutualMapper.findUserByIdentity(username);
    }

    @Override
    public Integer findSumBranch(String username) {
        return mutualMapper.findSumBranch(username);
    }

    @Override
    public Integer findCountUsername(String username) {
        return mutualMapper.findCountUsername(username);
    }

    @Override
    public List<Mutual> findCommonMutual(String username, String name1, String year) {
        return mutualMapper.findCommonMutual(username,name1,year);
    }

    @Override
    public int insertMutual(Mutual mutual) {
        return mutualMapper.insertMutual(mutual);
    }

    @Override
    public List<Mutual> findMutualByUsername(String username) {
        return mutualMapper.findMutualByUsername(username);
    }

    @Override
    public List<Mutual> directorsLookMutual() {
        return mutualMapper.directorsLookMutual();
    }

    @Override
    public Mutual findMutualByMid(Integer mid) {
        return mutualMapper.findMutualByMid(mid);
    }

    @Override
    public int directorsUpdateMutual(Mutual mutual) {
        return mutualMapper.directorsUpdateMutual(mutual);
    }

    @Override
    public List<Mutual> findMutualByTotal(String username) {
        return mutualMapper.findMutualByTotal(username);
    }

    @Override
    public int oneMutual(List<Mutual> mutuals) {
        return mutualMapper.oneMutual(mutuals);
    }

    @Override
    public List<Mutual> findMutualByAll() {
        return mutualMapper.findMutualByAll();
    }

    @Override
    public List<Mutual> findDirectorHaveMutual() {
        return mutualMapper.findDirectorHaveMutual();
    }

    @Override
    public List<Mutual> findTeacherHaveMutual(String name1) {
        return mutualMapper.findTeacherHaveMutual(name1);
    }
}
