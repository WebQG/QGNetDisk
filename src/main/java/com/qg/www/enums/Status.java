package com.qg.www.enums;
/**
 * @author zenghuachen
 * @version  1.3
 * 返回状态码枚举类；
 */
public enum  Status {
    /**
     * 一切正常
     */
    NORMAL("200"),
    /**
     * 邮箱已注册
     */
    EMIAL_ISREGISTER("501"),
    /**
     * 验证码错误
     */
    VERIFYCODE_WROSE("502"),
    /**
     * 邮箱(用户名)不存在
     */
    EMAIL_NOEXIST("503"),
    /**
     * 密码与邮箱(用户名)不匹配
     */
    PASSWORD_WROSE("504"),
    /**
     * 文件删除失败
     */
    FILE_DELETE_WROSE("505"),
    /**
     * 文件名为空
     */
    FILE_NAME_ISEMPTY("506"),
    /**
     * 没有权限
     */
    STATUS_NO("507"),
    /**
     * 信息更改失败
     */
    INFO_CHANGE_WROSE("508");

    private String status;
    Status(String status){
        this.status = status;
    }
    public String getStatus(){
        return status;
    }
}