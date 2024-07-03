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
            Toast.makeText(context, valid ? "OK" : "Invalid", Toast.LENGTH_SHORT).show();
            return valid;
        }
        db.close();
        Toast.makeText(context, "Invalid", Toast.LENGTH_SHORT).show();
        return false;
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

        db.close();
    }

    public List<Pair<String, Pair<Integer, Integer>>> getTop20Recipes(List<String> ingredients) {
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder ingredientsCondition = new StringBuilder();
        for (int i = 0; i < ingredients.size(); i++) {
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
}
