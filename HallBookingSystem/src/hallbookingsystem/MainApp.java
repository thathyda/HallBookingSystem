package hallbookingsystem;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MainApp {
    static Scanner scanner = new Scanner(System.in);
    static int row = 0, seat = 0;
    static int historyIndex = 0;
    static int no = 1;
    static int f = 0;
    static LocalDate DateTime;

    public static void logo() {
        String logo = """
                    __  __      _                  _   _            _       ____     ____      \s
                  U|' \\/ '|uU  /"\\  u     ___     | \\ |"|       U  /"\\  u U|  _"\\ uU|  _"\\ u   \s
                  \\| |\\/| |/ \\/ _ \\/     |_"_|   <|  \\| |>       \\/ _ \\/  \\| |_) |/\\| |_) |/   \s
                   | |  | |  / ___ \\      | |    U| |\\  |u       / ___ \\   |  __/   |  __/     \s
                   |_|  |_| /_/   \\_\\   U/| |\\u   |_| \\_|       /_/   \\_\\  |_|      |_|        \s
                  <<,-,,-.   \\\\    >>.-,_|___|_,-.||   \\\\,-.     \\\\    >>  ||>>_    ||>>_      \s
                   (./  \\.) (__)  (__)\\_)-' '-(_/ (_")  (_/     (__)  (__)(__)__)  (__)__)     \s
                                                                                                     
                """;
        System.out.println(logo);

    }

    public static void main(String[] args) {
        logo();
        index();
        String[][] hallA = new String[row][seat];
        String[][] hallB = new String[row][seat];
        String[][] hallC = new String[row][seat];
        String[] histroy = new String[row * seat];
        String[] found = new String[seat];
        while (true) {
            ruler();
            System.out.println(" [ Application Menu] ");
            option();
            String option = validate("[a-fA-F]", "optionMenu");
            boolean isBook = false;
            switch (option) {
                case "A", "a" -> booking(hallA, hallB, hallC, histroy, found);
                case "B", "b" -> display(hallA, hallB, hallC);
                case "C", "c" -> showTime();
                case "D", "d" -> {

                    reboot(hallA, hallB, hallC, histroy);
                    histroy = new String[histroy.length];
                }
                case "E", "e" -> showHistory(histroy);
                case "F", "f" -> System.exit(0);
            }
        }
    }


    public static void index() {
        System.out.println("# ".repeat(30));
        System.out.println("#" + " ".repeat(7) + "Hall Booking System" + " ".repeat(8) + "###");
        System.out.println("# ".repeat(30));
        row = Integer.parseInt(validate("^(1[0-9]?|2[0-6]?|[3-9])$$$", "row"));
        seat = Integer.parseInt(validate("^(1[0-9]?|2[0-6]?|3[0]?|[4-9])$$$", "seat"));
        System.out.println("Created hall successfully with  " + (row * seat) + " seats in hall");
    }

    public static void booking(String[][] hallA, String[][] hallB, String[][] hallC, String[] history, String[] found) {
        ruler();
        System.out.println(" +++----------------------------+++");
        System.out.println(" Start booking process");
        System.out.println("+++-----------------------------+++");
        System.out.println("### Showtime information");
        showTime();
        System.out.print("Please select option: ");
        String optionShowtime = scanner.nextLine();

        switch (optionShowtime) {
            case "A", "a" -> {
                System.out.println("Hall A Morning");
                displayEachHall(hallA, hallB, hallC, 1);
                bookingEachHall(hallA, hallB, hallC, history, found, 1);
            }
            case "B", "b" -> {
                System.out.println("Hall A Evening");
                displayEachHall(hallA, hallB, hallC, 2);
                bookingEachHall(hallA, hallB, hallC, history, found, 2);
            }
            case "C", "c" -> {
                System.out.println("Hall A Night");
                displayEachHall(hallA, hallB, hallC, 3);
                bookingEachHall(hallA, hallB, hallC, history, found, 3);
            }
        }
    }

    public static void bookingEachHall(String[][] hallA, String[][] hallB, String[][] hallC, String[] history, String[] found, int option) {
        System.out.println("### INSTRUCTION");
        System.out.println("### Single: C-1 ");
        System.out.println("### Multiple (Separate by comma) : C-1, C-2");
        System.out.print("> Please select available seat: ");
        String hallAInput = scanner.nextLine().replaceAll("\\s", "");
        String[] seats = hallAInput.toUpperCase().split(",");

        // check seat fo available or not
        int count = 0;
        boolean seatFound = false;
        for (String seat : seats) {
            for (int i = 0; i < hallA.length; i++) {
                if (i <= 26) {
                    String tempRow = String.valueOf((char) ('A' + i));
                    String perRow = tempRow + "-";
                    for (int j = 0; j < hallA[i].length; j++) {
                        String item = perRow + (j + 1);
                        if (seat.equals(item)) {
                            seatFound = true;
                            if (option == 1) {
                                if (hallA[i][j] == null) {
                                    System.out.println(item + " is available");
                                    count = 1;
                                } else {
                                    System.out.println(item + " is no available");
                                }
                            } else if (option == 2) {
                                if (hallB[i][j] == null) {
                                    System.out.println(item + " is available");
                                    count = 1;
                                } else {
                                    System.out.println(item + " is no available");
                                }
                            } else if (option == 3) {
                                if (hallC[i][j] == null) {
                                    System.out.println(item + " is available");
                                    count = 1;
                                } else {
                                    System.out.println(item + " is no available");
                                }
                            }
                        }
                    }
                }
                if (seatFound) {
                    break;
                }
            }
            if (!seatFound) {
                System.out.println("Seat " + seat + " not found.");
            }
        }


        if (count == 0) return;
        boolean isBooking = false;

        String time = null;
        // booking process
        System.out.print("Do you want to book [y/n]: ");
        String check = scanner.nextLine();
        int t = 0;
        StringBuilder temp = new StringBuilder();
        String[] test = new String[row];
        if (check.equals("y")) {
            int id;
            String tempItem = null;
            System.out.print("Please enter Student ID fo booking : ");
            id = scanner.nextInt();
            for (String seat : seats) {
                for (int i = 0; i < hallA.length; i++) {
                    if (i <= 26) {
                        String tempRow = String.valueOf((char) ('A' + i));
                        String perRow = tempRow + "-";
                        for (int j = 0; j < hallA[i].length; j++) {
                            String item = perRow + (j + 1);
                            if (seat.equals(item)) {
                                if (option == 1) {
                                    if (hallA[i][j] == null) {
                                        hallA[i][j] = String.valueOf(id);
                                        isBooking = true;
                                        DateTime = LocalDate.now();
                                        //tempItem = item;
                                        found[f++] = String.format("%s", tempItem);
                                        temp.append(",").append(item);
                                    }
                                } else if (option == 2) {
                                    if (hallB[i][j] == null) {
                                        hallB[i][j] = String.valueOf(id);
                                        isBooking = true;
                                        found[f++] = String.format("%s", tempItem);
                                        temp.append(",").append(item);
                                    }
                                } else if (option == 3) {
                                    if (hallC[i][j] == null) {
                                        hallC[i][j] = String.valueOf(id);
                                        isBooking = true;
                                        found[f++] = String.format("%s", tempItem);
                                        temp.append(",").append(item);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // history;
            time = String.valueOf(DateTime);
            String resultBooking = "\b\b" + temp;
            history[historyIndex++] = String.format(" No: %d\n Hall: Morning\n Student ID: %d\n Seats: %s Date\n : %s ", no, id, resultBooking, time);
            //System.out.println("Booking history updated.");
            no++;
        }
        if (isBooking) System.out.println("Booking successfully ...!");
    }

    public static void showHistory(String[] history) {

        boolean isAllHistoryNull = true;
        for (String his : history) {
            if (his != null) {
                isAllHistoryNull = false;

                break;
            }
        }
        if(isAllHistoryNull){
            System.out.println("================================");
            System.out.println("   There is no history to show ");
            System.out.println("===============================");
            return;
        }
        for (String his : history) {
            if (his != null) {
                System.out.println("\n " + his + "\n ");
            }
        }


    }

    public static void display(String[][] hallA, String[][] hallB, String[][] hallC) {
        String booking = "";
        for (int z = 0; z < 3; z++) {
            String[][] tempHall = (z == 0) ? hallA : (z == 1) ? hallB : hallC;
            String header = (z == 0) ? "Morning" : (z == 1) ? "Afternoon" : "Night";
            System.out.println("# Hall " + header);
            for (int i = 0; i < tempHall.length; i++) {
                if (i <= 36) {
                    String tempRow = String.valueOf((char) ('A' + i));
                    String perRow = "|" + tempRow;
                    for (int j = 0; j < tempHall[i].length; j++) {
                        if (tempHall[i][j] == null) booking = "AV";
                        else booking = "BO";
                        System.out.print(perRow + "-" + (j + 1) + "::" + booking + "|| ");
                    }
                }
                System.out.println();
            }
            ruler();
        }
    }

    public static void displayEachHall(String[][] hallA, String[][] hallB, String[][] hallC, int option) {
        String tempRow, perRow, booking = "";
        for (int i = 0; i < hallA.length; i++) {
            if (i <= 36) {
                tempRow = String.valueOf((char) ('A' + i));
                perRow = "||" + tempRow;
                for (int j = 0; j < hallA[i].length; j++) {
                    if (option == 1) {
                        if (hallA[i][j] == null) booking = "AV";
                        else booking = "BO";
                        System.out.print(perRow + "-" + (j + 1) + "::" + booking + "|| ");
                    } else if (option == 2) {
                        if (hallB[i][j] == null) booking = "AV";
                        else booking = "BO";
                        System.out.print(perRow + "-" + (j + 1) + "::" + booking + "|| ");
                    } else if (option == 3) {
                        if (hallC[i][j] == null) booking = "AV";
                        else booking = "BO";
                        System.out.print(perRow + "-" + (j + 1) + "::" + booking + "|| ");
                    }
                }
                System.out.println();
            }
        }
    }

    public static void reboot(String[][] hallA, String[][] hallB, String[][] hallC, String[] history) {
        System.out.println("Do you want to reboot : yes or no [y/n]: ");
        String check = scanner.nextLine();
        boolean isCheck = false;
        if (check.equalsIgnoreCase("yes") || check.equalsIgnoreCase("y")) {
            //  history = new String[history.length];


            historyIndex = 0;
            no = 0;
            for (int i = 0; i < hallA.length; i++) {
                for (int j = 0; j < hallA[i].length; j++) {
                    hallA[i][j] = null;
                    hallB[i][j] = null;
                    hallC[i][j] = null;

                }
            }
//            for (String h : history) {
//                h = null;
//            }
            isCheck = true;
        }
        if (isCheck) System.out.println("Reboot Showtime successfully...!!!");
        else System.out.println("Thank you no reboot ...!!!");
    }

    public static void history(String[][] hallA, String[][] hallB, String[][] hallC) {

    }

    public static void ruler() {
        System.out.println("#".repeat(40));
    }

    public static void option() {
        ruler();
        System.out.println("<A> Booking");
        System.out.println("<B> Hall");
        System.out.println("<C> ShowTime");
        System.out.println("<D> Reboot ShowTime");
        System.out.println("<E> History");
        System.out.println("<F> Exit");
    }

    public static void showTime() {
        System.out.println();
        System.out.println(" # ".repeat(15));
        System.out.println("Daily Showtime of ISTAD Hall:");
        System.out.println("A) Morning ( 10:00AM - 12:30PM )");
        System.out.println("B) Afternoon ( 3:00PM - 5:30PM )");
        System.out.println("C) Night ( 7:00PM - 9:30PM )");
        System.out.println(" # ".repeat(15));
        System.out.println();
    }

    public static String validate(String regex, String op) {
        Pattern pattern = Pattern.compile(regex);
        while (true) {
            if (Objects.equals(op, "row")) System.out.print("Config total row in hall : ");
            if (Objects.equals(op, "seat")) System.out.print("Config seats per row in hall : ");
            if (Objects.equals(op, "optionMenu")) System.out.print(" => please select menu no(A-F): ");
            if (Objects.equals(op, "optionShowtime")) System.out.print("Please select showtime (A || B || C) : ");
            if (Objects.equals(op, "option")) System.out.print("Please choose option: ");
            if (Objects.equals(op, "booking")) System.out.print("> Please select available seat: ");
            if (Objects.equals(op, "yn")) System.out.print("please choose yes or no [y/n]: ");
            if (Objects.equals(op, "BookingHall")) {
                System.out.print("> Please select available seat: ");
            }
            String userInput = scanner.nextLine();
            if (pattern.matcher(userInput).matches()) {
                return userInput;
            } else {
                if (Objects.equals(op, "row"))
                    System.out.println("invalid, you should be input row range 1-36 ...!");
                if (Objects.equals(op, "seat"))
                    System.out.println("invalid, you should be input seat range 1-40 ...!");
                if (Objects.equals(op, "optionMenu"))
                    System.out.println("invalid, you should be input option A to F ...!");
                if (Objects.equals(op, "booking"))
                    System.out.print("Invalid format, please try again ....!");
            }
        }
    }
}