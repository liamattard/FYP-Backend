package com.example.fypBackend.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.example.fypBackend.entities.Characteristics;

public class Tools {

    public static String COMMA_DELIMITER = ",";

    public static HashMap<Integer, String> categories = new HashMap<Integer, String>();

    public static HashMap<String, String> facebookCategories = new HashMap<String, String>();

    public static Characteristics updateUserLikes(String facebookCategory, Characteristics characteristics) {

        if (facebookCategories.containsKey(facebookCategory)) {
            System.out.println("Facebook Category = " + facebookCategory);
            String category = facebookCategories.get(facebookCategory);
            System.out.println("CATEGORY: " + category);
            if (category.equals("Museums")) {
                characteristics.setMuseums();
            } else if (category.equals("Beach")) {
                characteristics.setBeach();
            } else if (category.equals("Bar")) {
                characteristics.setBars();
            } else if (category.equals("Nature")) {
                characteristics.setNature();
            } else if (category.equals("Shopping")) {
                characteristics.setShopping();
            } else if (category.equals("Clubbing")) {
                characteristics.setNight_club();
            }

        }
        return characteristics;
    }

    public static Characteristics changeUserCategory(int categoryId, Characteristics characteristics) {

        if (categories.containsKey(categoryId)) {
            String category = categories.get(categoryId);
            System.out.println("CATEGORY: " + category);
            if (category.equals("Museums")) {
                characteristics.setMuseums();
            } else if (category.equals("Beach")) {
                characteristics.setBeach();
            } else if (category.equals("Bars")) {
                characteristics.setBars();
            } else if (category.equals("Nature")) {
                characteristics.setNature();
            } else if (category.equals("Shopping")) {
                characteristics.setShopping();
            } else if (category.equals("Clubbing")) {
                characteristics.setNight_club();
            }

        }
        return characteristics;
    }

    public static void setLikesApiCategories() throws IOException {

        List<List<String>> categories = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("assets/likes_classification.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                categories.add(Arrays.asList(values));
            }
        }

        for (List<String> row : categories) {
            Tools.facebookCategories.put(row.get(0), row.get(1));
        }

    }

    public static void setPlacesApiCategories() throws IOException {

        List<List<String>> categories = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("assets/image_classificationP2.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                categories.add(Arrays.asList(values));
            }
        }

        for (List<String> row : categories) {

            Tools.categories.put(Integer.parseInt(row.get(0)), row.get(1));
        }

    }

}
