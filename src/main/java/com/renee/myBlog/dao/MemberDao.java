package com.renee.myBlog.dao;

import com.renee.myBlog.model.MemberBean;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    @Autowired
    private SqlSession sqlSession;

    // 회원가입
    public void joinMember(MemberBean memberBean) throws Exception {
        sqlSession.insert("memberns.join", memberBean);
    }

    // 회원정보 확인
    public MemberBean checkUser(String id) throws Exception {
        return (MemberBean) sqlSession.selectOne("memberns.getMember", id);
    }

    // 아이디 중복 확인
    public int checkId(String id) throws Exception {
        int result = -1;
        MemberBean memberBean = sqlSession.selectOne("memberns.getMember", id);
        if (memberBean != null)
            result = 1;
        return result;
    }

    // 이메일 중복 확인
    public MemberBean checkEmail(String email) throws Exception {
        return (MemberBean) sqlSession.selectOne("memberns.checkEmail", email);
    }

    // 비밀번호 확인
    public MemberBean pwFind(MemberBean memberBean) throws Exception {
        return (MemberBean) sqlSession.selectOne("memberns.pwFind", memberBean);
    }

    // 회원정보 수정
    public void updateUserInfo(MemberBean member) throws Exception {
        sqlSession.update("memberns.update_user_info", member);
    }
}
