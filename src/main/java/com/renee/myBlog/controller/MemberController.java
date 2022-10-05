package com.renee.myBlog.controller;

import com.renee.myBlog.model.MemberBean;
import com.renee.myBlog.service.MemberService;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;

@Controller
public class MemberController {
    @Autowired
    private MemberService memberService;
    // 회원 가입
    @RequestMapping(value = "joinMember", method = RequestMethod.POST)
    public String joinMember(@RequestParam("profile") MultipartFile multipartFile, MemberBean memberBean,
                             HttpServletRequest request, Model model) throws Exception {
        String filename = multipartFile.getOriginalFilename();
        int size = (int) multipartFile.getSize();

        @SuppressWarnings("deprecation")
        String path = request.getRealPath("WEB-INF/upload");
        System.out.println("path = " + path);

        int result;
        String[] file = new String[2];
        String new_filename = "";

        if (size > 0) {
            StringTokenizer st = new StringTokenizer(filename, ".");
            file[0] = st.nextToken();
            file[1] = st.nextToken();
            UUID uuid = UUID.randomUUID();

            new_filename = uuid.toString() + "." + file[1];
            System.out.println("new_filename = " + new_filename);
            if (size > 10000000) {
                result = 1;
                model.addAttribute("result", result);

                return "member/upload_file";

            } else if (!file[1].equals("jpg") && !file[1].equals("gif") && !file[1].equals("jpeg")
                    && !file[1].equals("png")) {
                result = 2;
                model.addAttribute("result", result);

                return "member/upload_file";
            }
        }

        if (size > 0) {
            multipartFile.transferTo(new File(path + "/" + new_filename));
        }

        memberBean.setPhoto(new_filename);
        memberService.joinMember(memberBean);

        return "redirect:main";
    }

    // ID 중복 확인
    @RequestMapping(value = "checkId", method = RequestMethod.POST)
    public String checkId(@RequestParam("member_id") String id, Model model) throws Exception {
        int result = memberService.checkId(id);
        model.addAttribute("result", result);

        return "member/check_id_result";
    }

    // 이메일 인증
    @RequestMapping("checkEmail")
    public String checkEmail(String email, Model model) {

        Random random = new Random();
        int auth = random.nextInt(88888) + 11111;

        String charSet = "utf-8";
        String hostSMTP = "smtp.naver.com";
        String hostSMTPid = "abcd@naver.com";
        String hostSMTPpwd = "abcd1234";

        String fromEmail = "abcd@naver.com";
        String fromName = "Renee's Blog";
        String subject = "회원 가입 이메일 인증번호 입니다.";

        try {
            HtmlEmail mail = new HtmlEmail();
            mail.setDebug(true);
            mail.setCharset(charSet);
            mail.setSSL(true);
            mail.setHostName(hostSMTP);
            mail.setSmtpPort(587);

            mail.setAuthentication(hostSMTPid, hostSMTPpwd);
            mail.setTLS(true);
            mail.addTo(email, charSet);
            mail.setFrom(fromEmail, fromName, charSet);
            mail.setSubject(subject);
            mail.setHtmlMsg("<p align = 'center'><b>회원가입을 환영합니다.</b></p><br>" + "<div align='center'> 인증번호 : " + auth + "</div>");
            mail.send();
        } catch (Exception e) {
            System.out.println(e);
        }

        model.addAttribute("auth", auth);

        return "member/validate_email";
    }

    // 로그인
    @RequestMapping(value = "loginOk", method = RequestMethod.POST)
    public String loginOk(@RequestParam("id") String id, @RequestParam("pw") String pw,
                          HttpSession session, Model model) throws Exception {
        int result = 0;
        MemberBean memberBean = memberService.checkUser(id);

        if (memberBean == null) {
            result = 1;
            model.addAttribute("result", result);

            return "member/login_result";
        } else {
            if (memberBean.getPw().equals(pw) && memberBean.getQuit().equals("n")) {

                session.setAttribute("id", id);

                String photo = memberBean.getPhoto();
                model.addAttribute("photo", photo);

                return "redirect:main";
            } else {
                result = 2;
                model.addAttribute("result", result);

                return "member/login_result";
            }
        }
    }
    // 비밀번호 찾기 인증 메일 전송 알림
    @RequestMapping(value = "pwFind")
    public String pwFind() {
        return "member/find_pw";
    }

    // 비밀번호 찾기 인증 메일 전송하기
    @RequestMapping(value = "pwFindOk", method = RequestMethod.POST)
    public String pwFindOk(@ModelAttribute MemberBean memberBean, HttpServletResponse response,
                           Model model) throws Exception {
        response.setContentType("text/html;charset=UTF-8");

        MemberBean member = memberService.pwFind(memberBean);

        int result = 0;
        if (member == null || member.getQuit().equals("y")) {
            return "member/validate_pw";
        } else {
            String charSet = "utf-8";
            String hostSMTP = "smtp.naver.com";
            String hostSMTPid = "abcd@naver.com";
            String hostSMTPpwd = "abcd1234";

            String fromEmail = "abcd@naver.com";
            String fromName = "Renee's Blog";
            String subject = "비밀번호 찾기 안내 이메일입니다.";
            String email = member.getEmail();
            try {
                HtmlEmail mail = new HtmlEmail();
                mail.setDebug(true);
                mail.setCharset(charSet);
                mail.setSSL(true);
                mail.setHostName(hostSMTP);
                mail.setSmtpPort(587);

                mail.setAuthentication(hostSMTPid, hostSMTPpwd);
                mail.setTLS(true);
                mail.addTo(email, charSet);
                mail.setFrom(fromEmail, fromName, charSet);
                mail.setSubject(subject);
                mail.setHtmlMsg("<p align = 'center'><b>" + member.getName() + "</b>님의 비밀번호는</p><br>" + "<div align='center'>" + member.getPw() + "입니다.</div>");
                mail.send();
                result = 1;
            } catch (Exception e) {
                System.out.println(e);
            }

            model.addAttribute("result", result);

            return "member/find_pw";
        }

    }

    // 개인정보
    @RequestMapping("userInfo")
    public String myPage(HttpSession session, Model model) throws Exception {

        String id = (String) session.getAttribute("id");
        MemberBean member = memberService.checkUser(id);
        // 비밀번호 '********'
        int length = member.getPw().length();
        String encrypt = "";
        for (int i=0; i<length; i++) {
            encrypt += "*";
        }

        model.addAttribute("member", member);
        model.addAttribute("id", id);
        model.addAttribute("encrypt", encrypt);

        return "member/user_info";
    }

    // 회원정보 수정
    @RequestMapping(value = "updateUserInfo", method = RequestMethod.POST)
    public String memberModify(@RequestParam("photo") MultipartFile mf,
                               MemberBean member,
                               HttpServletRequest request,
                               HttpSession session,
                               Model model) throws Exception {
        String filename = mf.getOriginalFilename();
        int size = (int) mf.getSize();

        String path = request.getRealPath("upload");
        System.out.println("path:" + path);

        int result = 0;
        String file[] = new String[2];
        String new_filename = "";

        if (size > 0) {
            StringTokenizer st = new StringTokenizer(filename, ".");
            file[0] = st.nextToken();
            file[1] = st.nextToken();
            UUID uuid = UUID.randomUUID();

            new_filename = uuid.toString() + "." + file[1];
            System.out.println("new_filename: " + new_filename);
            if (size > 10000000) {
                result = 1;
                model.addAttribute("result", result);

                return "member/upload_file";
            } else if (!file[1].equals("jpg") && !file[1].equals("jpeg") && !file[1].equals("gif")
                    && !file[1].equals("png")) {
                result = 2;
                model.addAttribute("result", result);

                return "member/upload_file";
            }
        }

        if (size > 0) {
            mf.transferTo(new File(path + "/" + new_filename));
        }

        String id = (String) session.getAttribute("id");
        MemberBean memberBean = memberService.checkUser(id);

        if (size > 0) {
            member.setPhoto(new_filename);
        } else {
            member.setPhoto(memberBean.getPhoto());
        }

        member.setId(id);
        memberService.updateUserInfo(member);

        model.addAttribute("name", member.getName());
        model.addAttribute("photo", member.getPhoto());

        return "redirect:userInfo";
    }
    // 로그아웃
    @RequestMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "member/logout";
    }

}
