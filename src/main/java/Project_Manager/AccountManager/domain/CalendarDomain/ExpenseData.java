package Project_Manager.AccountManager.domain.CalendarDomain;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ExpenseData {
    private long amount;
    private LocalDate calendarDate;

    public ExpenseData(long amount, LocalDate calendarDate) {
        this.amount = amount;
        this.calendarDate = calendarDate;
    }
}