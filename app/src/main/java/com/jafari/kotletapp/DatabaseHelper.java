package com.jafari.kotletapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final Context context;

    private static final String DATABASE_NAME = "kotlet";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String USERS_COLUMN_ID = "_id";
    private static final String USERS_COLUMN_EMAIL = "email";
    private static final String USERS_COLUMN_PASSWORD = "password";
    private static final String USERS_COLUMN_FULL_NAME = "full_name";


    private static final String TABLE_INGREDIENTS = "ingredients";
    private static final String INGREDIENTS_COLUMN_ID = "_id";
    private static final String INGREDIENTS_COLUMN_NAME = "name";
    private static final String INGREDIENTS_COLUMN_CALORIE = "calorie";

    private static final String TABLE_RECIPES = "recipes";
    private static final String RECIPES_COLUMN_ID = "_id";
    private static final String RECIPES_COLUMN_NAME = "name";
    private static final String RECIPES_COLUMN_DESCRIPTION = "description";
    private static final String RECIPES_COLUMN_CREATOR = "creator";
    private static final String RECIPES_COLUMN_LIKES = "likes";

    private static final String TABLE_RECIPE_INGREDIENT = "recipe_ingredient";
    private static final String RECIPE_INGREDIENT_COLUMN_ID = "_id";
    private static final String RECIPE_INGREDIENT_COLUMN_RECIPE = "recipe";
    private static final String RECIPE_INGREDIENT_COLUMN_INGREDIENT = "ingredient";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                USERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERS_COLUMN_EMAIL + " TEXT UNIQUE, " +
                USERS_COLUMN_PASSWORD + " TEXT, " +
                USERS_COLUMN_FULL_NAME + " TEXT);");

        db.execSQL("CREATE TABLE " + TABLE_INGREDIENTS + " (" +
                INGREDIENTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                INGREDIENTS_COLUMN_NAME + " TEXT UNIQUE, " +
                INGREDIENTS_COLUMN_CALORIE + " REAL);");

        db.execSQL("CREATE TABLE " + TABLE_RECIPES + " (" +
                RECIPES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RECIPES_COLUMN_NAME + " TEXT, " +
                RECIPES_COLUMN_DESCRIPTION + " TEXT, " +
                RECIPES_COLUMN_CREATOR + " TEXT, " +
                RECIPES_COLUMN_LIKES + " INTEGER);");

        db.execSQL("CREATE TABLE " + TABLE_RECIPE_INGREDIENT + " (" +
                RECIPE_INGREDIENT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RECIPE_INGREDIENT_COLUMN_RECIPE + " INTEGER, " +
                RECIPE_INGREDIENT_COLUMN_INGREDIENT + " INTEGER, " +
                "FOREIGN KEY(" + RECIPE_INGREDIENT_COLUMN_RECIPE + ") REFERENCES " + TABLE_RECIPES + "(" + RECIPES_COLUMN_ID + "), " +
                "FOREIGN KEY(" + RECIPE_INGREDIENT_COLUMN_INGREDIENT + ") REFERENCES " + TABLE_INGREDIENTS + "(" + INGREDIENTS_COLUMN_ID + "));");

        // Insert recipes
        db.execSQL("INSERT INTO recipes (name, description) VALUES ('Avocado Toast', 'To make Avocado Toast, toast bread until golden brown. Mash ripe avocado and spread it on the toast. Add salt, pepper, and lemon juice to taste. Enjoy this quick and nutritious snack or breakfast option.')");
        db.execSQL("INSERT INTO recipes (name, description) VALUES ('Caprese Salad', 'To make Caprese Salad, slice fresh tomatoes and mozzarella cheese into rounds. Arrange them on a plate, alternating tomato and cheese slices. Scatter fresh basil leaves over the top. Drizzle with olive oil and balsamic vinegar. Season with salt and pepper to taste. Serve immediately as a light and refreshing appetizer or side dish.')");
        db.execSQL("INSERT INTO recipes (name, description) VALUES ('Spaghetti Aglio e Olio', 'To prepare Spaghetti Aglio e Olio, cook spaghetti according to package instructions until al dente. In a separate pan, heat olive oil and sauté minced garlic and red pepper flakes until fragrant. Toss the cooked spaghetti in the garlic-infused oil, season with salt and pepper, and sprinkle with chopped parsley. Serve hot with grated Parmesan cheese on top, if desired. Enjoy this simple and flavorful Italian dish!')");
        db.execSQL("INSERT INTO recipes (name, description) VALUES ('Chicken Stir-Fry', 'To make chicken stir-fry, sauté sliced chicken breast in a hot pan with olive oil until cooked through. Add sliced bell peppers, broccoli, and garlic, cooking until tender. Stir in soy sauce and sesame seeds, mixing well. Serve hot over rice or noodles.')");
        db.execSQL("INSERT INTO recipes (name, description) VALUES ('Greek Yogurt Parfait', 'To make a Greek yogurt parfait, layer Greek yogurt, honey, granola, fresh berries, nuts, and chia seeds in a glass. Repeat the layers and serve immediately for a delicious and nutritious breakfast or snack.')");
        db.execSQL("INSERT INTO recipes (name, description) VALUES ('Vegetable Quesadilla', 'To make a vegetable quesadilla, sauté diced onions, bell peppers, and black beans until tender. Heat a tortilla, sprinkle half with shredded cheddar cheese, add the sautéed veggies, and fold the tortilla. Cook until golden brown and cheese melts, then slice and serve with salsa.')");
        db.execSQL("INSERT INTO recipes (name, description) VALUES ('Banana Smoothie', 'To make a refreshing banana smoothie, combine ripe bananas, milk, Greek yogurt, honey, and ice cubes in a blender. Blend the ingredients until smooth and creamy. For added flavor, you can also include a dash of vanilla extract or a pinch of cinnamon. Serve the smoothie chilled in a glass, and enjoy it as a nutritious breakfast or a quick snack.')");

        // Insert ingredients
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Bread', 265)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Avocado', 160)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Olive oil', 119)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Lemon juice', 6)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Salt', 0)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Pepper', 5)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Tomatoes', 18)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Fresh mozzarella', 280)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Basil leaves', 23)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Balsamic vinegar', 14)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Spaghetti', 158)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Garlic', 149)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Red pepper flakes', 6)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Parsley', 36)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Parmesan cheese', 431)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Chicken breast', 165)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Bell peppers', 24)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Broccoli', 55)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Soy sauce', 53)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Sesame seeds', 573)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Greek yogurt', 59)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Honey', 64)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Granola', 471)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Fresh berries', 32)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Nuts', 607)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Chia seeds', 486)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Tortillas', 159)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Cheddar cheese', 403)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Onion', 40)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Black beans', 132)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Salsa', 36)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Bananas', 89)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Milk', 42)");
        db.execSQL("INSERT INTO ingredients (name, calorie) VALUES ('Ice cubes', 0)");

        // Insert recipe_ingredient relationships
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (1, 1)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (1, 2)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (1, 3)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (1, 4)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (1, 5)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (1, 6)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (2, 7)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (2, 8)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (2, 9)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (2, 3)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (2, 10)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (2, 5)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (2, 6)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (3, 11)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (3, 12)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (3, 3)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (3, 13)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (3, 14)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (3, 15)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (3, 5)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (4, 16)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (4, 17)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (4, 18)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (4, 19)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (4, 12)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (4, 3)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (4, 20)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (5, 21)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (5, 22)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (5, 23)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (5, 24)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (5, 25)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (5, 26)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (6, 27)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (6, 28)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (6, 17)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (6, 29)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (6, 30)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (6, 31)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (7, 32)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (7, 33)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (7, 21)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (7, 22)");
        db.execSQL("INSERT INTO recipe_ingredient (recipe, ingredient) VALUES (7, 34)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENT);
        onCreate(db);
    }

    public boolean addUser(String email, String password, String full_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USERS_COLUMN_EMAIL, email);
        cv.put(USERS_COLUMN_PASSWORD, password);
        cv.put(USERS_COLUMN_FULL_NAME, full_name);

        long result = db.insert(TABLE_USERS, null, cv);
        Toast.makeText(context, result == -1 ? "Failed to add user" : "User added successfully", Toast.LENGTH_SHORT).show();
        db.close();
        return result != -1;
    }

    public boolean validUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + USERS_COLUMN_EMAIL + "=? AND " + USERS_COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        if (cursor != null) {
            boolean valid = cursor.getCount() > 0;
            cursor.close();
            Toast.makeText(context, valid ? "Welcome" : "Invalid", Toast.LENGTH_SHORT).show();
            return valid;
        }
        db.close();
        Toast.makeText(context, "Invalid", Toast.LENGTH_SHORT).show();
        return false;
    }

    public String getFullName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + USERS_COLUMN_EMAIL + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_FULL_NAME));
            cursor.close();
            return fullName;
        }
        return "";
    }

    public String[] allIngredients() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + INGREDIENTS_COLUMN_NAME + " FROM " + TABLE_INGREDIENTS;
        Cursor cursor = db.rawQuery(query, null);

        List<String> ingredientsList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String ingredient = cursor.getString(cursor.getColumnIndexOrThrow(INGREDIENTS_COLUMN_NAME));
                ingredientsList.add(ingredient);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        System.out.println("printing " + Arrays.toString(ingredientsList.toArray(new String[0])));

        return ingredientsList.toArray(new String[0]);
    }

    public void addNewIngredient(String name, double calorie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(INGREDIENTS_COLUMN_NAME, name);
        cv.put(INGREDIENTS_COLUMN_CALORIE, calorie);

        long newRowId = db.insert(TABLE_INGREDIENTS, null, cv);

        System.out.println("adding " + name);

        db.close();
    }

    public boolean validIngredient(String ingredient) {
        String[] all = allIngredients();
        for (String i : all)
            if (i.equals(ingredient))
                return true;
        return false;
    }

    public void addNewRecipe(String name, List<String> ingredients, String description, String creator) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(RECIPES_COLUMN_NAME, name);
        cv.put(RECIPES_COLUMN_CREATOR, creator);
        cv.put(RECIPES_COLUMN_DESCRIPTION, description);
        cv.put(RECIPES_COLUMN_LIKES, 0);

        long newRowId = db.insert(TABLE_RECIPES, null, cv);
        System.out.println("new row id = " + newRowId);

        for (String ingredient: ingredients) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(RECIPE_INGREDIENT_COLUMN_RECIPE, newRowId);
            String query = "SELECT " + INGREDIENTS_COLUMN_ID + " FROM " + TABLE_INGREDIENTS +
                            " WHERE " + INGREDIENTS_COLUMN_NAME + "=?;";
            Cursor cursor = db.rawQuery(query, new String[]{ingredient});
            if (cursor != null && cursor.moveToFirst()) {
                System.out.println("looking for " + ingredient);
                System.out.println(cursor.getLong(0));
                contentValues.put(RECIPE_INGREDIENT_COLUMN_INGREDIENT, cursor.getLong(0));
                cursor.close();
            }
            db.insert(TABLE_RECIPE_INGREDIENT, null, contentValues);
        }
        Toast.makeText(context , "recipe added successfully!" , Toast.LENGTH_SHORT).show();
        db.close();
    }

    public List<Pair<String, Pair<Integer, Integer>>> getTop20Recipes(List<String> ingredients) {
        SQLiteDatabase db = this.getReadableDatabase();

        System.out.println(ingredients.size());

        StringBuilder ingredientsCondition = new StringBuilder();
        for (int i = 0; i < ingredients.size(); i++) {
            System.out.println(ingredients.get(i));
            ingredientsCondition.append("'").append(ingredients.get(i)).append("'");
            if (i < ingredients.size() - 1)
                ingredientsCondition.append(", ");
        }

        System.out.println("number of rows in RI = " + this.getTableRowCount(TABLE_RECIPE_INGREDIENT));
        System.out.println("total number of ingredients = " + this.getTableRowCount(TABLE_RECIPES));

        String query = "SELECT r." + RECIPES_COLUMN_NAME + ", r." + RECIPES_COLUMN_ID + ", COUNT(ri." + RECIPE_INGREDIENT_COLUMN_INGREDIENT + ") AS score " +
                "FROM " + TABLE_RECIPES + " r " +
                "JOIN " + TABLE_RECIPE_INGREDIENT + " ri ON r." + RECIPES_COLUMN_ID + " = ri." + RECIPE_INGREDIENT_COLUMN_RECIPE + " " +
                "JOIN " + TABLE_INGREDIENTS + " i ON ri." + RECIPE_INGREDIENT_COLUMN_INGREDIENT + " = i." + INGREDIENTS_COLUMN_ID + " " +
                "WHERE i." + INGREDIENTS_COLUMN_NAME + " IN (" + ingredientsCondition.toString() + ") " +
                "GROUP BY r." + RECIPES_COLUMN_ID + " " +
                "ORDER BY score DESC " +
                "LIMIT 20;";

        System.out.println("outer query " + query);

        Cursor cursor = db.rawQuery(query, null);

        List<Pair<String, Pair<Integer, Integer>>> resultList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                System.out.println("begin");
                String recipeName = cursor.getString(cursor.getColumnIndexOrThrow(RECIPES_COLUMN_NAME));
                System.out.println("recipe name: " + recipeName);
                long recipeID = cursor.getLong(cursor.getColumnIndexOrThrow(RECIPES_COLUMN_ID));
                System.out.println("hey, recipe id = " + recipeID);
                int currentIngredients = cursor.getInt(cursor.getColumnIndexOrThrow("score"));
                System.out.println("score = " + currentIngredients);
                String innerQuery = "SELECT COUNT(*) FROM " +
                                        TABLE_RECIPE_INGREDIENT +
                                        " WHERE " + RECIPE_INGREDIENT_COLUMN_RECIPE + "=" + recipeID +
                                        " GROUP BY " + RECIPE_INGREDIENT_COLUMN_RECIPE;
                System.out.println("inner query " + innerQuery);
                Cursor innerCursor = db.rawQuery(innerQuery, null);
                if (innerCursor.moveToFirst()) {
                    do {
                        System.out.println("hello there");
                        int totalIngredients = innerCursor.getInt(0);
                        System.out.println(totalIngredients);
                        resultList.add(new Pair<>(recipeName, new Pair<>(totalIngredients, currentIngredients)));
                    } while (innerCursor.moveToNext());
                }
                innerCursor.close();
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return resultList;
    }

    public void likeRecipe(int recipeID) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            String query = "UPDATE " + TABLE_RECIPES +
                    " SET " + RECIPES_COLUMN_LIKES + " = " + RECIPES_COLUMN_LIKES + " + 1" +
                    " WHERE " + RECIPES_COLUMN_ID + " = " + recipeID;
            db.execSQL(query);
        }
    }

    public int getTableRowCount(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + tableName;
        Cursor cursor = db.rawQuery(query, null);

        int rowCount = 0;
        if (cursor.moveToFirst()) {
            rowCount = cursor.getInt(0);
        }

        cursor.close();
        // db.close(); //TODO

        return rowCount;
    }

    public String[] getNameAndDescriptionOfRecipe(String recipe) {
        SQLiteDatabase db = this.getReadableDatabase();
        String description = null;
        String name = null;

        String query = "SELECT " + RECIPES_COLUMN_NAME + ", " + RECIPES_COLUMN_DESCRIPTION +
                        " FROM " + TABLE_RECIPES +
                        " WHERE " + RECIPES_COLUMN_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{recipe});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndexOrThrow(RECIPES_COLUMN_NAME));
                description = cursor.getString(cursor.getColumnIndexOrThrow(RECIPES_COLUMN_DESCRIPTION));
            }
            cursor.close();
        }
        db.close();

        return new String[]{name, description};
    }

    public String[] getIngredients(String recipe) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> ingredientsList = new ArrayList<>();

        String query = "SELECT i." + INGREDIENTS_COLUMN_NAME +
                " FROM " + TABLE_INGREDIENTS + " i" +
                " JOIN " + TABLE_RECIPE_INGREDIENT + " ri ON i." + INGREDIENTS_COLUMN_ID + " = ri." + RECIPE_INGREDIENT_COLUMN_INGREDIENT +
                " JOIN " + TABLE_RECIPES + " r ON ri." + RECIPE_INGREDIENT_COLUMN_RECIPE + " = r." + RECIPES_COLUMN_ID +
                " WHERE r." + RECIPES_COLUMN_NAME + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{recipe});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String ingredient = cursor.getString(cursor.getColumnIndexOrThrow(INGREDIENTS_COLUMN_NAME));
                    ingredientsList.add(ingredient);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();

        return ingredientsList.toArray(new String[0]);
    }
}
