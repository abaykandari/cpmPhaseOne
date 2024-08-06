package com.incture.cpm.Service;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.incture.cpm.Entity.File;
import com.incture.cpm.Repo.FileRepository;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public String addFile(MultipartFile file, String name) throws SerialException, SQLException, IOException {
        try {
            File newFile = new File();
            newFile.setFileName(name);
            Blob pdfFile = new SerialBlob(file.getBytes());
            newFile.setPdf(pdfFile);
            fileRepository.save(newFile);
            String returnMessage = name + " file saved successfully!!";
            return returnMessage;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error Occurred while saving file!!";
        }
    }
}
