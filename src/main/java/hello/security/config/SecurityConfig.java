package hello.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity//스프링 시큐리티 필터(아래의 SecurityConfig 클래스)가 스프링 필터 체인에 등록됨
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()//접근 막기(권한이 있어야 접근가능)
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()//위의 antMatchers들을 제외하고 나머지 경로는 접근 허용
                .and()
                .formLogin()
                .loginPage("/login");//권한이 없는 사람이 권한이 필요한 요청을 하면 login으로 보내버림
    }
}
