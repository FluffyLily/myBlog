package com.renee.myBlog.service;

import com.renee.myBlog.dao.MainDao;
import com.renee.myBlog.model.BoardBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainService {

    @Autowired
    private MainDao mainDao;

    // 포스트 목록 불러오기
    public List<BoardBean> getPostList() throws Exception {
        return mainDao.getPostList();
    }

}
