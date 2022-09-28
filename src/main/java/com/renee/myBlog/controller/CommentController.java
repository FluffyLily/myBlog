package com.renee.myBlog.controller;

import com.renee.myBlog.model.CommentBean;
import com.renee.myBlog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    // 댓글 작성 양식(with bno, page)
    @RequestMapping(value = "/writeCommentForm")
    public String writeCommentForm(@RequestParam("bno") int bno,
                                   @RequestParam("page") int page,
                                   Model model) throws Exception {
        model.addAttribute("bno", bno);
        model.addAttribute("page", page);

        return "board/comment_form";
    }

    // 댓글 쓰기
    @RequestMapping("writeComment")
    public String writeComment(@RequestParam("bno") int bno,
                               @RequestParam("page") int page,
                               Model model, HttpServletRequest request,
                               CommentBean commentBean) throws Exception {
        bno = Integer.parseInt(request.getParameter("bno"));
        commentBean.setBno(bno);

        if(request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        commentService.setComment(commentBean);

        return "redirect:/postDetail?bno="+ bno + "&page=" + page;
    }

    // 댓글 불러오기
    @RequestMapping("queryComment")
    public String commentList(@RequestParam("bno") int bno,
                              @RequestParam("page") int page,
                              Model model) throws Exception {
        List<CommentBean> commentList = new ArrayList<CommentBean>();

        // 댓글 개수
        int commentCnt = commentService.countComment(bno);

        // 댓글 목록
        commentList = commentService.queryComment(bno);

        model.addAttribute("commentCnt", commentCnt);
        model.addAttribute("commentList", commentList);

        return "redirect:/postDetail?bno="+ bno + "&page=" + page;
    }

    // 댓글 삭제하기
    @RequestMapping("deleteComment")
    public String deleteComment(@RequestParam("rno") int rno,
                                @RequestParam("bno") int bno,
                                @RequestParam("page") int page) throws Exception {
        commentService.deleteComment(rno);
        return "redirect:/postDetail?bno=" + bno + "&page=" + page;
    }
}
