package com.example.qsl.user.repository;

import com.example.qsl.user.entity.SiteUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원 2명 생성")
    void t1() {

        SiteUser u9 = SiteUser.builder()
                .username("user9")
                .password("{noop}1234")
                .email("user9@test.com")
                .build();

        SiteUser u10 = SiteUser.builder()
                .username("user10")
                .password("{noop}1234")
                .email("user10@test.com")
                .build();

        userRepository.saveAll(Arrays.asList(u9, u10));
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

    @Test
    @DisplayName("전체 회원 수")
    void t4() {

        long count = userRepository.getQslCount();

        assertThat(count).isGreaterThan(0);

    }

    @Test
    @DisplayName("가장 오래된 회원 1명")
    void t5() {
        SiteUser u1 = userRepository.getQslUserOrderByIdAscOne();

        assertThat(u1.getId()).isEqualTo(1L);
        assertThat(u1.getUsername()).isEqualTo("user1");
        assertThat(u1.getPassword()).isEqualTo("{noop}1234");
        assertThat(u1.getEmail()).isEqualTo("user1@test.com");
    }

    @Test
    @DisplayName("가장 오래된 순으로")
    void t6() {
        List<SiteUser> siteUsers = userRepository.getQslUsersOrderById();

        SiteUser u1 = siteUsers.get(0);

        assertThat(u1.getId()).isEqualTo(1L);
        assertThat(u1.getUsername()).isEqualTo("user1");
        assertThat(u1.getPassword()).isEqualTo("{noop}1234");
        assertThat(u1.getEmail()).isEqualTo("user1@test.com");

        SiteUser u2 = siteUsers.get(1);

        assertThat(u2.getId()).isEqualTo(2L);
        assertThat(u2.getUsername()).isEqualTo("user2");
        assertThat(u2.getPassword()).isEqualTo("{noop}1234");
        assertThat(u2.getEmail()).isEqualTo("user2@test.com");
    }

    @Test
    @DisplayName("검색, List 리턴")
    void t7() {
        // 검색대상 : username, email
        // user1 로 검색
        List<SiteUser> users = userRepository.searchQsl("user1");

        assertThat(users.size()).isEqualTo(1);

        SiteUser u = users.get(0);

        assertThat(u.getId()).isEqualTo(1L);
        assertThat(u.getUsername()).isEqualTo("user1");
        assertThat(u.getEmail()).isEqualTo("user1@test.com");
        assertThat(u.getPassword()).isEqualTo("{noop}1234");

        // user2 로 검색
        users = userRepository.searchQsl("user2");

        assertThat(users.size()).isEqualTo(1);

        u = users.get(0);

        assertThat(u.getId()).isEqualTo(2L);
        assertThat(u.getUsername()).isEqualTo("user2");
        assertThat(u.getEmail()).isEqualTo("user2@test.com");
        assertThat(u.getPassword()).isEqualTo("{noop}1234");
    }

    @Test
    @DisplayName("검색, Page 리턴, pageSize = 1, page = 0")
    void t8() {
        long totalCount = userRepository.count();
        int pageSize = 1; // 한 페이지에 보여줄 아이템 개수
        int totalPage = (int) Math.ceil(totalCount / (double)pageSize);
        int page = 1;
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("id"));
        Pageable pageable = PageRequest.of(1, pageSize, Sort.by(sorts)); // 한 페이지에 10까지 가능
        Page<SiteUser> usersPage = userRepository.searchQsl("user", pageable);

        assertThat(usersPage.getTotalElements()).isEqualTo(totalPage);
        assertThat(usersPage.getNumber()).isEqualTo(page);
        assertThat(usersPage.getSize()).isEqualTo(pageSize);

        List<SiteUser> users = usersPage.get().toList();

        assertThat(users.size()).isEqualTo(pageSize);

        SiteUser u = users.get(0);

        assertThat(u.getId()).isEqualTo(2L);
        assertThat(u.getUsername()).isEqualTo("user2");
        assertThat(u.getEmail()).isEqualTo("user2@test.com");
        assertThat(u.getPassword()).isEqualTo("{noop}1234");

    }

    @Test
    @DisplayName("검색, Page 리턴, id DESC, pageSize=1, page=0")
    void t9() {
        long totalCount = userRepository.count();
        int pageSize = 1; // 한 페이지에 보여줄 아이템 개수
        int totalPages = (int) Math.ceil(totalCount / (double) pageSize);
        int page = 1;
        String kw = "user";

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts)); // 한 페이지에 10까지 가능
        Page<SiteUser> usersPage = userRepository.searchQsl(kw, pageable);

        assertThat(usersPage.getTotalPages()).isEqualTo(totalPages);
        assertThat(usersPage.getNumber()).isEqualTo(page);
        assertThat(usersPage.getSize()).isEqualTo(pageSize);

        List<SiteUser> users = usersPage.get().toList();

        assertThat(users.size()).isEqualTo(pageSize);
        SiteUser u = users.get(0);

        assertThat(u.getId()).isEqualTo(7L);
        assertThat(u.getUsername()).isEqualTo("user7");
        assertThat(u.getEmail()).isEqualTo("user7@test.com");
        assertThat(u.getPassword()).isEqualTo("{noop}1234");
    }

    @Test
    @DisplayName("회원에게 관심사를 등록할 수 있다.")
    void t10() {
        SiteUser u2 = userRepository.getQslUser(2L);

        u2.addInterestKeywordContent("축구");
        u2.addInterestKeywordContent("롤");
        u2.addInterestKeywordContent("헬스");
        u2.addInterestKeywordContent("헬스"); // 중복등록은 무시

        userRepository.save(u2);
        // 엔티티클래스 : InterestKeyword(interest_keyword 테이블)
        // 중간테이블도 생성되어야 함, 힌트 : @ManyToMany
        // interest_keyword 테이블에 축구, 롤, 헬스에 해당하는 row 3개 생성
    }


    @Test
    @DisplayName("축구에 관심이 있는 회원들 검색")
    void t11() {

        List<SiteUser> users = userRepository.getQslUserByInterestKeyword("축구");

        assertThat(users.size()).isEqualTo(1);

        SiteUser u = users.get(0);

        assertThat(u.getId()).isEqualTo(1L);
        assertThat(u.getUsername()).isEqualTo("user1");
        assertThat(u.getEmail()).isEqualTo("user1@test.com");
        assertThat(u.getPassword()).isEqualTo("{noop}1234");

    }

    @Test
    @DisplayName("no sql, 축구에 관심이 있는 회원들 검색")
    void t12() {

        List<SiteUser> users = userRepository.findByInterestKeywords_content("축구");

        assertThat(users.size()).isEqualTo(1);

        SiteUser u = users.get(0);

        assertThat(u.getId()).isEqualTo(1L);
        assertThat(u.getUsername()).isEqualTo("user1");
        assertThat(u.getEmail()).isEqualTo("user1@test.com");
        assertThat(u.getPassword()).isEqualTo("{noop}1234");

    }

    @Test
    @DisplayName("u2=아이돌, u1=팬 u1은 u2의 팔로워이다.")
    void t13() {

        SiteUser u1 = userRepository.getQslUser(1L);
        SiteUser u2 = userRepository.getQslUser(2L);

        u2.follow(u1);

        userRepository.save(u2);

    }

    @Test
    @DisplayName("본인은 본인을 follow할 수 없다.")
    void t14() {

        SiteUser u1 = userRepository.getQslUser(1L);

        u1.follow(u1);

        assertThat(u1.getFollowers().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("특정 회원의 followers들과 following들을 모두 알 수 있어야 한다.")
    void t15() {

        SiteUser u1 = userRepository.getQslUser(1L);
        SiteUser u2 = userRepository.getQslUser(2L);

        u1.follow(u2);

        assertThat(u1.getFollowers().size()).isEqualTo(0);
        assertThat(u1.getFollowings().size()).isEqualTo(1);

        assertThat(u2.getFollowers().size()).isEqualTo(1);
        assertThat(u2.getFollowings().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("u1은 더 이상 농구에 관심이 없습니다.")
    void t16() {
        SiteUser u1 = userRepository.getQslUser(1L);

        u1.removeInterestKeywordContent("농구");
    }

    @Test
    @DisplayName("팔로우중인 사람들의 관심사")
    void t17() {
        SiteUser u = userRepository.getQslUser(8L);

        List<String> keywordContents = userRepository.getKeywordContentsByFollowingsOf(u);

        assertThat(keywordContents.size()).isEqualTo(5);

        u = userRepository.getQslUser(7L);

        keywordContents = userRepository.getKeywordContentsByFollowingsOf(u);

        assertThat(keywordContents.size()).isEqualTo(4);
    }
}