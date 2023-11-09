package notice.repository;

import notice.model.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, String>, QuerydslPredicateExecutor<Notice>, NoticeRepositoryQ {
}
