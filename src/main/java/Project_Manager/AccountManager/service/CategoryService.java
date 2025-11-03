package Project_Manager.AccountManager.service;

import Project_Manager.AccountManager.domain.CategoryDomain;
import Project_Manager.AccountManager.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // 전체 카테고리 조회
    public List<CategoryDomain> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 특정 유저의 카테고리 조회 (기본 카테고리 생성 로직 추가)
    public List<CategoryDomain> getCategoriesByUser(Long user_id) {
        // 1. DB에서 유저의 카테고리 조회
        List<CategoryDomain> categories = categoryRepository.findByUserId(user_id);

        // 2. 조회된 카테고리가 없을 시(새로운 사용자), 기본 카테고리를 생성
        if (categories.isEmpty()) {
            System.out.println("사용자 ID " + user_id + "의 카테고리가 없어 기본값을 생성합니다.");

            // 3. 기본 카테고리 목록 정의
            List<CategoryDomain> defaultCategories = Arrays.asList(
                    new CategoryDomain(user_id, "식비", "🍚"),
                    new CategoryDomain(user_id, "교통비", "🚌"),
                    new CategoryDomain(user_id, "쇼핑", "🛍️"),
                    new CategoryDomain(user_id, "문화생활", "🎬"),
                    new CategoryDomain(user_id, "여행", "✈️"),
                    new CategoryDomain(user_id, "통신비", "📱"),
                    new CategoryDomain(user_id, "주거비", "🏠"),
                    new CategoryDomain(user_id, "의료비", "💊"),
                    new CategoryDomain(user_id, "교육비", "📚"),
                    new CategoryDomain(user_id, "기타", "✨")
            );

            // 4. 각 기본 카테고리를 DB에 저장
            for (CategoryDomain category : defaultCategories) {
                categoryRepository.save(category);
            }

            // 5. 기본 카테고리를 저장한 후, 다시 조회하여 반환
            return categoryRepository.findByUserId(user_id);
        }

        // 6. 이미 카테고리가 있다면 그대로 반환
        return categories;
    }


    // 카테고리 ID로 조회
    public CategoryDomain getCategoryById(Long category_id) {
        return categoryRepository.findById(category_id);
    }

    // 카테고리 생성
    public boolean createCategory(CategoryDomain category) {
        try {
            categoryRepository.save(category);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 카테고리 수정
    public boolean updateCategory(CategoryDomain category) {
        try {
            categoryRepository.update(category);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 카테고리 삭제
    public boolean deleteCategory(Long category_id) {
        try {
            categoryRepository.delete(category_id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 사용자 카테고리 초기화
    public List<CategoryDomain> resetUserCategories(Long user_id) {
        // 1. 기존 카테고리를 모두 삭제
        categoryRepository.deleteByUserId(user_id);

        // 2. 카테고리 조회 메소드를 다시 호출
        return getCategoriesByUser(user_id);
    }
}