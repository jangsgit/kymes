package com.dae.kdmes.controller.app03;

import com.dae.kdmes.DTO.App01.IndexCa613Dto;
import com.dae.kdmes.DTO.App02.Index11Dto;
import com.dae.kdmes.DTO.Appm.FPLAN_VO;
import com.dae.kdmes.DTO.app03.Index59Dto;
import com.dae.kdmes.Service.App02.Index11Service;
import com.dae.kdmes.Service.App03.Index35Service;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hpsf.Decimal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

// @RestController : return을 텍스트로 반환함.
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/app03m", method = RequestMethod.POST)
public class App03ListController {


    private final Index11Service service11;
    private final Index35Service service35;
    Index11Dto index11Dto = new Index11Dto();
    FPLAN_VO fplanDto = new FPLAN_VO();
    List<Index11Dto> index11DtoList = new ArrayList<>();
    List<FPLAN_VO> fplanDtoList = new ArrayList<>();

    protected Log log =  LogFactory.getLog(this.getClass());



    //생산계획현황
    @GetMapping(value="/index11/list")
    public Object getList(@RequestParam("frdate") String frdate,
                          @RequestParam("todate") String todate,
                          @RequestParam("pcode") String pcode,
                          @RequestParam("acode") String acode,
                          @RequestParam("perid") String perid,
                               Model model, HttpServletRequest request) throws Exception{
        try {

//            String ls_yeare = frdate.substring(0,4);
//            String ls_mm = frdate.substring(5,7);
//            String ls_dd = frdate.substring(8,10);
//            frdate =  ls_yeare + ls_mm + ls_dd;
//                 ls_yeare = todate.substring(0,4);
//                 ls_mm = todate.substring(5,7);
//                 ls_dd = todate.substring(8,10);
//            todate =  ls_yeare + ls_mm + ls_dd;

            index11Dto.setFrdate(frdate);
            index11Dto.setTodate(todate);
            index11Dto.setCltcd(acode);
            index11Dto.setPcode(pcode);
            index11Dto.setWrps(perid);
            index11DtoList = service11.getIndex11List(index11Dto);

            model.addAttribute("itemList",index11DtoList);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("index11/list Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return index11DtoList;
    }



    //사출공정현황
    @GetMapping(value="/index12/list01")
    public Object App12List01_index(@RequestParam("frdate") String frdate,
                          @RequestParam("todate") String todate,
                          @RequestParam("lotno") String lotno,
                          Model model, HttpServletRequest request) throws Exception{
        try {

            String ls_yeare = frdate.substring(0,4);
            String ls_mm = frdate.substring(5,7);
            String ls_dd = frdate.substring(8,10);
            frdate =  ls_yeare + ls_mm + ls_dd;
            ls_yeare = todate.substring(0,4);
            ls_mm = todate.substring(5,7);
            ls_dd = todate.substring(8,10);
            todate =  ls_yeare + ls_mm + ls_dd;

            index11Dto.setFrdate(frdate);
            index11Dto.setTodate(todate);
            index11Dto.setLotno(lotno);

            index11DtoList = service11.getIndex12List01(index11Dto);

            model.addAttribute("itemList",index11DtoList);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("index11/list Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return index11DtoList;
    }


    //검사공정현황
    @GetMapping(value="/index12/list02")
    public Object App12List02_index(@RequestParam("frdate") String frdate,
                                    @RequestParam("todate") String todate,
                                    @RequestParam("lotno") String lotno,
                                    Model model, HttpServletRequest request) throws Exception{
        try {

            String ls_yeare = frdate.substring(0,4);
            String ls_mm = frdate.substring(5,7);
            String ls_dd = frdate.substring(8,10);
            frdate =  ls_yeare + ls_mm + ls_dd;
            ls_yeare = todate.substring(0,4);
            ls_mm = todate.substring(5,7);
            ls_dd = todate.substring(8,10);
            todate =  ls_yeare + ls_mm + ls_dd;

            fplanDto.setFrdate(frdate);
            fplanDto.setTodate(todate);
            fplanDto.setLotno(lotno);

            fplanDtoList = service11.getIndex12List02(fplanDto);

            model.addAttribute("itemList",fplanDtoList);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("index11/list Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return fplanDtoList;
    }


    //  사출현황
    @GetMapping(value="/index13/list01")
    public Object App13List01_index(@RequestParam("frdate") String frdate,
                                    @RequestParam("todate") String todate,
                                    @RequestParam("inwrps") String inwrps,
                                    @RequestParam("lotno") String lotno,
                                    @RequestParam("inwrmc") String inwrmc,
                                    Model model, HttpServletRequest request) throws Exception{
        List<Index11Dto> _index11DtoList = new ArrayList<>();
        try {

            Index11Dto _index11Dto = new Index11Dto();

            _index11Dto.setFrdate(frdate);
            _index11Dto.setTodate(todate);
            _index11Dto.setWrps(inwrps);
            _index11Dto.setLotno(lotno);
            _index11Dto.setWrmcnm(inwrmc);
//            log.info("frdate =====>" + frdate);
//            log.info("todate =====>" + todate);
//            log.info("inwrps =====>" + inwrps);
//            log.info("lotno =====>" + lotno);
            _index11DtoList = service11.getIndex13List01(_index11Dto);

            model.addAttribute("itemList", _index11DtoList);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App13List01 Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return _index11DtoList;
    }


    //  사출가동현황
    @GetMapping(value="/index13/list03")
    public Object App13List03_index(@RequestParam("frdate") String frdate,
                                    @RequestParam("todate") String todate,
                                    @RequestParam("lotno") String lotno,
                                    Model model, HttpServletRequest request) throws Exception{
        List<Index11Dto> _index11DtoList = new ArrayList<>();
        try {

            Index11Dto _index11Dto = new Index11Dto();

            _index11Dto.setFrdate(frdate);
            _index11Dto.setTodate(todate);
            _index11Dto.setLotno(lotno);
            _index11DtoList = service11.getIndex13List03(_index11Dto);

            model.addAttribute("itemList", _index11DtoList);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App13List01 Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return _index11DtoList;
    }

    //사원별 사출현황
    @GetMapping(value="/index13/list02")
    public Object App13List02_index(@RequestParam("frdate") String frdate,
                                    @RequestParam("todate") String todate,
                                    @RequestParam("inwrps") String inwrps,
                                    Model model, HttpServletRequest request) throws Exception{
        try {

            String ls_yeare = frdate.substring(0,4);
            String ls_mm = frdate.substring(5,7);
            String ls_dd = frdate.substring(8,10);
            frdate =  ls_yeare + ls_mm + ls_dd;
            ls_yeare = todate.substring(0,4);
            ls_mm = todate.substring(5,7);
            ls_dd = todate.substring(8,10);
            todate =  ls_yeare + ls_mm + ls_dd;

            fplanDto.setFrdate(frdate);
            fplanDto.setTodate(todate);
            fplanDto.setWrps(inwrps);

            fplanDtoList = service11.getIndex13List02(fplanDto);

            model.addAttribute("itemList",fplanDtoList);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App13List01 Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return fplanDtoList;
    }


    //일별 불량별 사출현황
    @GetMapping(value="/index14/list01")
    public Object App14List01_index(@RequestParam("frdate") String frdate,
                                    @RequestParam("todate") String todate,
                                    @RequestParam("wcode") String wcode,
                                    @RequestParam("lotno") String lotno,
                                    @RequestParam("pcode") String pcode,
                                    Model model, HttpServletRequest request) throws Exception{
        try {

//            String ls_yeare = frdate.substring(0,4);
//            String ls_mm = frdate.substring(5,7);
//            String ls_dd = frdate.substring(8,10);
//            frdate =  ls_yeare + ls_mm + ls_dd;
//            ls_yeare = todate.substring(0,4);
//            ls_mm = todate.substring(5,7);
//            ls_dd = todate.substring(8,10);
//            todate =  ls_yeare + ls_mm + ls_dd;

            if(pcode == null || pcode.equals("")){
                pcode = "%";
            }
            index11Dto.setFrdate(frdate);
            index11Dto.setTodate(todate);
            index11Dto.setWcode(wcode);
            index11Dto.setLotno(lotno);
            index11Dto.setPcode(pcode);
            index11DtoList = service11.getIndex14List01(index11Dto);

            model.addAttribute("itemList",index11DtoList);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App14List01_index Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return index11DtoList;
    }


    //월별 불량별 사출현황
    @GetMapping(value="/index14/list02")
    public Object App14List02_index(@RequestParam("frdate") String frdate,
                                    @RequestParam("todate") String todate,
                                    @RequestParam("wcode") String wcode,
                                    @RequestParam("lotno") String lotno,
                                    @RequestParam("pcode") String pcode,
                                    Model model, HttpServletRequest request) throws Exception{
        try {

//            String ls_yeare = frdate.substring(0,4);
//            String ls_mm = frdate.substring(5,7);
//            String ls_dd = frdate.substring(8,10);
//            frdate =  ls_yeare + ls_mm + ls_dd;
//            ls_yeare = todate.substring(0,4);
//            ls_mm = todate.substring(5,7);
//            ls_dd = todate.substring(8,10);
//            todate =  ls_yeare + ls_mm + ls_dd;
            if(pcode == null || pcode.equals("")){
                pcode = "%";
            }
            index11Dto.setFrdate(frdate);
            index11Dto.setTodate(todate);
            index11Dto.setWcode(wcode);
            index11Dto.setLotno(lotno);
            index11Dto.setPcode(pcode);

            index11DtoList = service11.getIndex14List02(index11Dto);

            model.addAttribute("itemList",index11DtoList);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App14List02_index Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return index11DtoList;
    }


    //유형별 불량별 사출현황
    @GetMapping(value="/index14/list03")
    public Object App14List03_index(@RequestParam("frdate") String frdate,
                                    @RequestParam("todate") String todate,
                                    @RequestParam("wcode") String wcode,
                                    @RequestParam("lotno") String lotno,
                                    @RequestParam("pcode") String pcode,
                                    Model model, HttpServletRequest request) throws Exception{
        try {

//            String ls_yeare = frdate.substring(0,4);
//            String ls_mm = frdate.substring(5,7);
//            String ls_dd = frdate.substring(8,10);
//            frdate =  ls_yeare + ls_mm + ls_dd;
//            ls_yeare = todate.substring(0,4);
//            ls_mm = todate.substring(5,7);
//            ls_dd = todate.substring(8,10);
//            todate =  ls_yeare + ls_mm + ls_dd;
            if(pcode == null || pcode.equals("")){
                pcode = "%";
            }
            index11Dto.setFrdate(frdate);
            index11Dto.setTodate(todate);
            index11Dto.setWcode(wcode);
            index11Dto.setLotno(lotno);
            index11Dto.setPcode(pcode);

            index11DtoList = service11.getIndex14List03(index11Dto);

            model.addAttribute("itemList",index11DtoList);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App14List03_index Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return index11DtoList;
    }


    //일별 불량별  검사현황
    @GetMapping(value="/index56/list01")
    public Object App56List01_index(@RequestParam("frdate") String frdate,
                                    @RequestParam("todate") String todate,
                                    @RequestParam("wcode") String wcode,
                                    @RequestParam("lotno") String lotno,
                                    Model model, HttpServletRequest request) throws Exception{
        try {

//            String ls_yeare = frdate.substring(0,4);
//            String ls_mm = frdate.substring(5,7);
//            String ls_dd = frdate.substring(8,10);
//            frdate =  ls_yeare + ls_mm + ls_dd;
//            ls_yeare = todate.substring(0,4);
//            ls_mm = todate.substring(5,7);
//            ls_dd = todate.substring(8,10);
//            todate =  ls_yeare + ls_mm + ls_dd;

            index11Dto.setFrdate(frdate);
            index11Dto.setTodate(todate);
            index11Dto.setWcode(wcode);
            index11Dto.setLotno(lotno);

            index11DtoList = service11.getIndex56List01(index11Dto);

            model.addAttribute("itemList",index11DtoList);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App14List01_index Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return index11DtoList;
    }



    //월별 불량별 검사현황
    @GetMapping(value="/index56/list02")
    public Object App56List02_index(@RequestParam("frdate") String frdate,
                                    @RequestParam("todate") String todate,
                                    @RequestParam("wcode") String wcode,
                                    @RequestParam("lotno") String lotno,
                                    Model model, HttpServletRequest request) throws Exception{
        try {

//            String ls_yeare = frdate.substring(0,4);
//            String ls_mm = frdate.substring(5,7);
//            String ls_dd = frdate.substring(8,10);
//            frdate =  ls_yeare + ls_mm + ls_dd;
//            ls_yeare = todate.substring(0,4);
//            ls_mm = todate.substring(5,7);
//            ls_dd = todate.substring(8,10);
//            todate =  ls_yeare + ls_mm + ls_dd;
            index11Dto.setFrdate(frdate);
            index11Dto.setTodate(todate);
            index11Dto.setWcode(wcode);
            index11Dto.setLotno(lotno);

            index11DtoList = service11.getIndex56List02(index11Dto);

            model.addAttribute("itemList",index11DtoList);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App14List02_index Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return index11DtoList;
    }

    //유형별 불량별 검사현황
    @GetMapping(value="/index56/list03")
    public Object App56List03_index(@RequestParam("frdate") String frdate,
                                    @RequestParam("todate") String todate,
                                    @RequestParam("wcode") String wcode,
                                    @RequestParam("lotno") String lotno,
                                    Model model, HttpServletRequest request) throws Exception{
        try {

//            String ls_yeare = frdate.substring(0,4);
//            String ls_mm = frdate.substring(5,7);
//            String ls_dd = frdate.substring(8,10);
//            frdate =  ls_yeare + ls_mm + ls_dd;
//            ls_yeare = todate.substring(0,4);
//            ls_mm = todate.substring(5,7);
//            ls_dd = todate.substring(8,10);
//            todate =  ls_yeare + ls_mm + ls_dd;
            index11Dto.setFrdate(frdate);
            index11Dto.setTodate(todate);
            index11Dto.setWcode(wcode);
            index11Dto.setLotno(lotno);

            index11DtoList = service11.getIndex56List03(index11Dto);

            model.addAttribute("itemList",index11DtoList);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App14List03_index Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return index11DtoList;
    }

    //출하현황
    @GetMapping(value="/index16/list01")
    public Object App16List01_index(@RequestParam("frdate") String frdate,
                                    @RequestParam("todate") String todate,
                                    @RequestParam("lotno") String lotno,
                                    Model model, HttpServletRequest request) throws Exception{
        try {

            String ls_yeare = frdate.substring(0,4);
            String ls_mm = frdate.substring(5,7);
            String ls_dd = frdate.substring(8,10);
            frdate =  ls_yeare + ls_mm + ls_dd;
            ls_yeare = todate.substring(0,4);
            ls_mm = todate.substring(5,7);
            ls_dd = todate.substring(8,10);
            todate =  ls_yeare + ls_mm + ls_dd;

            index11Dto.setFrdate(frdate);
            index11Dto.setTodate(todate);
            index11Dto.setLotno(lotno);

            index11DtoList = service11.getIndex16List01(index11Dto);

            model.addAttribute("itemList",index11DtoList);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App13List01 Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return index11DtoList;
    }


    //생산계획현황
    @GetMapping(value="/index21/janlist")
    public Object App21List01_index(@RequestParam("frdate") String frdate,
                          @RequestParam("todate") String todate,
                          @RequestParam("pcode") String pcode,
                          Model model, HttpServletRequest request) throws Exception{
        IndexCa613Dto _indexCa613Dto = new IndexCa613Dto();
        List<IndexCa613Dto> _indexCa613ListDto = new ArrayList<>();
        try {

//            String ls_yeare = frdate.substring(0,4);
//            String ls_mm = frdate.substring(5,7);
//            String ls_dd = frdate.substring(8,10);
//            frdate =  ls_yeare + ls_mm + ls_dd;
//                 ls_yeare = todate.substring(0,4);
//                 ls_mm = todate.substring(5,7);
//                 ls_dd = todate.substring(8,10);
//            todate =  ls_yeare + ls_mm + ls_dd;

            _indexCa613Dto.setFrdate(frdate);
            _indexCa613Dto.setTodate(todate);
            _indexCa613Dto.setPcode(pcode);

            _indexCa613ListDto = service11.GetIndex21JanList(_indexCa613Dto);

            model.addAttribute("itemList",_indexCa613ListDto);

        } catch (Exception ex) {
//                dispatchException = ex;
            log.info("App21List01_index Exception =====>" + ex.toString());
//            log.debug("Exception =====>" + ex.toString() );
        }

        return _indexCa613ListDto;
    }



    //생산계획현황
    @GetMapping(value="/index59/save")
    public Object App59Save_index(@RequestParam("frdate") String frdate
                                ,@RequestParam( value =  "jcode[]") List<String> jcode
                                ,@RequestParam( value =  "jqty[]") List<String> jqty,
                                Model model, HttpServletRequest request) throws Exception{
        try {
            Index59Dto _index59Dto = new Index59Dto();
//            String year = frdate.substring(0,4) ;
//            String month = frdate.substring(5,7) ;
//            String day   = frdate.substring(8,10) ;
//            frdate = year + month + day ;

            Boolean result = false;
            if( jcode.size() > 0){
                for(int i = 0; i < jcode.size(); i++){
                    _index59Dto.setAcorp("00");
                    _index59Dto.setKey1(frdate);
                    _index59Dto.setJepm(jcode.get(i));
                    _index59Dto.setIjaego_su1(Integer.parseInt(jqty.get(i)));
                    _index59Dto.setJepm_size("00");
//                    log.info("frdate  =====>" + frdate);
//                    log.info("jcode   =====>" + jcode.get(i));
//                    log.info("jqty   =====>" + jqty.get(i));
                    String ls_acorp = "";
                    //일자별로 n개등록가능하므로 업데이트 막음.
                    //ls_acorp = service04.SelectJegoCheck(_index59Dto);
                    //if(ls_acorp == null ){
                    ls_acorp = GetMaxSeqIpgo(_index59Dto);
                    _index59Dto.setAcorp(ls_acorp);
                    result = service35.InsertJegoIpgo(_index59Dto);
                    // }else{
                    //     _index59Dto.setAcorp(ls_acorp);
                    //     result = service04.UpdateJegoIpgo(_index59Dto);
                    // }
                    if (!result){
                        return "error";
                    }
                }
            }

        }catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
        return "success";
    }


    //재고실사 리스트
    @GetMapping(value="/index59/list")
    public Object App59List_index(@RequestParam("ipfrdate") String ipfrdate,
                                  @RequestParam("iptodate") String iptodate,
                                  @RequestParam("jpbgubn") String jpbgubn,
                                  @RequestParam("jkey") String jkey,
                                  Model model, HttpServletRequest request) throws Exception{
        Index59Dto _index59Dto = new Index59Dto();
        List<Index59Dto> _index59List = new ArrayList<>();
        try {
//            String year = ipfrdate.substring(0,4) ;
//            String month = ipfrdate.substring(5,7) ;
//            String day   = ipfrdate.substring(8,10) ;
//            ipfrdate = year + month + day ;
//            year = iptodate.substring(0,4) ;
//            month = iptodate.substring(5,7) ;
//            day   = iptodate.substring(8,10) ;
//            iptodate = year + month + day ;
            _index59Dto.setFrdate(ipfrdate);
            _index59Dto.setTodate(iptodate);
            _index59Dto.setJkey(jkey);
            _index59List = service35.SelectJegoIpgo(_index59Dto);
            model.addAttribute("index59List",_index59List);

        } catch (Exception ex) {
            log.info("index59List Exception =====>" + ex.toString());
        }

        return _index59List;
    }


    @RequestMapping(value="/index59/del")
    public String index59Delete(  @RequestParam("ipdate") String ipdate,
                                  @RequestParam("acorp") String acorp,
                                  @RequestParam("jepm") String jepm,
                                  Model model,   HttpServletRequest request){
        Index59Dto _index59Dto = new Index59Dto();
        _index59Dto.setKey1(ipdate);
        _index59Dto.setAcorp(acorp);
        _index59Dto.setJepm(jepm);
        Boolean result = false;
        if(jepm.length() > 0){
            result = service35.DeleteJaegoIpgoAcorp(_index59Dto);
        }else{
            result = service35.DeleteJaegoIpgo(_index59Dto);
        }
        if (!result) {
            return "error";
        }
        return "success";
    }



    //그룹별 재고현황 리스트
    @GetMapping(value="/index60/jaegofromlist")
    public Object App04JaegoFromList_index(@RequestParam("searchtxt") String searchtxt,
                                           @RequestParam("jcustcd") String jcustcd,
                                           @RequestParam("frdate") String frdate,
                                           @RequestParam("todate") String todate,
                                           @RequestParam("jpbgubn") String jpbgubn,
                                           Model model, HttpServletRequest request) throws Exception{
        Index59Dto _index59Dto = new Index59Dto();
        List<Index59Dto> _index03List = new ArrayList<>();
        try {
            if(searchtxt == null || searchtxt.equals("")){
                searchtxt = "%";
            }
            if(jcustcd == null || jcustcd.equals("")){
                jcustcd = "%";
            }

            //index59Dto.setJcustomer_code(jcustcd);
            _index59Dto.setJkey(searchtxt);
            _index59Dto.setJfrdate("20000101");
            _index59Dto.setJpb_gubn(jpbgubn);

//            String year = frdate.substring(0,4) ;
//            String month = frdate.substring(5,7) ;
//            String day   = frdate.substring(8,10) ;
//            frdate = year + month + day ;
//
//            year = todate.substring(0,4) ;
//            month = todate.substring(5,7) ;
//            day   = todate.substring(8,10) ;
//            todate = year + month + day ;
            _index59Dto.setJtodate(frdate);
            _index59Dto.setFrdate(frdate);
            _index59Dto.setTodate(todate);
//            log.info("001 ->" + _index59Dto.getJpum());
//            log.info("002 ->" + _index59Dto.getFrdate());
//            log.info("003 ->" + _index59Dto.getTodate());
            //log.info("004 ->" + _index59Dto.getJpb_gubn());
            _index03List = service35.GetJpumFromJaegoList(_index59Dto);
            //log.info("002 ->" + _index59Dto.getJpb_gubn());
            model.addAttribute("index03List",_index03List);

        } catch (Exception ex) {
            log.info("App04JaegoCustList_index Exception =====>" + ex.toString());
        }

        return _index03List;
    }



    //기간별수불현황
    @GetMapping(value="/index63/subul01")
    public Object App63SubulList_index(@RequestParam("frdate") String frdate,
                                       @RequestParam("todate") String todate,
                                       @RequestParam("jpbgubn") String jpbgubn,
                                       @RequestParam("searchtxt") String searchtxt,
                                       Model model, HttpServletRequest request) throws Exception{
        Index59Dto index59Dto_S = new Index59Dto();
        List<Index59Dto> _index03List = new ArrayList<>();

        try {

            if(searchtxt == null || searchtxt.equals("")){
                searchtxt = "%";
            }
//            String year = frdate.substring(0,4) ;
//            String month = frdate.substring(5,7) ;
//            String day   = frdate.substring(8,10) ;
//            frdate = year + month + day ;
//            year = todate.substring(0,4) ;
//            month = todate.substring(5,7) ;
//            day   = todate.substring(8,10) ;
//            todate = year + month + day ;
            index59Dto_S.setJkey(searchtxt);
            index59Dto_S.setFrdate(frdate);
            index59Dto_S.setTodate(todate);

            _index03List = service35.GetJpumSubul02(index59Dto_S);
            model.addAttribute("index03List",_index03List);

        } catch (Exception ex) {
            log.info("App03SubulList_index Exception =====>" + ex.toString());
        }

        return _index03List;
    }

    //기간별수불현황
    @GetMapping(value="/index63/subul02")
    public Object App03SubulList02_index(@RequestParam("frdate") String frdate,
                                         @RequestParam("todate") String todate,
                                         @RequestParam("jpbgubn") String jpbgubn,
                                         @RequestParam("searchtxt") String searchtxt,
                                         Model model, HttpServletRequest request) throws Exception{
        Index59Dto index59Dto_S = new Index59Dto();
        List<Index59Dto> _index03List = new ArrayList<>();

        try {

            if(searchtxt == null || searchtxt.equals("")){
                searchtxt = "%";
            }
            index59Dto_S.setJkey(searchtxt);
            index59Dto_S.setFrdate(frdate);
            index59Dto_S.setTodate(todate);

            _index03List = service35.GetJpumSubul02(index59Dto_S);
            model.addAttribute("index03List",_index03List);

        } catch (Exception ex) {
            log.info("App03SubulList02_index Exception =====>" + ex.toString());
        }

        return _index03List;
    }


    //소요량등록
    @GetMapping(value="/index06/save")
    public Object App06BomSave_index(@RequestParam("jpname") String jpname,
                                         @RequestParam("jpcode") String jpcode,
                                         @RequestParam("pcodeArr[]") List<String> pcodeArr,
                                         @RequestParam("pnameArr[]") List<String> pnameArr,
                                         @RequestParam("psizeArr[]") List<String> psizeArr,
                                         @RequestParam("pqtyArr[]") List<BigDecimal> pqtyArr,
                                         Model model, HttpServletRequest request) throws Exception{

        try {
            Boolean result = true;
            String ls_wflag = "";
            IndexCa613Dto _indexCa613Dto = new IndexCa613Dto();
            _indexCa613Dto.setOpcod(jpcode);


            result = service35.DeleteBom501(_indexCa613Dto);
            if (!result) {
                //log.info("DeleteBom501 =====>" );
                //return "error";
            }

            for(int i = 0; i < pcodeArr.size(); i++){
                _indexCa613Dto.setSpcod(pcodeArr.get(i));
                _indexCa613Dto.setPname(pnameArr.get(i));
                _indexCa613Dto.setPsize(psizeArr.get(i));
                if (pqtyArr.get(i) == null){
                    _indexCa613Dto.setDeqty(BigDecimal.ZERO);
                }else{
                    _indexCa613Dto.setDeqty(pqtyArr.get(i));
                }
                //_indexCa613Dto.setWeight(pweightArr.get(i));
                ls_wflag = service35.SelectBomCheck(_indexCa613Dto);
                if (ls_wflag == null || ls_wflag.equals("") ){
                    result = service35.InsertBom501(_indexCa613Dto);
                }else{
                    _indexCa613Dto.setWflag("Y");
                    result = service35.UpdateBom501(_indexCa613Dto);
                }
                if (!result) {
                    log.info("for(int i = 0; i < pcodeArr.size() =====>" );
                    return "error";
                }
            }
            return "success";
        } catch (Exception ex) {
            log.info("App06BomSave_index Exception =====>" + ex.toString());
        }

        return "success";
    }


    //소요량삭제
    @GetMapping(value="/index06/deltot")
    public Object App06BomDeltot_index(@RequestParam("jpname") String jpname,
                                     @RequestParam("jpcode") String jpcode,
                                     Model model, HttpServletRequest request) throws Exception{

        try {
            Boolean result = true;
            IndexCa613Dto _indexCa613Dto = new IndexCa613Dto();
            _indexCa613Dto.setOpcod(jpcode);
            result = service35.DeleteBom501(_indexCa613Dto);
            if (!result) {
                return "error";
            }
            return "success";
        } catch (Exception ex) {
            log.info("App06BomDeltot_index Exception =====>" + ex.toString());
        }

        return "success";
    }


    //소요량 모품목조회
    @GetMapping(value="/index06/listopcod")
    public Object App06BomListOpcod_index(@RequestParam("jpname") String jpname,
                                       @RequestParam("jpcode") String jpcode,
                                       Model model, HttpServletRequest request) throws Exception{

        List<IndexCa613Dto> _indexCa613ListDto = new ArrayList<>();
        IndexCa613Dto _indexCa613Dto = new IndexCa613Dto();

        try {
            Boolean result = true;
            _indexCa613Dto.setOpcod(jpcode);
            _indexCa613ListDto = service35.SelectBomListOpcod(_indexCa613Dto);
            if (!result) {
                return "error";
            }
        } catch (Exception ex) {
            log.info("App06BomListOpcod_index Exception =====>" + ex.toString());
        }

        return _indexCa613ListDto;
    }

    //소요량 품목조회
    @GetMapping(value="/index06/list")
    public Object App06BomList_index(@RequestParam("jpname") String jpname,
                                          @RequestParam("jpcode") String jpcode,
                                          Model model, HttpServletRequest request) throws Exception{

        List<IndexCa613Dto> _indexCa613ListDto = new ArrayList<>();
        IndexCa613Dto _indexCa613Dto = new IndexCa613Dto();

        try {
            Boolean result = true;
            _indexCa613Dto.setOpcod("%");
            _indexCa613ListDto = service35.SelectBomListTot(_indexCa613Dto);
            if (!result) {
                return "error";
            }
        } catch (Exception ex) {
            log.info("App06BomList_index Exception =====>" + ex.toString());
        }

        return _indexCa613ListDto;
    }

    public String GetMaxSeqIpgo(Index59Dto asDto){
        String ls_seq = service35.SelectMaxSeqIpgo(asDto);
        if(ls_seq == null || ls_seq.equals("00")){
            ls_seq = "01";
        }else{
            Integer ll_seq = Integer.parseInt(ls_seq) + 1;
            ls_seq = ll_seq.toString();
            if (ls_seq.length() == 1){
                ls_seq = "0" + ls_seq;
            }
        }
        return ls_seq;
    }

}
