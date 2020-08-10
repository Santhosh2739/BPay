package newflow;

public class CategoryModel {



    private String categoryName;
    private int categoryIcon;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(int categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public CategoryModel() {
    }
    public CategoryModel(String _categoryName, int _categoryIcon) {
        this.categoryName = _categoryName;
        this.categoryIcon = _categoryIcon;

    }

}