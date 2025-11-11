package com.ganzi.backend.animal.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimalApiItem {

    private String desertionNo;
    private String rfidCd;
    private String kindNm;
    private String upKindCd;
    private String upKindNm;
    private String age;
    private String sexCd;
    private String neuterYn;
    private String weight;
    private String colorCd;
    private String happenDt;
    private String happenPlace;
    private String noticeSdt;
    private String noticeEdt;
    private String processState;
    private String careNm;
    private String careTel;
    private String careAddr;
    private String orgNm;
    private String specialMark;
    private String sfeHealth;
    private String vaccinationChk;
    private String healthChk;
    private String sfeSoci;

    private String popfile1;
    private String popfile2;
    private String popfile3;
    private String popfile4;
    private String popfile5;
    private String popfile6;
    private String popfile7;
    private String popfile8;
}
