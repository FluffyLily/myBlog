package com.renee.myBlog.service;

import com.renee.myBlog.dao.CommentDao;
import com.renee.myBlog.model.CommentBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;

    // 댓글 쓰기
    public void setComment(CommentBean comment) throws Exception {
        commentDao.setComment(comment);
    }

    // 댓글 불러오기
    public List<CommentBean> queryComment(int bno) throws Exception {
        return commentDao.queryComment(bno);

    }

    // 댓글 삭제하기
    public void deleteComment(int rno) throws Exception {
        commentDao.deleteComment(rno);
    }

    // 댓글 개수 카운트
    public int countComment(int bno) throws Exception {
        return commentDao.countComment(bno);
    }
}
