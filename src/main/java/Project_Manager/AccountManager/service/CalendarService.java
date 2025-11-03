package Project_Manager.AccountManager.service;

import Project_Manager.AccountManager.domain.CalendarDomain.CalendarDomain;
import Project_Manager.AccountManager.dto.calendarDto.*;
import Project_Manager.AccountManager.repository.CalendarRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;

@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final ObjectMapper objectMapper;

    public CalendarService(CalendarRepository calendarRepository, ObjectMapper objectMapper) {
        this.calendarRepository = calendarRepository;
        this.objectMapper = objectMapper;
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

        // 1. JOIN을 통해 category_name까지 한번에 가져옵니다.
        List<CalendarDomain> domains = calendarRepository.getAllData(user_id, startDate, endDate);

        // 2. 총 수입/지출 계산 (기존과 동일)
        Map<String, Long> totalsByType = domains.stream()
                .collect(groupingBy(
                        CalendarDomain::getEntry_type,
                        summingLong(CalendarDomain::getAmount)
                ));

        long totalIncome = totalsByType.getOrDefault("INCOME", 0L);
        long totalExpense = totalsByType.getOrDefault("EXPENSE", 0L);

        calendarView.setTotalIncome(totalIncome);
        calendarView.setTotalExpense(-totalExpense);

        // 3. Transaction DTO를 만들 때, Domain 객체에 이미 있는 category_name을 직접 사용합니다.
        //    (복잡한 categoryMap 로직이 완전히 사라져 코드가 간결해졌습니다.)
        Map<Integer, List<Transaction>> transactionsDay = domains.stream().
                collect(groupingBy(
                        d -> d.getCalendar_date().getDayOfMonth(),
                        Collectors.mapping(d -> new Transaction(
                                d.getCategory_name() != null ? d.getCategory_name() : "미분류",
                                d.getAmount(),
                                d.getMemo(),
                                d.getEntry_type()
                        ), Collectors.toList())
                ));

        // 4. 생성된 Map을 JSON 문자열로 변환하여 View로 전달합니다.
        try {
            String json = objectMapper.writeValueAsString(transactionsDay);
            System.out.println("생성된 JSON: " + json);
            calendarView.setTransactionsByDayJson(json);
        } catch (JsonProcessingException e) {
            System.out.println("JSON 변환 실패: " + e.getMessage());
            calendarView.setTransactionsByDayJson("{}");
        }

        // 5. 달력의 주(week)와 일(day) 구조를 만듭니다. (기존과 동일)
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

    //지출, 수입 추가
    public void setExIn(AddTransactionDto dto) {
        CalendarDomain domain = new CalendarDomain();

        domain.setUser_id(dto.getUserId());
        domain.setAmount(dto.getAmount());
        domain.setEntry_type(dto.getEntryType());
        domain.setMemo(dto.getMemo());
        domain.setCategory_id(dto.getCategoryId());
        domain.setCalendar_date(LocalDate.parse(dto.getDate()));

        calendarRepository.setExpenseIncome(domain);

    }

//    public List<Category> findAllCategories() {
//        return calendarRepository.findAllCategories();
//    }

    public List<CategoryAll> findCategory(long userId) {
        return calendarRepository.findCategory(userId);
    }
}