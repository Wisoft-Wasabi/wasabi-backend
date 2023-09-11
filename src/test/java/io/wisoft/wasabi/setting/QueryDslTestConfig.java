package io.wisoft.wasabi.setting;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.wisoft.wasabi.domain.board.BoardQueryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class QueryDslTestConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public BoardQueryRepository boardQueryRepository() {
        return new BoardQueryRepository(jpaQueryFactory());
    }
}
