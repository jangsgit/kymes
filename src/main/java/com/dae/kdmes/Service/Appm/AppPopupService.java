package com.dae.kdmes.Service.Appm;

import com.dae.kdmes.DTO.*;
import com.dae.kdmes.DTO.App01.Index01Dto;
import com.dae.kdmes.DTO.Appm.FPLANBOM_VO;
import com.dae.kdmes.DTO.Appm.FPLANIWORK_VO;
import com.dae.kdmes.DTO.Appm.TBPopupVO;
import com.dae.kdmes.Mapper.Appm.TBPopupMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service("AppPopupService")
public class AppPopupService {
    private final TBPopupMapper Popupmapper;
    protected Log log =  LogFactory.getLog(this.getClass());
    public Object GetWrmcList(TBPopupVO parm){
        return Popupmapper.GetWrmcList(parm);
    }

    public Object GetWfperidList(TBPopupVO parm){
        return Popupmapper.GetWfperidList(parm);
    }
    public Object GetWrmcList_blank(TBPopupVO parm){  return Popupmapper.GetWrmcList_blank(parm);  }
    public Object GetWfbomList(FPLANBOM_VO parm){ return Popupmapper.GetWfbomList(parm); }
    public Object GetWfbomList_blank(){ return Popupmapper.GetWfbomList_blank(); }
    public Object GetWfiworkList(FPLANIWORK_VO parm){ return Popupmapper.GetWfiworkList(parm); }
    public Object GetNewWfiworkList(FPLANIWORK_VO parm){ return Popupmapper.GetNewWfiworkList(parm); }
    public Object GetWfiworkList_blank(){ return Popupmapper.GetWfiworkList_blank(); }
    public Object GetWscnt(TBPopupVO parm){
        return Popupmapper.GetWscnt(parm);
    }
    public String GetWtimeSeq(TBPopupVO parm){
        return Popupmapper.GetWtimeSeq(parm);
    }
    public void TB_Fplan_WtimeInsert(TBPopupVO parm){Popupmapper.TB_Fplan_WtimeInsert(parm);}
    public String  GetWtimeEndSeq(TBPopupVO parm){
        return Popupmapper.GetWtimeEndSeq(parm);
    }
    public void TB_Fplan_WtimeUpdate(TBPopupVO parm){Popupmapper.TB_Fplan_WtimeUpdate(parm);}
    public Object GetWBadList(TBPopupVO parm){
        return Popupmapper.GetWBadList(parm);
    }
    public Object GetWBadDDList(TBPopupVO parm){
        return Popupmapper.GetWBadDDList(parm);
    }
    public Object GetWBadList_blank(){ return Popupmapper.GetWBadList_blank(); }
    public List<TBPopupVO> FPLAN_OWORK_List(TBPopupVO parm){return Popupmapper.FPLAN_OWORK_List(parm);}


    public List<TBPopupVO> GetPernmList(TBPopupVO parm){
        return Popupmapper.GetPernmList(parm);
    }
    public Object GetWrmcList01(TBPopupVO parm){
        return Popupmapper.GetWrmcList01(parm);
    }
    public Object GetWBadList01(TBPopupVO parm){
        return Popupmapper.GetWBadList01(parm);
    }

    public List<TBPopupVO> GetStopList(TBPopupVO parm){
        return Popupmapper.GetStopList(parm);
    }

    public Object GetStoreList(TBPopupVO parm){
        return Popupmapper.GetStoreList(parm);
    }



}
