package com.ganzi.backend.animal.infrastructure.mapper;

import com.ganzi.backend.animal.domain.Animal;
import com.ganzi.backend.animal.domain.AnimalType;
import com.ganzi.backend.animal.domain.NeuterStatus;
import com.ganzi.backend.animal.domain.ProcessState;
import com.ganzi.backend.animal.domain.Sex;
import com.ganzi.backend.animal.infrastructure.dto.AnimalApiItem;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AnimalMapper {

    public Animal toEntity(AnimalApiItem item) {
        Animal animal = Animal.builder()
                .desertionNo(item.getDesertionNo())
                .rfidCode(item.getRfidCd())
                .breedName(item.getKindNm())
                .animalType(AnimalType.fromCode(item.getUpKindCd()))
                .age(item.getAge())
                .sex(Sex.fromCode(item.getSexCd()))
                .neuterStatus(NeuterStatus.fromCode(item.getNeuterYn()))
                .weight(item.getWeight())
                .color(item.getColorCd())
                .foundDate(item.getHappenDt())
                .foundPlace(item.getHappenPlace())
                .noticeStartDate(item.getNoticeSdt())
                .noticeEndDate(item.getNoticeEdt())
                .status(ProcessState.fromApiValue(item.getProcessState()))
                .shelterName(item.getCareNm())
                .shelterTel(item.getCareTel())
                .shelterAddress(item.getCareAddr())
                .province(extractProvince(item.getOrgNm()))
                .city(extractCity(item.getOrgNm()))
                .specialMark(item.getSpecialMark())
                .healthInfo(item.getSfeHealth())
                .vaccination(item.getVaccinationChk())
                .healthCheck(item.getHealthChk())
                .personality(item.getSfeSoci())
                .build();

        List<String> imageUrls = extractImageUrls(item);
        animal.addImages(imageUrls);

        return animal;
    }

    private String extractProvince(String orgNm) {
        if (orgNm == null || orgNm.isBlank()) {
            return null;
        }
        String[] parts = orgNm.split(" ");
        return parts[0];
    }

    private String extractCity(String orgNm) {
        if (orgNm == null || orgNm.isBlank()) {
            return null;
        }
        String[] parts = orgNm.split(" ");
        if (parts.length >= 2) {
            return parts[1];
        }
        return null;
    }

    private List<String> extractImageUrls(AnimalApiItem item) {
        List<String> urls = new ArrayList<>();

        addIfNotEmpty(urls, item.getPopfile1());
        addIfNotEmpty(urls, item.getPopfile2());
        addIfNotEmpty(urls, item.getPopfile3());
        addIfNotEmpty(urls, item.getPopfile4());
        addIfNotEmpty(urls, item.getPopfile5());
        addIfNotEmpty(urls, item.getPopfile6());
        addIfNotEmpty(urls, item.getPopfile7());
        addIfNotEmpty(urls, item.getPopfile8());

        return urls;
    }

    private void addIfNotEmpty(List<String> list, String value) {
        if (value != null && !value.isBlank()) {
            list.add(value);
        }
    }
}
