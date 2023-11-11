package notice.repository;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import notice.model.entity.Notice;
import notice.model.entity.QNotice;
import notice.model.entity.QNoticeAttach;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * QueryDSL을 사용하여 공지사항을 조회하는 Repository 구현체.
 */
@Repository
public class NoticeRepositoryQImpl implements NoticeRepositoryQ {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 공지사항 코드를 기반으로 공지사항을 조회한다.
     *
     * @param noticeCd 조회할 공지사항의 코드
     * @return 조회된 공지사항, 없을 경우 Optional.empty()
     */
    @Override
    public Optional<Notice> findByNoticeCd(String noticeCd) {
        // QueryDSL을 사용하여 동적 쿼리를 작성하는 JPAQueryFactory 객체 생성
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        // QueryDSL을 사용하여 공지사항과 첨부 파일을 조인하고, 필요한 조건으로 필터링
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

        // 조회된 결과를 Optional로 감싸서 반환
        return Optional.ofNullable(query.fetchOne());
    }
}
