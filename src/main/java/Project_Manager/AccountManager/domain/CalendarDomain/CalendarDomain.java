package Project_Manager.AccountManager.domain.CalendarDomain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CalendarDomain {
    private long calendar_id;
    private long user_id;
    private long category_id;
    private long amount;
    private String entry_type;
    private String memo;
    private LocalDate calendar_date;

    public CalendarDomain(long calendar_id, long user_id, long category_id, long amount, String entry_type, String memo, LocalDate calendar_date) {
        this.calendar_id = calendar_id;
        this.user_id = user_id;
        this.category_id = category_id;
        this.amount = amount;
        this.entry_type = entry_type;
        this.memo = memo;
        this.calendar_date = calendar_date;
    }
}
