package com.room7.moneygement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class QnAViewController {

    @GetMapping("/qnachat")
    public String showQnaPage() {
        return "main/qnachat"; // qnachat.html 파일 이름과 동일해야 합니다.
    }
}
