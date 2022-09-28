package com.renee.myBlog.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@Alias("board")
public class BoardBean {
    private Integer bno;
    private String title;
    private String content;
    private String photo;
    private Date reg_date;
    private Integer read_count;
    private String id;
    private String category;
}
