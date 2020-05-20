package com.csr.rjgcjys.service.serviceimpl;

import com.csr.rjgcjys.entities.Subject;
import com.csr.rjgcjys.mapper.SubjectMapper;
import com.csr.rjgcjys.service.SubjectService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service("subjectService")
public class SubjectServiceimpl implements SubjectService {
    @Autowired
    private SubjectMapper subjectMapper;
    @Override
    public int insertSubject(Subject subject) {
        return subjectMapper.insertSubject(subject);
    }

    @Override
    public List<Subject> findSubjectAll() {
        return subjectMapper.findSubjectAll();
    }

    @Override
    public Subject findSubjectBySid(Integer sid) {
        return subjectMapper.findSubjectBySid(sid);
    }

    @Override
    public int directorsUpdateSubject(Subject subject) {
        return subjectMapper.directorsUpdateSubject(subject);
    }

    @Override
    public int deleteSubjectBySid(Integer sid) {
        return subjectMapper.deleteSubjectBySid(sid);
    }

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @Override
    public boolean batchImport(String fileName, MultipartFile file) throws Exception {

        boolean notNull = false;
        List<Subject> subjectList = new ArrayList<Subject>();
//        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
//            throw new MyException("上传文件格式不正确");
//        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        if(sheet!=null){
            notNull = true;
        }
        Subject subject;
        for (int r = 2; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null){
                continue;
            }

            subject = new Subject();

//            if( row.getCell(0).getCellType() !=1){
//                throw new MyException("导入失败(第"+(r+1)+"行,姓名请设为文本格式)");
//            }

            String code = row.getCell(0).getStringCellValue();

            String sclass = row.getCell(1).getStringCellValue();

            String name = row.getCell(2).getStringCellValue();

            String subjectTime = row.getCell(3).getStringCellValue();

            String subjectPlace = row.getCell(4).getStringCellValue();

            String nature = row.getCell(5).getStringCellValue();


//            if(name == null || name.isEmpty()){
//                throw new MyException("导入失败(第"+(r+1)+"行,姓名未填写)");
//            }
            row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
            String  credit =  row.getCell(6).getStringCellValue();
//            if(phone==null || phone.isEmpty()){
//                throw new MyException("导入失败(第"+(r+1)+"行,电话未填写)");
//            }
            row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
            String assessment = row.getCell(7).getStringCellValue();
//            if(add==null){
//                throw new MyException("导入失败(第"+(r+1)+"行,不存在此单位或单位未填写)");
//            }

//            Date date;
//            if(row.getCell(3).getCellType() !=0){
//                throw new MyException("导入失败(第"+(r+1)+"行,入职日期格式不正确或未填写)");
//            }else{
//                date = row.getCell(3).getDateCellValue();
//            }
            row.getCell(8).setCellType(Cell.CELL_TYPE_STRING);
            String totaltime = row.getCell(8).getStringCellValue();
            row.getCell(9).setCellType(Cell.CELL_TYPE_STRING);
            String lecturetime = row.getCell(9).getStringCellValue();
            row.getCell(10).setCellType(Cell.CELL_TYPE_STRING);
            String  experimenttime = row.getCell(10).getStringCellValue();
            row.getCell(11).setCellType(Cell.CELL_TYPE_STRING);
            String computertime = row.getCell(11).getStringCellValue();
            row.getCell(12).setCellType(Cell.CELL_TYPE_STRING);
            String number =row.getCell(12).getStringCellValue();

            String college = row.getCell(13).getStringCellValue();

            String consist = row.getCell(14).getStringCellValue();

            subject.setCode(code);
            subject.setSclass(sclass);
            subject.setName(name);
            subject.setSubjectTime(subjectTime);
            subject.setSubjectPlace(subjectPlace);
            subject.setNature(nature);
            subject.setCredit(credit);
            subject.setAssessment(assessment);
            subject.setTotaltime(totaltime);
            subject.setLecturetime(lecturetime);
            subject.setExperimenttime(experimenttime);
            subject.setComputertime(computertime);
            subject.setNumber(number);
            subject.setCollege(college);
            subject.setConsist(consist);

            subjectList.add(subject);
        }
        for (Subject subjectResord : subjectList) {



                subjectMapper.insertSubject(subjectResord);

        }
        return notNull;
    }

    @Override
    public List<Subject> findSclassByName(String name) {
        return subjectMapper.findSclassByName(name);
    }

    @Override
    public List<Subject> teacherFindSubjectByName(String name) {
        return subjectMapper.teacherFindSubjectByName(name);
    }

}
