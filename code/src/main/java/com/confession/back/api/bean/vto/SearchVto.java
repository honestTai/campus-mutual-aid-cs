package com.confession.back.api.bean.vto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SearchVto {

    private Integer pageSize;

    private Integer pageIndex;

    private Integer type;

    private List<String> dateList;

    private Integer id;

    private String likeString;

    private Integer delType;

    private Integer userId;
}
