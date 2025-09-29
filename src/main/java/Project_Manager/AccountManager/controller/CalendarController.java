package Project_Manager.AccountManager.controller;

import Project_Manager.AccountManager.dto.calendarDto.CalendarView;
import Project_Manager.AccountManager.service.CalendarService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class CalendarController {
    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/calendar/calendar")
    public String moveCalendar(
            @RequestParam("user_id") long user_id,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            Model model) {

        //URL로 전달받은 year, month가 있다면 그걸로 / 없다면 오늘날짜 기준으로 세팅
        LocalDate today = LocalDate.now();
        int currentYear = (year != null) ? year : today.getYear();
        int currentMonth = (month != null) ? month : today.getMonthValue();

        CalendarView calendarView = calendarService.createCalendarView(user_id, currentYear, currentMonth);

        model.addAttribute("view", calendarView);

        return "/calendar/calendar";
    }

    @GetMapping("/calendar/add")
    public String addExpense() {

        return "/calendar/add";
    }


}
