package Project_Manager.AccountManager.service;

import Project_Manager.AccountManager.domain.CalendarDomain.CalendarDomain;
import Project_Manager.AccountManager.dto.calendarDto.CalendarView;
import Project_Manager.AccountManager.dto.calendarDto.Category;
import Project_Manager.AccountManager.dto.calendarDto.DayCell;
import Project_Manager.AccountManager.dto.calendarDto.Transaction;
import Project_Manager.AccountManager.repository.CalendarRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;

    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    //총 수입/지출
    public long getTotal(long user_Id, String entry_type,LocalDate startDate, LocalDate endDate) {
        long amount = calendarRepository.getTotal(user_Id, entry_type, startDate, endDate);

        if(entry_type.equals("EXPENSE"))
            amount = -amount;

        return amount;
    }

    //달력 만들기
    public CalendarView createCalendarView(long user_id, int year, int month) {
        CalendarView calendarView = new CalendarView();

        calendarView.setYear(year);
        calendarView.setMonth(month);

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();

        List<CalendarDomain> domains = calendarRepository.getAllData(user_id, startDate, endDate);

        long totalIncome = domains.stream().filter(d -> d.getAmount() > 0).mapToLong(CalendarDomain::getAmount).sum();
        long totalExpense = domains.stream().filter(d -> d.getAmount() < 0).mapToLong(CalendarDomain::getAmount).sum();
        calendarView.setTotalIncome(totalIncome);
        calendarView.setTotalExpense(totalExpense);

        Map<Long, String> categoryMap = calendarRepository.findAllCategories().stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        // ✨ 수정된 부분 2: stream 내부에서 categoryMap을 사용해 ID를 실제 카테고리 이름으로 변환합니다.
        Map<Integer, List<Transaction>> transactionsDay = domains.stream().
                collect(Collectors.groupingBy(
                        d -> d.getCalendar_date().getDayOfMonth(),
                        Collectors.mapping(d -> new Transaction(
                                // ID로 이름을 찾고, 없으면 "미분류"로 표시
                                categoryMap.getOrDefault(d.getCategory_id(), "미분류"),
                                d.getAmount(),
                                d.getMemo()
                        ), Collectors.toList())
                ));

        List<List<DayCell>> weeks = new ArrayList<>();
        LocalDate today = LocalDate.now();
        int firstDayOfWeek = startDate.getDayOfWeek().getValue() % 7;

        List<DayCell> currentWeek = new ArrayList<>();
        for(int i = 0; i < firstDayOfWeek; i++) {
            currentWeek.add(new DayCell());
        }

        for(int day = 1; day <= endDate.getDayOfMonth(); day++) {
            DayCell dayCell = new DayCell();
            dayCell.setDayOfMonth(day);
            dayCell.setCurrentMonth(true);
            dayCell.setToday(LocalDate.of(year, month, day).equals(today));
            dayCell.setTransactions(transactionsDay.getOrDefault(day, new ArrayList<>()));

            currentWeek.add(dayCell);
            if (currentWeek.size() == 7) {
                weeks.add(currentWeek);
                currentWeek = new ArrayList<>();
            }
        }

        if (!currentWeek.isEmpty()) {
            while (currentWeek.size() < 7) {
                currentWeek.add(new DayCell());
            }
            weeks.add(currentWeek);
        }

        calendarView.setWeeks(weeks);

        return calendarView;
    }
}
