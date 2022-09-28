package com.example.qsl.user.repository;

import com.example.qsl.user.entity.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {

    SiteUser getQslUser(Long id);

    long getQslCount();

    SiteUser getQslUserOrderByIdAscOne();

    List<SiteUser> getQslUsersOrderById();

    List<SiteUser> searchQsl(String kw);

    Page<SiteUser> searchQsl(String kw, Pageable pageable);

    List<SiteUser> getQslUserByInterestKeyword(String keywordContent);

    List<String> getKeywordContentsByFollowingsOf(SiteUser user);
}
