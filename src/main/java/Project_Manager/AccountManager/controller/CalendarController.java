package Project_Manager.AccountManager.controller;

import Project_Manager.AccountManager.dto.calendarDto.AddTransactionDto;
import Project_Manager.AccountManager.dto.calendarDto.CalendarView;
import Project_Manager.AccountManager.service.CalendarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class CalendarController {
    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    //캘린더 메인 페이지
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

    //지출/수입 추가
    @GetMapping("/calendar/add")
    public String addExpense(
            @RequestParam("user_id") long user_id,
            @RequestParam("date") String date,
            Model model
    ) {
        AddTransactionDto dto = new AddTransactionDto();
        dto.setUserId(user_id);
        dto.setDate(date);

        model.addAttribute("transaction", dto);
//        model.addAttribute("catagories", calendarService.findAllCategories());
        model.addAttribute("categories", calendarService.findCategory(user_id));

        return "/calendar/add";
    }

    @PostMapping("/calendar/add")
    public String addExIn(@ModelAttribute("transaction") AddTransactionDto dto) {
        calendarService.setExIn(dto);

        String[] dateParts = dto.getDate().split("-");
        String year = dateParts[0];
        String month = String.valueOf(Integer.parseInt(dateParts[1]));

        return "redirect:/calendar/calendar?user_id=" + dto.getUserId() + "&year=" + year + "&month=" + month;
    }



}
