package com.first.demo.service;

import com.first.demo.domain.CollectionBin;
import com.first.demo.repository.CollectionBinRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;

@Service
public class CSVImporter {

    private final CollectionBinRepository collectionBinRepository;

    @Autowired
    public CSVImporter(CollectionBinRepository collectionBinRepository) {
        this.collectionBinRepository = collectionBinRepository;
    }

    public void importCSV(MultipartFile file) throws IOException {
        // CSV 파일 읽기
        try (Reader reader = new InputStreamReader(file.getInputStream(), "UTF-8")) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()  // 첫 번째 줄을 헤더로 처리
                    .withIgnoreHeaderCase()
                    .withTrim()
                    .parse(reader);

            for (CSVRecord record : records) {
                try {
                    // CSV 파일의 필드 이름과 일치하는 데이터 가져오기
                    String locationName = record.get("행정동");
                    String address = record.get("주소");
                    double latitude = Double.parseDouble(record.get("위도"));
                    double longitude = Double.parseDouble(record.get("경도"));

                    // CollectionBin 객체 생성
                    CollectionBin collectionBin = new CollectionBin();
                    collectionBin.setLocationName(locationName);
                    collectionBin.setAddress(address);
                    collectionBin.setLatitude(latitude);
                    collectionBin.setLongitude(longitude);
                    collectionBin.setCreatedAt(LocalDateTime.now());
                    collectionBin.setModifiedAt(LocalDateTime.now());

                    // DB에 저장
                    collectionBinRepository.save(collectionBin);
                } catch (NumberFormatException e) {
                    System.err.println("잘못된 숫자 형식: " + record);
                } catch (Exception e) {
                    System.err.println("CSV 데이터 변환 중 오류 발생: " + record);
                }
            }
        }
    }
}
