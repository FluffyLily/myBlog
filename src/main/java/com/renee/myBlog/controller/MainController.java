package com.renee.myBlog.controller;

import com.renee.myBlog.model.BoardBean;
import com.renee.myBlog.model.MemberBean;
import com.renee.myBlog.service.BoardService;
import com.renee.myBlog.service.MainService;
import com.renee.myBlog.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MainService mainService;

    @RequestMapping("home")
    public String homePage() throws Exception {
        return "home";
    }

    @RequestMapping("main")
    public String mainPage(HttpSession session, HttpServletRequest request,
                           Model model) throws Exception {

        String id = (String) session.getAttribute("id");
        MemberBean member = memberService.checkUser(id);

        int page = 1;
        if(request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        List<BoardBean> blogList = new ArrayList<BoardBean>();
        blogList = mainService.getPostList();

        model.addAttribute("page", page);
        model.addAttribute("member", member);
        model.addAttribute("blogList", blogList);

        return "main";
    }

    @RequestMapping("about")
    public String aboutMe() throws Exception {
        return "about";
    }

    @RequestMapping("memeMaker")
    public String memeMaker() throws Exception {
        return "meme_maker";
    }

}
