package com.example.qsl.user.repository;

import com.example.qsl.user.entity.QSiteUser;
import com.example.qsl.user.entity.SiteUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.function.LongSupplier;

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

    @Override
    public SiteUser getQslUserOrderByIdAscOne() {

        return jpaQueryFactory
                .select(siteUser)
                .from(siteUser)
                .orderBy(siteUser.id.asc())
                .limit(1)
                .fetchOne();

    }

    @Override
    public List<SiteUser> getQslUsersOrderById() {

        return jpaQueryFactory
                .select(siteUser)
                .from(siteUser)
                .orderBy(siteUser.id.asc())
                .fetch();

    }

    @Override
    public List<SiteUser> searchQsl(String kw) {
        return jpaQueryFactory
                .select(siteUser)
                .from(siteUser)
                .where(
                        siteUser.username.contains(kw)
                                .or(siteUser.email.contains(kw))
                )
                .orderBy(siteUser.id.asc())
                .fetch();
    }

    @Override
    public Page<SiteUser> searchQsl(String kw, Pageable pageable) {

        List<SiteUser> users = jpaQueryFactory
                .select(siteUser)
                .from(siteUser)
                .where(
                        siteUser.username.contains(kw)
                                .or(siteUser.email.contains(kw))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(siteUser.id.asc())
                .fetch();

        LongSupplier totalSupplier = () -> 2;

        return PageableExecutionUtils.getPage(users, pageable, totalSupplier);
    }

}
