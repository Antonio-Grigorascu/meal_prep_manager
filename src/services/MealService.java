package services;

import dao.MealDAO;
import models.meals.Meal;

import java.util.List;

public class MealService {

    private static MealService instance;
    private final MealDAO mealDAO;

    private MealService() {
        this.mealDAO = new MealDAO();
    }

    public static synchronized MealService getInstance() {
        if (instance == null) {
            instance = new MealService();
        }
        return instance;
    }

    public void insertMeal(Meal meal, int userId) {
        mealDAO.insertMeal(meal, userId);
    }

    public List<Meal> getMealsByUserId(int userId) {
        return mealDAO.getMealsByUserId(userId);
    }

    public void updateMeal(Meal meal) {
        mealDAO.updateMeal(meal);
    }

    public void deleteMeal(int mealId) {
        mealDAO.deleteMeal(mealId);
    }
}