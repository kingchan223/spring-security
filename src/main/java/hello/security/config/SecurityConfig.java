package hello.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity//스프링 시큐리티 필터(아래의 SecurityConfig 클래스)가 스프링 필터 체인에 등록됨
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }


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
                .defaultSuccessUrl("/");//로그인에 성공하면 '/'로 이동한다.(그외로 요청했으면 로그인 후 거기로 보내준다.)
    }
}


