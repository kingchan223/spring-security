package hello.security.config.auth;

import hello.security.model.User;
import hello.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//시큐리티 설정에서 loginProcessingUrl("/login");
// '/login' 요청이 오면 자동으로 UserDetailsService 타입으로 ioc되어 있는 loadUserByUsername함수가 실행된다.
// 스프링이 로그인 요청을 가로챌 때, username, password 2개를 가로채는데
// password부분은 알아서 처라하고,
// username이 DB에 있는지만 확인해주면 된다.
@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override//요 아래에 username이랑 html form post에서 보내지는 username이랑 일치해야한다.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username)
                .orElseThrow(()->{return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. :"+username);});
        if(userEntity!=null){
            return new PrincipalDetails(userEntity);//이렇게 반환하면 Authentications내부에 UserDetails가 들어가게 된다.
            //그리고 시큐리티 session에 Authentications(UserDetails)가 들어가게 된다. (PrincipalDetails주석 참고)
            //즉 시큐리티 세션에 유저 정보가 저장이 된다.
        }
        return null;
    }
}
