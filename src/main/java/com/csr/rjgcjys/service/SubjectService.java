package com.csr.rjgcjys.service;

import com.csr.rjgcjys.entities.Subject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SubjectService {
    int insertSubject(Subject subject);
    List<Subject> findSubjectAll();
    Subject findSubjectBySid(Integer sid);
    int directorsUpdateSubject(Subject subject);
    int deleteSubjectBySid(Integer sid);
    boolean batchImport(String fileName, MultipartFile file) throws Exception;
    List<Subject>findSclassByName(String name);
    List<Subject>teacherFindSubjectByName(String name);

}
