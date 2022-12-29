package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository 라는 어노테이션이 없어도 IOC 됨. JpaRepository를 상속했기때문에 자동으로 빈 등록이 됨.
public interface UserRepository extends JpaRepository<User, Integer> {

    //select * from user where username = ?
    public User findByUsername(String username);
}
