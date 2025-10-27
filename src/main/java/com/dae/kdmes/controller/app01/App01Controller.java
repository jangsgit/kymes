package com.dae.kdmes.controller.app01;

import com.dae.kdmes.DTO.App01.*;
import com.dae.kdmes.DTO.Appm.FPLANW010_VO;
import com.dae.kdmes.DTO.Appm.FPLAN_VO;
import com.dae.kdmes.DTO.Appm.TBPopupVO;
import com.dae.kdmes.DTO.Cms.CmsIndex01Dto;
import com.dae.kdmes.DTO.CommonDto;
import com.dae.kdmes.DTO.Popup.PopupDto;
import com.dae.kdmes.DTO.UserFormDto;
import com.dae.kdmes.Service.App01.*;
import com.dae.kdmes.Service.App03.Index35Service;
import com.dae.kdmes.Service.Appm.AppPopupService;
import com.dae.kdmes.Service.Appm.Appcom01Service;
import com.dae.kdmes.Service.Cms.CmsIndex01Service;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// @RestController : return을 텍스트로 반환함.
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/app01", method = RequestMethod.POST)
public class App01Controller {
    private final Index01Service service01;

    private final Index02Service service02;

    private final Index03Service service03;

    private final Index04Service service04;

    private final Index08Service service08;
    private final Index35Service service35;

    private final Appcom01Service appcom01Service;
    private final AppPopupService appPopupService;
    CommonDto CommDto = new CommonDto();
    PopupDto popupDto = new PopupDto();

    TBPopupVO wrmcDto = new TBPopupVO();

    Index01Dto index01Dto = new Index01Dto();

    Index02Dto index02Dto = new Index02Dto();

    Index03Dto index03Dto = new Index03Dto();

    Index04Dto index04Dto = new Index04Dto();

    List<PopupDto> popupListDto = new ArrayList<>();

    List<PopupDto> popupListDto1 = new ArrayList<>();

    List<PopupDto> popupListDto2 = new ArrayList<>();
    List<Index01Dto> index01ListDto = new ArrayList<>();

    List<Index01Dto> index01ListDto1 = new ArrayList<>();

    List<Index02Dto> index02ListDto = new ArrayList<>();
    List<Index03Dto> index03List = new ArrayList<>();

    List<Index04Dto> index04ListDto = new ArrayList<>();
    List<Index02Dto> index07List = new ArrayList<>();

    Index02Dto index07Dto = new Index02Dto();

    protected Log log =  LogFactory.getLog(this.getClass());
    //공통코드등록
    @GetMapping(value="/index01")
    public String App01_index(Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("공통코드등록");
        CommDto.setMenuUrl("기준정보>공통코드등록");
        CommDto.setMenuCode("index01");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        try {
            index01Dto.setCom_cls("%");
            index01ListDto = service01.getComCodeList(index01Dto);
            index01ListDto1 = service01.getCom_rem2_keyList(index01Dto);

            model.addAttribute("com_rem2_keyList",index01ListDto1);
            model.addAttribute("comcodeList",index01ListDto);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App01_index Exception =============================");
            log.info("Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return "App01/index01";
    }
    @GetMapping(value="/index18")
    public String App18_index(Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("사원코드등록");
        CommDto.setMenuUrl("기준정보>사원코드등록");
        CommDto.setMenuCode("index18");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        try {
            index01Dto.setCom_cls("002");
            index01ListDto = service01.getComCodeList(index01Dto);
            index01ListDto1 = service01.getCom_rem2_keyList(index01Dto);

            model.addAttribute("com_rem2_keyList",index01ListDto1);
            model.addAttribute("comcodeList",index01ListDto);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App18_index Exception =============================");
            log.info("Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return "App01/index18";
    }



    @GetMapping(value="/index02")
    public String App01_index02(Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("공정기준등록");
        CommDto.setMenuUrl("기준정보>공정기준등록");
        CommDto.setMenuCode("index02");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        try {
            index02ListDto = service02.getWflagList(index02Dto);
         //   index02ListDto = service02.getWrcmList(index02Dto);

            model.addAttribute("WflagList",index02ListDto);
        //    model.addAttribute("WrcmList",index02ListDto);
        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App02_index Exception =============================");
            log.info("Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return "App01/index02";
    }


    //재고실사등록
    @GetMapping(value="/index03")
    public String App03_index( Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("재고등록");
        CommDto.setMenuUrl("기준정보>재고등록");
        CommDto.setMenuCode("index03");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        try {

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("index03 Exception ================================================================");
            log.info("Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return "App01/index03";
    }

    //제품등록
    @GetMapping(value="/index05")
    public String App05_index( Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("kcc제품등록");
        CommDto.setMenuUrl("기준정보>제품정보");
        CommDto.setMenuCode("index05");
        TBPopupVO _indexpopDto = new TBPopupVO();
        List<TBPopupVO> _indexJpumList = new ArrayList<>();
        List<TBPopupVO> _indexDoor1List = new ArrayList<>();
        List<TBPopupVO> _indexDoor2List = new ArrayList<>();
        List<TBPopupVO> _indexFormList = new ArrayList<>();
        List<TBPopupVO> _indexColorList = new ArrayList<>();
        List<TBPopupVO> _indexJthickList = new ArrayList<>();
        List<PopupDto> _popupListDto = new ArrayList<>();
        List<PopupDto> _popupListDto1 = new ArrayList<>();
        List<PopupDto> _popupListDto2 = new ArrayList<>();
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);
        popupDto.setJ1_key("%");
        try {
            index03Dto.setJpum("%");
            index03Dto.setJ_misayong("%");
            index03Dto.setW_b_gubn("J");
            index03Dto.setJ_dae("02");
            index03List = service03.GetJpumList(index03Dto);
            _popupListDto = service03.getj1_keyList(popupDto);
            _popupListDto1 = service03.getj2_keyList(popupDto);
            _popupListDto2 = service03.getGumtype_keyList(popupDto);


            _indexJpumList = service03.GetJpumComboList(_indexpopDto);
            _indexDoor1List = service03.GetDoor1ComboList(_indexpopDto);
            _indexDoor2List = service03.GetDoor2ComboList(_indexpopDto);
            _indexFormList = service03.GetFormComboList(_indexpopDto);
            _indexColorList = service03.GetColorComboList(_indexpopDto);
            _indexJthickList = service03.GetJthickComboList(_indexpopDto);


            model.addAttribute("j1_keyList",_popupListDto);
            model.addAttribute("j2_keyList",_popupListDto1);
            model.addAttribute("j3_keyList",_popupListDto2);
            model.addAttribute("index03List",index03List);
            model.addAttribute("jpumList",_indexJpumList);
            model.addAttribute("door1List",_indexDoor1List);
            model.addAttribute("door2List",_indexDoor2List);
            model.addAttribute("formList",_indexFormList);
            model.addAttribute("colorList",_indexColorList);
            model.addAttribute("jthickList",_indexJthickList);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App03001Tab01Form Exception ===== ======");
            log.info("Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return "App01/index05";
    }


    //제품등록
    @GetMapping(value="/index16")
    public String App16_index( Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("매출제품등록");
        CommDto.setMenuUrl("기준정보>제품정보");
        CommDto.setMenuCode("index16");
        TBPopupVO _indexpopDto = new TBPopupVO();
        List<TBPopupVO> _indexJpumList = new ArrayList<>();
        List<TBPopupVO> _indexDoor1List = new ArrayList<>();
        List<TBPopupVO> _indexDoor2List = new ArrayList<>();
        List<TBPopupVO> _indexFormList = new ArrayList<>();
        List<TBPopupVO> _indexColorList = new ArrayList<>();
        List<TBPopupVO> _indexJthickList = new ArrayList<>();

        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        try {
            index03Dto.setJpum("%");
            index03Dto.setJ_misayong("%");
            index03Dto.setW_b_gubn("J");
            index03Dto.setJ_dae("04");
            index03List = service03.GetJpumList(index03Dto);
            popupListDto = service03.getj1_keyList(popupDto);
            popupListDto1 = service03.getj2_keyList(popupDto);
            popupListDto2 = service03.getGumtype_keyList(popupDto);


            _indexJpumList = service03.GetJpumComboList(_indexpopDto);
            _indexDoor1List = service03.GetDoor1ComboList(_indexpopDto);
            _indexDoor2List = service03.GetDoor2ComboList(_indexpopDto);
            _indexFormList = service03.GetFormComboList(_indexpopDto);
            _indexColorList = service03.GetColorComboList(_indexpopDto);
            _indexJthickList = service03.GetJthickComboList(_indexpopDto);


            model.addAttribute("j1_keyList",popupListDto);
            model.addAttribute("j2_keyList",popupListDto1);
            model.addAttribute("j3_keyList",popupListDto2);
            model.addAttribute("index03List",index03List);
            model.addAttribute("jpumList",_indexJpumList);
            model.addAttribute("door1List",_indexDoor1List);
            model.addAttribute("door2List",_indexDoor2List);
            model.addAttribute("formList",_indexFormList);
            model.addAttribute("colorList",_indexColorList);
            model.addAttribute("jthickList",_indexJthickList);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App03001Tab01Form Exception ===== ======");
            log.info("Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return "App01/index16";
    }

    //제품등록
    @GetMapping(value="/index051")
    public String App051_index( Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("원자재등록");
        CommDto.setMenuUrl("기준정보>제품정보");
        CommDto.setMenuCode("index051");
        TBPopupVO _indexpopDto = new TBPopupVO();
        List<TBPopupVO> _indexJpumList = new ArrayList<>();
        List<TBPopupVO> _indexDoor1List = new ArrayList<>();
        List<TBPopupVO> _indexDoor2List = new ArrayList<>();
        List<TBPopupVO> _indexFormList = new ArrayList<>();
        List<TBPopupVO> _indexColorList = new ArrayList<>();
        List<TBPopupVO> _indexJthickList = new ArrayList<>();

        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        try {
            index03Dto.setJpum("%");
            index03Dto.setJ_misayong("%");
            index03Dto.setW_b_gubn("W");
            index03Dto.setJ_dae("%");
            popupDto.setJ1_key("01");
            index03List = service03.GetJpumList(index03Dto);
            popupListDto = service03.getj1_keyList(popupDto);
            popupListDto1 = service03.getj2_keyList(popupDto);
            popupListDto2 = service03.getGumtype_keyList(popupDto);

            _indexJpumList = service03.GetJpumComboList(_indexpopDto);
            _indexDoor1List = service03.GetDoor1ComboList(_indexpopDto);
            _indexDoor2List = service03.GetDoor2ComboList(_indexpopDto);
            _indexFormList = service03.GetFormComboList(_indexpopDto);
            _indexColorList = service03.GetColorComboList(_indexpopDto);
            _indexJthickList = service03.GetJthickComboList(_indexpopDto);


            model.addAttribute("j1_keyList",popupListDto);
            model.addAttribute("j2_keyList",popupListDto1);
            model.addAttribute("j3_keyList",popupListDto2);
            model.addAttribute("index03List",index03List);
            model.addAttribute("jpumList",_indexJpumList);
            model.addAttribute("door1List",_indexDoor1List);
            model.addAttribute("door2List",_indexDoor2List);
            model.addAttribute("formList",_indexFormList);
            model.addAttribute("colorList",_indexColorList);
            model.addAttribute("jthickList",_indexJthickList);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App03001Tab01Form Exception ===== ======");
            log.info("Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return "App01/index051";
    }


    //제품등록
    @GetMapping(value="/index052")
    public String App052_index( Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("예비부품등록");
        CommDto.setMenuUrl("기준정보>제품정보");
        CommDto.setMenuCode("index052");
        TBPopupVO _indexpopDto = new TBPopupVO();
        List<TBPopupVO> _indexJpumList = new ArrayList<>();
        List<TBPopupVO> _indexDoor1List = new ArrayList<>();
        List<TBPopupVO> _indexDoor2List = new ArrayList<>();
        List<TBPopupVO> _indexFormList = new ArrayList<>();
        List<TBPopupVO> _indexColorList = new ArrayList<>();
        List<TBPopupVO> _indexJthickList = new ArrayList<>();

        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        try {
            index03Dto.setJpum("%");
            index03Dto.setJ_misayong("%");
            index03Dto.setW_b_gubn("H");
            index03Dto.setJ_dae("%");
            popupDto.setJ1_key("03");
            index03List = service03.GetJpumList(index03Dto);
            popupListDto = service03.getj1_keyList(popupDto);
            popupListDto1 = service03.getj2_keyList(popupDto);
            popupListDto2 = service03.getGumtype_keyList(popupDto);


            _indexJpumList = service03.GetJpumComboList(_indexpopDto);
            _indexDoor1List = service03.GetDoor1ComboList(_indexpopDto);
            _indexDoor2List = service03.GetDoor2ComboList(_indexpopDto);
            _indexFormList = service03.GetFormComboList(_indexpopDto);
            _indexColorList = service03.GetColorComboList(_indexpopDto);
            _indexJthickList = service03.GetJthickComboList(_indexpopDto);


            model.addAttribute("j1_keyList",popupListDto);
            model.addAttribute("j2_keyList",popupListDto1);
            model.addAttribute("j3_keyList",popupListDto2);
            model.addAttribute("index03List",index03List);
            model.addAttribute("jpumList",_indexJpumList);
            model.addAttribute("door1List",_indexDoor1List);
            model.addAttribute("door2List",_indexDoor2List);
            model.addAttribute("formList",_indexFormList);
            model.addAttribute("colorList",_indexColorList);
            model.addAttribute("jthickList",_indexJthickList);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App03001Tab01Form Exception ===== ======");
            log.info("Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return "App01/index052";
    }


    @GetMapping(value="/index04")
    public String App01_index04(Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("품목그룹등록");
        CommDto.setMenuUrl("기준정보>품목그룹등록");
        CommDto.setMenuCode("index04");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        try {
            index04ListDto = service04.getPgunList(index04Dto);

            model.addAttribute("PgunList",index04ListDto);
        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App04_index Exception =============================");
            log.info("Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return "App01/index04";
    }



    @GetMapping(value="/index06")
    public String App01_index06(Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("소요량등록");
        CommDto.setMenuUrl("기준정보>소요량등록");
        CommDto.setMenuCode("index06");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);
        List<IndexCa613Dto> _indexCa613ListDto = new ArrayList<>();
        IndexCa613Dto _indexCa613Dto = new IndexCa613Dto();

        try {
            _indexCa613Dto.setOpcod("%");
            _indexCa613ListDto = service35.SelectBomListTot(_indexCa613Dto);
            //   index02ListDto = service02.getWrcmList(index02Dto);

            model.addAttribute("WflagList",_indexCa613ListDto);
            //    model.addAttribute("WrcmList",index02ListDto);
        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App06_index Exception =============================");
            log.info("Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return "App01/index06";
    }


    @GetMapping(value="/index07")
    public String App01_index07(Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("거래처등록");
        CommDto.setMenuUrl("기준정보>거래처정보");
        CommDto.setMenuCode("index02");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);
//        userformDto.setPagetree01("거래처등록");
//        userformDto.setPagenm("본사기준정보");
//        model.addAttribute("CommDto", CommDto);

        try {
            index07Dto.setAcorp("%");
            index07List = service02.GetCifList(index07Dto);
            popupListDto = service02.getCifCodeList(popupDto);

            model.addAttribute("index07List",index07List);
            model.addAttribute("cifcodeList",popupListDto);
        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App03001Tab01Form Exception ================================================================");
            log.info("Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return "App01/index07";
    }


    @GetMapping(value="/index08")
    public String App01_index08(Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("금형등록");
        CommDto.setMenuUrl("기준정보>금형등록");
        CommDto.setMenuCode("index08");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);
        TBPopupVO _popupDto = new TBPopupVO();
        List<Pc110Dto> _index08ListDto = new ArrayList<>();
        List<PopupDto> _popupListDto = new ArrayList<>();
        List<TBPopupVO> _popupBoganListDto = new ArrayList<>();
        Pc110Dto _index08Dto = new Pc110Dto();
        try {
            _popupListDto = service03.getj1_keyList(popupDto);
            _popupBoganListDto = service03.GetBoganComboList(_popupDto);
            _index08Dto.setMachcd("%");
            _index08ListDto = service08.getMachList(_index08Dto);
            //   index02ListDto = service02.getWrcmList(index02Dto);

            model.addAttribute("Index08List",_index08ListDto);
            model.addAttribute("j1_keyList",_popupListDto);
            model.addAttribute("boganlist",_popupBoganListDto);
            //    model.addAttribute("WrcmList",index02ListDto);
        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App08_index Exception =============================");
            log.info("Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return "App01/index08";
    }


    @GetMapping(value="/index22")
    public String App01_index22(Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("설비등록");
        CommDto.setMenuUrl("기준정보>설비등록");
        CommDto.setMenuCode("index22");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);
        List<Pc120Dto> _index08ListDto = new ArrayList<>();
        Pc120Dto _index08Dto = new Pc120Dto();
        try {
            _index08Dto.setFacname("%");
            _index08ListDto = service08.getFacList(_index08Dto);
            //   index02ListDto = service02.getWrcmList(index02Dto);

            model.addAttribute("Index22List",_index08ListDto);
            //    model.addAttribute("WrcmList",index02ListDto);
        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App01_index22 Exception =============================");
            log.info("Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return "App01/index22";
    }


    @GetMapping(value="/index23")
    public String App01_index23(Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("수주업로드");
        CommDto.setMenuUrl("기준정보>수주업로드");
        CommDto.setMenuCode("index23");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);
        List<Pc120Dto> _index08ListDto = new ArrayList<>();
        List<TBPopupVO> _indexJpumList = new ArrayList<>();
        Pc120Dto _index08Dto = new Pc120Dto();
        TBPopupVO _indexpopDto = new TBPopupVO();

        try {
            _index08Dto.setFacname("%");
            _index08ListDto = service08.getFacList(_index08Dto);
            //   index02ListDto = service02.getWrcmList(index02Dto);
            model.addAttribute("Index23List",_index08ListDto);
            wrmcDto.setWflag("00010");
            wrmcDto.setMachname("%");
            model.addAttribute("wrmcist", appPopupService.GetWrmcList01(wrmcDto));


            _indexJpumList = service03.GetJpumComboList(_indexpopDto);
            model.addAttribute("jpumList",_indexJpumList);


            //    model.addAttribute("WrcmList",index02ListDto);
        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App01_index23 Exception =============================");
            log.info("Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return "App01/index23";
    }

    @GetMapping(value="/index09")
    public String App01_index09(Model model, HttpServletRequest request) throws Exception{
        List<FPLAN_VO> _itemSachulDtoList = new ArrayList<>();
        CommDto.setMenuTitle("사출공정");  //
        CommDto.setMenuUrl("생산공정>사출공정");
        CommDto.setMenuCode("appcom01");
        FPLAN_VO fplanDto = new FPLAN_VO();
        FPLANW010_VO itemDto = new FPLANW010_VO();
        List<FPLAN_VO> itemDtoList = new ArrayList<>();
        TBPopupVO wrmcDto = new TBPopupVO();
        TBPopupVO wperidDto = new TBPopupVO();


        String fdate = getFrDate();
        String tdate =  getAddDate();
        String cltcd = "%";
        String pcode = "%";
        String lotno = "%";
        String ls_month = "%";
        fplanDto.setLine("00");
        fplanDto.setWflag("00010");
        fplanDto.setLotno(lotno);
        fplanDto.setFdate(fdate);
        fplanDto.setTdate(tdate);
        fplanDto.setCltcd(cltcd);
        fplanDto.setPcode(pcode);
        ls_month = tdate.substring(4,6);
        if(ls_month.substring(0,1).equals("0")){ ;
            ls_month = ls_month.substring(1,2) + "월";
        }else{
            ls_month = ls_month.substring(0,2) + "월";
        }
        fplanDto.setInmonth(ls_month);
        fplanDto.setInweeks("%");
        itemDto.setPlan_no("%");
        itemDtoList = appcom01Service.GetFPLAN_List(fplanDto);
//        model.addAttribute("itemDto", itemDto);
        model.addAttribute("itemDtoList", itemDtoList);
        _itemSachulDtoList = appcom01Service.GetFPLAN_SachulList(fplanDto);
        model.addAttribute("itemSachulDtoList", _itemSachulDtoList);




        wperidDto.setWflag("00010");  //첫번째공정
        wperidDto.setWpernm("%");
        wrmcDto.setPlan_no("%");      //불량구분 팝업
        wrmcDto.setWseq("%");
        wrmcDto.setMachname("%");
        wrmcDto.setWflag("00010");
        wrmcDto.setWclscode("1");
        model.addAttribute("CommDto", CommDto);
        model.addAttribute("wrmcDto", appPopupService.GetWrmcList01(wrmcDto));          //설비명
//        log.info("Exception =====>" + appPopupService.GetPernmList(wperidDto).toString());
        model.addAttribute("wperidDto", appPopupService.GetPernmList(wperidDto));       //작업자
        model.addAttribute("wstopDto", appPopupService.GetStopList(wperidDto));        //비가동사유

//        wbomDto.setPlan_no("202108120027");
        model.addAttribute("wfbomDto", appPopupService.GetWfbomList_blank());
//        model.addAttribute("wfbomDto", appPopupService.GetWfbomList_blank());
        model.addAttribute("wfiworkDto", appPopupService.GetWfiworkList_blank());

        model.addAttribute("wbadDto", appPopupService.GetWBadList01(wrmcDto));          //불량구분
        model.addAttribute("wbadDDDto", appPopupService.GetWBadDDList(wrmcDto));          //불량구분

        return "App01/index09";
    }


    @GetMapping(value="/indexds01")
    public String App01_indexds01(Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("공정기준등록");
        CommDto.setMenuUrl("기준정보>공정기준등록");
        CommDto.setMenuCode("index02");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);


        return "App01/indexds01";
    }

    @GetMapping(value="/indexds02")
    public String App01_indexds02(Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("공정기준등록");
        CommDto.setMenuUrl("기준정보>공정기준등록");
        CommDto.setMenuCode("index02");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);


        return "App01/indexds02";
    }

    @GetMapping(value="/indexds03")
    public String App01_indexds03(Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("공정기준등록");
        CommDto.setMenuUrl("기준정보>공정기준등록");
        CommDto.setMenuCode("index02");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);


        return "App01/indexds03";
    }

    @GetMapping(value="/indexds04")
    public String App01_indexds04(Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("생산가동현황");
        CommDto.setMenuUrl("모니터링>생산가동현황");
        CommDto.setMenuCode("index04");
        List<CmsIndex01Dto> cms01List = new ArrayList<>();
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        return "App01/indexds04";
    }



    private String getFrDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, -100); // 빼고 싶다면 음수 입력
        Date date      = new Date(cal1.getTimeInMillis());

        return formatter.format(date);
    }

    private String getAddDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, 14); // 빼고 싶다면 음수 입력
        Date date      = new Date(cal1.getTimeInMillis());

        return formatter.format(date);
    }




}

