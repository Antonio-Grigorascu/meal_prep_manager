package services;

import dao.UserDAO;
import models.plans.User;

import java.util.List;

public class UserService {

    private static UserService instance;
    private final UserDAO userDAO;

    private UserService() {
        this.userDAO = new UserDAO();
    }

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public boolean insertUser(User user) {
        return userDAO.insertUser(user);
    }

    public User getUserByName(String name) {
        return userDAO.getUserByName(name);
    }

    public boolean updateUserWeight(int userId, double newWeight){
        return userDAO.updateUserWeight(userId, newWeight);
    }

    public List<Double> getWeightHistory(int userId){
        return userDAO.getWeightHistory(userId);
    }

    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    public boolean deleteUser(int id) {
        return userDAO.deleteUser(id);
    }
}