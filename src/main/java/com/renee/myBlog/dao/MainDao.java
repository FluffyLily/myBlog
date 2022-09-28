package com.renee.myBlog.dao;

import com.renee.myBlog.model.BoardBean;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MainDao {

    @Autowired
    private SqlSession sqlSession;
    public List<BoardBean> getPostList() throws Exception{
        List<BoardBean> blogList = sqlSession.selectList("mainns.getPostList");
        return blogList;
    }
}
