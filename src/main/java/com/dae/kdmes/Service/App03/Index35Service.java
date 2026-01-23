package com.dae.kdmes.Service.App03;

import com.dae.kdmes.DTO.App01.IndexCa613Dto;
import com.dae.kdmes.DTO.app03.Index35Dto;
import com.dae.kdmes.DTO.app03.Index59Dto;
import com.dae.kdmes.DTO.app03.IndexJaegotemp;
import com.dae.kdmes.Mapper.App03.Index35Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service("Index35Service")
public class Index35Service {

    @Autowired
    Index35Mapper indexMapper;
    public List<Index35Dto> getList(Index35Dto parm){
        return indexMapper.getList(parm);
    }


    public List<Index59Dto> SelectJegoIpgo(Index59Dto parm){
        return  indexMapper.SelectJegoIpgo(parm);
    }
    public String SelectJegoCheck(Index59Dto parm){
        return  indexMapper.SelectJegoCheck(parm);
    }
    public String SelectMaxSeqIpgo(Index59Dto parm){
        return  indexMapper.SelectMaxSeqIpgo(parm);
    }

    public Boolean InsertJegoIpgo(Index59Dto parm){ return  indexMapper.InsertJegoIpgo(parm);}
    public Boolean DeleteJaegoIpgo(Index59Dto parm){ return  indexMapper.DeleteJaegoIpgo(parm);}
    public Boolean DeleteJaegoIpgoAcorp(Index59Dto parm){ return  indexMapper.DeleteJaegoIpgoAcorp(parm);}
    public Boolean UpdateJegoIpgo(Index59Dto parm){ return  indexMapper.UpdateJegoIpgo(parm);}

    public List<Index59Dto> SelectJegoList(Index59Dto parm){
        return  indexMapper.SelectJegoList(parm);
    }
    public List<IndexJaegotemp> SelectJegoList_temp01(IndexJaegotemp parm){
        return  indexMapper.SelectJegoList_temp01(parm);
    }

    public List<Index59Dto> GetJpumFromJaegoList(Index59Dto parm){
        return  indexMapper.GetJpumFromJaegoList(parm);
    }
    public List<Index59Dto> GetJpumSubul01(Index59Dto parm){
        return  indexMapper.GetJpumSubul01(parm);
    }
    public List<Index59Dto> GetJpumSubul02(Index59Dto parm){
        return  indexMapper.GetJpumSubul02(parm);
    }
    public String SelectBomCheck(IndexCa613Dto parm){ return  indexMapper.SelectBomCheck(parm);}

    public Boolean InsertBom501(IndexCa613Dto parm){ return  indexMapper.InsertBom501(parm);}

    public Boolean UpdateBom501(IndexCa613Dto parm){ return  indexMapper.UpdateBom501(parm);}

    public Boolean DeleteBom501(IndexCa613Dto parm){ return  indexMapper.DeleteBom501(parm);}
    public List<IndexCa613Dto> SelectBomListOpcod(IndexCa613Dto parm){
        return  indexMapper.SelectBomListOpcod(parm);
    }
    public List<IndexCa613Dto> SelectBomListTot(IndexCa613Dto parm){
        return  indexMapper.SelectBomListTot(parm);
    }



}
