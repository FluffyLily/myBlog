package com.renee.myBlog.service;

import com.renee.myBlog.dao.MemberDao;
import com.renee.myBlog.model.MemberBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    @Autowired
    private MemberDao memberDao;

    // 회원가입
    public void joinMember(MemberBean memberBean) throws Exception {
        memberDao.joinMember(memberBean);
    }

    // 회원정보 확인
    public MemberBean checkUser(String id) throws Exception {
        return memberDao.checkUser(id);
    }

    // 아이디 중복 확인
    public int checkId(String id) throws Exception {
        return memberDao.checkId(id);
    }

    // 이메일 중복 확인
    public MemberBean checkEmail(String email) throws Exception {
        return memberDao.checkEmail(email);
    }

    // 비밀번호 확인
    public MemberBean pwFind(MemberBean memberBean)throws Exception {
        return memberDao.pwFind(memberBean);
    }

    // 회원정보 수정하기
    public void updateUserInfo(MemberBean memberBean) throws Exception {
        memberDao.updateUserInfo(memberBean);
    }
}
