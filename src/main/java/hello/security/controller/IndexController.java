package hello.security.controller;

import hello.security.model.User;
import hello.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller("/")
public class IndexController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @GetMapping
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

    @GetMapping("loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("joinForm")
    public String joinForm(){
        return "joinForm";
    }


    @PostMapping("join")
    public String join(@ModelAttribute User user){
        System.out.println("user = " + user);
        user.setRole("ROLE_USER");
        String rawPw = user.getPassword();
        String encPw = bCryptPasswordEncoder.encode(rawPw);
        user.setPassword(encPw);
        userRepository.save(user);
        return "redirect:/loginForm";
    }
}
