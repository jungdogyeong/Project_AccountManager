package Project_Manager.AccountManager.dto.calendarDto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DayCell {
    private int dayOfMonth;
    private boolean isToday;
    private boolean isCurrentMonth;
    private List<Transaction> transactions = new ArrayList<>();
}
