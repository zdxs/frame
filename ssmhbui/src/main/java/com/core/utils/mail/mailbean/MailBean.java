package com.core.utils.mail.mailbean;

/**
 * 邮件发送基础类
 *
 * @author xiaosun
 * @version 1.0
 */
public class MailBean {

    public static final String ENCODEING = "UTF-8";

    private String host; // 服务器地址

    private String sender; // 发件人的邮箱

    private String receiver; // 收件人的邮箱

    private String name; // 发件人昵称

    private String affix; // 附件地址

    private String affixName; // 附件名称

    private String username; // 账号

    private String password; // 密码

    private String subject; // 主题

    private String message; // 信息(支持HTML)

    private String ischeck;// 是否ssl验证0或者空否，1是

    public MailBean(String host, String sender, String receiver, String name,
            String affix, String affixName, String username, String password,
            String subject, String message, String ischeck) {
        super();
        this.host = host;
        this.sender = sender;
        this.receiver = receiver;
        this.name = name;
        this.affix = affix;
        this.affixName = affixName;
        this.username = username;
        this.password = password;
        this.subject = subject;
        this.message = message;
        this.ischeck = ischeck;
    }

    public String getAffix() {
        return affix;
    }

    public void setAffix(String affix) {
        this.affix = affix;
    }

    public String getAffixName() {
        return affixName;
    }

    public void setAffixName(String affixName) {
        this.affixName = affixName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }

    public static String getEncodeing() {
        return ENCODEING;
    }

    public MailBean(String host, String sender, String receiver, String name,
            String username, String password, String subject, String message,
            String ischeck) {
        super();
        this.host = host;
        this.sender = sender;
        this.receiver = receiver;
        this.name = name;
        this.username = username;
        this.password = password;
        this.subject = subject;
        this.message = message;
        this.ischeck = ischeck;
    }

    public MailBean() {
        super();
        // TODO Auto-generated constructor stub
    }

}
