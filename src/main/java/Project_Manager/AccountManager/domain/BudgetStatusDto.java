package Project_Manager.AccountManager.domain;

import lombok.Data;

@Data

// HTML에 보여줄 데이터(이름, 아이콘, 예산액, 지출액)를 한 곳에 담아둘 DTO
public class BudgetStatusDto {
    private Long categoryId;
    private String categoryName; // 이름
    private String categoryIcon; // 아이콘
    private Long expectedAmount = 0L; // 에산액
    private Long actualAmount = 0L; // 지출액(실 사용액)

    // 진행률 계산 메서드
    public int getProgress() {
        if(expectedAmount == null || expectedAmount <= 0)
            return 0;

        if(actualAmount == null || actualAmount <= 0)
            return 0;

        int progress = (int)(actualAmount * 100.0 / expectedAmount);

        return Math.min(progress, 100); // 진행률 바는 100%가 최대 (지출 > 예산의 경우 처리)
    }
}
