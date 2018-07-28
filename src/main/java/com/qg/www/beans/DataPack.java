package com.qg.www.beans;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataPack {
     @Expose  private String status;
     @Expose private Data data;
}
