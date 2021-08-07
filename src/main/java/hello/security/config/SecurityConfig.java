package hello.security.config;

import hello.security.config.auth.PrincipalDetailsService;
import hello.security.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity//스프링 시큐리티 필터(아래의 SecurityConfig 클래스)가 스프링 필터 체인에 등록됨
@Configuration
//@EnableGlobalMethodSecurity:: securedEnabled = true : secure어노테이션 활성화, prePostEnabled = true : preAuthorize어노테이션 활성화
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalDetailsService principalDetailsService;
    private final PrincipalOauth2UserService principalOauth2UserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

//    @Scope("prototype")
//    @Bean
//    public BCryptPasswordEncoder encodePwd(){
//        return new BCryptPasswordEncoder();
//    }

    //시큐리티가 대신 로그인 해주는데, 그때 물론 password가 필요함.
    //근데 password가 뭘로 해쉬되었는 알아야 스프링이 해쉬화를 통해 회원가입을 해줄 수 있음
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailsService).passwordEncoder(bCryptPasswordEncoder);
//        auth.authenticationProvider(authProvider);
    }//principalDetailsService를 안넣어주면 패스워드 비교를 못한다.

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()//인증만 되면 들어갈 수 있는 주소
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()//위의 antMatchers들을 제외하고 나머지 경로는 접근 허용
                .and()
                .formLogin()
                .loginPage("/loginForm")//권한이 없는 사람이 권한이 필요한 요청을 하면 login으로 보내버림
                .usernameParameter("username")//PrincipalDetailsService의 username과 일치하는지 확인
                .loginProcessingUrl("/login")// : '/login'이 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
                .defaultSuccessUrl("/")//로그인에 성공하면 '/'로 이동한다.(그외로 요청했으면 로그인 후 거기로 보내준다.)
                .and()
                .oauth2Login()
                .loginPage("/loginForm")//구글 로그인이 완료된 후의 처리가 필요.
                .defaultSuccessUrl("/user")
                // 1.코드 받기(인증됨)
                // 2.엑세스 토큰(권한)
                // 3.사용자 프로필 정보 가져옴
                // 4.그 정보로 회원가입 자동으로 시키기도함 또는 더 추가적인 정보를 얻고
                .userInfoEndpoint()    // Tip: 사실 구글 로그인이 완료되면 코드를 받는 것이 아니라 <액세스 토큰+사용사 프로필 정보>를 한방에 받는다.
                .userService(principalOauth2UserService);
    }
}


