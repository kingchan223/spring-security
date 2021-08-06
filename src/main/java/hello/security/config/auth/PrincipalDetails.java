package hello.security.config.auth;

// 시큐리티가 /login을 낚아채서 로그인을 진행한다.
// 이때 로그인 진행이 완료되면 시큐리티 session을 만들어준다. (Security ContextHolder)
// 이때 이 세션에 들어갈 수 있는 오브젝트는 정해져 있다. -> Authentication 객체
// Authentication 객체 안에는 User 정보가 있어야 한다.
// User 오브젝트 타입은 -> UserDetatils타입 객체이어야한다.
// :: Security Session => Authentication => UserDetails(PrincipalDetails)

import hello.security.model.User;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
//principal-접근주체
public class PrincipalDetails implements UserDetails, OAuth2User {//접근주체를 구체화한다.

    private User user;//콤포지션
    public PrincipalDetails(User user){
        this.user = user;
    }

    /* UserDetails */

    //해당 User의 권한을 반환하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
//        collect.add(new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                return user.getRole();
//            }
//        });
        //위의 6줄 자리와 같음
        collect.add(()->{return user.getRole();});
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        //1년 동안 로그인 안한 회원이 있다면 휴먼 계정으로 변경
        //현재 시간 - 로그인 시간
        return true;
    }

    /* OAuth2User */

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
