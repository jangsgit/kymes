package com.dae.kdmes.controller;

import com.dae.kdmes.DTO.App01.Index03Dto;
import com.dae.kdmes.DTO.Appm.FPLAN_VO;
import com.dae.kdmes.DTO.Cms.CmsIndex01Dto;
import com.dae.kdmes.DTO.CommonDto;
import com.dae.kdmes.DTO.Popup.PopupDto;
import com.dae.kdmes.DTO.UserFormDto;
import com.dae.kdmes.Service.Appm.Appcom01Service;
import com.dae.kdmes.Service.Cms.CmsIndex01Service;
import com.dae.kdmes.Service.master.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RequestMapping("/auth")
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final Appcom01Service appcom01Service;

    PopupDto popParmDto = new PopupDto();
    List<FPLAN_VO> itemDtoList = new ArrayList<>();

    UserFormDto userformDto = new UserFormDto();
    CommonDto CommDto = new CommonDto();
    FPLAN_VO fplanDto = new FPLAN_VO();

    protected Log log =  LogFactory.getLog(this.getClass());


    // 관리자 화면
    @GetMapping(value="/admin")
    public String AdminLoginForm(Model model){

        UserFormDto custReturnDto = authService.GetCustInfo(userformDto);
        model.addAttribute("custReturnDto", custReturnDto);
        return "loginFormAdmin";
    }


    // 관리자 화면
    @GetMapping(value="/dashboard")
    public String DashboardForm(Model model){

        UserFormDto custReturnDto = authService.GetCustInfo(userformDto);
        model.addAttribute("custReturnDto", custReturnDto);
        return "dashboard";
    }

    @GetMapping(value="/emmsdashboard")
    public String memberEmmsBoardForm( Model model
            , RedirectAttributes redirectAttributes
            , HttpServletRequest request){
        HttpSession session = request.getSession();
        UserFormDto userformDto = (UserFormDto) session.getAttribute("userformDto");
        if (userformDto == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not logged in.");
            return "redirect:/";  // 리다이렉트할 페이지로 변경하세요.
        }

        String ls_flag = userformDto.getFlag();
        userformDto.setPagetree01("관리자모드");
        userformDto.setPagenm("Dashboard");
        model.addAttribute("userFormDto", userformDto);
        CmsIndex01Dto cmsdto = new CmsIndex01Dto();
        if (ls_flag.equals("AA")){

            Date nowData = new Date();
            SimpleDateFormat endDate = new SimpleDateFormat("yyyyMMdd");
            String indate = endDate.format(nowData).toString();
            CommDto.setMenuTitle("공통코드등록");
            CommDto.setMenuUrl("기준정보>공통코드등록");
            CommDto.setMenuCode("index01");
            model.addAttribute("userformDto",userformDto);

            userformDto.setPernm(userformDto.getUsername());
            userformDto.setUsername(userformDto.getUsername());
            userformDto.setUserid(userformDto.getUserid());
            userformDto.setCustcd(userformDto.getCustcd());
            userformDto.setFlag(userformDto.getFlag());

            model.addAttribute("userDto", userformDto );

//            System.out.println("리스트 데이터:");
//            for (CmsIndex01Dto item : cms01List) {
//                System.out.println("- " + item.getAdditional_Info_1());
//            }

            return "mainframe";
        } else if (userformDto == null) {
            model.addAttribute("msg", "로그인실패");
            return "/";
        } else if (ls_flag.equals("BB")) {
            CommDto.setMenuTitle("조립공정");  //
            CommDto.setMenuUrl("생산공정>조립공정");
            CommDto.setMenuCode("appcom21");
            String fdate = getFrDate();
            String tdate = getAddDate();
            String cltcd = "%";
            String pcode = "%";
            fplanDto.setLine("00");
            fplanDto.setWflag("00020");
            fplanDto.setFdate(fdate);
            fplanDto.setTdate(tdate);
            fplanDto.setCltcd(cltcd);
            fplanDto.setPcode(pcode);
            fplanDto.setPlan_no("%");
            itemDtoList = appcom01Service.GetFPLAN_List03(fplanDto);

            model.addAttribute("itemDtoList", itemDtoList);
            return "App01/index21";
        } else if (ls_flag.equals("ZZ")){
            //현재날짜기준 월초(1일) 구하기
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Date date  = new Date(System.currentTimeMillis());
            String time = formatter.format(date);
            String time2 = time.substring(0,6) + "01";
            //log.info(time2);


            //현재날짜기준 당월말일 구하기
            String year = time.substring(0,4);
            String month = time.substring(4,6);
            String day = time.substring(6,8);


            int year1 = Integer.parseInt(year);
            int month1 = Integer.parseInt(month);
            int day1 = Integer.parseInt(day);

            Calendar cal = Calendar.getInstance();
            cal.set(year1, month1-1, day1);


            String lastday1 = String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            String lastday = year+month+lastday1;
            log.info(lastday);

            /*popParmDto.setFrdate(time2);
            popParmDto.setTodate(lastday);*/
            popParmDto.setFrdate(time2);
            popParmDto.setTodate(lastday);

            log.info(time2);
            log.info(lastday);

            return "mainframadmin";
        } else{

            //현재날짜기준 월초(1일) 구하기
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Date date  = new Date(System.currentTimeMillis());
            String time = formatter.format(date);
            String time2 = time.substring(0,6) + "01";
            log.info(time2);


            //현재날짜기준 당월말일 구하기
            String year = time.substring(0,4);
            String month = time.substring(4,6);
            String day = time.substring(6,8);


            int year1 = Integer.parseInt(year);
            int month1 = Integer.parseInt(month);
            int day1 = Integer.parseInt(day);

            Calendar cal = Calendar.getInstance();
            cal.set(year1, month1-1, day1);


            String lastday1 = String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            String lastday = year+month+lastday1;
            log.info(lastday);

            /*popParmDto.setFrdate(time2);
            popParmDto.setTodate(lastday);*/
            popParmDto.setFrdate(time2);
            popParmDto.setTodate(lastday);

            log.info(time2);
            log.info(lastday);

            return "mainframcustom";
        }


    }


    private String getFrDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, -100); // 빼고 싶다면 음수 입력
        Date date      = new Date(cal1.getTimeInMillis());

        return formatter.format(date);
    }
    private String getToDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date date      = new Date(System.currentTimeMillis());

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
