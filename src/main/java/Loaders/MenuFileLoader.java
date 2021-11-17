package Loaders;

import Entities.MenuItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MenuFileLoader {

    private final static String[] HEADERS = { "MenuItem Name", "Type", "Price", "Calorie" };
    private String fileName;

    private static final int CALORIE_PART = 3;

    public MenuFileLoader(String fileName) {
        this.fileName = fileName;
    }

    public File getDefaultFile() {
        return new File("./menu.txt");
    }

    public List<MenuItem> load() {
        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        int numberOfColumns = HEADERS.length;
        boolean useDefault = true;

        List<List<String>> result = new ArrayList<List<String>>();

        extracted(numberOfColumns, useDefault, result);
        for (List<String> line : result) {
            String dishName = line.get(0).trim();
            String dishType = line.get(1).toUpperCase().trim();
            double dishPrice = Double.parseDouble(line.get(2).trim());
            double dishCalorie = Double.parseDouble(line.get(CALORIE_PART).trim());
            MenuItem menuItem = new MenuItem(dishName, dishType, dishPrice, dishCalorie);
            menuItems.add(menuItem);
        }
        return menuItems;
    }

    private void extracted(int numberOfColumns, boolean useDefault, List<List<String>> result) {
        BufferedReader breader = null;
        File file;
        try {
            file = new File(fileName);
            if (!file.exists()) {
                if (useDefault) {
                    file = getDefaultFile();
                } else {
                    String error = "The specified Components.Menu File does not exist.";
                    throw new IllegalArgumentException(error);
                }
            }

            breader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

            String line = breader.readLine();
            extracted2(numberOfColumns, result, breader, line);
        } catch (IOException e) {
            // no handler
        } finally {
            try {
                if (breader != null) {
                    breader.close();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void extracted2(int numberOfColumns, List<List<String>> result, 
    BufferedReader breader, String line)
            throws IOException {
        while (line != null) {
            String[] entries = line.split(",");
            List<String> lineEntry = new ArrayList<String>();

            if (entries.length != numberOfColumns) {
                throw new IllegalArgumentException("The specified Columns are incorrect.");
            }
            for (String entry : entries) {
                lineEntry.add(entry);
            }
            result.add(lineEntry);
            line = breader.readLine();
        }
    }
}
