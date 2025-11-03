package Project_Manager.AccountManager.repository;

import Project_Manager.AccountManager.domain.UserDomain;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 회원 정보 저장
     */
    public UserDomain save(UserDomain member) {
        String sql = "INSERT INTO MEMBER (login_id, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, member.getLoginId(), member.getPassword());
        return member; // 실제로는 저장 후 id를 받아와서 반환하는 로직이 더 좋습니다.
    }

    /**
     * 로그인 ID로 회원 조회
     */
    public Optional<UserDomain> findByLoginId(String loginId) {
        String sql = "SELECT id, login_id, password FROM MEMBER WHERE login_id = ?";
        try {
            UserDomain member = jdbcTemplate.queryForObject(sql, memberRowMapper(), loginId);
            return Optional.of(member);
        } catch (Exception e) {
            return Optional.empty(); // 조회된 데이터가 없을 경우 예외가 발생하므로, 비어있는 Optional을 반환
        }
    }

    // DB 조회 결과를 Member 객체로 변환해주는 RowMapper
    private RowMapper<UserDomain> memberRowMapper() {
        return (rs, rowNum) -> {
            UserDomain member = new UserDomain();
            member.setId(rs.getLong("id"));
            member.setLoginId(rs.getString("login_id"));
            member.setPassword(rs.getString("password"));
            return member;
        };
    }
}