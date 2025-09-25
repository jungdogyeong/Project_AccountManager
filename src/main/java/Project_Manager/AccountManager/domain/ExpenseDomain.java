package Project_Manager.AccountManager.domain;

import lombok.Data;

@Data
public class ExpenseDomain {
    private long id;
    private int amount;

    private String memo;
    private long member_id;
    private long category_id;
}
