package hello.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("/")
public class IndexController {

    @GetMapping
    @ResponseBody
    public String index(){
        return "index";
    }

    @GetMapping("user")
    @ResponseBody
    public String user(){
        return "user";
    }

    @GetMapping("admin")
    @ResponseBody
    public String admin(){
        return "admin";
    }

    @GetMapping("manager")
    @ResponseBody
    public String manager(){
        return "manager";
    }

    @GetMapping("login")
    @ResponseBody
    public String login(){
        return "login";
    }

    @GetMapping("join")
    @ResponseBody
    public String join(){
        return "join";
    }

    @ResponseBody
    @GetMapping("joinProc")
    public String joinProc(){
        return "회원가입 완료됨";
    }
}
