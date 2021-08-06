package hello.security.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // 구글로 부터 받은 userRequest데이터에 대한 후처리 되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration = " + userRequest.getClientRegistration());//registraionId로 어떤 OAuth로 로그인 했는지 알 수 있음
        System.out.println("getAccessToken = " + userRequest.getAccessToken().getTokenValue());
        //구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인을 완료 -> code를 리턴(Oauth-Client라이브러리가 받음) -> AccessToken을 요청 ->
        // userRequest에 AccessToken들어옴 -> userRequest에 있는 정보로 회원 프로필을 받는다. 그 때 사용되는 함수가 loadUser => 회원 프로필 얻음
        System.out.println("getRegistrationId = " + userRequest.getClientRegistration().getRegistrationId());
        System.out.println("super.loadUser(userRequest).getAttributes() = " + super.loadUser(userRequest).getAttributes());

        OAuth2User oauth2User = super.loadUser(userRequest);

        return super.loadUser(userRequest);
    }
}
