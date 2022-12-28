package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller  //View를 return 하겠다
public class IndexController {

    @GetMapping({"", "/"})
    public String index() {
        // 머스테치 기본 폴더 src/main/resources/
        // 뷰리졸버 설정 : templates(prefix), .mustache(suffix) 생략 가능 !!
        return "index";   // src/main/resources/templates/index.mustache
    }

}
