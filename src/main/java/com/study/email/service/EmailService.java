package com.study.email.service;

import com.study.email.pojo.Email;

public interface EmailService {
    //发送邮件
    public Email sendEmail(Email email);

    //检测邮件信息类
    public void checkEmail(Email email);

    //构建复杂邮件信息类
    public void sendMimeMail(Email email);

    //保存邮件
    public Email saveEmail(Email email);

    //获取邮件发信人
    public String getEmailSendFrom();

}
