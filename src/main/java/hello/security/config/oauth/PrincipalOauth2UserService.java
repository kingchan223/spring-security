package hello.security.config.oauth;

import hello.security.config.auth.PrincipalDetails;
import hello.security.config.auth.provider.FaceBookUserInfo;
import hello.security.config.auth.provider.GoogleUserInfo;
import hello.security.config.auth.provider.OAuth2UserInfo;
import hello.security.model.User;
import hello.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    // 구글로 부터 받은 userRequest데이터에 대한 후처리 되는 함수
    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        System.out.println("oauth2User.getAttributes() = " + oauth2User.getAttributes());
        /* 구글인지 페이스북인지 구분하여 oAuth2UserInfo 객체를 다르게 처리해준다. */
        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());

        }else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            oAuth2UserInfo = new FaceBookUserInfo(oauth2User.getAttributes());
            System.out.println("페이스북 로그인 요청");

        }else{
            System.out.println("구글과 페이스북만 지원합니다.");
        }
        /*구글에서 제공한 데이터로 강제로 회원가입 진행하기*/
        String provider = oAuth2UserInfo.getProvider(); //google
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider+"_"+providerId;
        String email = oAuth2UserInfo.getEmail(); //google
        String password = bCryptPasswordEncoder.encode("welcomeTomyhome");
        String role = "ROLE_USER";
        //혹시 이미 회원가입이 되었다면 회원가입 시키지 않음
        Optional<User> finduser = userRepository.findByUsername(username);
        if(finduser.isPresent()){
            System.out.println("구글 로그인을 이미 한적이 있아, 자동 회원가입을 하신 적이 있습니다.");
            User olduser = finduser.get();
            return new PrincipalDetails(olduser);
        }else{
            System.out.println("구글 로그인을 최초로 하셨습니다.");
            User newuser = User.createUser(username, email, password, role, provider, providerId);
            userRepository.save(newuser);
            return new PrincipalDetails(newuser, oauth2User.getAttributes());
        }
    }
}
