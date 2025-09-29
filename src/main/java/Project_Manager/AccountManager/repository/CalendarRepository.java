package Project_Manager.AccountManager.repository;

import Project_Manager.AccountManager.domain.CalendarDomain.CalendarDomain;
import Project_Manager.AccountManager.dto.calendarDto.Category;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class CalendarRepository {
    private final JdbcTemplate jdbcTemplate;

    public CalendarRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CalendarDomain> calendarDomainRowMapper = (rs, rowNum) -> new CalendarDomain(
            rs.getLong("calendar_id"),
            rs.getLong("user_id"),
            rs.getLong("category_id"),
            rs.getLong("amount"),
            rs.getString("entry_type"),
            rs.getString("memo"),
            rs.getDate("calendar_date").toLocalDate()
    );

    //총 지출/수입
    public long getTotal(long userId, String entryType, LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT SUM(amount)
                FROM calendars
                WHERE user_id = ?
                  AND entry_type = ?
                  AND calendar_date BETWEEN ? AND ?
            """;

        Long total = jdbcTemplate.queryForObject(sql, Long.class, userId, entryType, startDate, endDate);

        return Optional.ofNullable(total).orElse(0L);
    }

    public List<CalendarDomain> getAllData(long user_id, LocalDate startDate, LocalDate endDate) {
        String sql = """
                    SELECT *
                    FROM calendars
                    WHERE user_id = ?
                        AND calendar_date BETWEEN ? AND ?
                """;

        return jdbcTemplate.query(sql, calendarDomainRowMapper, user_id, startDate, endDate);
    }

    private final RowMapper<Category> categoryRowMapper = (rs, rowNum) -> new Category(
            rs.getLong("category_id"),
            rs.getString("category_name")
    );

    // 모든 카테고리 정보를 가져오는 메서드
    public List<Category> findAllCategories() {
        String sql = """
                   SELECT category_id, category_name
                   FROM categories
                """;

        return jdbcTemplate.query(sql, categoryRowMapper);
    }

}
