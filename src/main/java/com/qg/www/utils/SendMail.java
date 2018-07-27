package com.qg.www.utils;

import java.util.Properties;
import com.sun.mail.util.MailSSLSocketFactory;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Transport;
import javax.mail.Address;
/**
 * 发送邮箱验证码；
 * @author net
 * @version 1.0
 */
public class SendMail {
    /**
     * 邮箱验证码发送；
     * @param email
     * @param verfyCode
     */
    public static void  sendMail(String email,String verfyCode) throws Exception{
        Properties props = new Properties();
        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.exmail.qq.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");
        //ssl加密；
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);
        Session session = Session.getInstance(props);
        Message msg = new MimeMessage(session);
        //设置邮件标题；
        msg.setSubject("QG云盘");
        StringBuilder builder = new StringBuilder();
        //邮件内容；
        builder.append("\nWelcome to QG NetDisk！" + "Your verifyCode is ：" + verfyCode);
        msg.setText(builder.toString());
        msg.setFrom(new InternetAddress("3117004845@mail2.gdut.edu.cn"));
        //获取传送；
        Transport transport = session.getTransport();
        transport.connect("smtp.exmail.qq.com", "3117004845@mail2.gdut.edu.cn", "V2r5NkgzDPNtq3i8");
        //传送到用户邮箱；
        transport.sendMessage(msg, new Address[]{new InternetAddress(email)});
        transport.close();
    }
}
