package models.mealprep;

import dao.GoalDAO;
import dao.IngredientDAO;
import dao.RecipeDAO;
import dao.UserDAO;
import enums.ActivityLevel;
import enums.MealType;
import models.ingredients.*;
import models.meals.Meal;
import models.meals.Recipe;
import models.plans.*;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Menu {

    private static Menu instance; // Singleton

    final private Scanner scanner;
    private User user;
    private MealPlan mealPlan;
    private NutritionalGoal goal;

    private List<Ingredient> ingredientList = new ArrayList<>();
    private List<Recipe> recipeList = new ArrayList<>();

    private Menu() {
        scanner = new Scanner(System.in);
        mealPlan = new MealPlan();
    }

    public static Menu getInstance() {
        if (instance == null) {
            instance = new Menu();
        }
        return instance;
    }

    public void start() {
        System.out.println("🍽️ Bine ai venit la MealPrep Manager!");

        handleUserAuth();

        boolean running = true;
        while (running) {
            System.out.println("\nApasă Enter pentru a reveni la meniu...");
            scanner.nextLine();
            System.out.println("===== Meniu =====");
            System.out.println("1. Adaugă o masă");
            System.out.println("2. Vezi planul curent");
            System.out.println("3. Evaluează planul");
            System.out.println("4. Adaugă ingredient");
            System.out.println("5. Adaugă rețetă");
            System.out.println("6. Vezi toate rețetele");
            System.out.println("7. Vezi toate ingredientele");
            System.out.println("8. Actualizează greutatea");
            System.out.println("9. Vezi progresul greutății");
            System.out.println("0. Ieși");
            System.out.print("Alege opțiunea: ");

            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    addMeal();
                    break;
                case "2":
                    viewPlan();
                    break;
                case "3":
                    evaluatePlan();
                    break;
                case "4":
                    addIngredient();
                    break;
                case "5":
                    createRecipe();
                    break;
                case "6":
                    viewRecipes();
                    break;
                case "7":
                    viewIngredients();
                    break;
                case "8":
                    updateUserWeight();
                    break;
                case "9":
                    showWeightProgress();
                    break;
                case "0":
                    running = false;
                    System.out.println("👋 La revedere!");
                    break;
                default:
                    System.out.println("⚠️ Opțiune invalidă.");
            }
        }

        scanner.close();
    }

    private void handleUserAuth() {
        System.out.println("\n🔐 Autentificare / Înregistrare");
        System.out.println("1. Autentificare cu utilizator existent");
        System.out.println("2. Înregistrare utilizator nou");
        System.out.print("Alege opțiunea: ");

        String option;
        while (true) {
            option = scanner.nextLine().trim();
            if (option.equals("1") || option.equals("2")) break;
            System.out.print("⚠️ Opțiune invalidă. Alege 1 sau 2: ");
        }

        if (option.equals("1")) {
            loginUser();
        } else {
            int result = createUser();
            if (result != 0){
                System.out.println("⛔ Nu se poate continua fără utilizator valid!");
                handleUserAuth();
                return;
            }
        }
        chooseGoal();
    }

    private int createUser() {
        System.out.print("Nume: ");
        String name;
        while (true) {
            name = scanner.nextLine().trim();
            if (!name.isEmpty()) break;
            System.out.print("⚠️ Numele nu poate fi gol. Introdu un nume valid: ");
        }

        System.out.print("Vârstă: ");
        int age;
        while (true) {
            try {
                age = Integer.parseInt(scanner.nextLine());
                if (age > 0) break;
                System.out.print("⚠️ Vârsta trebuie să fie un număr pozitiv. Reintrodu: ");
            } catch (NumberFormatException e) {
                System.out.print("⚠️ Vârsta trebuie să fie un număr. Reintrodu: ");
            }
        }

        System.out.print("Greutate (kg): ");
        double weight;
        while (true) {
            try {
                weight = Double.parseDouble(scanner.nextLine());
                if (weight > 0) break;
                System.out.print("⚠️ Greutatea trebuie să fie un număr pozitiv. Reintrodu: ");
            } catch (NumberFormatException e) {
                System.out.print("⚠️ Greutatea trebuie să fie un număr. Reintrodu: ");
            }
        }

        System.out.print("Înălțime (cm): ");
        double height;
        while (true) {
            try {
                height = Double.parseDouble(scanner.nextLine());
                if (height > 0) break;
                System.out.print("⚠️ Înălțimea trebuie să fie un număr pozitiv. Reintrodu: ");
            } catch (NumberFormatException e) {
                System.out.print("⚠️ Înălțimea trebuie să fie un număr. Reintrodu: ");
            }
        }

        System.out.print("Sex (male/female): ");
        String gender;
        while (true) {
            gender = scanner.nextLine().trim().toLowerCase();
            if (gender.equals("male") || gender.equals("female")) break;
            System.out.print("⚠️ Sexul trebuie să fie 'male' sau 'female'. Reintrodu: ");
        }

        System.out.println("Nivel de activitate:\n1. Sedentar\n2. Ușor activ\n3. Activ moderat\n4. Foarte activ\n5. Extra activ");
        ActivityLevel activityLevel;
        while (true) {
            String level = scanner.nextLine().trim();
            switch (level) {
                case "1":
                    activityLevel = ActivityLevel.SEDENTARY;
                    break;
                case "2":
                    activityLevel = ActivityLevel.LIGHTLY_ACTIVE;
                    break;
                case "3":
                    activityLevel = ActivityLevel.MODERATELY_ACTIVE;
                    break;
                case "4":
                    activityLevel = ActivityLevel.VERY_ACTIVE;
                    break;
                case "5":
                    activityLevel = ActivityLevel.EXTRA_ACTIVE;
                    break;
                default:
                    System.out.print("⚠️ Nivel invalid. Alege un nivel între 1 și 5: ");
                    continue;
            }
            break;
        }

        user = new User(name, age, weight, height, gender, activityLevel);

        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.insertUser(user);
        if (success) {
            System.out.println("✅ Utilizator creat cu succes! Bun venit, " + user.getName() + "!");
            return 0;
        } else {
            System.out.println("❌ Eroare la crearea utilizatorului!");
            return -1;
        }
    }

    private void loginUser() {
        UserDAO userDAO = new UserDAO();
        int attempts = 0;

        while (attempts < 3) {
            System.out.print("\nIntrodu numele utilizatorului: ");
            String username = scanner.nextLine().trim();

            user = userDAO.getUserByName(username);

            if (user != null) {
                System.out.println("✅ Autentificare reușită! Bun venit, " + user.getName() + "!");
                return;
            }

            attempts++;
            System.out.println("⚠️ Utilizatorul nu există. Mai ai " + (3 - attempts) + " încercări.");
        }

        System.out.println("❌ Prea multe încercări eșuate. Crează un utilizator nou.");
        createUser();
    }


    private void chooseGoal() {

        GoalDAO goalDAO = new GoalDAO();

        String existingGoalType = goalDAO.getUserGoalType(user.getId());
        if (existingGoalType != null) {
            System.out.println("Ai deja un obiectiv setat: " + existingGoalType);
            System.out.print("Dorești să îl schimbi? (da/nu): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("da") || response.equals("d")) {
                System.out.println("Obiectiv:\n1. Pierdere în greutate\n2. Creștere în greutate\n3. Mentinere");
                String goalOption;

                while (true) {
                    goalOption = scanner.nextLine().trim();
                    if (goalOption.equals("1")) {
                        goal = new WeightLoss();
                        break;
                    } else if (goalOption.equals("2")) {
                        goal = new WeightGain();
                        break;
                    } else if (goalOption.equals("3")) {
                        goal = new Maintenance();
                        break;
                    } else {
                        System.out.println("⚠️ Opțiune invalidă. Alege 1, 2 sau 3:");
                    }
                }
                String normalizedGoalName = goal.getGoalName()
                        .toUpperCase()
                        .replace(" ", "_");


                goalDAO.saveUserGoal(user.getId(),normalizedGoalName);
                System.out.println("Obiectiv salvat cu succes în baza de date!");
                return;
            }
            else{
                return;
            }
        }

        System.out.println("Obiectiv:\n1. Pierdere în greutate\n2. Creștere în greutate\n3. Mentinere");
        String goalOption;

        while (true) {
            goalOption = scanner.nextLine().trim();
            if (goalOption.equals("1")) {
                goal = new WeightLoss();
                break;
            } else if (goalOption.equals("2")) {
                goal = new WeightGain();
                break;
            } else if (goalOption.equals("3")) {
                goal = new Maintenance();
                break;
            } else {
                System.out.println("⚠️ Opțiune invalidă. Alege 1, 2 sau 3:");
            }
        }


        String normalizedGoalName = goal.getGoalName()
                .toUpperCase()
                .replace(" ", "_");


        goalDAO.saveUserGoal(user.getId(),normalizedGoalName);
        System.out.println("Obiectiv salvat cu succes în baza de date!");
    }

    private void addMeal() {
        System.out.println("Tipul mesei: 0. Ieși | 1. Mic dejun | 2. Prânz | 3. Cină | 4. Gustare");
        String tip;
        MealType type = null;

        while (true) {
            tip = scanner.nextLine().trim();
            if (tip.equals("0")) {
                System.out.println("❌ Ai ales să nu adaugi nicio masă.");
                return;
            } else if (tip.equals("1")) {
                type = MealType.BREAKFAST;
            } else if (tip.equals("2")) {
                type = MealType.LUNCH;
            } else if (tip.equals("3")) {
                type = MealType.DINNER;
            } else if (tip.equals("4")) {
                type = MealType.SNACK;
            } else {
                System.out.println("⚠️ Opțiune invalidă. Alege 0, 1, 2, 3 sau 4:");
                continue;
            }

            // Variabila final pentru fucntia lambda
            MealType finalType = type;

            boolean exists = mealPlan.getMeals().stream().anyMatch(meal -> meal.getMealType() == finalType);
            if (exists) {
                System.out.println("⚠️ Acest tip de masă există deja în plan. Alege alt tip:");
            } else {
                break;
            }
        }
        System.out.println("Alege rețetă existentă (index) sau 'nou' pentru a crea una:");
        for (int i = 0; i < recipeList.size(); i++) {
            System.out.println(i + ": " + recipeList.get(i).getName());
        }

        String input;
        Recipe selectedRecipe = null;

        while (true) {
            input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("nou")) {
                createRecipe();
                selectedRecipe = recipeList.get(recipeList.size() - 1); // ultima
                break;
            } else {
                try {
                    int index = Integer.parseInt(input);
                    if (index >= 0 && index < recipeList.size()) {
                        selectedRecipe = recipeList.get(index);
                        break;
                    } else {
                        System.out.println("⚠️ Index invalid. Alege un index valid:");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Input invalid. Alege un index valid sau 'nou':");
                }
            }
        }

        Meal meal = new Meal(type, selectedRecipe);
        mealPlan.addMeal(meal);
        System.out.println("✅ Masă adăugată.");
    }


    private void viewPlan() {
        System.out.println("\n📋 Planul curent:");

        if (mealPlan.getMeals().isEmpty()) {
            System.out.println("⚠️ Nu ai adăugat mese.");
            return;
        }

        for (Meal m : mealPlan.getMeals()) {
            System.out.println(m.getMealType() + ": " + m.getRecipe().getName());
        }

        System.out.println("Total: " + mealPlan.getTotalMacros());
    }

    private void evaluatePlan() {
        UserPlan userPlan = new UserPlan(user, mealPlan, goal);
        userPlan.evaluatePlan();
    }

    private void addIngredient() {
        System.out.print("Nume ingredient: ");
        String name;
        while (true) {
            name = scanner.nextLine().trim();
            if (!name.isEmpty()) break;
            System.out.print("⚠️ Numele ingredientului nu poate fi gol. Introdu un nume valid: ");
        }

        System.out.println("Tip ingredient:");
        System.out.println("1. Ingredient pe unitate (bucăți)");
        System.out.println("2. Ingredient pe greutate (grame)");
        System.out.println("3. Ingredient pe volum (mililitri)");
        String type;
        while (true) {
            type = scanner.nextLine().trim();
            if (type.equals("1") || type.equals("2") || type.equals("3")) break;
            System.out.print("⚠️ Tip invalid. Alege 1, 2 sau 3: ");
        }

        String unitLabel = switch (type) {
            case "1" -> "buc.";
            case "2" -> "100g";
            case "3" -> "100ml";
            default -> "100g";
        };


        System.out.print("Calorii per " + unitLabel + ": ");
        int cal;
        while (true) {
            try {
                cal = Integer.parseInt(scanner.nextLine());
                if (cal >= 0) break;
                System.out.print("⚠️ Caloriile trebuie să fie un număr pozitiv sau zero. Reintrodu: ");
            } catch (NumberFormatException e) {
                System.out.print("⚠️ Caloriile trebuie să fie un număr. Reintrodu: ");
            }
        }

        System.out.print("Proteine per " + unitLabel + ": ");
        int prot;
        while (true) {
            try {
                prot = Integer.parseInt(scanner.nextLine());
                if (prot >= 0) break;
                System.out.print("⚠️ Proteinele trebuie să fie un număr pozitiv sau zero. Reintrodu: ");
            } catch (NumberFormatException e) {
                System.out.print("⚠️ Proteinele trebuie să fie un număr. Reintrodu: ");
            }
        }

        System.out.print("Grăsimi per " + unitLabel + ": ");
        int fat;
        while (true) {
            try {
                fat = Integer.parseInt(scanner.nextLine());
                if (fat >= 0) break;
                System.out.print("⚠️ Grăsimile trebuie să fie un număr pozitiv sau zero. Reintrodu: ");
            } catch (NumberFormatException e) {
                System.out.print("⚠️ Grăsimile trebuie să fie un număr. Reintrodu: ");
            }
        }

        System.out.print("Carbohidrați per " + unitLabel + ": ");
        int carb;
        while (true) {
            try {
                carb = Integer.parseInt(scanner.nextLine());
                if (carb >= 0) break;
                System.out.print("⚠️ Carbohidrații trebuie să fie un număr pozitiv sau zero. Reintrodu: ");
            } catch (NumberFormatException e) {
                System.out.print("⚠️ Carbohidrații trebuie să fie un număr. Reintrodu: ");
            }
        }

        Macros macros = new Macros(cal, prot, fat, carb);
        Ingredient ingredient = switch (type) {
            case "1" -> new UnitIngredient(name, macros);
            case "2" -> new WeightIngredient(name, macros);
            case "3" -> new VolumeIngredient(name, macros);
            default -> throw new IllegalStateException("Tip invalid.");
        };

        ingredientList.add(ingredient);

        IngredientDAO ingredientDAO = new IngredientDAO();
        ingredientDAO.insertIngredient(ingredient);
    }

    private void createRecipe() {
        System.out.print("Nume rețetă: ");
        String name;
        while (true) {
            name = scanner.nextLine().trim();
            if (!name.isEmpty()) break;
            System.out.print("⚠️ Numele rețetei nu poate fi gol. Introdu un nume valid: ");
        }
        Recipe recipe = new Recipe(name);

        while (true) {
            System.out.println("Alege ingredient din listă (index) sau scrie 'nou' pentru a adăuga unul nou, 'gata' pentru a încheia:");
            for (int i = 0; i < ingredientList.size(); i++) {
                System.out.println(i + ": " + ingredientList.get(i).getName());
            }

            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("gata")) break;
            if (input.equalsIgnoreCase("nou")) {
                addIngredient();
                continue;
            }

            try {
                int index = Integer.parseInt(input);
                if (index < 0 || index >= ingredientList.size()) {
                    System.out.println("⚠️ Index invalid. Alege un index valid.");
                    continue;
                }
                Ingredient chosen = ingredientList.get(index);

                System.out.print("Cantitate (" + chosen.getUnit() + "): ");
                double quantity;
                while (true) {
                    try {
                        quantity = Double.parseDouble(scanner.nextLine());
                        if (quantity > 0) break;
                        System.out.print("⚠️ Cantitatea trebuie să fie un număr pozitiv. Reintrodu: ");
                    } catch (NumberFormatException e) {
                        System.out.print("⚠️ Cantitatea trebuie să fie un număr. Reintrodu: ");
                    }
                }

                recipe.addIngredient(chosen, quantity);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Input invalid. Alege un index valid sau 'nou'.");
            }
        }

        recipeList.add(recipe);

        RecipeDAO recipeDAO = new RecipeDAO();
        recipeDAO.insertRecipe(recipe);
    }

    private void viewRecipes() {
        RecipeDAO recipeDAO = new RecipeDAO();
        List<Recipe> recipes = recipeDAO.getAllRecipes();
        
        if (recipes.isEmpty()) {
            System.out.println("Nu există rețete în baza de date.");
            return;
        }
        
        System.out.println("\n📋 Rețete din baza de date:");
        for (Recipe recipe : recipes) {
            System.out.printf("%d: %s\n", recipe.getId(), recipe.getName());
            System.out.println("   Ingrediente:");
            for (Map.Entry<Ingredient, Double> entry : recipe.getIngredients().entrySet()) {
                Ingredient ing = entry.getKey();
                System.out.printf("   - %s: %.2f %s\n", 
                    ing.getName(), 
                    entry.getValue(), 
                    ing.getUnit());
            }
        }
    }

    private void viewIngredients() {
        IngredientDAO ingredientDAO = new IngredientDAO();
        List<Ingredient> ingredients = ingredientDAO.getAllIngredients();
        
        if (ingredients.isEmpty()) {
            System.out.println("⚠️ Nu există ingrediente în baza de date.");
            return;
        }

        System.out.println("\n📋 Lista ingredientelor din baza de date:");
        for (Ingredient ingredient : ingredients) {
            System.out.printf("%d: %s (%s)\n", 
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getClass().getSimpleName());
            Macros m = ingredient.getMacros();
            System.out.printf("   Calorii: %.1f | Proteine: %.1fg | Grăsimi: %.1fg | Carbohidrați: %.1fg\n\n",
                m.getCalories(), m.getProteins(), m.getFats(), m.getCarbs());
        }
    }

    private void updateUserWeight() {
        System.out.print("Introduceți noua greutate (kg): ");
        double newWeight;
        while (true) {
            try {
                newWeight = Double.parseDouble(scanner.nextLine().trim());
                if (newWeight > 0) break;
                System.out.print("⚠️ Greutatea trebuie să fie un număr pozitiv. Reintrodu: ");
            } catch (NumberFormatException e) {
                System.out.print("⚠️ Greutatea trebuie să fie un număr. Reintrodu: ");
            }
        }
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.updateUserWeight(user.getId(), newWeight);

        if (success) {
            user.updateWeight(newWeight);
            System.out.println("✅ Greutatea a fost actualizată! Noua greutate este: " + user.getWeight() + " kg.");
        } else {
            System.out.println("❌ Eroare la actualizarea greutății în baza de date!");
        }
    }

    private void showWeightProgress() {
        UserDAO userDAO = new UserDAO();
        List<Double> weightHistory = userDAO.getWeightHistory(user.getId());

        user.setWeightHistory(weightHistory);
        user.printWeightProgress();
    }
}