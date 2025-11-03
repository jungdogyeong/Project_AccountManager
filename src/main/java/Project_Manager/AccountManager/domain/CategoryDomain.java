package Project_Manager.AccountManager.domain;

import lombok.Data;

@Data
public class CategoryDomain {
    private long category_id;
    private long user_id;
    private String category_name;
    private String category_img;

    public CategoryDomain(){}

    public CategoryDomain(long category_id, long user_id, String category_name, String category_img) {
        this.category_id = category_id;
        this.user_id = user_id;
        this.category_name = category_name;
        this.category_img = category_img;
    }

    public CategoryDomain(long user_id, String category_name, String category_img) {
        this.user_id = user_id;
        this.category_name = category_name;
        this.category_img = category_img;
    }
}

