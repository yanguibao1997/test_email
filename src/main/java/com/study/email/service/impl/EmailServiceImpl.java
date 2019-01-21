package com.study.email.service.impl;

import com.study.email.pojo.Email;
import com.study.email.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
public class EmailServiceImpl implements EmailService {

    private Logger logger = LoggerFactory.getLogger(getClass());//提供日志类

    @Autowired
    private JavaMailSenderImpl mailSender;//注入邮件工具类

    /**
     * 发送邮件
     * @param email
     * @return
     */
    @Override
    public Email sendEmail(Email email) {
        try {
            checkEmail(email); //1.检测邮件
            sendMimeMail(email); //2.发送邮件
            return saveEmail(email); //3.保存邮件
        } catch (Exception e) {
            logger.error("发送邮件失败:", e);//打印错误信息
            email.setStatus("fail");
            email.setError(e.getMessage());
            return email;
        }
    }

    /**
     * 检查邮件
     * @param email
     */
    @Override
    public void checkEmail(Email email) {
        if (StringUtils.isEmpty(email.getTo())) {
            throw new RuntimeException("邮件收信人不能为空");
        }
        if (StringUtils.isEmpty(email.getSubject())) {
            throw new RuntimeException("邮件主题不能为空");
        }
        if (StringUtils.isEmpty(email.getText())) {
            throw new RuntimeException("邮件内容不能为空");
        }
    }

    /**
     * 构建复杂邮件信息类
     * @param email
     */
    @Override
    public void sendMimeMail(Email email) {
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);//true表示支持复杂类型
            email.setFrom(getEmailSendFrom());//邮件发信人从配置项读取
            messageHelper.setFrom(email.getFrom());//邮件发信人
            messageHelper.setTo(email.getTo().split(","));//邮件收信人
            messageHelper.setSubject(email.getSubject());//邮件主题
            messageHelper.setText(email.getText());//邮件内容
            if (!StringUtils.isEmpty(email.getCc())) {//抄送
                messageHelper.setCc(email.getCc().split(","));
            }
            if (!StringUtils.isEmpty(email.getBcc())) {//密送
                messageHelper.setCc(email.getBcc().split(","));
            }
            if (email.getMultipartFiles() != null) {//添加邮件附件
                for (MultipartFile multipartFile : email.getMultipartFiles()) {
                    messageHelper.addAttachment(multipartFile.getOriginalFilename(), multipartFile);
                }
            }
            if (StringUtils.isEmpty(email.getSentDate())) {//发送时间
                email.setSentDate(new Date());
                messageHelper.setSentDate(email.getSentDate());
            }
            mailSender.send(messageHelper.getMimeMessage());//正式发送邮件
            email.setStatus("ok");
            logger.info("发送邮件成功：{}->{}", email.getFrom(), email.getTo());
        } catch (Exception e) {
            throw new RuntimeException(e);//发送失败
        }
    }

    /**
     * 保存邮件
     * @param email
     * @return
     */
    @Override
    public Email saveEmail(Email email) {

        //将邮件保存到数据库..

        return email;
    }

    /**
     * 获得发件人
     * @return
     */
    @Override
    public String getEmailSendFrom() {
        return mailSender.getJavaMailProperties().getProperty("from");
    }
}
