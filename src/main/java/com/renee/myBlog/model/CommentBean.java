package com.renee.myBlog.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@Alias("comment")
public class CommentBean {
    private int rno;
    private String commenter;
    private String content;
    private String photo;
    private Date reg_date;
    private String email;
    private String sns;
    private int like;
    private int bno;
}
