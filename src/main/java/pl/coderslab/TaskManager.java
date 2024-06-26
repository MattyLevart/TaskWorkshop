package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    static final String FILE_NAME = "src/05_attachment_Zasoby do projektu.pl/tasks.csv";
    static final String[] OPTIONS = {"add","remove","list","exit"};
    static String[][] tasks;

    public static void showOptions(String[] tab) {
        System.out.println(ConsoleColors.BLUE);
        System.out.println("Please select an option: " + ConsoleColors.RESET);
        for (String option : tab) {
            System.out.println(option);
        }

    }
    public static void main(String[] args) {
        tasks = loadDataToTab(FILE_NAME);
        showOptions(OPTIONS);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String input = scanner.nextLine();
            switch (input) {
                case "exit":
                    saveTabToFile(FILE_NAME, tasks);
                    System.out.println(ConsoleColors.RED + "Bye, bye.");
                    System.exit(0);
                    break;
                case "add":
                    addTask();
                    break;
                case "remove":
                    removeTask(tasks,getTheNumber());
                    System.out.println("Wartość została usunięta.");
                    break;
                case "list":
                    printTab(tasks);
                    break;
                default:
                    System.out.println("Wybierz prawidłową opcję:");
            }
            showOptions(OPTIONS);
        }

    }



    public static String[][] loadDataToTab(String fileName){
        Path dir = Paths.get(fileName);
        if (!Files.exists(dir)){
            System.out.println("Plik nie istnieje");
            System.exit(0);
        }
        String[][] tab = null;

        try {
            List<String> strings = Files.readAllLines(dir);
            tab = new String[strings.size()][strings.get(0).split(",").length];

            for (int i =0; i<strings.size();i++){
                String[] split = strings.get(i).split(",");
                for (int j =0; j<split.length;j++){
                    tab[i][j] = split[j];
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return tab;
    }

    public static void printTab(String[][] tab){
        for (int i=0; i< tab.length; i++){
            System.out.println(i+" : ");
            for (int j =0; j<tab[i].length;j++){
                System.out.println(tab[i][j]+ " ");
            }
            System.out.println();
        }
    }

    private static void addTask(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Proszę wprowadzić opis zadania :");
        String description = scanner.nextLine();
        System.out.println("Proszę wprowadzić termin zakończenia zadania :");
        String dueDate = scanner.nextLine();
        System.out.println("Czy zadanie jest ważne? TAK/NIE");
        String isImportant = scanner.nextLine();

        tasks = Arrays.copyOf(tasks,tasks.length +1);
        tasks[tasks.length-1]= new String[3];
        tasks[tasks.length-1][0] = description;
        tasks[tasks.length-1][1] = dueDate;
        tasks[tasks.length-1][2] = isImportant;
    }

    public static boolean isNumberGreaterEqualZero(String input){
        if (NumberUtils.isParsable(input)){
            return  Integer.parseInt(input) >= 0;
        }
        return false;

    }

    public static int getTheNumber(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Proszę wybrać numer w celu usunięcia :");

        String n = scanner.nextLine();
        while (!isNumberGreaterEqualZero(n)) {
            System.out.println("Podano błędny argument. Proszę podać liczbę większą lub równą 0 :");
            n = scanner.nextLine();
        }
        return Integer.parseInt(n);
    }

    private static void removeTask(String[][] tab, int index){
        try{
            if (index < tab.length){
                tasks = ArrayUtils.remove(tab, index);
            }
        } catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("Nie ma takiego elementu w tabeli.");
        }
    }

    public static void saveTabToFile(String fileName, String[][] tab) {
        Path dir = Paths.get(fileName);

        String[] lines = new String[tasks.length];
        for (int i = 0; i < tab.length; i++) {
            lines[i] = String.join(",", tab[i]);
        }

        try {
            Files.write(dir, Arrays.asList(lines));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
