package Project_Manager.AccountManager.dto.calendarDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transaction {
    private String category;
    private long amount;
    private String memo;
}
