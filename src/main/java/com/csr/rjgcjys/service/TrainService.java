package com.csr.rjgcjys.service;

import com.csr.rjgcjys.entities.Train;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TrainService {
    int insertTrain(Train train);
    List<Train>teachersFindTrainByName(String name);
    Train findTrainByRid(Integer rid);
    int teachersUpdateStudy(Train train);
    int deleteTrainByRid(Integer rid);
    List<Train>directorsFindTrainAll();
    List<Train>findSclassAll();
    List<Train>findClassName();
    List<Train>findCodeAll();
    List<Train> findTrainByCode(String code);
    boolean batchImport(String fileName, MultipartFile file) throws Exception;
}
