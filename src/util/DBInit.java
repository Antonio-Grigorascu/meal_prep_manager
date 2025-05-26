package util;

import java.sql.Connection;
import java.sql.Statement;

public class DBInit {
    public static void initDatabase() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Tabel: users
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL UNIQUE,
                age INT,
                weight DOUBLE,
                height DOUBLE,
                gender ENUM('male', 'female'),
                activity_level ENUM('SEDENTARY', 'LIGHTLY_ACTIVE', 'MODERATELY_ACTIVE', 'VERY_ACTIVE', 'EXTRA_ACTIVE') NOT NULL
            )
        """);

            // Tabel: weight_history
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS weight_history (
                id INT AUTO_INCREMENT PRIMARY KEY,
                user_id INT NOT NULL,
                weight DECIMAL(5,2) NOT NULL COMMENT 'In kg',
                recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            )
        """);

            // Tabel: user_goals
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS user_goals (
                    user_id INT PRIMARY KEY,
                    goal_type ENUM('WEIGHT_LOSS', 'WEIGHT_GAIN', 'MAINTENANCE') NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                )
            """);

            // Tabel: ingredients
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

            // Tabel: recipes
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS recipes (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL UNIQUE
            )
        """);

            // Tabel: recipe_ingredients
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

            // Tabel: meals
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS meals (
                id INT AUTO_INCREMENT PRIMARY KEY,
                recipe_id INT NOT NULL,
                meal_type ENUM('BREAKFAST', 'LUNCH', 'DINNER', 'SNACK'),
                FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
            )
        """);

            // Tabel: user_meal_plan_entries
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS user_meal_plan_entries (
                user_id INT NOT NULL,
                meal_id INT NOT NULL,
                PRIMARY KEY (user_id, meal_id),
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
                FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE ON UPDATE CASCADE
            )
        """);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
