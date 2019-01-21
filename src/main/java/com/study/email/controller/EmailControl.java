package com.study.email.controller;

import com.study.email.pojo.Email;
import com.study.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class EmailControl {
    @Autowired
    private EmailService emailService;


    /**
     * 发送邮件的主界面
     */
    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("mail/sendMail");//打开发送邮件的页面
        mv.addObject("from", emailService.getEmailSendFrom());//邮件发信人
        return mv;
    }


    /**
     * 发送邮件
     */
    @PostMapping("/mail/send")
    public Email sendMail(Email email, MultipartFile[] files) {
        email.setMultipartFiles(files);
        return emailService.sendEmail(email);
    }
}
