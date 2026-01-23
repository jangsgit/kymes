package com.dae.kdmes.Mapper.Cms;

import com.dae.kdmes.DTO.App01.Index01Dto;
import com.dae.kdmes.DTO.App01.Index02Dto;
import com.dae.kdmes.DTO.Cms.CmsIndex01Dto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CmsIndex01Mapper {

    public Integer getSHOTDATA_wotqty(CmsIndex01Dto parm) ;




    public List<CmsIndex01Dto> getSHOTDATA_addinfo(CmsIndex01Dto parm) ;
    public List<CmsIndex01Dto> getSHOTDATA_machine(CmsIndex01Dto parm) ;
    public List<CmsIndex01Dto> getSHOTDATA_realtime(CmsIndex01Dto parm) ;
    public List<CmsIndex01Dto> getSHOTDATA_realtime_chart(CmsIndex01Dto parm) ;

    public List<CmsIndex01Dto> getSHOTDATA_machine_eq(CmsIndex01Dto parm) ;
    public List<CmsIndex01Dto> getSHOTDATA_realtime_eq(CmsIndex01Dto parm) ;
    public List<CmsIndex01Dto> getSHOTDATA_realtime_chart_eq(CmsIndex01Dto parm) ;


    public List<CmsIndex01Dto> GetADD_INFO(CmsIndex01Dto parm) ;

}
