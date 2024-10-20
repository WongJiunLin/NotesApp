package com.example.notesapp.database.managers;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.notesapp.database.dao.CategoryDAO;
import com.example.notesapp.database.dao.NoteDAO;
import com.example.notesapp.models.Category;

import java.util.List;

public class CategoryManager {

    private static String tag = "CategoryManager";
    private CategoryDAO categoryDAO;
    private NoteDAO noteDAO;
    private static CategoryManager categoryManager;

    public CategoryManager(SQLiteDatabase db) {
        categoryDAO = new CategoryDAO(db);
        noteDAO = new NoteDAO(db);
    }
    public static synchronized CategoryManager getInstance(SQLiteDatabase db) {
        if (categoryManager == null)
            categoryManager = new CategoryManager(db);
        return categoryManager;
    }

    public static synchronized void resetInstance() {
        categoryManager = null;
    }

    public List<Category> getAllCategories(boolean latestThreeNote) {
        return categoryDAO.getAllCategories(noteDAO, latestThreeNote);
    }

    /**
     * This method is used to add new category
     * @param category
     * @return
     */
    public long addCategory(Category category) {
        if (!checkCategoryNameExist(category.getCategoryName())
            && !category.getCategoryIcon().equalsIgnoreCase("")) {
            long status = categoryDAO.insert(category);
            if (status == -1) {
                Log.e(tag, "addCategory failed with status - " + status);
            }
            return status;
        }
        return -1;
    }

    /**
     * This method is used to find category based on category ID
     * @param id
     * @return
     */
    public Category getCategoryById(long id) {
        return categoryDAO.getCategoryById(id, null);
    }

    /**
     * This method is used to check if the category with input category name exist
     * @param categoryName
     * @return
     */
    public boolean checkCategoryNameExist(String categoryName) {
        if (categoryDAO.getCategoryByName(categoryName, null) != null)
            return true;
        return false;
    }

    /**
     * This method is used to delete all categories
     */
    public void deleteAllCategories() {
        categoryDAO.deleteAll();
    }

    /**
     * This method is used to delete category based on the input category ID
     * @param categoryId
     */
    public void deleteByCategoryId(long categoryId) {
        categoryDAO.deleteByCategoryId(categoryId);
    }

}
