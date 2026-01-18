package com.dae.kdmes.controller.appm;

import com.dae.kdmes.DTO.*;

import com.dae.kdmes.DTO.App02.Index10Dto;
import com.dae.kdmes.DTO.Appm.*;
import com.dae.kdmes.DTO.Cms.CmsIndex01Dto;
import com.dae.kdmes.Service.App02.Index10Service;
import com.dae.kdmes.Service.Appm.AppPopupService;
import com.dae.kdmes.Service.Appm.Appcom01Service;
//import com.dae.kdmes.Service.Appcom02Service;
//import com.dae.kdmes.Service.Appcom03Service;
//import com.dae.kdmes.Service.Appcom04Service;
import com.dae.kdmes.Service.Cms.CmsIndex01Service;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


import java.awt.image.BufferedImage;
import java.util.List;
import javax.imageio.ImageIO;


// @RestController : return을 텍스트로 반환함.
//@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/input", method = RequestMethod.POST)
public class Appm01CrudController {
    private final Appcom01Service appcom01Service;
    private final Index10Service service10;
    private final AppPopupService appPopupService;
    private final CmsIndex01Service cmsservice01;
    FPLANWPERID_VO wperDto = new FPLANWPERID_VO();
    FPLANWTIME_VO wtimeDto = new FPLANWTIME_VO();
    FPLANW010_VO workDto = new FPLANW010_VO();
    FPLANIWORK_VO IworkDto = new FPLANIWORK_VO();
    protected Log log =  LogFactory.getLog(this.getClass());
    Date nowData = new Date();
    SimpleDateFormat endDate = new SimpleDateFormat("yyyyMMdd");
    String ToDate = endDate.format(nowData).toString();
    CommonDto CommDto = new CommonDto();
    FPLANW010_VO workDtoDetail = new FPLANW010_VO();
    Boolean result = false;

    @RequestMapping(value="/w010", method = RequestMethod.POST)
    public Object AppW010_index(@RequestParam("custcd") String custcd
            ,@RequestParam("spjangcd") String spjangcd
            ,@RequestParam("wono") String wono
            ,@RequestParam("cltnm") String cltnm
            ,@RequestParam("pname") String pname
            ,@RequestParam("ostore") String ostore
            ,@RequestParam("plan_no") String plan_no
            ,@RequestParam("lotno") String lotno
            ,@RequestParam("wflag") String wflag
            ,@RequestParam("wrmc") String wrmc
            ,Model model, HttpServletRequest request) throws Exception {

        TBPopupVO wscntDto = new TBPopupVO();
        FPLANW010_VO workDto = new FPLANW010_VO();
//        FPLANIWORK_VO iworkDto = new FPLANIWORK_VO();
//        FPLANWTIME_VO wtimeDto = new FPLANWTIME_VO();
//        FPLANWBAD_VO wbadDto = new FPLANWBAD_VO();
//        FPLANWPERID_VO wperDto = new FPLANWPERID_VO();
        workDto.setCustcd(custcd);
        workDto.setSpjangcd(spjangcd);
        workDto.setWono(wono);
        workDto.setPlan_no(plan_no);
        workDto.setLotno(lotno);
        wflag = "00010";
        workDto.setWflag(wflag);
        workDto.setWrmc(wrmc);
        workDto.setWseq("01");
        workDto.setAseq("0");
        workDto.setDecision("1");
        workDto.setDecision1("1");
        workDto.setWtable("TB_FPLAN_W010");
        wscntDto.setPlan_no(plan_no);
        wperDto.setCustcd(custcd);
        wperDto.setSpjangcd(spjangcd);
        wperDto.setWseq("01");
        wperDto.setWflag(wflag);
        wperDto.setSeq("001");
        wperDto.setPlan_no(plan_no);
        wperDto.setLotno(lotno);
        wtimeDto.setCustcd(custcd);
        wtimeDto.setSpjangcd(spjangcd);
        wtimeDto.setPlan_no(plan_no);
        wtimeDto.setLotno(lotno);
        wtimeDto.setWseq("01");
        wtimeDto.setSeq("001");
        wtimeDto.setWflag(wflag);

//        String ls_seq = appcom01Service.GetWtimeWseq(wscntDto);
//        wscntDto.setWflag(wflag);
//        if(ls_seq == null){
//            workDto.setWseq("01");
//            appcom01Service.FPLANWORK_Insert(workDto);
//            appcom01Service.FPLANW010_Insert(workDto);
////            appcom01Service.FPLAN_WPERID_Insert(wperDto);
////            appcom01Service.FPLAN_WTIME_Insert(wtimeDto);
//
////            appcom01Service.FPLANI_WORK_Insert(workDto);
////            appcom01Service.FPLAN_WBAD_Insert(wbadDto);
////            appcom01Service.FPLAN_OWORK_Insert(iworkDto);
//        }else{
//            if(ls_seq.length() == 1){ls_seq = "0" + ls_seq;}
//            workDto.setWseq(ls_seq);
//            //appcom01Service.FPLANWORK_Update(workDto);
//            //appcom01Service.FPLANW010_Update(workDto);
//        }
//
//        appcom01Service.FPLAN_Update(workDto);
        return appcom01Service.FPLAN_WPERID_SELECT(workDto);
    }

    @RequestMapping(value="/w010ordlist", method = RequestMethod.POST)
    public Object AppW010OrdList_index(@RequestParam("lotno") String lotno
            ,@RequestParam("inmonth") String inmonth
            ,@RequestParam("inweeks") String inweeks
            ,@RequestParam("wrmc") String wrmc
            ,Model model, HttpServletRequest request) throws Exception {
        CommDto.setMenuTitle("사출공정");  //
        CommDto.setMenuUrl("생산공정>사출공정");
        CommDto.setMenuCode("appcom01");
        FPLAN_VO fplanDto = new FPLAN_VO();
        List<FPLAN_VO> itemDtoList = new ArrayList<>();

        String fdate = getFrDate();
        String tdate = getToDate();

        fplanDto.setLine("00");
        fplanDto.setWflag("00010");
        fplanDto.setFdate(fdate);
        fplanDto.setTdate(tdate);
        fplanDto.setCltcd("%");
        fplanDto.setPcode("%");
        fplanDto.setLotno(lotno);
        fplanDto.setInmonth(inmonth);
        fplanDto.setInweeks(inweeks);
        fplanDto.setWrmc(wrmc);


        itemDtoList = appcom01Service.GetFPLAN_List(fplanDto);

        return itemDtoList;
    }


    @RequestMapping(value="/w010Sachullist", method = RequestMethod.POST)
    public Object AppW010SachulList_index(@RequestParam("lotno") String lotno
            ,@RequestParam("inmonth") String inmonth
            ,@RequestParam("inweeks") String inweeks
            ,Model model, HttpServletRequest request) throws Exception {
        CommDto.setMenuTitle("사출공정");  //
        CommDto.setMenuUrl("생산공정>사출공정");
        CommDto.setMenuCode("appcom01");
        FPLAN_VO fplanDto = new FPLAN_VO();
        List<FPLAN_VO> itemDtoList = new ArrayList<>();

        String fdate = getFrDate();
        String tdate = getToDate();

        fplanDto.setLine("00");
        fplanDto.setWflag("00010");
        fplanDto.setFdate(fdate);
        fplanDto.setTdate(tdate);
        fplanDto.setCltcd("%");
        fplanDto.setPcode("%");
        fplanDto.setLotno(lotno);
        fplanDto.setInmonth(inmonth);
        fplanDto.setInweeks(inweeks);


        itemDtoList = appcom01Service.GetFPLAN_SachulList(fplanDto);

        return itemDtoList;
    }

    @RequestMapping(value="/w010upd", method = RequestMethod.POST)
    public String AppW010Update_index(@RequestParam("custcd") String custcd
            ,@RequestParam("spjangcd") String spjangcd
            ,@RequestParam("wseq") String wseq
            ,@RequestParam("startDate") String startDate
            ,@RequestParam("endDate") String endDate
            ,@RequestParam("plan_no") String plan_no
            ,@RequestParam("lotno") String lotno
            ,@RequestParam("wrmc") String wrmc
            ,@RequestParam("winqt") Integer winqt
            ,@RequestParam("wbdqt") Integer wbdqt
            ,@RequestParam("wotqt") Integer wotqt
            ,@RequestParam("wsyul") float wsyul
            ,@RequestParam("wflag") String wflag
            ,@RequestParam("wremark") String wremark
            ,@RequestParam("wrps") String wrps
            ,@RequestParam("winps") Integer winps
            ,@RequestParam("decision") String decision
            ,@RequestParam("pcode") String pcode
            ,@RequestParam("wono") String wono
            ,@RequestParam("partcode") String partcode
            ,@RequestParam("partnm") String partnm
            ,@RequestParam("subpartcode") String subpartcode
            ,@RequestParam("subpartnm") String subpartnm
            ,@RequestParam("workdv") String workdv
            ,@RequestParam("rwflag") String rwflag
            ,@RequestParam("wbgubn") String wbgubn
            ,@RequestParam("jgumnm") String jgumnm
            ,Model model, HttpServletRequest request) throws Exception {

        Index10Dto _index10Dto = new Index10Dto();
        FPLANWTIME_VO _wtimeDto = new FPLANWTIME_VO();
        CmsIndex01Dto cmsdto = new CmsIndex01Dto();
        String ls_flag = decision;
        if(startDate.equals("") || startDate == null || startDate.equals(" ") || startDate.length() ==0 ){
            startDate = null;
        }
        if(endDate.equals("") || endDate == null || endDate.equals(" ") || endDate.length() ==0  ){
            endDate = null;
        }
        if(workdv.equals("1") || workdv.equals("4") ){   //공정시작과 저장버튼 클릭
            endDate = null;  //공정완료일경우만 종료일이 입력된다.
            _wtimeDto.setWopdv("0");
            decision = "1";
        }else{
            _wtimeDto.setWopdv("1"); //비가동
        }

        FPLANW010_VO workDto = new FPLANW010_VO();
        workDto.setCustcd(custcd);
        workDto.setSpjangcd(spjangcd);
        workDto.setWseq("01");
        workDto.setWfrdt(startDate);
        workDto.setWtrdt(endDate);

        workDto.setPlan_no(plan_no);
        workDto.setLotno(lotno);
        workDto.setWrmc(wrmc);
        workDto.setWinqt(winqt); //계획량
        workDto.setBqty(wbdqt);  //불량
        workDto.setWbdqt(wbdqt);
        workDto.setWflag("00010");
        workDto.setWsyul(wsyul);
        workDto.setWremark(wremark);
        workDto.setAseq("0");
        workDto.setWrps(wrps);
        workDto.setWinps(winps);
        workDto.setStore("W01");
        workDto.setPcode(pcode);
        workDto.setWono(wono);
        workDto.setPartcode(partcode);
        workDto.setPartnm(partnm);
        workDto.setSubpartcode(subpartcode);
        workDto.setSubpartnm(subpartnm);
        String ls_lotno = "";
        if(lotno != null && lotno.length() > 0){
            ls_lotno = lotno;
        }
        if(rwflag.length() > 0){
            _index10Dto.setPcode(pcode);
            _index10Dto.setPlan_no(plan_no);
            _index10Dto.setIndate(getToDate());
            _index10Dto.setRwflag(rwflag);

            String ls_maxlotno = service10.SelectMaxLotno(_index10Dto);
//            log.info("rwflag =====>" + rwflag);
//           log.info("plan_no =====>" + plan_no);
//           log.info("ls_maxlotno =====>" + ls_maxlotno);
            String ls_seq;
            if (ls_maxlotno == null) {
                ls_lotno = plan_no.substring(0,8) + rwflag + "0001";
            } else {
                ls_seq = ls_maxlotno.substring(9, 13);
                int ll_seq = Integer.parseInt(ls_seq) + 1;
                ls_seq = Integer.toString(ll_seq);

                if (ls_seq.length() == 1) {
                    ls_seq = "000" + ls_seq;
                } else if (ls_seq.length() == 2) {
                    ls_seq = "00" + ls_seq;
                } else if (ls_seq.length() == 3) {
                    ls_seq = "0" + ls_seq;
                }

                ls_lotno = plan_no.substring(0,8) + rwflag + ls_seq;
            }
            _index10Dto.setLotno(ls_lotno);
            result = service10.UpdateFplan(_index10Dto);
            if (!result) {
                log.info("UpdateFplan result =====>" + result);
                return "error";
            }
            workDto.setLotno(ls_lotno);
        }

        if(!workdv.equals("4")){
            workDto.setWorkdv(workdv);
            workDto.setDecision(workdv);
            workDto.setDecision1(workdv);
        }
        if(workdv.equals("9")){        //작업중단
            workDto.setDecision("9");
            workDto.setDecision1("9");
        }
        if(workdv.equals("0")){          // 공정종료
            workDto.setClsflag("3");
            workDto.setWorkdv("3");
            workDto.setDecision("3");
            workDto.setDecision1("3");
//        jgumtype = "DM_HOLDER(FR)";
//        wrmc = "10";
            cmsdto.setMachine_name(wrmc); //호기
            cmsdto.setAdditional_Info_1(jgumnm); //금형
            // SimpleDateFormat을 이용하여 String을 Date로 변환
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date stparsedDate = dateFormat.parse(startDate);
            Date edparsedDate = dateFormat.parse(getToDateTime());
            // Date를 Timestamp로 변환
            Timestamp stimestamp = new Timestamp(stparsedDate.getTime());
            Timestamp edimestamp = new Timestamp(edparsedDate.getTime());

            cmsdto.setSTARTTIME(stimestamp); //작업시작시간
            cmsdto.setENDTIME(edimestamp);   //작업종료시간
//            log.info("wrmc =====> " + wrmc);
//            log.info("jgumtype =====> " + jgumnm);
//            log.info("stimestamp =====> " + stimestamp);
//            log.info("edimestamp =====> " + edimestamp);

            // 임시적으로 막음 25.01.21
            // wotqt = cmsservice01.getSHOTDATA_wotqty(cmsdto);  //생산량
            workDto.setWqty(wotqt);
            workDto.setQty(wotqt);
            workDto.setWotqt(wotqt);
            workDto.setJqty(wotqt - wbdqt);
        }else if(workdv.equals("4")){
            workDto.setWqty(wotqt);
            workDto.setQty(wotqt);
            workDto.setWotqt(wotqt);
            workDto.setJqty(wotqt - wbdqt);
        }else{
            workDto.setClsflag("2");        // 공정중
        }

        //ls_lotno = wono.substring(2,11);
        String wstdt = "";
        String wendt = "";
        if(startDate != null){
            wstdt = setDateFormat(startDate);
            wendt = setDateFormat(startDate);
        }else{
            wstdt = "" ;
            wendt = "";
        }

        workDto.setWstdt(wstdt);
        if(decision.equals("0")){
            workDto.setWendt(wendt);
        }else{
            workDto.setWendt(endDate);
        }
        workDto.setIndate(getToDate());
        workDto.setWtable("TB_FPLAN_W010");

        _wtimeDto.setCustcd(custcd);
        _wtimeDto.setSpjangcd(spjangcd);
        _wtimeDto.setPlan_no(plan_no);
        _wtimeDto.setLotno(ls_lotno);
        _wtimeDto.setWseq("01");
        _wtimeDto.setSeq("001");
        _wtimeDto.setWflag(wflag);
        _wtimeDto.setWfrdt(startDate);

//        workDtoDetail = (FPLANW010_VO) appcom01Service.GetFPLANW010_Detail(workDto);
        if(appcom01Service.GetFPLANW010_Detail(workDto) == null){
            result = appcom01Service.FPLANW010_Insert(workDto);
            if (!result) {
                log.info("error =====> FPLANW010_Insert");
                return "error";
            }
            result = appcom01Service.FPLANWORK_Insert(workDto);
            if (!result) {
                log.info("error =====> FPLANWORK_Insert");
                return "error";
            }
            _wtimeDto.setWtrdt(null);       //작업시작
            result = appcom01Service.FPLAN_WTIME_Insert(_wtimeDto);
            if (!result) {
                log.info("error =====> FPLAN_WTIME_Insert");
                return "error";
            }

        }else{
            result = appcom01Service.FPLANW010_Update(workDto);
            if (!result) {
                log.info("error =====> FPLANW010_Update");
                return "error";
            }
            result = appcom01Service.FPLANWORK_Update(workDto);
            if (!result) {
                log.info("error =====> FPLANWORK_Update");
                return "error";
            }
        }
        workDto.setEnd_qty(wotqt - wbdqt);
        workDto.setProd_qty(wotqt);

        if(workdv.equals("0")){  //공정종료
            _wtimeDto.setWtrdt(getToDateTime());
            //검사, 조립 공정구분
            if(wbgubn.equals("H")){
                workDto.setWflag("00030");
            }else{
                workDto.setWflag("00020");
            }
            result = appcom01Service.FPLAN_WTIME_Update(_wtimeDto);
            if (!result) {
                log.info("error =====> FPLAN_WTIME_Update 02");
                //return "error";
            }
        }
        result = appcom01Service.FPLAN_Update(workDto);
        if (!result) {
            log.info("error =====> FPLAN_Update 02");
            return "error";
        }
        if(workdv.equals("0")){     //공정종료//
            return Integer.toString(wotqt);
        }else{
            return ls_lotno;
        }

    }


    //절단공정삭제
    @RequestMapping(value="/w010del", method = RequestMethod.GET)
    public String Appcom01_delete(@RequestParam("plan_no") String plan_no
                                 ,@RequestParam("lotno") String lotno
                                 ,HttpServletRequest request){
        FPLANW010_VO workDto = new FPLANW010_VO();
        Index10Dto _index10Dto = new Index10Dto();
        workDto.setPlan_no(plan_no);
        workDto.setLotno(lotno);
        workDto.setWflag("00010");
        workDto.setWseq("01");
        wperDto.setPlan_no(plan_no);
        wperDto.setLotno(lotno);
        wperDto.setWseq("01");
        wperDto.setWflag("00010");
        result = appcom01Service.FPLANW010_Delete(workDto);
        if (!result) {
            log.info("error =====> FPLANW010_Delete");
            return "error";
        }
        result = appcom01Service.FPLAN_OWORK_Delete(workDto);
        if (!result) {
            log.info("error =====> FPLAN_OWORK_Delete");
            //return "error";
        }
//        result = appcom01Service.FPLAN_IWORK_Delete(workDto);
//        if (!result) {
//            log.info("error =====> FPLAN_IWORK_Delete");
//            return "error";
//        }
        result = appcom01Service.FPLAN_WORK_Delete(workDto);
        if (!result) {
            log.info("error =====> FPLAN_WORK_Delete");
            return "error";
        }
        result = appcom01Service.FPLAN_WTIME_Delete(workDto);
        if (!result) {
            log.info("error =====> FPLAN_WTIME_Delete");
            return "error";
        }
        result = appcom01Service.FPLAN_WPERID_Delete(wperDto);
        if (!result) {
            log.info("error =====> FPLAN_WPERID_Delete");
            //return "error";
        }
        result = appcom01Service.FPLAN_WBAD_Delete(workDto);
        if (!result) {
            log.info("error =====> FPLAN_WBAD_Delete");
            //return "error";
        }


        //log.info("w010del plan_no =====>" + plan_no);

        workDto.setDecision("0");
        workDto.setDecision1("0");
        workDto.setClsflag("1");
        result = appcom01Service.FPLAN_Update(workDto);
        if (!result) {
            log.info("error =====> FPLAN_Update");
            return "error";
        }

        _index10Dto.setPlan_no(plan_no);
        _index10Dto.setIndate(getToDate());
        _index10Dto.setLotno(" ");
        result = service10.UpdateFplan(_index10Dto);
        if (!result) {
            log.info("result =====>" + result);
            return "error";
        }

        var ls_line = "99";
        return "redirect:/appm/list01";
    }



    @RequestMapping(value="/wtimeupd", method = RequestMethod.POST)
    public String AppWTimeUpdate_index(@RequestParam("custcd") String custcd
            ,@RequestParam("spjangcd") String spjangcd
            ,@RequestParam("plan_no") String plan_no
            ,@RequestParam("lotno") String lotno
            ,@RequestParam("wflag") String wflag
            ,@RequestParam("decision") String decision
            ,@RequestParam("workdv") String workdv
            ,@RequestParam("instopcd") String instopcd
            ,@RequestParam("instopnm") String instopnm
            ,Model model, HttpServletRequest request) throws Exception {

        wtimeDto.setCustcd(custcd);
        wtimeDto.setSpjangcd(spjangcd);
        wtimeDto.setPlan_no(plan_no);
        wtimeDto.setLotno(lotno);
        wtimeDto.setWflag(wflag);
        switch (wflag){
            case "00010":
                wtimeDto.setWseq("01");
                break;
            case "00020":
                wtimeDto.setWseq("02");
                break;
            case "00030":
                wtimeDto.setWseq("03");
                break;
            case "00090":
                wtimeDto.setWseq("04");
                break;
            default:
                break;
        }

        if(decision.equals("2")){     //작업시작
            wtimeDto.setSeq(getWtimeMaxSeq());
            wtimeDto.setWfrdt(getToDateTime());
            wtimeDto.setWtrdt(getToDateTime());
            wtimeDto.setWdtcd("");
            wtimeDto.setWrerm("");
//            log.info("setWfrdt11 =====>" + wtimeDto.getWfrdt());
//            log.info("setWtrdt11 =====>" + wtimeDto.getWtrdt());
            result = appcom01Service.FPLAN_WTIME_Update(wtimeDto);
            if (!result) {
                log.info("error =====> FPLAN_WTIME_Update");
                ///return "error";
            }
            wtimeDto.setWtrdt(null);
//            log.info("setWfrdt22 =====>" + wtimeDto.getWfrdt());
//            log.info("setWtrdt22 =====>" + wtimeDto.getWtrdt());
            result = appcom01Service.FPLAN_WTIME_Insert(wtimeDto);
            if (!result) {
                log.info("error =====> FPLAN_WTIME_Insert");
                return "error";
            }
        }else{
            wtimeDto.setWdtcd(instopcd);
            wtimeDto.setWrerm(instopnm);
            wtimeDto.setWtrdt(getToDateTime());
//            log.info("setWtrdt33 =====>" + wtimeDto.getWtrdt());
            result = appcom01Service.FPLAN_WTIME_Update(wtimeDto);
            if (!result) {
                log.info("error =====> FPLAN_WTIME_Update");
                //return "error";
            }
        }
        workDto.setPlan_no(plan_no);
        workDto.setLotno(lotno);
        workDto.setWflag(wflag);
        if(!workdv.equals("4")){
            workDto.setWorkdv(workdv);
            workDto.setDecision(workdv);
            workDto.setDecision1(workdv);
        }
        workDto.setWseq("01");
        //log.info(" workdv =====>" + workdv);
        result = appcom01Service.FPLAN_Update(workDto);
        if (!result) {
            log.info("error =====> FPLAN_Update");
            return "error";
        }
        result = appcom01Service.FPLANW010_Update(workDto);
        if (!result) {
            log.info("error =====> FPLANW010_Update");
            return "error";
        }
        return "success";
    }



    //작업설비 update 조회
    @RequestMapping(value="/qcsearch", method = RequestMethod.POST)
    public Object AppQcSearch_index(@RequestParam("searchcode") String searchcode
                , Model model, HttpServletRequest request) throws Exception {

        FPLANIWORK_VO fplanDto = new FPLANIWORK_VO();
        List<FPLANIWORK_VO> fplanListDto = new ArrayList<>();
        fplanDto.setLotno(searchcode);
        fplanListDto =   appcom01Service.GetPlanSearch(fplanDto);
        model.addAttribute("fplanListDto",fplanListDto);
        return fplanListDto;

    }

    @ResponseBody
    @RequestMapping(value="/w010perid", method = RequestMethod.POST)
    public String AppW010Perid_index(@RequestParam(value = "custcd[]") List<String> custcd
            ,@RequestParam( value =  "spjangcd[]") List<String> spjangcd
            ,@RequestParam( value =  "linecd[]") List<String> linecd
            ,@RequestParam( value =  "linenm[]") List<String> linenm
            ,@RequestParam( value =  "wflagcd[]") List<String> wflagcd
            ,@RequestParam( value =  "wflagnm[]") List<String> wflagnm
            ,@RequestParam( value =  "wperid[]") List<String> wperid
            ,@RequestParam( value =  "wpernm[]") List<String> wpernm
            ,@RequestParam("lotno") String lotno
            ,@RequestParam("plan_no") String plan_no) throws Exception {

        FPLANWPERID_VO wperDto = new FPLANWPERID_VO();

        wperDto.setPlan_no(plan_no);
        wperDto.setLotno(lotno);
        wperDto.setWseq("01");
        wperDto.setAseq("01");
        wperDto.setWflag("00010");
        String ls_seq = "0";
        appcom01Service.FPLAN_WPERID_Delete(wperDto);
        if( custcd.size() > 0){
            for(int i = 0; i < custcd.size(); i++){

                int ll_seq = Integer.parseInt(ls_seq) + 1;
                ls_seq = Integer.toString(ll_seq);
                if (ls_seq.length() == 1) {
                    ls_seq = "0" + ls_seq;
                }

                wperDto.setCustcd("KDMES");
                wperDto.setSpjangcd("ZZ");
                wperDto.setWflag(wflagcd.get(i));
                wperDto.setSeq(ls_seq);
                wperDto.setPerid(wperid.get(i));
                wperDto.setIndate(getToDate());
                appcom01Service.FPLAN_WPERID_Insert(wperDto);
            }

            FPLANW010_VO workDto = new FPLANW010_VO();
            Integer ll_winps = custcd.size();
            workDto.setPlan_no(plan_no);
            workDto.setWflag("00010");
            workDto.setWseq("01");
            workDto.setWinps(ll_winps);
            workDto.setWrps(wperid.get(0));
            appcom01Service.FPLANW010_Update(workDto);
        }
        return "OK" ;
    }



    //작업설비 update 조회
    @RequestMapping(value="/w010wrmc", method = RequestMethod.POST)
    public String AppW010Wrmc_index(@RequestParam("custcd") String custcd
            ,@RequestParam("spjangcd") String spjangcd
            ,@RequestParam("plan_no") String plan_no
            ,@RequestParam("lotno") String lotno
            ,@RequestParam("wflag") String wflag
            ,@RequestParam("wrmc") String wrmc) throws Exception {

        FPLANW010_VO itemDto = new FPLANW010_VO();

        itemDto.setPlan_no(plan_no);
        itemDto.setLotno(lotno);
        itemDto.setWflag(wflag);
        itemDto.setWseq("01");
        itemDto.setWrmc(wrmc);
        itemDto.setIndate(getToDate());
        appcom01Service.FPLANW010_WrmcUpdate(itemDto);
        return "UPDATE OK";

    }


    //작지번호 조회
    @RequestMapping(value="/SELW010", method = RequestMethod.POST)
    public Object AppW010_SEL(@RequestParam("custcd") String custcd
            ,@RequestParam("spjangcd") String spjangcd
            ,@RequestParam("plan_no") String plan_no
            ,@RequestParam("lotno") String lotno
            ,@RequestParam("wflag") String wflag) throws Exception {

        FPLANW010_VO itemDto = new FPLANW010_VO();

        itemDto.setPlan_no(plan_no);
        itemDto.setLotno(lotno);
        itemDto.setWflag(wflag);
        itemDto.setWseq("01");
        return appcom01Service.GetFPLANW010_Detail(itemDto);

    }



    //불량내역  등록
    @RequestMapping(value="/w010wbad", method = RequestMethod.POST)
    public Integer AppWBAD_INT(@RequestParam("custcd") String custcd
            ,@RequestParam("spjangcd") String spjangcd
            ,@RequestParam("plan_no") String plan_no
            ,@RequestParam("lotno") String lotno
            ,@RequestParam("wflag") String wflag
            ,@RequestParam("wbqty") int wbqty
            ,@RequestParam("wcode") String wcode
            ,@RequestParam("wseq") String wseq
            ,@RequestParam("pcode") String pcode) throws Exception {
        Exception dispatchException = null;
        try {
            FPLANWBAD_VO wbadDto = new FPLANWBAD_VO();

            wbadDto.setCustcd(custcd);
            wbadDto.setSpjangcd(spjangcd);
            wbadDto.setPlan_no(plan_no);
            wbadDto.setLotno(lotno);
            wbadDto.setWflag(wflag);
            wbadDto.setWbqty(wbqty);
            wbadDto.setWcode(wcode);
            wbadDto.setWseq(wseq);
            wbadDto.setPcode(pcode);
            wbadDto.setIndate(getToDate());
            String lsChkseq = appcom01Service.FPLAN_WBAD_SELECT(wbadDto);

//            log.info("wseq =====> " + wseq);
//            log.info("lsChkseq =====> " + lsChkseq);
//            log.info("plan_no =====> " + plan_no);
//            log.info("lotno =====> " + lotno);
//            log.info("wbqty =====> " + wbqty);
            if (lsChkseq == null  || lsChkseq.equals("")){
                String ls_seq = appcom01Service.FPLAN_WBAD_MAXWSEQ(wbadDto);
                if(ls_seq == null){
                    ls_seq = "01";
                }else{
                    int ll_seq =  Integer.parseInt(ls_seq) + 1;
                    ls_seq = Integer.toString(ll_seq);
                    if(ls_seq.length() == 1){ls_seq = "0" + ls_seq;}
                }
                wseq = ls_seq;
                wbadDto.setWseq(ls_seq);
                appcom01Service.FPLAN_WBAD_Insert(wbadDto) ;
            }else{
                wbadDto.setWseq(lsChkseq);
                appcom01Service.FPLAN_WBAD_Update(wbadDto);
            }
            Integer ll_sumqty = appcom01Service.FPLAN_WBAD_SELECT_SUM(wbadDto);
            return ll_sumqty;
        } catch (Exception ex) {
            log.info("AppWBAD_INT Exception =====>" + ex.toString());
            return 0;
        }
    }



    //불량내역  list  010
    @RequestMapping(value="/SELWBAD", method = RequestMethod.POST)
    public Object AppWBAD_SEL(@RequestParam("custcd") String custcd
            ,@RequestParam("spjangcd") String spjangcd
            ,@RequestParam("plan_no") String plan_no
            ,@RequestParam("lotno") String lotno
            ,@RequestParam("wflag") String wflag) throws Exception {

        FPLANWBAD_VO wbadDto = new FPLANWBAD_VO();
        TBPopupVO wscntDto = new TBPopupVO();
        String ls_wbadflag = "1";
        switch (wflag){
            case "00010":
                ls_wbadflag = "1";
                break;
            case "00020":
                ls_wbadflag = "2";
                break;
            case "00030":
                ls_wbadflag = "3";
                break;
            case "00090":
                ls_wbadflag = "9";
                break;
            default:
                break;
        }
        wbadDto.setCustcd(custcd);
        wbadDto.setSpjangcd(spjangcd);
        wbadDto.setPlan_no(plan_no);
        wbadDto.setLotno(lotno);
        wbadDto.setWclscode(ls_wbadflag);
        wbadDto.setWflag(wflag);

        wscntDto.setCustcd(custcd);
        wscntDto.setSpjangcd(spjangcd);
        wscntDto.setPlan_no(plan_no);
        wscntDto.setLotno(lotno);
        wscntDto.setWclscode(ls_wbadflag);
        wscntDto.setWflag(wflag);
        if(appPopupService.GetWBadList(wscntDto) == null){
            return appcom01Service.FPLAN_WBAD_SELECT_blank(wbadDto);
        }else{
            return appPopupService.GetWBadList01(wscntDto);
        }
    }


    //생산량 등록
    @RequestMapping(value="/oworkupd", method = RequestMethod.POST)
    public String AppWOwork_Insert(@RequestParam("custcd") String custcd
            ,@RequestParam("spjangcd") String spjangcd
            ,@RequestParam("inplan_no") String plan_no
            ,@RequestParam("lotno") String lotno
            ,@RequestParam("inpcode") String pcode
            ,@RequestParam("inwono") String wono
            ,@RequestParam("popwotqt") float wotqt
            ,@RequestParam("popwbdqt") float wbdqt
            ,@RequestParam("popwkotDate") String wkokdate
            ,@RequestParam("wflag") String wflag
            ,@RequestParam("wseq") String wseq
            ,@RequestParam("seq") String seq
    ) throws Exception {

        FPLANW010_VO workDto = new FPLANW010_VO();
        FPLANW010_VO workDtoSum = new FPLANW010_VO();
        String ls_indate = getToDate();

//        String ls_yeare = wkokdate.substring(0,4);
//        String ls_mm = wkokdate.substring(5,7);
//        String ls_dd = wkokdate.substring(8,10);
//        wkokdate =  ls_yeare + ls_mm + ls_dd;

        workDto.setCustcd(custcd);
        workDto.setSpjangcd(spjangcd);
        workDto.setPlan_no(plan_no);
        workDto.setLotno(lotno);
        workDto.setPcode(pcode);
        workDto.setWono(wono);
        workDto.setWqty(wotqt);
        workDto.setBqty(wbdqt);
        if(wbdqt > 0 ){
            workDto.setQty(wotqt - wbdqt);
        }else{
            workDto.setQty(wotqt);
        }
        workDto.setWotdt(wkokdate);
        workDto.setWflag(wflag);
        switch (wflag){
            case "00010":
                workDto.setWseq("01");
                break;
            case "00020":
                workDto.setWseq("02");
                break;
            case "00030":
                workDto.setWseq("03");
                break;
            case "00090":
                workDto.setWseq("04");
                break;
            default:
                break;
        }

        workDto.setStore("W01");
        workDto.setIndate(getToDate());
//        String ls_lotno = wono.substring(2,11);
//        workDto.setLotno(ls_lotno);
        if(seq == null || seq.equals("")) {
            String ls_seq = appcom01Service.FPLAN_OWORK_MAXWSEQ(workDto);
            if (ls_seq == null) {
                ls_seq = "001";
            } else {
                int ll_seq = Integer.parseInt(ls_seq) + 1;
                ls_seq = Integer.toString(ll_seq);
                if (ls_seq.length() == 1) {
                    ls_seq = "00" + ls_seq;
                } else if (ls_seq.length() == 2) {
                    ls_seq = "0" + ls_seq;
                }
            }
            workDto.setSeq(ls_seq);
            appcom01Service.FPLAN_OWORK_Insert(workDto);
        }else{
            workDto.setSeq(seq);
            appcom01Service.FPLAN_OWORK_Update(workDto);
        }
        workDtoSum = (FPLANW010_VO) appcom01Service.FPLAN_OWORK_SUMQTY(workDto);
        workDto.setWotqt(workDtoSum.getWotqt());  //생산수량
        workDto.setWbdqt(workDtoSum.getWbdqt());  //불량수량
        switch (wflag){
            case "00010":
                appcom01Service.FPLANWORK_Update(workDto);
                appcom01Service.FPLANW010_Update(workDto);
                break;
            case "00020":
                appcom01Service.FPLANWORK_Update(workDto);
//                appcom02Service.FPLANW020_Update(workDto);
                break;
            case "00030":
                appcom01Service.FPLANWORK_Update(workDto);
//                appcom03Service.FPLANW030_Update(workDto);
                break;
            case "00090":
                appcom01Service.FPLANWORK_Update(workDto);
//                appcom04Service.FPLANW040_Update(workDto);
                break;
            default:
                break;
        }
        return "success";
    }


    @ResponseBody
    @RequestMapping(value="/oworkdel", method = RequestMethod.POST)
    public String AppWOwork_Delete(@RequestParam("plan_no") String plan_no
            ,@RequestParam("lotno") String lotno
            ,@RequestParam("wseq") String wseq
            ,@RequestParam("seq") String seq
            ,@RequestParam("pcode") String pcode
            ,@RequestParam("wflag") String wflag
    ) throws Exception {

        workDto.setPlan_no(plan_no);
        workDto.setLotno(lotno);
        workDto.setWseq(wseq);
        workDto.setSeq(seq);
        workDto.setPcode(pcode);
        workDto.setWflag(wflag);
        appcom01Service.FPLAN_OWORK_PERDELETE(workDto);
        return "success" ;
    }


    @ResponseBody
    @RequestMapping(value="/wbadlistdel", method = RequestMethod.POST)
    public Integer AppWbadlist_Delete(@RequestParam("lotno") String lotno
            ,@RequestParam("wseq") String wseq
            ,@RequestParam("wcode") String wcode
            ,@RequestParam("wflag") String wflag
            ,@RequestParam("plan_no") String plan_no
    ) throws Exception {

        FPLANWBAD_VO wbadDto = new FPLANWBAD_VO();
        wbadDto.setPlan_no(plan_no);
        wbadDto.setLotno(lotno);
        wbadDto.setWflag(wflag);
        wbadDto.setWcode(wcode);
        wbadDto.setWseq(wseq);
        result = appcom01Service.FPLAN_WBAD_WCODE_Delete(wbadDto);
        if (!result) {
            log.info("AppWbadlist_Delete =====> ");
            return 0;
        }
        Integer ll_sumqty = appcom01Service.FPLAN_WBAD_SELECT_SUM(wbadDto);
        return ll_sumqty ;
    }


    //작업설비 update 조회
    @RequestMapping(value="/prtcount", method = RequestMethod.POST)
    public String AppW010Prtcount_index(@RequestParam("lotno") String lotno
                                        ,@RequestParam("wflag") String wflag) throws Exception {

        FPLANW010_VO _itemDto = new FPLANW010_VO();

        _itemDto.setLotno(lotno);
        _itemDto.setIndate(getToDate());
        _itemDto.setWflag(wflag);
        _itemDto.setSpjangcd("ZZ");
        String ls_seq = appcom01Service.FPLAN_BARCODE_MAXCD(_itemDto);
        if (ls_seq == null) {
            ls_seq = "001";
        } else {
            int ll_seq = Integer.parseInt(ls_seq) + 1;
            ls_seq = Integer.toString(ll_seq);
            if (ls_seq.length() == 1) {
                ls_seq = "00" + ls_seq;
            } else if (ls_seq.length() == 2) {
                ls_seq = "0" + ls_seq;
            }
        }
        _itemDto.setSeq(ls_seq);
        appcom01Service.FPLANBarcode_Delete(_itemDto);
        appcom01Service.FPLAN_BARCODE_Insert(_itemDto);
        return ls_seq;

    }



    @GetMapping("/generate-barcode")
    public void generateBarcode(@RequestParam String text, HttpServletResponse response) throws IOException, WriterException {
        int width = 300;
        int height = 100;
        int fontSize = 20;
        int margin = 10;

        Code128Writer barcodeWriter = new Code128Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(text, BarcodeFormat.CODE_128, width, height);

        BufferedImage barcodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // Create a new image with space for the text
        BufferedImage combinedImage = new BufferedImage(width, height + fontSize + margin, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combinedImage.createGraphics();

        // Draw the barcode image
        g.drawImage(barcodeImage, 0, 0, null);

        // Set text properties
        g.setFont(new Font("Arial", Font.PLAIN, fontSize));
        g.setColor(Color.BLACK);

        // Draw the text below the barcode
        FontMetrics fontMetrics = g.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);
        int x = (width - textWidth) / 2;
        int y = height + fontSize + margin / 2;

        g.drawString(text, x, y);

        g.dispose();

        response.setContentType("image/png");
        ImageIO.write(combinedImage, "PNG", response.getOutputStream());
    }


    private String setDateFormat(String arg){

        String year = arg.substring(0,4) ;
        String month = arg.substring(5,7) ;
        String day   = arg.substring(8,10) ;
        return arg = year + month + day ;
    }
    private String getFrDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, -14); // 빼고 싶다면 음수 입력
        Date date      = new Date(cal1.getTimeInMillis());

        return formatter.format(date);
    }
    private String getToDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date date      = new Date(System.currentTimeMillis());

        return formatter.format(date);
    }
    private String getToDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //HH 24시 hh 12시
        Date date      = new Date(System.currentTimeMillis());

        return formatter.format(date); 
    }
    private String getWtimeMaxSeq(){

        String ls_seq = appcom01Service.FPLAN_WTIME_MAXSEQ(wtimeDto);
        if (ls_seq == null) {
            ls_seq = "001";
        } else {
            int ll_seq = Integer.parseInt(ls_seq) + 1;
            ls_seq = Integer.toString(ll_seq);
            if (ls_seq.length() == 1) {
                ls_seq = "00" + ls_seq;
            } else if (ls_seq.length() == 2) {
                ls_seq = "0" + ls_seq;
            }
        }
        return ls_seq;
    }
    ;
}
