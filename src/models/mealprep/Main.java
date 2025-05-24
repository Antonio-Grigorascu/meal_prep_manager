package models.mealprep;

import util.DBInit;

import java.sql.*;

public class Main {
//    public static void database() {
//        String url = "jdbc:mysql://localhost:3306/MealPrep";
//        String user = "root";
//        String password = "rootpa55";
//
//        try {
//            Connection conn = DriverManager.getConnection(url, user, password);
//            Statement stmt = conn.createStatement();
//
//            String sql = "SELECT id, name FROM TEST";
//            ResultSet rs = stmt.executeQuery(sql);
//
//            while (rs.next()) {
//                int id = rs.getInt("id");
//                String name = rs.getString("name");
//                System.out.println("ID: " + id + ", Name: " + name);
//            }
//
//            rs.close();
//            stmt.close();
//            conn.close();
//        } catch (SQLException e) {
//            System.out.println("Query failed!");
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        DBInit.initDatabase();

        Menu menu = Menu.getInstance();
        menu.start();
    }
}
