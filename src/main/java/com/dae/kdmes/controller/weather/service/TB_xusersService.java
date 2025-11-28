package com.dae.kdmes.controller.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TB_xusersService {

   // 로그인 한 사람의 사업장 주소 가져오기
  public String getUserAddress(String userid) {
    // TODO: 나중에 TB_XUSERS 테이블 생기면 여기서 실제 DB 조회로 변경
    return "세종특별자치시 연동면 명학산단중로 23";
  }
}
