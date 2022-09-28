package com.renee.myBlog.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@Alias("member")
public class MemberBean {
    private String id;
    private String pw;
    private String name;
    private String tel;
    private String email;
    private String sns;
    private String photo;
    private Date reg_date;
    private String quit;
}
