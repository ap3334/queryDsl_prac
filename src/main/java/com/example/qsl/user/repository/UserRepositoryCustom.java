package com.example.qsl.user.repository;

import com.example.qsl.user.entity.SiteUser;

import java.util.List;

public interface UserRepositoryCustom {

    SiteUser getQslUser(Long id);

    long getQslCount();

    SiteUser getQslUserOrderByIdAscOne();

    List<SiteUser> getQslUsersOrderById();

    List<SiteUser> searchQsl(String kw);
}
