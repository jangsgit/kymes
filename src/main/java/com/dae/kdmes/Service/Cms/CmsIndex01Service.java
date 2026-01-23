package com.dae.kdmes.Service.Cms;

import com.dae.kdmes.DTO.App02.Index10Dto;
import com.dae.kdmes.DTO.Cms.CmsIndex01Dto;
import com.dae.kdmes.Mapper.Cms.CmsIndex01Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service("CmsIndex01Service")
public class CmsIndex01Service {

    @Autowired
    CmsIndex01Mapper  cmsindexMapper;

    public Integer getSHOTDATA_wotqty(CmsIndex01Dto parm){
        return cmsindexMapper.getSHOTDATA_wotqty(parm);
    }

    public List<CmsIndex01Dto> getSHOTDATA_realtime(CmsIndex01Dto parm){
        return cmsindexMapper.getSHOTDATA_realtime(parm);
    }

    public List<CmsIndex01Dto> getSHOTDATA_realtime_chart(CmsIndex01Dto parm){
        return cmsindexMapper.getSHOTDATA_realtime_chart(parm);
    }
    public List<CmsIndex01Dto> getSHOTDATA_machine(CmsIndex01Dto parm){
        return cmsindexMapper.getSHOTDATA_machine(parm);
    }

    public List<CmsIndex01Dto> getSHOTDATA_addinfo(CmsIndex01Dto parm){
        return cmsindexMapper.getSHOTDATA_addinfo(parm);
    }


    public List<CmsIndex01Dto> getSHOTDATA_realtime_eq(CmsIndex01Dto parm){
        return cmsindexMapper.getSHOTDATA_realtime_eq(parm);
    }

    public List<CmsIndex01Dto> getSHOTDATA_realtime_chart_eq(CmsIndex01Dto parm){
        return cmsindexMapper.getSHOTDATA_realtime_chart_eq(parm);
    }
    public List<CmsIndex01Dto> getSHOTDATA_machine_eq(CmsIndex01Dto parm){
        return cmsindexMapper.getSHOTDATA_machine_eq(parm);
    }

    public List<CmsIndex01Dto> GetADD_INFO(CmsIndex01Dto parm){
        return cmsindexMapper.GetADD_INFO(parm);
    }
}
