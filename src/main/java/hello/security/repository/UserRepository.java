package hello.security.repository;

import hello.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

//JpaRepository가 기본적인 CRUD함수를 들고있음
//@Repository가 없어도 IOC가 된다. JpaRepository를 상속했기 때문.
public interface UserRepository extends JpaRepository<User, Integer> {

    // select * from user where username = 1?
    public User findByUsername(String username);

    // select * from user where email = 1?
    public User findByEmail(String email);
}
