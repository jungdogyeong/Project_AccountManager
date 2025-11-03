package Project_Manager.AccountManager.controller;

import Project_Manager.AccountManager.domain.CategoryDomain;
import Project_Manager.AccountManager.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categoryMain")
    public String categoryMain(Model model,
                               @RequestParam("user_id") long user_id) {
        List<CategoryDomain> categories = categoryService.getCategoriesByUser(user_id);
        model.addAttribute("categories", categories);
        model.addAttribute("user_id", user_id);
        return "/category/categoryMain";
    }

    @GetMapping("/categoryModify")
    public String categoryModifyForm(
            @RequestParam("user_id") long user_id,
            @RequestParam(name = "id", required = false) Long category_id, Model model) {

        if (category_id != null) {
            CategoryDomain category = categoryService.getCategoryById(category_id);
            model.addAttribute("category", category);
            model.addAttribute("pageTitle", "카테고리 편집");
        } else {
            CategoryDomain newCategory = new CategoryDomain();
            newCategory.setUser_id(user_id);
            model.addAttribute("category", newCategory);
            model.addAttribute("pageTitle", "카테고리 추가");
        }
        return "/category/categoryModify";
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute CategoryDomain category, RedirectAttributes redirectAttributes) {
        if (category.getCategory_id() > 0) {
            categoryService.updateCategory(category);
            redirectAttributes.addFlashAttribute("message", "카테고리가 수정되었습니다.");
        } else {
            categoryService.createCategory(category);
            redirectAttributes.addFlashAttribute("message", "카테고리가 추가되었습니다.");
        }
        return "redirect:/category/categoryMain?user_id=" + category.getUser_id();
    }

    @PostMapping("/delete")
    public String deleteCategory(@RequestParam("id") Long category_id,
                                 @RequestParam("user_id") Long user_id,
                                 RedirectAttributes redirectAttributes) {
        categoryService.deleteCategory(category_id);
        redirectAttributes.addFlashAttribute("message", "카테고리가 삭제되었습니다.");
        return "redirect:/category/categoryMain?user_id=" + user_id;
    }

    @PostMapping("/reset")
    public String resetCategories(@RequestParam("user_id") Long user_id, RedirectAttributes redirectAttributes) {
        categoryService.resetUserCategories(user_id);
        redirectAttributes.addFlashAttribute("message", "카테고리를 초기화했습니다.");
        return "redirect:/category/categoryMain?user_id=" + user_id;
    }
}