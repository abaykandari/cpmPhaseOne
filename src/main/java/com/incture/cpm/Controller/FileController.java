package com.incture.cpm.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.incture.cpm.Service.FileService;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam MultipartFile file, @RequestParam String name) {
        try {
            String msg = fileService.addFile(file, name);
            return ResponseEntity.status(HttpStatus.CREATED).body(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Some Error Occured");
        }
    }
}
