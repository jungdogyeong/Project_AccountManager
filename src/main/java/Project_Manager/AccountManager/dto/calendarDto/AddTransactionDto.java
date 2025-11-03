package Project_Manager.AccountManager.dto.calendarDto;

import lombok.Data;

@Data
public class AddTransactionDto {
    private long userId;
    private String date;
    private String entryType;
    private Long amount;
    private Long categoryId;
    private String memo;
}
