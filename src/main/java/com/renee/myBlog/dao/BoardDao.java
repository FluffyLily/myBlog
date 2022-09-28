package com.renee.myBlog.dao;

import com.renee.myBlog.model.BoardBean;
import com.renee.myBlog.model.SearchCondition;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardDao {

    @Autowired
    private SqlSession sqlSession;

    // 포스트 작성
    public void writePost(BoardBean boardBean) throws Exception {
        sqlSession.insert("boardns.write", boardBean);
    }

    // 포스트 수정
    public void modifyPost(BoardBean boardBean) throws Exception {
        sqlSession.update("boardns.modify", boardBean);
    }

    // 포스트 삭제
    public void deletePost(int bno) throws Exception {
        sqlSession.delete("boardns.delete", bno);
    }

    // 포스트 상세 페이지
    public BoardBean getDetail(int bno) throws Exception {
        return sqlSession.selectOne("boardns.getDetail", bno);
    }

    // 메인 페이지 포스트 목록
    public List<BoardBean> postList(int offset) throws Exception {
        List<BoardBean> postList = sqlSession.selectList("boardns.postList", offset);
        return postList;
    }

    // 포스트 개수
    public int getCount() throws Exception {
        int listCount = 0;
        listCount = sqlSession.selectOne("boardns.getCount");
        return listCount;
    }

    // 카테고리별 포스트 목록
    public List<BoardBean> categoryList(String category) throws Exception {
        List<BoardBean> categoryList = sqlSession.selectList("boardns.categoryList", category);
        return categoryList;
    }

    // 카테고리별 포스트 개수
    public int categoryCount(String category) throws Exception {
        int categoryCount = 0;
        categoryCount = sqlSession.selectOne("boardns.categoryCount", category);
        return categoryCount;
    }

    // 키워드 검색 하기
    public List<BoardBean> searchKeyword(SearchCondition sc) throws Exception {
        List<BoardBean> searchList = sqlSession.selectList("boardns.searchKeyword", sc);
        return searchList;
    }

    // 키워드 검색 결과 개수
    public int searchResultCnt(SearchCondition sc) throws Exception {
        int searchCnt = 0;
        searchCnt = sqlSession.selectOne("boardns.searchResultCnt", sc);
        return searchCnt;
    }
}
