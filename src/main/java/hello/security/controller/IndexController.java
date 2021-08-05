package hello.security.controller;

import hello.security.config.auth.PrincipalDetails;
import hello.security.model.User;
import hello.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@Controller("/")
public class IndexController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @GetMapping
    public String index(@AuthenticationPrincipal PrincipalDetails principal){
        if(principal!=null)
            log.info("로그인 사용자 ID:{}", principal.getUsername());
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

    @Secured("ROLE_ADMIN")
    @ResponseBody
    @GetMapping("/info")
    public String info(){
        return "개인정보";
    }

    //@PreAuthorize : 아래 컨트롤러 호출전에 실행됨. 여러 ROLE을 설정가능.
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @ResponseBody
    @GetMapping("/data")
    public String data(){
        return "개인정보";
    }
}
