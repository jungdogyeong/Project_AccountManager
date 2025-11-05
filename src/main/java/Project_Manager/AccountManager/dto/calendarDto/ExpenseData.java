package Project_Manager.AccountManager.dto.calendarDto;

import lombok.Data;

import java.util.Random;
import java.util.Scanner;

@Data
public class ExpenseData {
    // 필드: 주차(week)와 금액(amount)
    private String week;
    private int amount;

    // 생성자
    public ExpenseData(String week, int amount) {
        this.week = week;
        this.amount = amount;
    }

    // Getter
    public String Week() {
        return week;
    }


    // 문자열 출력 형식
    @Override
    public String toString() {
        return String.format("%-8s %,7d원", week, amount);
    }

    // ======= 메인 실행 =======
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println("        9월 주별 지출 입력");
        System.out.println("=================================");

        // ✅ 9월 주차 고정, 금액만 입력받기
        String[] weeks = {"9월 1주", "9월 2주", "9월 3주", "9월 4주"};
        ExpenseData[] september = new ExpenseData[weeks.length];

        for (int i = 0; i < weeks.length; i++) {
            System.out.printf("%s 지출 금액 입력: ", weeks[i]);
            int amount = sc.nextInt();
            september[i] = new ExpenseData(weeks[i], amount);
        }
// ✅ 9월 전체 지출 내역 출력
        System.out.println("\n========== 9월 주별 지출 ==========");
        int total = 0;
        for (ExpenseData data : september) {
            System.out.println(data);
            total += data.getAmount();
        }
        System.out.println("=================================");
        System.out.printf("총 지출: %,d원\n", total);
        System.out.println("=================================");

        // ✅ 카테고리 자동 분류 기능
        String[] categories = {"식비", "교통비", "쇼핑", "기타"};
        int[] categoryExpense = new int[categories.length];
        Random random = new Random();

        // 각 주차별 금액을 카테고리에 자동 분배 (랜덤 비율)
        for (ExpenseData data : september) {
            int remain = data.getAmount();
            for (int i = 0; i < categories.length; i++) {
                int split;
                if (i < categories.length - 1) {
                    // 카테고리별 무작위 분배 (비율은 자동)
                    split = random.nextInt(remain / 2 + 1);
                    remain -= split;
                } else {
                    split = remain; // 마지막 카테고리에 나머지 할당
                }
                categoryExpense[i] += split;
            }
        }

        // ✅ 카테고리별 지출 결과 출력
        System.out.println("\n========== 카테고리별 지출 ==========");
        for (int i = 0; i < categories.length; i++) {
            System.out.printf("%-6s %,7d원\n", categories[i], categoryExpense[i]);
        }

    }
}
