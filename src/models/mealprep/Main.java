package models.mealprep;

import util.DBInit;

public class Main {
    public static void main(String[] args) {
        DBInit.initDatabase();

        Menu menu = Menu.getInstance();
        menu.start();
    }
}
