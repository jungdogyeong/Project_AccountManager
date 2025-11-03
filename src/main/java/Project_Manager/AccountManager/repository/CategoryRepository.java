package Project_Manager.AccountManager.repository;

import Project_Manager.AccountManager.domain.CategoryDomain;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper 정의 (DB 결과 -> CategoryDomain 변환), 람다식을 이용한 간결한 작성
    private RowMapper<CategoryDomain> categoryRowMapper = (rs, rowNum) -> {
        CategoryDomain category = new CategoryDomain();
        category.setCategory_id(rs.getLong("category_id"));
        category.setUser_id(rs.getLong("user_id"));
        category.setCategory_name(rs.getString("category_name"));
        category.setCategory_img(rs.getString("category_img"));
        return category;
    };

    // 전체 카테고리 조회
    public List<CategoryDomain> findAll() {
        String sql = "SELECT * FROM categories";
        return jdbcTemplate.query(sql, categoryRowMapper);
    }

    // 사용자별 카테고리 조회
    public List<CategoryDomain> findByUserId(Long user_id) {
        String sql = "SELECT * FROM categories WHERE user_id = ?";
        return jdbcTemplate.query(sql, categoryRowMapper, user_id);
    }

    // 카테고리 ID로 조회
    public CategoryDomain findById(Long category_id) {
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        return jdbcTemplate.queryForObject(sql, categoryRowMapper, category_id);
    }

    // 카테고리 추가
    public int save(CategoryDomain category) {
        String sql = "INSERT INTO categories (user_id, category_name, category_img) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql,
                category.getUser_id(),
                category.getCategory_name(),
                category.getCategory_img());
    }

    // 카테고리 수정
    public int update(CategoryDomain category) {
        String sql = "UPDATE categories SET category_name = ?, category_img = ? WHERE category_id = ?";
        return jdbcTemplate.update(sql,
                category.getCategory_name(),
                category.getCategory_img(),
                category.getCategory_id());
    }

    // 카테고리 삭제
    public int delete(Long category_id) {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        return jdbcTemplate.update(sql, category_id);
    }

    // 특정 사용자의 모든 카테고리 삭제
    public int deleteByUserId(Long user_id) {
        String sql = "DELETE FROM categories WHERE user_id = ?";
        return jdbcTemplate.update(sql, user_id);
    }
}
