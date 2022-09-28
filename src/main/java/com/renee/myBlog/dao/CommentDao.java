package com.renee.myBlog.dao;

import com.renee.myBlog.model.CommentBean;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentDao {

    @Autowired
    private SqlSession sqlSession;

    // 댓글 쓰기
    public void setComment(CommentBean comment) throws Exception {
        sqlSession.insert("commentns.insertComment", comment);
    }

    // 댓글 불러오기
    public List<CommentBean> queryComment(int bno) throws Exception {
        List<CommentBean> commentList = sqlSession.selectList("commentns.queryComment", bno);
        return commentList;
    }

    // 댓글 삭제하기
    public void deleteComment(int rno) throws Exception {
        sqlSession.delete("commentns.deleteComment", rno);
    }

    // 댓글 개수 카운트
    public int countComment(int bno) throws Exception {
        int count = 0;
        count = sqlSession.selectOne("commentns.countComment", bno);
        return count;
    }

}

