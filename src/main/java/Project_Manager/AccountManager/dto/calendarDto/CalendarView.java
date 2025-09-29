package Project_Manager.AccountManager.dto.calendarDto;

import lombok.Data;
import java.util.List;

@Data
public class CalendarView {
    private int year;
    private int month;
    private long totalIncome;
    private long totalExpense;
    private List<List<DayCell>> weeks; // 2차원 리스트로 주(week)와 일(day)을 표현
}