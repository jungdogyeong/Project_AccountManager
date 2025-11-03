package Project_Manager.AccountManager.dto.calendarDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryAll {
    private long categoryId;
    private long userId;
    private String categoryName;
    private String categoryImg;
}
