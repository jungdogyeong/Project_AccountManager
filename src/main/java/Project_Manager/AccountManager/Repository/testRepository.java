package Project_Manager.AccountManager.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class testRepository {
    private final JdbcTemplate jdbcTemplate;

    public testRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String testff(String value){
        String sql = """
                SELECT variable
                FROM sys_config
                WHERE value = ?
                """;

        return jdbcTemplate.queryForObject(sql, String.class, value);
    }
}
