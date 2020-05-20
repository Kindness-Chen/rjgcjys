package com.csr.rjgcjys.service.serviceimpl;

import com.csr.rjgcjys.entities.Train;
import com.csr.rjgcjys.mapper.TrainMapper;
import com.csr.rjgcjys.service.TrainService;
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

@Service("trainService")
public class TrainServiceimpl implements TrainService {
    @Autowired
    private TrainMapper trainMapper;
    @Override
    public int insertTrain(Train train) {
        return trainMapper.insertTrain(train);
    }

    @Override
    public List<Train> teachersFindTrainByName(String name) {
        return trainMapper.teachersFindTrainByName(name);
    }

    @Override
    public Train findTrainByRid(Integer rid) {
        return trainMapper.findTrainByRid(rid);
    }

    @Override
    public int teachersUpdateStudy(Train train) {
        return trainMapper.teachersUpdateStudy(train);
    }

    @Override
    public int deleteTrainByRid(Integer rid) {
        return trainMapper.deleteTrainByRid(rid);
    }

    @Override
    public List<Train> directorsFindTrainAll() {
        return trainMapper.directorsFindTrainAll();
    }

    @Override
    public List<Train> findSclassAll() {
        return trainMapper.findSclassAll();
    }

    @Override
    public List<Train> findClassName() {
        return trainMapper.findClassName();
    }

    @Override
    public List<Train> findCodeAll() {
        return trainMapper.findCodeAll();
    }

    @Override
    public List<Train> findTrainByCode(String code) {
        return trainMapper.findTrainByCode(code);
    }
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @Override
    public boolean batchImport(String fileName, MultipartFile file) throws Exception {

        boolean notNull = false;
        List<Train> traintList = new ArrayList<Train>();
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
        Train train;
        for (int r = 2; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null){
                continue;
            }
            train = new Train();
            String code = row.getCell(0).getStringCellValue();
            String sclass = row.getCell(1).getStringCellValue();
            String name = row.getCell(2).getStringCellValue();
            row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
            String weektime = row.getCell(3).getStringCellValue();
            row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
            String strattime = row.getCell(4).getStringCellValue();
            row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
            String  credit =  row.getCell(5).getStringCellValue();
            row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
            String totaltime = row.getCell(6).getStringCellValue();
            row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
            String lecturetime = row.getCell(7).getStringCellValue();
            row.getCell(8).setCellType(Cell.CELL_TYPE_STRING);
            String  experimenttime = row.getCell(8).getStringCellValue();
            row.getCell(9).setCellType(Cell.CELL_TYPE_STRING);
            String computertime = row.getCell(9).getStringCellValue();
            row.getCell(10).setCellType(Cell.CELL_TYPE_STRING);
            String assessment = row.getCell(10).getStringCellValue();
            String nature = row.getCell(11).getStringCellValue();
            String category = row.getCell(12).getStringCellValue();
            row.getCell(13).setCellType(Cell.CELL_TYPE_STRING);
            String number =row.getCell(13).getStringCellValue();
            String college = row.getCell(14).getStringCellValue();
            String classname = row.getCell(15).getStringCellValue();
            String reson = row.getCell(16).getStringCellValue();

            train.setCode(code);
            train.setSclass(sclass);
            train.setName(name);
            train.setWeektime(weektime);
            train.setStrattime(strattime);
            train.setCredit(credit);
            train.setTotaltime(totaltime);
            train.setLecturetime(lecturetime);
            train.setExperimenttime(experimenttime);
            train.setComputertime(computertime);
            train.setAssessment(assessment);
            train.setNature(nature);
            train.setCategory(category);
            train.setNumber(number);
            train.setCollege(college);
            train.setClassname(classname);
            train.setReson(reson);
            traintList.add(train);
        }
        for (Train trainResord : traintList) {
            trainMapper.insertTrain(trainResord);
        }
        return notNull;
    }
}
