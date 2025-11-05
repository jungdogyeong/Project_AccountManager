package Project_Manager.AccountManager.repository;

import Project_Manager.AccountManager.domain.CalendarDomain.CalendarDomain;
import Project_Manager.AccountManager.dto.calendarDto.Category;
import Project_Manager.AccountManager.dto.calendarDto.CategoryAll;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import java.util.Map;
import java.util.HashMap;

@Repository
public class CalendarRepository {
    private final JdbcTemplate jdbcTemplate;

    public CalendarRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CalendarDomain> calendarDomainRowMapper = (rs, rowNum) -> {
        CalendarDomain domain = new CalendarDomain();
        domain.setCalendar_id(rs.getLong("calendar_id"));
        domain.setUser_id(rs.getLong("user_id"));
        domain.setCategory_id(rs.getLong("category_id"));
        domain.setCategory_name(rs.getString("category_name")); // [추가] category_name 매핑
        domain.setAmount(rs.getLong("amount"));
        domain.setEntry_type(rs.getString("entry_type"));
        domain.setMemo(rs.getString("memo"));
        domain.setCalendar_date(rs.getDate("calendar_date").toLocalDate());
        return domain;
    };

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
                    SELECT
                        c.calendar_id, c.user_id, c.category_id, cat.category_name,
                        c.amount, c.entry_type, c.memo, c.calendar_date
                    FROM calendars c
                    LEFT JOIN categories cat ON c.category_id = cat.category_id
                    WHERE c.user_id = ?
                        AND c.calendar_date BETWEEN ? AND ?
                """;

        return jdbcTemplate.query(sql, calendarDomainRowMapper, user_id, startDate, endDate);
    }

    private final RowMapper<Category> categoryRowMapper = (rs, rowNum) -> new Category(
            rs.getLong("category_id"),
            rs.getString("category_name")
    );

//    // 모든 카테고리 정보를 가져오는 메서드
//    public List<Category> findAllCategories() {
//        String sql = """
//                   SELECT category_id, category_name
//                   FROM categories
//                """;
//
//        return jdbcTemplate.query(sql, categoryRowMapper);
//    }

    //지출, 수입 추가
    public void setExpenseIncome(CalendarDomain domain) {
        String sql = """
                INSERT INTO calendars(user_id, amount, entry_type, calendar_date, category_id, memo)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(sql,
                domain.getUser_id(),
                domain.getAmount(),
                domain.getEntry_type(),
                domain.getCalendar_date(),
                domain.getCategory_id(),
                domain.getMemo()
        );
    }

    private final RowMapper<CategoryAll> categoryAllRowMapper = (rs, rowNum) -> new CategoryAll(
            rs.getLong("categoryId"),
            rs.getLong("userId"),
            rs.getString("categoryName"),
            rs.getString("categoryImg")
    );

    public List<CategoryAll> findCategory(long userId) {
        String sql = """
                SELECT *
                FROM categories
                WHERE user_id = ?
                """;

        return jdbcTemplate.query(sql, categoryAllRowMapper);
    }

    // 해당 월의 카테고리별 지출 내역을 Map으로 조회
    public Map<Long, Long> findSpendingByCategoriesForMonth(Long userId, int year, int month) {
        String sql = """
                SELECT category_id, SUM(amount) as total_spent
                FROM calendars
                WHERE user_id = ?
                    AND entry_type = 'EXPENSE'
                    AND YEAR(calendar_date) = ?
                    AND MONTH(calendar_date) = ?
                GROUP BY category_id
                """;

        // 카테고리별 지출 내역 Map으로 저장
        Map<Long, Long> spendingMap = new HashMap<>();
        jdbcTemplate.query(sql, (rs, rowNum) -> {
            spendingMap.put(rs.getLong("category_id"), rs.getLong("total_spent"));
            return null;
        }, userId, year, month);

        return spendingMap;
    }
}
