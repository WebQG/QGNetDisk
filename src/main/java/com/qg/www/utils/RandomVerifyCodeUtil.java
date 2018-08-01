package com.qg.www.utils;

import java.util.Random;

/**
 * 生成随机的四位验证码；
 * @author net
 * @version 1.0
 */
public class RandomVerifyCodeUtil {
    public static String getVerifyCode(){
        Random random=new Random();
        return ""+random.nextInt(9)+random.nextInt(9)+random.nextInt(9)+random.nextInt(9);
    }

}
