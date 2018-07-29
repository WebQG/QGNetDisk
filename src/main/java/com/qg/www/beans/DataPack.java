package com.qg.www.beans;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

/**
 * @author net
 * @version 1.1
 * 数据包装类
 */
@Getter
@Setter
public class DataPack {
    @Expose
    private String status;
    @Expose
    private Data data;
}
