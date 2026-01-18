package com.dae.kdmes.controller.app01;

import com.dae.kdmes.DTO.App01.*;
import com.dae.kdmes.DTO.App05ElvlrtDto;
import com.dae.kdmes.DTO.Appm.TBPopupVO;
import com.dae.kdmes.DTO.AttachDTO;
import com.dae.kdmes.DTO.CommonDto;
import com.dae.kdmes.DTO.Popup.PopupDto;
import com.dae.kdmes.DTO.UserFormDto;
import com.dae.kdmes.Exception.AttachFileException;
import com.dae.kdmes.Service.App01.*;
import com.dae.kdmes.Service.Appm.AppPopupService;
import com.dae.kdmes.Service.impl.AppUploadServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

// @RestController : return을 텍스트로 반환함.
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/app01m", method = RequestMethod.POST)
public class App01CrudController {
    private final Index01Service service01;

    private final Index02Service service02;

    private final Index03Service service03;

    private final Index04Service service04;

    private final Index08Service service08;
    private final AppPopupService appPopupService;
    private final AppUploadServiceImpl appServiceImpl;
    CommonDto CommDto = new CommonDto();
    PopupDto popupDto = new PopupDto();

    Index01Dto index01Dto = new Index01Dto();

    Index02Dto index02Dto = new Index02Dto();

    Index03Dto index03Dto = new Index03Dto();

    Index04Dto index04Dto = new Index04Dto();

    List<PopupDto> popupListDto = new ArrayList<>();
    List<Index01Dto> index01ListDto = new ArrayList<>();

    List<Index02Dto> index02ListDto = new ArrayList<>();
    List<Index03Dto> index03List = new ArrayList<>();

    List<Index04Dto> index04ListDto = new ArrayList<>();

    Index02Dto index07Dto = new Index02Dto();
    List<Index02Dto> index07List = new ArrayList<>();

    protected Log log =  LogFactory.getLog(this.getClass());



    @RequestMapping(value="/comcodesave")
    public String App01ComCodeSave_index(  @RequestParam("com_cls") String com_cls,
                                           @RequestParam("com_cnam") String com_cnam,
                                           Model model,   HttpServletRequest request){
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        Boolean result = false;
        index01Dto.setCom_cls(com_cls);
        index01Dto.setCom_cnam(com_cnam);
        index01ListDto = service01.getComCodeList(index01Dto);
        if(index01ListDto.isEmpty() || index01ListDto.size() == 0){
            result = service01.InsertComCode(index01Dto);
        }else{
            result = service01.UpdateComCode(index01Dto);
        }
        if (!result) {
            return "error";
        }
        return "success";
    }

    @RequestMapping(value="/comcodedel")
    public String App01ComCodeDel_index(  @RequestParam("com_cls") String com_cls,
                                          @RequestParam("com_cnam") String com_cnam,
                                          Model model,   HttpServletRequest request){
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        Boolean result = false;
        index01Dto.setCom_cls(com_cls);
        index01Dto.setCom_code(com_cnam);
        index01ListDto = service01.getComCodeList(index01Dto);
        result = service01.DeleteComCode(index01Dto);
        if (!result) {
            return "error";
        }
        return "success";
    }

    //업체분류현황
    @GetMapping(value="/comcodelist")
    public Object App01ComCodeList_index(@RequestParam("searchtxt") String searchtxt,
                                         Model model, HttpServletRequest request) throws Exception{
        try {

            if(searchtxt == null || searchtxt.equals("")){
                searchtxt = "%";
            }
            index01Dto.setCom_cls(searchtxt);
            index01ListDto = service01.getComCodeList(index01Dto);

            model.addAttribute("comcodeList",index01ListDto);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("insalist Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return index01ListDto;
    }

    //업체분류현황
    @GetMapping(value="/comcodelists")
    public Object App01ComCodeLists_index(@RequestParam("searchtxt") String searchtxt,
                                         Model model, HttpServletRequest request) throws Exception{
        try {

            if(searchtxt == null || searchtxt.equals("")){
                searchtxt = "%";
            }
            index01Dto.setCom_cls(searchtxt);
            index01ListDto = service01.getComCodeLists(index01Dto);

            model.addAttribute("comcodeLists",index01ListDto);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("insalist Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return index01ListDto;
    }

    //담당자현황
    @GetMapping(value="/wperidlist")
    public Object App01WperidList_index(@RequestParam("searchtxt") String searchtxt,
                                          Model model, HttpServletRequest request) throws Exception{
        try {

            if(searchtxt == null || searchtxt.equals("")){
                searchtxt = "%";
            }
            index01Dto.setCom_rem1(searchtxt);
//            index01Dto.setCom_cls(comcls);
            log.info("searchtxt =====>" + searchtxt);
            index01ListDto = service01.getWperidlist(index01Dto);

            model.addAttribute("wperidList",index01ListDto);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("insalist Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return index01ListDto;
    }


    //담당자현황
    @GetMapping(value="/wsaworkerlist")
    public Object App01WsaworkerList_index(@RequestParam("searchtxt") String searchtxt,
                                        Model model, HttpServletRequest request) throws Exception{
        TBPopupVO wperidDto = new TBPopupVO();
        List<TBPopupVO> _popupListDto = new ArrayList<>();
        try {

            if(searchtxt == null || searchtxt.equals("")){
                searchtxt = "%";
            }
            wperidDto.setWflag("00010");  //첫번째공정
            wperidDto.setWpernm(searchtxt);
            log.info("searchtxt =====>" + searchtxt);
            _popupListDto = appPopupService.GetPernmList(wperidDto);
            model.addAttribute("wperidList",_popupListDto);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("insalist Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return _popupListDto;
    }

    @GetMapping(value="/wbadlist")
    public Object App01WbadList_index(@RequestParam("searchtxt") String searchtxt,
                                        Model model, HttpServletRequest request) throws Exception{
        try {

            if(searchtxt == null || searchtxt.equals("")){
                searchtxt = "%";
            }
            index01Dto.setCom_code(searchtxt);
            index01Dto.setCom_rem1(searchtxt);
            index01ListDto = service01.getWbadlist(index01Dto);

            model.addAttribute("wbadlist",index01ListDto);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("insalist Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return index01ListDto;
    }

    @RequestMapping(value="/comcodedetailsave")
    public String App01ComCodeDetailSave_index(  @RequestParam("com_cls") String com_cls,
                                           @RequestParam("com_code") String com_code,
                                           @RequestParam("com_cnam") String com_cnam,
                                           @RequestParam("com_rem1") String com_rem1,
                                           @RequestParam("com_rem2") String com_rem2,
                                           @RequestParam("com_work") String com_work,
                                           @RequestParam("newflag") String newflag,
                                           Model model,   HttpServletRequest request){
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        Boolean result = false;
        index01Dto.setCom_cls(com_cls);
        index01Dto.setCom_code(com_code);
        index01Dto.setCom_cnam(com_cnam);
        index01Dto.setCom_rem1(com_rem1);
        index01Dto.setCom_rem2(com_rem2);
        index01Dto.setCom_work(com_work);
        String ls_comcode = service01.GetComCodeCheck(index01Dto);

        if(newflag == null || newflag.equals("")){
            newflag = "";
        }
        if(newflag.equals("nw")){
            if(ls_comcode != null ){
                return "duplicate";
            }
        }
        if(ls_comcode == null || ls_comcode.equals("")){
            result = service01.InsertComCodeDetail(index01Dto);
        }else{
            result = service01.UpdateComCodeDetail(index01Dto);
        }
        if (!result) {
            return "error";
        }
        return "success";
    }

    @RequestMapping(value="/comcodedetaildel")
    public String App01ComCodeDetailDel_index(  @RequestParam("com_cls") String com_cls,
                                          @RequestParam("com_cnam") String com_cnam,
                                          @RequestParam("com_code") String com_code,
                                          Model model,   HttpServletRequest request){
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        Boolean result = false;
        index01Dto.setCom_cls(com_cls);
        index01Dto.setCom_cnam(com_cnam);
        index01Dto.setCom_code(com_code);
        //index01ListDto = service01.getComCodeDetailList(index01Dto);
        result = service01.DeleteComCodeDetail(index01Dto);
        if (!result) {
            return "error";
        }
        return "success";
    }

    @GetMapping(value="/comcodedetaillist")
    public Object App01ComdodeDetailList_index(@RequestParam("searchtxt") String searchtxt,
                                           @RequestParam("com_cls") String com_cls,
                                           Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("공통코드등록");
        CommDto.setMenuUrl("기준정보>공통코드등록");
        CommDto.setMenuCode("index01");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        try {
            if(searchtxt == null || searchtxt.equals("")){
                searchtxt = "%";
            }
            if(com_cls == null || com_cls.equals("")){
                com_cls = "%";
            }
            log.info("searchtxt =====>" + searchtxt );

            index01Dto.setCom_cls(com_cls);
            index01Dto.setCom_cnam(searchtxt);;
            index01ListDto = service01.GetComcodeDetailList(index01Dto);
            model.addAttribute("index01ListDto",index01ListDto);

        } catch (Exception ex) {
            log.info("App01ComdodeDetailList_index Exception =====>" + ex.toString());
        }

        return index01ListDto;
    }

    //업체분류현황
    @GetMapping(value="/getWflagList")
    public Object getWflagList(@RequestParam("searchtxt") String searchtxt,
                                          Model model, HttpServletRequest request) throws Exception{
        try {

            if(searchtxt == null || searchtxt.equals("")){
                searchtxt = "%";
            }
            index02Dto.setCom_code(searchtxt);
            index02ListDto = service02.getWflagList(index02Dto);

            model.addAttribute("WflagList",index02ListDto);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("insalist Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return index02ListDto;
    }

    @GetMapping(value="/getFplanDetailList")
    public Object App01getFplanDetailList_index(@RequestParam("searchtxt") String searchtxt,
                                               @RequestParam("com_code") String com_code,
                                               Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("공통코드등록");
        CommDto.setMenuUrl("기준정보>공통코드등록");
        CommDto.setMenuCode("index01");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        try {
            if(searchtxt == null || searchtxt.equals("")){
                searchtxt = "%";
            }
            if(com_code == null || com_code.equals("")){
                com_code = "%";
            }
            log.debug("searchtxt =====>" + searchtxt );

            index02Dto.setCom_cls(com_code);
            index02Dto.setCom_cnam(searchtxt);;
            index02ListDto = service02.GetFplanDetailList(index02Dto);
            model.addAttribute("index02ListDto",index02ListDto);

        } catch (Exception ex) {
            log.info("App01ComdodeDetailList_index Exception =====>" + ex.toString());
        }

        return index02ListDto;
    }

    @RequestMapping(value="/fplandetailsave")
    public String App01FplanDetailSave_index(  @RequestParam("com_code") String com_code,
                                                 @RequestParam("com_rem1") String com_rem1,
                                                 @RequestParam("wrmcnm") String wrmcnm,
                                                 @RequestParam("wrmc") String wrmc,
                                                 @RequestParam("wperid") String wperid,
                                                 Model model,   HttpServletRequest request){
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        Boolean result = false;
        index02Dto.setCom_code(com_code);
        index02Dto.setCom_rem1(com_rem1);
        index02Dto.setWrmc(wrmc);
        index02Dto.setWperid(wperid);
        index02Dto.setWrmcnm(wrmcnm);
        String ls_fplan = service02.GetFplanCheck(index02Dto);
        if(ls_fplan == null || ls_fplan.equals("")){
            result = service02.InsertFplanDetail(index02Dto);
        }else{
            result = service02.UpdateFplanDetail(index02Dto);
        }
        if (!result) {
            return "error";
        }
        return "success";
    }

    @RequestMapping(value="/fplandetaildel")
    public String App01FplanDetailDel_index(  @RequestParam("wrmc") String wrmc,
                                                @RequestParam("wrmcnm") String wrmcnm,
                                                @RequestParam("wperid") String wperid,
                                                Model model,   HttpServletRequest request){
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        Boolean result = false;
        index02Dto.setWrmc(wrmc);
        index02Dto.setWrmcnm(wrmcnm);
        index02Dto.setWperid(wperid);
        //index01ListDto = service01.getComCodeDetailList(index01Dto);
        result = service02.DeleteFplanDetail(index02Dto);
        if (!result) {
            return "error";
        }
        return "success";
    }

    @GetMapping(value="/fplandetaillist")
    public Object App01FplanDetailList_index(@RequestParam("searchtxt") String searchtxt,
                                               @RequestParam("com_code") String com_code,
                                               Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("공통코드등록");
        CommDto.setMenuUrl("기준정보>공정별설비등록");
        CommDto.setMenuCode("index02");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        try {
            if(searchtxt == null || searchtxt.equals("")){
                searchtxt = "%";
            }
            if(com_code == null || com_code.equals("")){
                com_code = "%";
            }
//            log.debug("searchtxt =====>" + searchtxt );

            index02Dto.setCom_code(com_code);
            index02Dto.setCom_cnam(searchtxt);;
            index02ListDto = service02.GetFplanDetailList(index02Dto);
            model.addAttribute("index02ListDto",index02ListDto);

        } catch (Exception ex) {
            log.info("App01ComdodeDetailList_index Exception =====>" + ex.toString());
        }

        return index02ListDto;
    }
    @GetMapping(value="/index03/list")
    public Object App03List_index(@RequestParam("searchtxt") String searchtxt,
                                  @RequestParam("gubn") String gubn,
                                  Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("제품등록");
        CommDto.setMenuUrl("기준정보>제품정보");
        CommDto.setMenuCode("index03");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        try {

            if(searchtxt == null || searchtxt.equals("")){
                searchtxt = "%";
            }
            index03Dto.setJpum(searchtxt);
            index03Dto.setJkey(searchtxt);
            index03Dto.setJ_misayong("0");
            index03Dto.setW_b_gubn(gubn);
            index03Dto.setJ_dae("%");
//            log.info("gubn =====>" + gubn );
//            log.info("searchtxt =====>" + searchtxt );
            if (gubn.equals("W")) {
                index03Dto.setW_b_gubn("J");
                index03List = service03.GetJpumList_BH(index03Dto);
            }else if(gubn.equals("WW")){
                index03Dto.setW_b_gubn("W");
                index03List = service03.GetJpumList(index03Dto);
            }else{
                index03List = service03.GetJpumList(index03Dto);
            }
            model.addAttribute("index03List",index03List);

        } catch (Exception ex) {
            log.info("App02List_index Exception =====>" + ex.toString());
        }

        return index03List;
    }

    @GetMapping(value="/index03/listmain")
    public Object App03MainList_index(@RequestParam("searchtxt") String searchtxt,
                                      @RequestParam("congubn") String congubn,
                                      @RequestParam("j_dae") String j_dae,
                                  Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("제품등록");
        CommDto.setMenuUrl("기준정보>제품정보");
        CommDto.setMenuCode("index03");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        try {

            if(searchtxt == null || searchtxt.equals("")){
                searchtxt = "%";
            }
            if(j_dae == null || j_dae.equals("")){
                j_dae = "%";
            }
            index03Dto.setJpum(searchtxt);
            index03Dto.setJkey(searchtxt);
            index03Dto.setJ_misayong("%");
            index03Dto.setW_b_gubn(congubn);
            index03Dto.setJ_dae(j_dae);
            index03List = service03.GetJpumList(index03Dto);
            model.addAttribute("index03List",index03List);

        } catch (Exception ex) {
            log.info("App02List_index Exception =====>" + ex.toString());
        }

        return index03List;
    }
    @RequestMapping(value="/index03/save")
    public String index03Save(@RequestPart(value = "key") Map<String, Object> param
            , Model model
            , HttpServletRequest request){

        try {
            param.forEach((key, values) -> {
                switch (key) {
                    case "jkey":
                        index03Dto.setJkey(values.toString());
                        break;
                    case "j_dae":
                        index03Dto.setJ_dae(values.toString());
                        break;
                    case "j_jung":
                        index03Dto.setJ_jung(values.toString());
                        break;
                    case "jgugek":
                        index03Dto.setJgugek(values.toString());
                        break;
                    case "jpum":
                        index03Dto.setJpum(values.toString());
                        break;
                    case "jgugek2":
                        index03Dto.setJgugek2(values.toString());
                        break;
                    case "jpumcode":
                        index03Dto.setJpumcode(values.toString());
                        break;
                    case "jpumname":
                        index03Dto.setJpumname(values.toString());
                        break;
                    case "jsayang":
                        index03Dto.setJsayang(values.toString());
                        break;
                    case "j_net":
                        index03Dto.setJ_net(values.toString());
                        break;
                    case "jchajong":
                        index03Dto.setJchajong(values.toString());
                        break;
                    case "j_sr":
                        index03Dto.setJ_sr(values.toString());
                        break;
                    case "j_ct":
                        index03Dto.setJ_ct(values.toString());
                        break;
                    case "j_cavity":
                        index03Dto.setJ_cavity(values.toString());
                        break;
                    case "jdanwy":
                        index03Dto.setJdanwy(values.toString());
                        break;
                    case "jbigo":
                        index03Dto.setJbigo(values.toString());
                        break;
                    case "w_b_gubn":
                        index03Dto.setW_b_gubn(values.toString());
                        break;
                    case "jchgoga0":
                        index03Dto.setJchgoga0(values.toString());
                        break;
                    case "j_misayong":
                        index03Dto.setJ_misayong(values.toString());
                        break;
                    case "j_sang":
                        index03Dto.setJ_sang(values.toString());
                        break;
                    case "jboxsu1":
                        index03Dto.setJboxsu1(Integer.parseInt(values.toString()));
                        break;
                    case "acode":
                        index03Dto.setCltcd(values.toString());
                        break;
                    case "jgumtype":
                        index03Dto.setJgumtype(values.toString());
                    case "jdoor1":
                        index03Dto.setJdoor1(values.toString());
                    case "jdoor2":
                        index03Dto.setJdoor2(values.toString());
                        break;
                    case "jthick":
                        index03Dto.setJthick(values.toString());
                        break;
                    case "j_yangsan":
                        index03Dto.setJ_yangsan(values.toString());
                        break;
                    case "jolip":
                        index03Dto.setJolip(values.toString());
                        break;
                    default:
                        break;
                }
            });
            HttpSession session = request.getSession();
            UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
            model.addAttribute("userformDto",userformDto);
            String ls_jkeychk = index03Dto.getJkey();
            if (ls_jkeychk == null || ls_jkeychk.equals("")) {
                ls_jkeychk = index03Dto.getJ_dae() + index03Dto.getJ_jung();
                index03Dto.setJdaejung(ls_jkeychk);
                ls_jkeychk = "";
                ls_jkeychk = service03.GetMaxJkey(index03Dto);
                if(ls_jkeychk == null){
                    ls_jkeychk = index03Dto.getJdaejung() + "000001";
                }else{
                    Integer ll_jeky = Integer.parseInt(ls_jkeychk.substring(4,10)) + 1;
                    ls_jkeychk = index03Dto.getJdaejung() + String.format("%06d", ll_jeky);
                }

                index03Dto.setJkey(ls_jkeychk);
            }
            String ls_acode = service03.GetJpumCheck(index03Dto);
            Boolean result = false;
            if (ls_acode == null || ls_acode.equals("")) {
                result = service03.InsertJpum(index03Dto);
                if (!result) {
                    return "error";
                }
            } else {
                result = service03.UpdateJpum(index03Dto);
                if (!result) {
                    return "error";
                }
            }
//            model.addAttribute("userformDto",userformDto);
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
        return "success";
    }

    @RequestMapping(value="/index03/del")
    public String index03Delete(  @RequestParam("ascode") String ascode,
                                  Model model,   HttpServletRequest request){
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);
        index03Dto.setJkey(ascode);
        Boolean result = service03.DeleteJpum(index03Dto);
        if (!result) {
            return "error";
        }
        return "success";
    }


    //간편주문 리스트 01 등록
    @GetMapping(value="/index03/ganlist01")
    public Object App03GanList01_index(@RequestParam("jpbgubn") String jkey,
                                       @RequestParam("flag") String flag,
                                       Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("주문등록");
        CommDto.setMenuUrl("기준정보>주문등록");
        CommDto.setMenuCode("index14");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        try {
            if(jkey == null || jkey.equals("")){
                jkey = "%";
            }
            index03Dto.setJkey(jkey);
            index03List = service03.GetGanListBonsa01(index03Dto);
            model.addAttribute("index03GanList01",index03List);

        } catch (Exception ex) {
            log.info("App03GanList01_index Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return index03List;
    }

    @GetMapping(value="/getPgunList")
    public Object App01PgunList_index(@RequestParam("searchtxt") String searchtxt,
                                          Model model, HttpServletRequest request) throws Exception{
        try {

            if(searchtxt == null || searchtxt.equals("")){
                searchtxt = "%";
            }
            index04Dto.setCom_cls(searchtxt);
            index04ListDto = service04.getPgunList(index04Dto);

            model.addAttribute("pgunList",index04ListDto);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("insalist Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return index04ListDto;
    }

    @GetMapping(value="/getHcodList")
    public Object App01HcodList_index(@RequestParam("searchtxt") String searchtxt,
                                      @RequestParam("com_code") String com_code,
                                      Model model, HttpServletRequest request) throws Exception{
        try {

            if(searchtxt == null || searchtxt.equals("")){
                searchtxt = "%";
            }
            index04Dto.setPgun(searchtxt);
            index04Dto.setCom_code(com_code);
            index04ListDto = service04.getHcodList(index04Dto);

            model.addAttribute("hcodList",index04ListDto);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("insalist Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return index04ListDto;
    }

    @RequestMapping(value="/hcodsave")
    public String App01HcodSave_index(  @RequestParam("hcod") String hcod,
                                        @RequestParam("com_code") String com_code,
                                           @RequestParam("hnam") String hnam,
                                           Model model,   HttpServletRequest request){
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        Boolean result = false;
        index04Dto.setCom_code(com_code);
        index04Dto.setHcod(hcod);
        index04Dto.setHnam(hnam);
        String ls_hcod = service04.GetHcodCheck(index04Dto);
        if(ls_hcod == null || ls_hcod.equals("")){
            result = service04.InsertHcod(index04Dto);
        }else{
            result = service04.UpdateHcod(index04Dto);
        }
        if (!result) {
            return "error";
        }
        return "success";
    }

    @RequestMapping(value="/hcoddel")
    public String App01HcodDel_index(  @RequestParam("hcod") String hcod,
                                       @RequestParam("com_code") String com_code,
                                          @RequestParam("hnam") String hnam,
                                          Model model,   HttpServletRequest request){
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        Boolean result = false;
        index04Dto.setCom_code(com_code);
        index04Dto.setHcod(hcod);
        index04Dto.setHnam(hnam);
        index04ListDto = service04.getHcodList(index04Dto);
        result = service04.DeleteHcod(index04Dto);
        if (!result) {
            return "error";
        }
        return "success";
    }

    @GetMapping(value="/getBgroupList")
    public Object App01BgroupList_index(@RequestParam("searchtxt") String searchtxt,
                                        @RequestParam("hcod") String hcod,
                                        @RequestParam("com_code") String com_code,
                                      Model model, HttpServletRequest request) throws Exception{
        try {

            if(searchtxt == null || searchtxt.equals("")){
                searchtxt = "%";
            }
            index04Dto.setPgun(searchtxt);
            index04Dto.setHcod(hcod);
            index04Dto.setCom_code(com_code);
            index04ListDto = service04.getBgroupList(index04Dto);

            model.addAttribute("bgroupList",index04ListDto);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("insalist Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return index04ListDto;
    }

    @RequestMapping(value="/bgroupsave")
    public String App01BgroupSave_index(  @RequestParam("hcod") String hcod,
                                          @RequestParam("bgroup") String bgroup,
                                          @RequestParam("com_code") String com_code,
                                          @RequestParam("bgrpnm") String bgrpnm,
                                        Model model,   HttpServletRequest request){
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        Boolean result = false;
        index04Dto.setBgroup(bgroup);
        index04Dto.setHcod(hcod);
        index04Dto.setBgrpnm(bgrpnm);
        index04Dto.setCom_code(com_code);
        String ls_bgroup = service04.GetBgroupCheck(index04Dto);
        if(ls_bgroup == null || ls_bgroup.equals("")){
            result = service04.InsertBgroup(index04Dto);
        }else{
            result = service04.UpdateBgroup(index04Dto);
        }
        if (!result) {
            return "error";
        }
        return "success";
    }

    @RequestMapping(value="/bgroupdel")
    public String App01BgroupDel_index(  @RequestParam("hcod") String hcod,
                                       @RequestParam("bgroup") String bgroup,
                                       @RequestParam("bgrpnm") String bgrpnm,
                                       @RequestParam("com_code") String com_code,
                                       Model model,   HttpServletRequest request){
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        Boolean result = false;
        index04Dto.setBgroup(bgroup);
        index04Dto.setHcod(hcod);
        index04Dto.setBgrpnm(bgrpnm);
        index04Dto.setCom_code(com_code);
        index04ListDto = service04.getBgroupList(index04Dto);
        result = service04.DeleteBgroup(index04Dto);
        if (!result) {
            return "error";
        }
        return "success";
    }




    //거래처등록
    @GetMapping(value="/index07/list")
    public Object App07List_index(@RequestParam("searchtxt") String searchtxt,
                                  Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("거래처등록");
        CommDto.setMenuUrl("기준정보>거래처정보");
        CommDto.setMenuCode("index07");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        try {

            if(searchtxt == null || searchtxt.equals("")){
                searchtxt = "%";
            }
            index07Dto.setAcorp(searchtxt);
            index07List = service02.GetCifList(index07Dto);
            model.addAttribute("index07List",index07List);

        } catch (Exception ex) {
            log.info("App07List_index Exception =====>" + ex.toString());
        }

        return index07List;
    }


    //거래처등록
    @GetMapping(value="/index07/listtot")
    public Object App02ListTot_index(@RequestParam("conacorp1") String conacorp1,
                                     @RequestParam("conacorp") String conacorp,
                                     @RequestParam("conagita") String conagita,
                                     @RequestParam("abonsadam1") String abonsadam1,
                                     Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("거래처등록");
        CommDto.setMenuUrl("기준정보>거래처정보");
        CommDto.setMenuCode("index07");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        try {
            if(conacorp1 == null || conacorp1.equals("")){
                conacorp1 = "%";
            }
            if(conacorp == null || conacorp.equals("")){
                conacorp = "%";
            }
            if(conagita == null || conagita.equals("")){
                conagita = "%";
            }
            if(abonsadam1 == null || abonsadam1.equals("")){
                abonsadam1 = "%";
            }
            index07Dto.setAcorp1(conacorp1);
//            log.info("conacorp1 =====>" + conacorp1);
            index07Dto.setAcorp(conacorp);
            index07Dto.setAgita(conagita);
            index07Dto.setAbonsadam1(abonsadam1);
            index07List = service02.GetCifListTot(index07Dto);
            model.addAttribute("index07List",index07List);

        } catch (Exception ex) {
            log.info("App07ListTot_index Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return index07List;
    }


    @RequestMapping(value="/index07/save")
    public String index07Save(@RequestPart(value = "key") Map<String, Object> param
            , Model model
            , HttpServletRequest request){

        try {
            param.forEach((key, values) -> {
                switch (key) {
                    case "acorp1":
                        index07Dto.setAcorp1(values.toString());
                        break;
                    case "acorp2":
                        index07Dto.setAcorp2(values.toString());
                        break;
                    case "acorp3":
                        index07Dto.setAcorp3(values.toString());
                        break;
                    case "acode":
                        index07Dto.setAcode(values.toString());
                        log.info("setAcode");
                        log.info(values.toString());
                        break;
                    case "acorp":
                        index07Dto.setAcorp(values.toString());
                        break;
                    case "asano1":
                        index07Dto.setAsano1(values.toString());
                        break;
                    case "asano2":
                        index07Dto.setAsano2(values.toString());
                        break;
                    case "asano3":
                        index07Dto.setAsano3(values.toString());
                        break;
                    case "aname":
                        index07Dto.setAname(values.toString());
                        break;
                    case "aupte":
                        index07Dto.setAupte(values.toString());
                        break;
                    case "ajong":
                        index07Dto.setAjong(values.toString());
                        break;
                    case "apost1":
                        index07Dto.setApost1(values.toString());
                        break;
                    case "ajuso1":
                        index07Dto.setAjuso1(values.toString());
                        break;
                    case "ajuso2":
                        index07Dto.setAjuso2(values.toString());
                        break;
                    case "abigo":
                        index07Dto.setAbigo(values.toString());
                        break;
                    case "agita":
                        index07Dto.setAgita(values.toString());
                        break;
                    case "ajumi1":
                        index07Dto.setAjumi1(values.toString());
                        break;
                    case "ajumi2":
                        index07Dto.setAjumi2(values.toString());
                        break;
                    case "aascode1":
                        index07Dto.setAascode1(values.toString());
                        break;
                    case "aascode2":
                        index07Dto.setAascode2(values.toString());
                        break;
                    case "atelno":
                        index07Dto.setAtelno(values.toString());
                        break;
                    case "atelno2":
                        index07Dto.setAtelno2(values.toString());
                        break;
                    case "aemail":
                        index07Dto.setAemail(values.toString());
                        break;
                    case "ahand":
                        index07Dto.setAhand(values.toString());
                        break;
                    case "abonsadam1":
                        index07Dto.setAbonsadam1(values.toString());
                        break;
                    case "abonsadam2":
                        index07Dto.setAbonsadam2(values.toString());
                        break;
                    case "abonsadam3":
                        index07Dto.setAbonsadam3(values.toString());
                        break;
                    case "adomain":
                        index07Dto.setAdomain(values.toString());
                        break;
                    case "afax":
                        index07Dto.setAfax(values.toString());
                        break;
                    case "abuse1":
                        index07Dto.setAbuse1(values.toString());
                        break;
                    case "adam1":
                        index07Dto.setAdam1(values.toString());
                        break;
                    default:
                        break;
                }
            });
            HttpSession session = request.getSession();
            UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
            model.addAttribute("userformDto",userformDto);

            String ls_acode = index07Dto.getAcode();
            String ls_acorp2 = "";
            Boolean result = false;
            if (ls_acode == null || ls_acode.equals("")) {
                Integer ll_acorp2 = Integer.parseInt(service02.getIndex02MaxSeq(index07Dto.getAcorp1())) + 1;
                ls_acorp2 = ll_acorp2.toString();
                index07Dto.setAcorp2(ls_acorp2);
                result = service02.InsertCif(index07Dto);
                if (!result) {
                    return "error";
                }
            } else {
                result = service02.UpdateCif(index07Dto);
                if (!result) {
                    return "error";
                }
            }
//            model.addAttribute("userformDto",userformDto);
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
        return "success";
    }

    @RequestMapping(value="/index07/del")
    public String index07Delete(  @RequestParam("ascode") String ascode,
                                  Model model,   HttpServletRequest request){
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);
        index07Dto.setAcode(ascode);
        Boolean result = service02.DeleteCif(index07Dto);
        if (!result) {
            return "error";
        }
        return "success";
    }


    @RequestMapping(value="/index08/save")
    public String index08Save( @RequestPart(value = "key") Map<String, Object> param
                               ,@RequestPart(value = "file",required = false ) List<MultipartFile> file
            , Model model
            , HttpServletRequest request){
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);


        String ls_fileName = "";
        String ls_errmsg = "";
        /* 업로드 파일 정보를 담을 비어있는 리스트 */
        List<AttachDTO> attachList = new ArrayList<>();
        App05ElvlrtDto _App05Dto = new App05ElvlrtDto();


        Pc110Dto _index08Dto = new Pc110Dto();
        param.forEach((key, values) -> {
            switch (key) {
                case "inmachcd":
                    _index08Dto.setMachcd(values.toString());
                    break;
                case "inmachno":
                    _index08Dto.setMachno(values.toString());
                    break;
                case "inmachname":
                    _index08Dto.setMachname(values.toString());
                    break;
                case "inmachdate":
                    _index08Dto.setMachdate(values.toString());
                    break;
                case "inmachstdate":
                    _index08Dto.setMachstdate(values.toString());
                    break;
                case "inmacheddate":
                    _index08Dto.setMacheddate(values.toString());
                    break;
                case "inmachshot":
                    _index08Dto.setMachshot(Integer.parseInt(values.toString()));
                    break;
                case "inmachgugek01":
                    _index08Dto.setMachgugek01(Integer.parseInt(values.toString()));
                    break;
                case "inmachgugek02":
                    _index08Dto.setMachgugek02(Integer.parseInt(values.toString()));
                    break;
                case "inmachgugek03":
                    _index08Dto.setMachgugek03(Integer.parseInt(values.toString()));
                    break;
                case "inmachcavity":
                    _index08Dto.setMachcavity(values.toString());
                    break;
                case "inmachuse":
                    _index08Dto.setMachuse(values.toString());
                    break;
                case "inmachcha":
                    _index08Dto.setMachcha(values.toString());
                    break;
                case "inmachcltnm":
                    _index08Dto.setMachcltnm(values.toString());
                    break;
                case "inmachjaejak":
                    _index08Dto.setMachjaejak(values.toString());
                    break;
                case "inmachplace":
                    _index08Dto.setMachplace(values.toString());
                    break;
                case "inmachwon":
                    _index08Dto.setMachwon(values.toString());
                    break;
                case "inmachgrade":
                    _index08Dto.setMachgrade(values.toString());
                    break;
                case "inmachgram":
                    _index08Dto.setMachgram(Integer.parseInt(values.toString()));
                    break;
                case "inmachncha":
                    _index08Dto.setMachncha(values.toString());
                    break;
                case "inpcode":
                    _index08Dto.setInpcode(values.toString());
                    break;
                case "injpum":
                    _index08Dto.setInjpum(values.toString());
                    break;
                case "inmachgumnm":
                    _index08Dto.setMachgumnm(values.toString());
                    break;
                default:
                    break;
            }
        });



        _index08Dto.setIndate(getDate());
        //_index08Dto.setInperid(userformDto.getUserid());

        String ls_chkmachnm = service08.getDupleMachchk(_index08Dto);
        Boolean result = false;
        if (ls_chkmachnm != null && !ls_chkmachnm.equals("")) {
            log.info("ls_getMachcd =====>" + _index08Dto.getMachcd());
            log.info("ls_chkmachnm =====>" + ls_chkmachnm);
            result = service08.UpdateMach(_index08Dto);
            if (!result) {
                return "error";
            }
        }else{
            result = service08.InsertMach(_index08Dto);
            if (!result) {
                return "error";
            }
        }

//        C:\Project\aprjKDMES\src\main\resources\static\assets
//        C:\Project\aprjKDMES\src\main\java\com\dae\kdmes\controller\app01
        //D:\workspace\KDMES\src\main\resources\static\assets
        String _uploadPath = Paths.get("D:", "workspace", "KDMES","src","main","resources","static","assets","upload", _index08Dto.getMachcd()).toString();
        /* uploadPath에 해당하는 디렉터리가 존재하지 않으면, 부모 디렉터리를 포함한 모든 디렉터리를 생성 */
        File dir = new File(_uploadPath);
        if (dir.exists() == false) {
            dir.mkdirs();
        }

        try {

            for(MultipartFile multipartFile : file){
//                log.info("================================================================");
//                log.info("upload file name : " + multipartFile.getOriginalFilename());
//                log.info("upload file name : " + multipartFile.getSize());
                ls_fileName = multipartFile.getOriginalFilename();


                /* 파일이 비어있으면 비어있는 리스트 반환 */
                if (multipartFile.getSize() < 1) {
                    ls_errmsg = "success";
                    return ls_errmsg;
                }
                /* 파일 확장자 */
                final String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                /* 서버에 저장할 파일명 (랜덤 문자열 + 확장자) */
                final String saveName = getRandomString() + "." + extension;

                /* 업로드 경로에 saveName과 동일한 이름을 가진 파일 생성 */
                File target = new File(_uploadPath, saveName);

                log.info("uploadPath : " + _uploadPath);
                log.info("saveName : " + saveName);

                multipartFile.transferTo(target);
                String nseq1 = _index08Dto.getMachcd();
                /* 파일 정보 저장 */
                AttachDTO attach = new AttachDTO();
                attach.setBoardIdx(nseq1);
                attach.setOriginalName(multipartFile.getOriginalFilename());
                attach.setSaveName(saveName);
                attach.setSize(multipartFile.getSize());
                attach.setFlag("NN");
                /* 파일 정보 추가 */
                attachList.add(attach);
            }

            result  = appServiceImpl.registerMNotice(_App05Dto, attachList);
            if(!result){
                return  "error";
            }
        }catch (DataAccessException e){
            log.info("memberUpload DataAccessException ================================================================");
            log.info(e.toString());
            throw new AttachFileException("[" + ls_fileName + "] DataAccessException to save");
            //utils.showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다", "/App05/App05list/", Method.GET, model);
        } catch (Exception  e){
            /*log.info("memberUpload Exception ================================================================");
            log.info(e.toString());
            ls_errmsg = "[" + ls_fileName + "] failed to save";
            throw new AttachFileException("[" + ls_fileName + "] failed to save");*/
            //utils.showMessageWithRedirect("시스템에 문제가 발생하였습니다", "/app05/App05list/", Method.GET, model);
        }





        return "success";
    }

    @RequestMapping(value="/index08/del")
    public String index08Delete(  @RequestParam("inmachcd") String inmachcd,
                                  Model model,   HttpServletRequest request){
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);
        Pc110Dto _index08Dto = new Pc110Dto();
        _index08Dto.setMachcd(inmachcd);
        Boolean result = service08.DeleteMach(_index08Dto);
        if (!result) {
            return "error";
        }
        result = service08.DeleteGumALLIMG(_index08Dto);
        result = service08.DeleteMachFixAll(_index08Dto);

        return "success";
    }


    @RequestMapping(value="/index22/del")
    public String index22Delete(  @RequestParam("infaccd") String infaccd,
                                  Model model,   HttpServletRequest request){
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);
        Pc120Dto _index120Dto = new Pc120Dto();
        Pc110Dto _index08Dto = new Pc110Dto();
        _index120Dto.setFaccd(infaccd);
        Boolean result = service08.DeleteFac(_index120Dto);
        if (!result) {
            return "error";
        }
        _index08Dto.setMachcd(infaccd);
        result = service08.DeleteGumALLIMG(_index08Dto);
        //result = service08.DeleteMachFixAll(_index08Dto);

        return "success";
    }

    @RequestMapping(value="/index22/save")
    public String index22Save( @RequestPart(value = "key") Map<String, Object> param
            ,@RequestPart(value = "file",required = false ) List<MultipartFile> file
            , Model model
            , HttpServletRequest request){
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);


        String ls_fileName = "";
        String ls_errmsg = "";
        /* 업로드 파일 정보를 담을 비어있는 리스트 */
        List<AttachDTO> attachList = new ArrayList<>();
        App05ElvlrtDto _App05Dto = new App05ElvlrtDto();


        Pc120Dto _index08Dto = new Pc120Dto();
        param.forEach((key, values) -> {
            switch (key) {
                case "infaccd":
                    _index08Dto.setFaccd(values.toString());
                    break;
                case "infacflag":
                    _index08Dto.setFacflag(values.toString());
                    break;
                case "infacno":
                    _index08Dto.setFacno(values.toString());
                    break;
                case "infacname":
                    _index08Dto.setFacname(values.toString());
                    break;
                case "infacdate":
                    _index08Dto.setFacdate(values.toString());
                    break;
                case "infacstdate":
                    _index08Dto.setFacstdate(values.toString());
                    break;
                case "infaceddate":
                    _index08Dto.setFaceddate(values.toString());
                    break;
                case "infacmodel":
                    _index08Dto.setFacmodel(values.toString());
                    break;
                case "infacjaejo":
                    _index08Dto.setFacjaejo(values.toString());
                    break;
                case "infacjaejono":
                    _index08Dto.setFacjaejono(values.toString());
                    break;
                case "infacbycltnm":
                    _index08Dto.setFacbycltnm(values.toString());
                    break;
                case "infacbypernm":
                    _index08Dto.setFacbypernm(values.toString());
                    break;
                case "infactelno":
                    _index08Dto.setFactelno(values.toString());
                    break;
                case "infacgubun":
                    _index08Dto.setFacgubun(values.toString());
                    break;
                case "infacamt":
                    _index08Dto.setFacamt(Integer.parseInt(values.toString()));
                    break;
                case "infacjunwon":
                    _index08Dto.setFacjunwon(values.toString());
                    break;
                case "infacdivnm":
                    _index08Dto.setFacdivnm(values.toString());
                    break;
                case "infacgita":
                    _index08Dto.setFacgita(values.toString());
                    break;
                case "infacsize":
                    _index08Dto.setFacsize(values.toString());
                    break;
                case "infacweight":
                    _index08Dto.setFacweight(values.toString());
                    break;
                case "infachyung":
                    _index08Dto.setFachyung(values.toString());
                    break;
                case "infacscrew":
                    _index08Dto.setFacscrew(values.toString());
                    break;
                case "infactype":
                    _index08Dto.setFactype(values.toString());
                    break;
                case "infacvolumn":
                    _index08Dto.setFacvolumn(values.toString());
                    break;
                case "infacgunondo":
                    _index08Dto.setFacgunondo(values.toString());
                    break;
                case "infacgunpung":
                    _index08Dto.setFacgunpung(values.toString());
                    break;
                case "infacpower":
                    _index08Dto.setFacpower(values.toString());
                    break;
                default:
                    break;
            }
        });


        String ls_maxfaccd = _index08Dto.getFaccd();
        String ls_seq;
        Boolean result = false;
        if (ls_maxfaccd == null || ls_maxfaccd.equals("")) {
            _index08Dto.setYymm(getDate().substring(0,6));
            log.info(_index08Dto.getYymm());
            ls_maxfaccd = service08.SelectMaxFac(_index08Dto);
            if (ls_maxfaccd != null && !ls_maxfaccd.equals("")) {
                ls_seq = ls_maxfaccd.substring(6, 8);
                int ll_seq = Integer.parseInt(ls_seq) + 1;
                ls_seq = Integer.toString(ll_seq);

                if (ls_seq.length() == 1) {
                    ls_seq = "000" + ls_seq;
                } else if (ls_seq.length() == 2) {
                    ls_seq = "00" + ls_seq;
                } else if (ls_seq.length() == 3) {
                    ls_seq = "0" + ls_seq;
                }
                ls_maxfaccd = getDate().substring(0,5) + ls_seq;
            }else{
                ls_maxfaccd = getDate().substring(0,6) + "001";
            }
            _index08Dto.setFaccd(ls_maxfaccd);
            result = service08.InsertFac(_index08Dto);
            if (!result) {
                return "error";
            }
        } else {
            result = service08.UpdateFac(_index08Dto);
            if (!result) {
                return "error";
            }
        }
//        C:\Project\aprjKDMES\src\main\resources\static\assets
//        C:\Project\aprjKDMES\src\main\java\com\dae\kdmes\controller\app01
        //D:\workspace\KDMES\src\main\resources\static\assets
        String _uploadPath = Paths.get("D:", "workspace", "KDMES","src","main","resources","static","assets","upload", _index08Dto.getFaccd()).toString();
        /* uploadPath에 해당하는 디렉터리가 존재하지 않으면, 부모 디렉터리를 포함한 모든 디렉터리를 생성 */
        File dir = new File(_uploadPath);
        if (dir.exists() == false) {
            dir.mkdirs();
        }

        try {

            for(MultipartFile multipartFile : file){
//                log.info("================================================================");
//                log.info("upload file name : " + multipartFile.getOriginalFilename());
//                log.info("upload file name : " + multipartFile.getSize());
                ls_fileName = multipartFile.getOriginalFilename();


                /* 파일이 비어있으면 비어있는 리스트 반환 */
                if (multipartFile.getSize() < 1) {
                    ls_errmsg = "success";
                    return ls_errmsg;
                }
                /* 파일 확장자 */
                final String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                /* 서버에 저장할 파일명 (랜덤 문자열 + 확장자) */
                final String saveName = getRandomString() + "." + extension;

                /* 업로드 경로에 saveName과 동일한 이름을 가진 파일 생성 */
                File target = new File(_uploadPath, saveName);

                log.info("uploadPath : " + _uploadPath);
                log.info("saveName : " + saveName);

                multipartFile.transferTo(target);
                String nseq1 = _index08Dto.getFaccd();
                /* 파일 정보 저장 */
                AttachDTO attach = new AttachDTO();
                attach.setBoardIdx(nseq1);
                attach.setOriginalName(multipartFile.getOriginalFilename());
                attach.setSaveName(saveName);
                attach.setSize(multipartFile.getSize());
                attach.setFlag("FF");
                /* 파일 정보 추가 */
                attachList.add(attach);
            }

            result  = appServiceImpl.registerMNotice(_App05Dto, attachList);
            if(!result){
                return  "error";
            }
        }catch (DataAccessException e){
            log.info("memberUpload DataAccessException ================================================================");
            log.info(e.toString());
            throw new AttachFileException("[" + ls_fileName + "] DataAccessException to save");
            //utils.showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다", "/App05/App05list/", Method.GET, model);
        } catch (Exception  e){
            /*log.info("memberUpload Exception ================================================================");
            log.info(e.toString());
            ls_errmsg = "[" + ls_fileName + "] failed to save";
            throw new AttachFileException("[" + ls_fileName + "] failed to save");*/
            //utils.showMessageWithRedirect("시스템에 문제가 발생하였습니다", "/app05/App05list/", Method.GET, model);
        }
        return "success";
    }




    //거래처등록
    @GetMapping(value="/index08/listtot")
    public Object App082ListTot_index(@RequestParam("searchtxt") String inmachcd,
                                     Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("금형등록");
        CommDto.setMenuUrl("기준정보>금형등록");
        CommDto.setMenuCode("index08");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        List<Pc110Dto> _index08ListDto = new ArrayList<>();
        Pc110Dto _index08Dto = new Pc110Dto();
        try {
            if(inmachcd == null || inmachcd.equals("")){
                inmachcd = "%";
            }
            _index08Dto.setMachcd(inmachcd);
            _index08ListDto = service08.getMachList(_index08Dto);
            model.addAttribute("index08List",_index08ListDto);

        } catch (Exception ex) {
            log.info("App08ListTot_index Exception =====>" + ex.toString());
        }

        return _index08ListDto;
    }

    //거래처등록
    @GetMapping(value="/index22/listtot")
    public Object App022ListTot_index(@RequestParam("searchtxt") String inFaccd,
                                      Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("설비등록");
        CommDto.setMenuUrl("기준정보>설비등록");
        CommDto.setMenuCode("index22");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        List<Pc120Dto> _index08ListDto = new ArrayList<>();
        Pc120Dto _index08Dto = new Pc120Dto();
        try {
            if(inFaccd == null || inFaccd.equals("")){
                inFaccd = "%";
            }
            _index08Dto.setFaccd(inFaccd);
            _index08ListDto = service08.getFacList(_index08Dto);
            model.addAttribute("index08List",_index08ListDto);

        } catch (Exception ex) {
            log.info("App022ListTot_index Exception =====>" + ex.toString());
        }

        return _index08ListDto;
    }


    @RequestMapping(value="/index08/fixsave")
    public String index08FixSave( @RequestPart(value = "key") Map<String, Object> param
            , Model model
            , HttpServletRequest request){
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        PcFixDto _index08FixDto = new PcFixDto();
        param.forEach((key, values) -> {
            switch (key) {
                case "infixid":
                    _index08FixDto.setFixid(Integer.parseInt(values.toString()));
                    break;
                case "infixmachcd":
                    _index08FixDto.setFixmachcd(values.toString());
                    break;
                case "inreqtext":
                    _index08FixDto.setReqtext(values.toString());
                    break;
                case "infixtext":
                    _index08FixDto.setFixtext(values.toString());
                    break;
                case "infixcltnm":
                    _index08FixDto.setFixcltnm(values.toString());
                    break;
                case "inremark":
                    _index08FixDto.setRemark(values.toString());
                    break;
                case "inreqdate":
                    _index08FixDto.setReqdate(values.toString());
                    break;
                case "inrespdate":
                    _index08FixDto.setRespdate(values.toString());
                    break;
                case "infixflag":
                    _index08FixDto.setFixflag(values.toString());
                    break;
                default:
                    break;
            }
        });

        _index08FixDto.setIndate(getDate());
        //_index08FixDto.setInperid(userformDto.getUserid());

        Boolean result = false;
        if (_index08FixDto.getFixid() > 0) {
            result = service08.UpdateMachFix(_index08FixDto);
            if (!result) {
                return "error";
            }
        }else{
            result = service08.InsertMachFix(_index08FixDto);
            if (!result) {
                return "error";
            }
        }

        return "success";
    }


    @RequestMapping(value="/index08/delfix")
    public String index08FixDelete(  @RequestParam("infixid") String infixid,
                                  Model model,   HttpServletRequest request){
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);
        PcFixDto _index08FixDto = new PcFixDto();
        _index08FixDto.setFixid(Integer.parseInt(infixid));
        Boolean result = service08.DeleteMachFix(_index08FixDto);
        if (!result) {
            return "error";
        }
        return "success";
    }


    //거래처등록
    @GetMapping(value="/index08/listtotfix")
    public Object App082ListTotFix_index(@RequestParam("searchtxt") String inmachcd,
                                      Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("금형수리등록");
        CommDto.setMenuUrl("기준정보>금형수리등록");
        CommDto.setMenuCode("index08");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        List<PcFixDto> _index08FixListDto = new ArrayList<>();
        PcFixDto _index08FixDto = new PcFixDto();
        try {
            if(inmachcd == null || inmachcd.equals("")){
                inmachcd = "%";
            }
            _index08FixDto.setFixmachcd(inmachcd);
            _index08FixListDto = service08.getMachFixList(_index08FixDto);
            model.addAttribute("index08List",_index08FixListDto);

        } catch (Exception ex) {
            log.info("App08ListTot_index Exception =====>" + ex.toString());
        }

        return _index08FixListDto;
    }


    //거래처등록
    @GetMapping(value="/index08/printview")
    public Object AppPrintView_index(@RequestParam("inmachcd") String inmachcd,
                                     @RequestParam("inmachno") String inmachno,
                                         Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("금형이력카드");
        CommDto.setMenuUrl("기준정보>금형이력카드");
        CommDto.setMenuCode("index08");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        Pc110Dto _index08Dto = new Pc110Dto();
        List<Pc110Dto> _Pc110DtoListDto = new ArrayList<>();
        try {
            if(inmachcd == null || inmachcd.equals("")){
                inmachcd = "%";
            }
            _index08Dto.setMachcd(inmachcd);
            _Pc110DtoListDto = service08.getMachList(_index08Dto);
            model.addAttribute("index08List",_Pc110DtoListDto);

        } catch (Exception ex) {
            log.info("AppPrintView_index Exception =====>" + ex.toString());
        }

        return _Pc110DtoListDto;
    }



    //거래처등록
    @GetMapping(value="/index22/printview")
    public Object AppPrintView22_index(@RequestParam("infacname") String infacname,
                                     Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("설비이력카드");
        CommDto.setMenuUrl("기준정보>설비이력카드");
        CommDto.setMenuCode("index22");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        Pc120Dto _index08Dto = new Pc120Dto();
        List<Pc120Dto> _Pc120DtoListDto = new ArrayList<>();
        try {
            if(infacname == null || infacname.equals("")){
                infacname = "%";
            }
            _index08Dto.setFacname(infacname);
            _Pc120DtoListDto = service08.getFacList(_index08Dto);
            model.addAttribute("index08List",_Pc120DtoListDto);

        } catch (Exception ex) {
            log.info("AppPrintView22_index Exception =====>" + ex.toString());
        }

        return _Pc120DtoListDto;
    }



    //이미지불러오기
    @GetMapping(value="/index08/printimg")
    public Object AppPrintJPG_index(@RequestParam("inmachcd") String inmachcd,
                                     Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("금형이력카드");
        CommDto.setMenuUrl("기준정보>금형이력카드");
        CommDto.setMenuCode("index08");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);
        String uploadPath = "D:/workspace/KDMES/src/main/resources/static/assets/upload/";
        Pc110Dto _index08Dto = new Pc110Dto();
        List<Pc110Dto> _Pc110DtoListDto = new ArrayList<>();
        try {
            _index08Dto.setMachcd(inmachcd);
            _Pc110DtoListDto = service08.getPringImg(_index08Dto);
            int i = 0;
            String ls_filename = "";
            if(_Pc110DtoListDto.size() > 0){
                for(int h =0; h < _Pc110DtoListDto.size(); h++){
                    // 첫 번째 객체의 file_url 설정
                    Pc110Dto dto1 = _Pc110DtoListDto.get(h);
                    ls_filename = dto1.getSave_name();
//                    log.info("size =====>" + _Pc110DtoListDto.size());
//                    log.info("inmachcd =====>" + inmachcd);
//                    log.info("ls_filenamen =====>" + ls_filename);
                    dto1.setFile_url(uploadPath + inmachcd + "/" + ls_filename); // 첫 번째 file_url 값 설정
                    _Pc110DtoListDto.set(h, dto1);
 ;
                }
            }
            model.addAttribute("index08List",_Pc110DtoListDto);
        } catch (Exception ex) {
            log.info("AppPrintJPG_index Exception =====>" + ex.toString());
        }

        return _Pc110DtoListDto;
    }



    //이미지불러오기
    @GetMapping(value="/index08/printimgdel")
    public Object AppPrintJPGDel_index(@RequestParam("savename") String savename,
                                    Model model, HttpServletRequest request) throws Exception{
        CommDto.setMenuTitle("금형이력카드");
        CommDto.setMenuUrl("기준정보>금형이력카드");
        CommDto.setMenuCode("index08");
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        model.addAttribute("userformDto",userformDto);

        Pc110Dto _index08Dto = new Pc110Dto();
        List<Pc110Dto> _Pc110DtoListDto = new ArrayList<>();
        try {
            _index08Dto.setSave_name(savename);
            Boolean result = service08.DeleteGumIMG(_index08Dto);
            if (!result) {
                return "error";
            }
        } catch (Exception ex) {
            log.info("AppPrintView_index Exception =====>" + ex.toString());
        }

        return "success";
    }


    /**
     * 서버에 생성할 파일명을 처리할 랜덤 문자열 반환
     * @return 랜덤 문자열
     */
    private final String getRandomString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Calendar cal1 = Calendar.getInstance();
        Date date      = new Date(cal1.getTimeInMillis());

        return formatter.format(date);
    }

}
