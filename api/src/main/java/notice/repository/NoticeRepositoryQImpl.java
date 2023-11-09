package notice.repository;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import notice.model.entity.Notice;
import notice.model.entity.QNotice;
import notice.model.entity.QNoticeAttach;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NoticeRepositoryQImpl implements NoticeRepositoryQ {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Notice> findByNoticeCd(String noticeCd) {
        System.out.println("noticeCd: " + noticeCd);
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        QNotice notice = new QNotice("qNotice");
        QNoticeAttach noticeAttach = QNoticeAttach.noticeAttach;

        JPQLQuery<Notice> query = queryFactory
                .selectFrom(notice)
                .leftJoin(notice.noticeAttachs, noticeAttach).fetchJoin()
                .where(
                    notice.noticeCd.eq(noticeCd)
                    .and(notice.deleteYn.eq("N"))
                    .and(noticeAttach.deleteYn.eq("N"))
                )
                .orderBy(notice.insertDt.desc());

        return Optional.ofNullable(query.fetchOne());
    }
}