package com.renee.myBlog.service;

import com.renee.myBlog.dao.BoardDao;
import com.renee.myBlog.model.BoardBean;
import com.renee.myBlog.model.SearchCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardDao boardDao;

    // 포스트 작성
    public void writePost(BoardBean boardBean) throws Exception {
        boardDao.writePost(boardBean);
    }

    // 포스트 수정
    public void modifyPost(BoardBean boardBean) throws Exception {
        boardDao.modifyPost(boardBean);
    }

    // 포스트 삭제
    public void deletePost(int bno) throws Exception {
        boardDao.deletePost(bno);
    }

    // 포스트 상세 페이지
    public BoardBean getDetail(int bno) throws Exception {
        BoardBean boardBean = boardDao.getDetail(bno);
        return boardBean;
    }

    // 메인페이지 포스트 목록
    public List<BoardBean> postList(int offset) throws Exception {
        return boardDao.postList(offset);
    }

    // 포스트 개수
    public int getCount() throws Exception {
        return boardDao.getCount();
    }

    // 카테고리별 포스트 목록
    public List<BoardBean> categoryList(String category) throws Exception {
        return boardDao.categoryList(category);
    }

    // 카테고리별 포스트 개수
    public int categoryCount(String category) throws Exception {
        return boardDao.categoryCount(category);
    }

    // 키워드 검색 하기
    public List<BoardBean> searchKeyword(SearchCondition sc) throws Exception {
        return boardDao.searchKeyword(sc);
    }

    // 키워드 검색 결과 개수
    public int searchResultCnt(SearchCondition sc) throws Exception {
        return boardDao.searchResultCnt(sc);
    }
}
