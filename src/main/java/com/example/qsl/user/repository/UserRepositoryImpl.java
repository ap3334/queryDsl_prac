package com.example.qsl.user.repository;

import com.example.qsl.user.entity.QSiteUser;
import com.example.qsl.user.entity.SiteUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.example.qsl.user.entity.QSiteUser.*;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public SiteUser getQslUser(Long id) {
        return jpaQueryFactory
                .select(siteUser)
                .from(siteUser)
                .where(siteUser.id.eq(id))
                .fetchOne();
    }

    @Override
    public long getQslCount() {

        return jpaQueryFactory
                .select(siteUser.count())
                .from(siteUser)
                .fetchOne();

    }

}
