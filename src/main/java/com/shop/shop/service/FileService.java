package com.shop.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

// 이미지 파일 저장 로직을 담당할 Service 객체
// 파일 저장은 DB 에 저장되는 것이 아님 -> Repository 필요 없음 -> FileOutputStream가 대신함

@Service
@Log
public class FileService {

    // 이미지 파일 업로드
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws IOException {

        // UUID 를 이용하여 파일명 새로 생성
        // UUID - 서로 다른 객체들을 구별하기 위한 클래스
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension; // 파일명

        // 경로 + 파일명
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        // FileOutputStream 객체를 이용하여 파일을 저장
        FileOutputStream fos = new FileOutputStream((fileUploadFullUrl));
        fos.write(fileData);
        fos.close();

        return savedFileName;
    }


    // 이미지 파일 삭제
    public void deleteFile(String filePath) {

        File deleteFile = new File(filePath);

        if(deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
