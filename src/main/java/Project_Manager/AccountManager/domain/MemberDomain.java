package Project_Manager.AccountManager.domain;

import lombok.Data;

@Data
public class MemberDomain {
    private Long id;
    private String loginId;
    private String password;
}
