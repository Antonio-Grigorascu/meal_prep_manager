package util;

import java.sql.Connection;
import java.sql.Statement;

public class DBInit {
    public static void initDatabase() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Table: users
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                age INT,
                weight DOUBLE,
                height DOUBLE,
                gender ENUM('male', 'female'),
                activity_level ENUM('SEDENTARY', 'LIGHTLY_ACTIVE', 'MODERATELY_ACTIVE', 'VERY_ACTIVE', 'EXTRA_ACTIVE') NOT NULL
            )
        """);

            // Table: weight_history
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS weight_history (
                id INT AUTO_INCREMENT PRIMARY KEY,
                user_id INT NOT NULL,
                weight DECIMAL(5,2) NOT NULL COMMENT 'In kg',
                recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            )
        """);

            // Table: goal_types
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS goal_types (
                id INT AUTO_INCREMENT PRIMARY KEY,
                goal_name VARCHAR(50) NOT NULL UNIQUE COMMENT 'e.g. Weight Loss, Gain, Maintenance'
            )
        """);

            // Table: user_goals
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS user_goals (
                id INT AUTO_INCREMENT PRIMARY KEY,
                user_id INT NOT NULL,
                goal_type_id INT NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                FOREIGN KEY (goal_type_id) REFERENCES goal_types(id) ON DELETE CASCADE
            )
        """);

            // Table: ingredients
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS ingredients (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL UNIQUE,
                base_unit ENUM('g', 'ml', 'piece') NOT NULL,
                macros_definition_unit ENUM('PER_100_UNITS', 'PER_PIECE') NOT NULL,
                calories DECIMAL(10,2) NOT NULL,
                protein DECIMAL(10,2) NOT NULL,
                carbs DECIMAL(10,2) NOT NULL,
                fats DECIMAL(10,2) NOT NULL
            )
        """);

            // Table: recipes
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS recipes (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL UNIQUE
            )
        """);

            // Table: recipe_ingredients
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS recipe_ingredients (
                recipe_id INT NOT NULL,
                ingredient_id INT NOT NULL,
                quantity DECIMAL(10,2) NOT NULL,
                PRIMARY KEY (recipe_id, ingredient_id),
                FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
                FOREIGN KEY (ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE
            )
        """);

            // Table: meal_types
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS meal_types (
                id INT AUTO_INCREMENT PRIMARY KEY,
                type_name ENUM('BREAKFAST', 'LUNCH', 'DINNER', 'SNACK') NOT NULL UNIQUE
            )
        """);

            // Table: meals (was missing!)
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS meals (
                id INT AUTO_INCREMENT PRIMARY KEY,
                recipe_id INT NOT NULL,
                meal_type_id INT NOT NULL,
                FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
                FOREIGN KEY (meal_type_id) REFERENCES meal_types(id) ON DELETE CASCADE
            )
        """);

            // Table: user_meal_plans
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS user_meal_plans (
                id INT AUTO_INCREMENT PRIMARY KEY,
                user_id INT NOT NULL,
                plan_date DATE NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            )
        """);

            // Table: user_meal_plan_entries
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS user_meal_plan_entries (
                id INT AUTO_INCREMENT PRIMARY KEY,
                user_meal_plan_id INT NOT NULL,
                meal_id INT NOT NULL,
                FOREIGN KEY (user_meal_plan_id) REFERENCES user_meal_plans(id) ON DELETE CASCADE,
                FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE,
                UNIQUE (user_meal_plan_id, meal_id) COMMENT 'Only one instance of a meal per plan'
            )
        """);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
