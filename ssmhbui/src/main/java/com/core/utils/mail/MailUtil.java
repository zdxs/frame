package com.core.utils.mail;

import com.core.utils.mail.mailbean.MailBean;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

/**
 * 实现邮件发送
 *
 * @author xiaosun
 * @version 1.0
 */
public class MailUtil {

    protected final Logger logger = Logger.getLogger(getClass());

    public boolean send(MailBean mail) {
        // 发送email
        HtmlEmail email = new HtmlEmail();
        try {
            // 这里是SMTP发送服务器的名字：163的如下："smtp.163.com"
            email.setHostName(mail.getHost());
            // 字符编码集的设置
            email.setCharset(MailBean.ENCODEING);
            // 收件人的邮箱
            email.addTo(mail.getReceiver());
            // 发送人的邮箱
            email.setFrom(mail.getSender(), mail.getName());
            // 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码
            email.setAuthentication(mail.getUsername(), mail.getPassword());
            // 要发送的邮件主题
            email.setSubject(mail.getSubject());
            // 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
            email.setMsg(mail.getMessage());
            // 发送
            email.send();
            if (logger.isDebugEnabled()) {
                logger.debug(mail.getSender() + " 发送邮件到 " + mail.getReceiver());
            }
            return true;
        } catch (EmailException e) {
            e.printStackTrace();
            logger.info(mail.getSender() + " 发送邮件到 " + mail.getReceiver()
                    + " 失败");
            return false;
        }
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        MailBean mail = new MailBean();
        mail.setHost("smtp.163.com"); // 设置邮件服务器,如果不用163的,自己找找看相关的
        mail.setSender("17008258884@163.com");
        mail.setReceiver("552011781@qq.com"); // 接收人
        mail.setUsername("17008258884@163.com"); // 登录账号,一般都是和邮箱名一样吧
        mail.setPassword("szq123123com"); // 发件人邮箱的登录密码
        mail.setSubject("呵呵");
        mail.setMessage("您好<br></hr><span><b>测试下</b></span><hr/><img src='https://ss3.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/super/crop%3D0%2C0%2C966%2C604/sign=f2ca40fd0b3b5bb5aa987abe0be3f909/2934349b033b5bb5131461a83ed3d539b700bca1.jpg'>");
        System.out.println(new MailUtil().send(mail));
    }
}
