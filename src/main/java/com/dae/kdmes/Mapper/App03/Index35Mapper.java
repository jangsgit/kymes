package com.dae.kdmes.Mapper.App03;

import com.dae.kdmes.DTO.App01.IndexCa613Dto;
import com.dae.kdmes.DTO.app03.Index35Dto;
import com.dae.kdmes.DTO.app03.Index59Dto;
import com.dae.kdmes.DTO.app03.IndexJaegotemp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface Index35Mapper {

    public List<Index35Dto> getList(Index35Dto parm) ;

    public List<Index59Dto> SelectJegoIpgo(Index59Dto parm) ;
    public String SelectJegoCheck(Index59Dto parm) ;
    public String SelectMaxSeqIpgo(Index59Dto parm) ;

    public Boolean InsertJegoIpgo(Index59Dto parm) ;
    public Boolean UpdateJegoIpgo(Index59Dto parm) ;

    public Boolean DeleteJaegoIpgo(Index59Dto parm) ;
    public Boolean DeleteJaegoIpgoAcorp(Index59Dto parm) ;

    public List<Index59Dto> SelectJegoList(Index59Dto parm) ;
    public List<IndexJaegotemp> SelectJegoList_temp01(IndexJaegotemp parm) ;

    public List<Index59Dto> GetJpumFromJaegoList(Index59Dto parm) ;
    public List<Index59Dto> GetJpumSubul01(Index59Dto parm) ;
    public List<Index59Dto> GetJpumSubul02(Index59Dto parm) ;
    public String SelectBomCheck(IndexCa613Dto parm) ;
    public Boolean InsertBom501(IndexCa613Dto parm) ;
    public Boolean UpdateBom501(IndexCa613Dto parm) ;
    public Boolean DeleteBom501(IndexCa613Dto parm) ;

    public List<IndexCa613Dto> SelectBomListOpcod(IndexCa613Dto parm) ;

    public List<IndexCa613Dto> SelectBomListTot(IndexCa613Dto parm) ;






}
