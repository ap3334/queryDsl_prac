package com.example.qsl.user.repository;

import com.example.qsl.user.entity.SiteUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원 2명 생성")
    void t1() {

        SiteUser u3 = SiteUser.builder()
                .username("user3")
                .password("{noop}1234")
                .email("user3@test.com")
                .build();

        SiteUser u4 = SiteUser.builder()
                .username("user4")
                .password("{noop}1234")
                .email("user4@test.com")
                .build();

        userRepository.saveAll(Arrays.asList(u3, u4));
    }

    @Test
    @DisplayName("1번 회원을 querydsl로 가져오기")
    void t2() {

        SiteUser u1 = userRepository.getQslUser(1L);

        assertThat(u1.getId()).isEqualTo(1L);
        assertThat(u1.getUsername()).isEqualTo("user1");
        assertThat(u1.getPassword()).isEqualTo("{noop}1234");
        assertThat(u1.getEmail()).isEqualTo("user1@test.com");

    }

    @Test
    @DisplayName("2번 회원을 querydsl로 가져오기")
    void t3() {

        SiteUser u1 = userRepository.getQslUser(2L);

        assertThat(u1.getId()).isEqualTo(2L);
        assertThat(u1.getUsername()).isEqualTo("user2");
        assertThat(u1.getPassword()).isEqualTo("{noop}1234");
        assertThat(u1.getEmail()).isEqualTo("user2@test.com");

    }
}