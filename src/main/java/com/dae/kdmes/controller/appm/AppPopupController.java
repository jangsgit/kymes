package com.dae.kdmes.controller.appm;

import com.dae.kdmes.DTO.*;
import com.dae.kdmes.DTO.Appm.FPLANBOM_VO;
import com.dae.kdmes.DTO.Appm.FPLANIWORK_VO;
import com.dae.kdmes.Service.Appm.AppPopupService;
import com.dae.kdmes.DTO.Appm.TBPopupVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


// @RestController : return을 텍스트로 반환함.
//@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/popup", method = RequestMethod.POST)
public class AppPopupController {
    private final AppPopupService appPopupService;
    protected Log log =  LogFactory.getLog(this.getClass());
    CommonDto CommDto = new CommonDto();

    //작업설비
    @GetMapping(value="/wrmc")
    public Object Appwrmc_index(@RequestParam("machname") String machname
                                ,@RequestParam("wflag") String wflag
                                ,Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("작업설비");
        CommDto.setMenuUrl("popup>작업설비");
        CommDto.setMenuCode("wrmc");

        TBPopupVO wrmcDto = new TBPopupVO();
        if (machname == null || machname.equals("")){
            machname = "%";
        }
        wrmcDto.setMachname(machname);
        wrmcDto.setWflag(wflag);
        if(appPopupService.GetWrmcList01(wrmcDto) == null){
            return appPopupService.GetWrmcList_blank(wrmcDto);
        }else{
            return  appPopupService.GetWrmcList01(wrmcDto);
        }

    }

    //작업자
    @GetMapping(value="/wperid")
    public Object Appwrmc_index(
            @RequestParam("plan_no") String plan_no,
            @RequestParam("wseq") String wseq,
            @RequestParam("wflag") String wflag,
            Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("작업자");
        CommDto.setMenuUrl("popup>작업자");
        CommDto.setMenuCode("wperid");
        TBPopupVO wperidDto = new TBPopupVO();
        if (plan_no == null || plan_no.equals("")){
            plan_no = "%";
        }
        if (wseq == null || wseq.equals("")){
            wseq = "%";
        }
        if (wflag == null || wflag.equals("")){
            wflag = "10";   //첫째공정
        }
        wperidDto.setPlan_no(plan_no);
        wperidDto.setWseq(wseq);
        wperidDto.setWflag(wflag);
        if(appPopupService.GetWfperidList(wperidDto) == null){
            return appPopupService.GetWrmcList_blank(wperidDto);
        }else{
            return  appPopupService.GetWfperidList(wperidDto);
        }

    }


    //BOM LIST
    @GetMapping(value="/wbom")
    public Object Appwbom_index(@RequestParam("plan_no") String plan_no
            ,Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("BOM LIST");
        CommDto.setMenuUrl("popup>BOM_LIST");
        CommDto.setMenuCode("wbom");
        FPLANBOM_VO wbomDto = new FPLANBOM_VO();
        if (plan_no == null || plan_no.equals("")){
            plan_no = "%";
        }
        wbomDto.setPlan_no(plan_no);
        if(appPopupService.GetWfbomList(wbomDto) == null){
            return appPopupService.GetWfbomList_blank();
        }else{
            return  appPopupService.GetWfbomList(wbomDto);
        }
    }


    //fplan iwork  LIST
    @GetMapping(value="/wiwork")
    public Object Appwiwork_index( @RequestParam("plan_no") String plan_no
                                   ,@RequestParam("wseq") String wseq
                                   ,@RequestParam("wflag") String wflag
                                   ,Model model, HttpServletRequest request) throws Exception{
        FPLANIWORK_VO winworkDto = new FPLANIWORK_VO();
        if (plan_no == null || plan_no.equals("")){
            plan_no = "%";
        }
        winworkDto.setPlan_no(plan_no);
        winworkDto.setWseq(wseq);
        winworkDto.setWflag(wflag);
        if(appPopupService.GetWfiworkList(winworkDto) == null){
            return appPopupService.GetWfiworkList_blank();
        }else{
            return  appPopupService.GetWfiworkList(winworkDto);
        }
    }

    //fplan iwork  LIST
    @GetMapping(value="/wfiwork")
    public Object Appwfiwork_index( @RequestParam("plan_no") String plan_no
            ,@RequestParam("wflag") String wflag
            ,Model model, HttpServletRequest request) throws Exception{
        FPLANIWORK_VO winworkDto = new FPLANIWORK_VO();
        if (plan_no == null || plan_no.equals("")){
            plan_no = "%";
        }
        Date nowData = new Date();
        SimpleDateFormat endDate = new SimpleDateFormat("yyyyMMdd");
        String startDate = endDate.format(nowData).toString();
        String toDate = endDate.format(nowData).toString();

        winworkDto.setPlan_no(plan_no);
        winworkDto.setWflag(wflag);
        winworkDto.setIndate(toDate);

//        if(appPopupService.GetNewWfiworkList(winworkDto) == null){
//            return appPopupService.GetWfiworkList_blank();
//        }else{
//            return  appPopupService.GetNewWfiworkList(winworkDto);
//        }
        return null;
    }

    //fplan iwork  LIST
    @GetMapping(value="/wscnt")
    public Object Appwscnt_index( @RequestParam("custcd") String custcd
            ,@RequestParam("spjangcd") String spjangcd
            ,@RequestParam("wrmc") String wrmc
            ,@RequestParam("sdate") String sdate
            ,@RequestParam("wfrdt") String wfrdt
            ,@RequestParam("plan_no") String plan_no
            ,@RequestParam("wflag") String wflag
            ,Model model, HttpServletRequest request) throws Exception{
        TBPopupVO wscntDto = new TBPopupVO();
        if (wrmc == null || wrmc.equals("")){
            wrmc = "%";
        }
        wscntDto.setCustcd(custcd);
        wscntDto.setSpjangcd(spjangcd);
        wscntDto.setWrmc(wrmc);
        wscntDto.setSdate(sdate);
        wscntDto.setWfrdt(wfrdt);
        wscntDto.setPlan_no(plan_no);
        wscntDto.setWflag(wflag);
        wscntDto.setWseq("01");
        wscntDto.setAseq("0");
        String ls_seq = appPopupService.GetWtimeSeq(wscntDto);
        if(ls_seq == null){
            wscntDto.setSeq("01");
        }else{
            int ll_seq =  Integer.parseInt(ls_seq) + 1;
            ls_seq = Integer.toString(ll_seq);
            if(ls_seq.length() == 1){ls_seq = "0" + ls_seq;}
            wscntDto.setSeq(ls_seq);
        }
        appPopupService.TB_Fplan_WtimeInsert(wscntDto);
        return  appPopupService.GetWscnt(wscntDto);
    }

    //fplan iwork  LIST
    @GetMapping(value="/wgadongend")
    public Object AppwgadongStop_index( @RequestParam("custcd") String custcd
            ,@RequestParam("spjangcd") String spjangcd
            ,@RequestParam("wrmc") String wrmc
            ,@RequestParam("sdate") String sdate
            ,@RequestParam("wtrdt") String wtrdt
            ,@RequestParam("plan_no") String plan_no
            ,@RequestParam("wflag") String wflag
            ,Model model, HttpServletRequest request) throws Exception{
        TBPopupVO wscntDto = new TBPopupVO();
        if (wrmc == null || wrmc.equals("")){
            wrmc = "%";
        }
        wscntDto.setCustcd(custcd);
        wscntDto.setSpjangcd(spjangcd);
        wscntDto.setWrmc(wrmc);
        wscntDto.setSdate(sdate);
        wscntDto.setWtrdt(wtrdt);
        wscntDto.setPlan_no(plan_no);
        wscntDto.setWflag(wflag);
        wscntDto.setWseq("01");
        wscntDto.setAseq("0");
        String ls_seq = appPopupService.GetWtimeEndSeq(wscntDto);
        wscntDto.setSeq(ls_seq);
        appPopupService.TB_Fplan_WtimeUpdate(wscntDto);
        return  appPopupService.GetWscnt(wscntDto);
    }

    //wbad  LIST
    @GetMapping(value="/wbad")
    public Object Appwiwork02_index(
             @RequestParam("plan_no") String plan_no
            ,@RequestParam("lotno") String lotno
            ,@RequestParam("wflag") String wflag
            ,Model model, HttpServletRequest request) throws Exception{
        TBPopupVO wbadDto = new TBPopupVO();
        if (plan_no == null || plan_no.equals("")){
            plan_no = "%";
        }
        wbadDto.setCustcd("KDMES");
        wbadDto.setSpjangcd("ZZ");
        wbadDto.setPlan_no(plan_no);
        wbadDto.setLotno(lotno);
        wbadDto.setWflag(wflag);
        if(appPopupService.GetWBadList01(wbadDto) == null){
            return appPopupService.GetWBadList_blank();
        }else{
            return  appPopupService.GetWBadList01(wbadDto);
        }
    }


    //wstop  LIST
    @GetMapping(value="/wstop")
    public Object Appwwstop_index(
            @RequestParam("searchtxt") String searchtxt,Model model, HttpServletRequest request) throws Exception{
        TBPopupVO wperidDto = new TBPopupVO();
        if (searchtxt == null || searchtxt.equals("")){
            searchtxt = "%";
        }
        wperidDto.setWpernm(searchtxt);
        return  appPopupService.GetStopList(wperidDto);

    }


    @GetMapping(value="/oworklist")
    public Object Appwiwork_index( @RequestParam("plan_no") String plan_no
            ,@RequestParam("lotno") String lotno
            ,@RequestParam("inpcode") String inpcode
            ,@RequestParam("inwono") String inwono
            ,@RequestParam("wflag") String wflag
            ,Model model, HttpServletRequest request) throws Exception{

        TBPopupVO wPopDto = new TBPopupVO();
        List<TBPopupVO> wPopDtoList = new ArrayList<>();

        if (plan_no == null || plan_no.equals("")){
            plan_no = "%";
        }
        wPopDto.setPlan_no(plan_no);
        wPopDto.setLotno(lotno);
        wPopDto.setPcode(inpcode);
        wPopDto.setWflag(wflag);
        //wPopDto.setLotno(inwono.substring(2,11));
        wPopDtoList = appPopupService.FPLAN_OWORK_List(wPopDto) ;
        return wPopDtoList;
    }


}
