package com.renee.myBlog.controller;

import com.renee.myBlog.model.BoardBean;
import com.renee.myBlog.model.CommentBean;
import com.renee.myBlog.model.MemberBean;
import com.renee.myBlog.model.SearchCondition;
import com.renee.myBlog.service.BoardService;
import com.renee.myBlog.service.CommentService;
import com.renee.myBlog.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private MemberService memberService;

    // 포스트 작성 양식
    @RequestMapping("postWriteForm")
    public String writeForm() { return "board/post_form"; }

    // 포스트 작성
    @RequestMapping("postWrite")
    public String write(HttpSession session, HttpServletRequest request, BoardBean boardBean,
                        Model model, MultipartHttpServletRequest multiRequest) throws Exception {
        List<MultipartFile> fileList = multiRequest.getFiles("post_photo");
        String path = request.getRealPath("WEB-INF/upload");
        System.out.println("path = " + path);

        StringBuilder files = new StringBuilder();
        for (MultipartFile multiFile : fileList) {
            String originFileName = multiFile.getOriginalFilename();

            String[] file = new String[2];
            StringTokenizer st = new StringTokenizer(originFileName, ".");
            file[0] = st.nextToken();    // 파일명
            file[1] = st.nextToken();    // 확장자

            int result = 0;
            int size = (int) multiFile.getSize();
            if (size > 10000000) { // 파일 사이즈가 100KB 초과시

                result = 1;
                model.addAttribute("result", result);

                return "member/upload_file";
            }else if (!file[1].equals("jpg") &&
                      !file[1].equals("jpeg") &&
                      !file[1].equals("png") &&
                      !file[1].equals("gif")) {
                result = 2;
                model.addAttribute("result", result);
                return "member/upload_file";
            }
            System.out.println("originFileName = " + originFileName);

            String safeFile = System.currentTimeMillis() + originFileName;
            files.append(safeFile + ",");
            System.out.println("safeFile = " + safeFile);

            multiFile.transferTo(new File(path + "/" + safeFile));
            System.out.println("첨부파일들 : " + files.toString());
        }
        System.out.println("첨부파일들 : " + files.toString());
        boardBean.setPhoto(files.toString());

        System.out.println("insert 전");
        boardService.writePost(boardBean);
        System.out.println("insert 완료");

        return "main";
    }

    // 포스트 목록
    @RequestMapping("postList")
    public String postList(HttpSession session, HttpServletRequest request,
                           MemberBean member, SearchCondition sc, Model model) throws Exception {
        String id = (String) session.getAttribute("id");
        member = memberService.checkUser(id);

        List<BoardBean> postList = new ArrayList<BoardBean>();
        int page = 1;
        int limit = 6;

        if(request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        int offset = (page - 1) * limit;
        int listCount = boardService.getCount();
        postList = boardService.postList(offset);

        int pageCount = listCount / limit + ((listCount % limit == 0) ? 0 : 1);
        // 페이지 네비게이션
        int startPage = ((page - 1) / 10) * limit + 1;
        int endPage = startPage + 10 - 1;

        if (endPage > pageCount)
            endPage = pageCount;

        model.addAttribute("member", member);
        model.addAttribute("id", id);
        model.addAttribute("page", page);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("listCount", listCount);
        model.addAttribute("postList", postList);

        return "board/post_list";
    }

    // 포스트 상세 페이지
    @RequestMapping("postDetail")
    public String postDetail(@RequestParam("bno") int bno,
                             @RequestParam("page") int page,
                             HttpServletRequest request, HttpSession session,
                             BoardBean boardBean, MemberBean memberBean,
                             Model model) throws Exception {
        String id = (String) session.getAttribute("id");

        if(request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        boardBean = boardService.getDetail(bno);
        boardBean.setId(id);

        // 댓글 불러오기
        List<CommentBean> commentList = new ArrayList<CommentBean>();

        // 댓글 개수
        int commentCnt = commentService.countComment(bno);

        // 댓글 목록
        commentList = commentService.queryComment(bno);

        model.addAttribute("commentCnt", commentCnt);
        model.addAttribute("commentList", commentList);
        model.addAttribute("boardBean", boardBean);
        model.addAttribute("page", page);
        return "board/post_detail";
    }

    // 포스트 수정 양식
    @RequestMapping("postModifyForm")
    public String modifyForm(@RequestParam("bno") int bno,
                             @RequestParam("page") String page,
                             HttpSession session,
                             BoardBean boardBean, Model model) throws Exception{
        String id = (String) session.getAttribute("id");

        boardBean = boardService.getDetail(bno);
        boardBean.setId(id);
        model.addAttribute("board", boardBean);
        model.addAttribute("page", page);
        return "board/modify_form";
    }

    // 포스트 수정
    @RequestMapping(value = "postModify", method = RequestMethod.POST)
    public String postModify(MultipartHttpServletRequest multiRequest, HttpServletRequest request,
                             HttpSession session, Model model, @RequestParam("page") String page,
                             @ModelAttribute BoardBean boardBean) throws Exception {
        List<MultipartFile> fileList = multiRequest.getFiles("post_photo");
        String path = request.getRealPath("upload");
        System.out.println("path = " + path);

        StringBuilder files = new StringBuilder();
        int size = 0;

        for (MultipartFile mf : fileList) {
            String originFileName = mf.getOriginalFilename();

            int result = 0;
            size = (int) mf.getSize();

            if (size > 0) {
                String[] file = new String[2];
                StringTokenizer st = new StringTokenizer(originFileName, ".");
                file[0] = st.nextToken();
                file[1] = st.nextToken();

                if (size > 10000000) {

                    result = 1;
                    model.addAttribute("result", result);

                    return "member/upload_file";
                } else if (!file[1].equals("jpg") &&
                           !file[1].equals("jpeg") &&
                           !file[1].equals("gif") &&
                           !file[1].equals("png")) {
                    result = 2;
                    model.addAttribute("result", result);

                    return "member/upload_file";
                }
                System.out.println("originFileName = " + originFileName);
                String safeFile = System.currentTimeMillis() + originFileName;
                files.append(safeFile + ",");
                mf.transferTo(new File(path + "/" + safeFile));
            }
        }
        System.out.println("files = " + files.toString());
        String id = (String) session.getAttribute("id");

        // old file을 new file로 변경하기
        BoardBean old = boardService.getDetail(boardBean.getBno());
        if (size > 0) {
            boardBean.setPhoto(files.toString());
        } else {
            boardBean.setPhoto(old.getPhoto());
        }
        boardService.modifyPost(boardBean);

        return "redirect:/postDetail?bno=" + boardBean.getBno() + "&page=" + page;
    }

    // 포스트 삭제
    @RequestMapping(value = "postDelete", method = RequestMethod.POST)
    public String postDelete(@RequestParam("bno") int bno,
                             @RequestParam("page") int page,
                             Model model) throws Exception {
        BoardBean boardBean = boardService.getDetail(bno);
        boardService.deletePost(bno);
        return "redirect:/postList?page=" + page;

    }

    // 카테고리 분류
    @RequestMapping("categoryList")
    public String categoryList(HttpSession session, HttpServletRequest request,
                               BoardBean boardBean,
                               @RequestParam("category") String category, Model model) throws Exception {
        String postCategory = request.getParameter("category");
        if (postCategory != null)
            category = postCategory;

        List<BoardBean> categoryList = new ArrayList<BoardBean>();
        int page = 1;
        int limit = 6;

        if(request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        int categoryCount = boardService.categoryCount(category);
        categoryList = boardService.categoryList(category);

        int pageCount = categoryCount / limit + ((categoryCount % limit == 0) ? 0 : 1);
        // 페이지 네비게이션
        int startPage = ((page - 1) / 10) * limit + 1;
        int endPage = startPage + 10 - 1;

        if (endPage > pageCount)
            endPage = pageCount;

        model.addAttribute("page", page);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("categoryCount", categoryCount);
        model.addAttribute("categoryList", categoryList);

        return "board/post_list";
    }

    // 키워드 검색 하기
    @RequestMapping("searchKeyword")
    public String search(@RequestParam("keyword") String searchKeyword, Model model,
                         HttpServletRequest request, SearchCondition sc) throws Exception {
        List<BoardBean> searchList = new ArrayList<BoardBean>();

        String keyword = request.getParameter("keyword");
        if (searchKeyword != null)
            keyword = searchKeyword;

        int page = 1;
        int limit = 6;

        if (request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));

        sc.setKeyword(keyword);
        sc.setPage(page);
        sc.setLimit(limit);

        int offset = (page - 1) * limit;
        sc.setOffset(offset);

        int searchCount = boardService.searchResultCnt(sc);
        searchList = boardService.searchKeyword(sc);

        // 검색 결과 페이징 처리(페이지 수, 시작 페이지, 마지막 페이지)
        int pageCount = searchCount / limit + ((searchCount % limit == 0) ? 0 : 1);
        int startPage = ((page - 1) / 10) * limit + 1;
        int endPage = startPage + 10 - 1;

        if (endPage > pageCount)
            endPage = pageCount;

        model.addAttribute("page", page);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("searchCount", searchCount);
        model.addAttribute("searchList", searchList);
        model.addAttribute("keyword", keyword);

        return "search";
    }


}
