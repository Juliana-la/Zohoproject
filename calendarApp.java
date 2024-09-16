import java.util.*;

// Class to represent an event
class Event {
    String title;
    String description;
    int day;
    int month;
    int year;

    public Event(String title, String description, int day, int month, int year) {
        this.title = title;
        this.description = description;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @Override
    public String toString() {
        return day + "/" + month + "/" + year + " - " + title + ": " + description;
    }
}

public class calendarApp {

    private static List<Event> events = new ArrayList<>();

    // Method to determine if the year is a leap year
    public static boolean isLeapYear(int year) {
        if (year % 400 == 0) {
            return true;
        } else if (year % 100 == 0) {
            return false;
        } else if (year % 4 == 0) {
            return true;
        } else {
            return false;
        }
    }

    // Method to get the number of days in a given month
    public static int getDaysInMonth(int month, int year) {
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return 31;
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return (isLeapYear(year)) ? 29 : 28;
            default:
                return 0;  // Invalid month
        }
    }

    // Method to calculate the day of the week for the first day of a month
    // Using Zeller's Congruence
    public static int getFirstDayOfMonth(int month, int year) {
        if (month == 1 || month == 2) {
            month += 12;
            year -= 1;
        }

        int K = year % 100;  // Year of the century
        int J = year / 100;  // Zero-based century (i.e., 20 for 2000-2099)

        int firstDay = (1 + (13 * (month + 1)) / 5 + K + K / 4 + J / 4 + 5 * J) % 7;

        // Zeller's formula gives Saturday as 0, so we adjust it to make Sunday 0, Monday 1, etc.
        return (firstDay + 6) % 7;  // Adjust so Sunday is 0, Monday is 1, and so on.
    }

    // Method to print a month calendar
    public static void printMonthCalendar(int month, int year) {
        String[] months = { "", "January", "February", "March", "April", "May", "June", 
                            "July", "August", "September", "October", "November", "December" };
        
        // Print month and year
        System.out.println("\n    " + months[month] + " " + year);
        System.out.println("Sun Mon Tue Wed Thu Fri Sat");

        // Get the first day of the month and the number of days in the month
        int firstDay = getFirstDayOfMonth(month, year);
        int daysInMonth = getDaysInMonth(month, year);
        
        // Print leading spaces for the first week
        for (int i = 0; i < firstDay; i++) {
            System.out.print("    ");
        }

        // Print the days of the month
        for (int day = 1; day <= daysInMonth; day++) {
            System.out.printf("%3d ", day);

            // Go to the next line after Saturday
            if ((day + firstDay) % 7 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    // Method to navigate through months in a year
    public static void navigateYearCalendar(int year) {
        Scanner scanner = new Scanner(System.in);
        int currentMonth = 1;
        String[] months = { "", "January", "February", "March", "April", "May", "June", 
                        "July", "August", "September", "October", "November", "December" };

        while (true) {
            printMonthCalendar(currentMonth, year);
            printEventsForMonth(currentMonth, year);

            // Loop until a valid command is given
            String command;
            while (true) {
                // Ask for user input to navigate
                System.out.print("Enter 'next' to go to the next month, 'prev' to go to the previous month,'add' to add events, or 'exit' to stop: ");
                command = scanner.next().toLowerCase();

                // Check if the input is valid
                if (command.equals("next") || command.equals("prev") || command.equals("add") || command.equals("exit")) {
                    break;  // Exit loop if valid input
                } else {
                    System.out.println("Invalid command... Try again :(");
                }
            }

            // Process valid commands
            switch (command) {
                case "next":
                    if (currentMonth < 12) {
                        currentMonth++;
                    } else {
                        currentMonth = 1;
                        year++;  // Move to the next year
                        System.out.println("You are moving to " + months[currentMonth] + " " + year);
                    }
                    break;
                case "prev":
                    if (currentMonth > 1) {
                        currentMonth--;
                    } else {
                        currentMonth = 12;
                        year--;  // Move to the previous year
                        System.out.println("You are moving to " + months[currentMonth] + " " + year);
                    }
                    break;
                case "add":
                    addEvent(scanner, currentMonth, year);
                    break;
                case "exit":
                    return;  // Exit the loop and method
            }
        }
    }
    
    // Method to get a valid integer input from the user with proper error handling
    public static int getValidIntegerInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input... Try again :(");
                scanner.next();  // Clear the invalid input
            }
        }
    }

    // Method to add an event
    public static void addEvent(Scanner scanner, int month, int year) {
        int day;
        do {
            day = getValidIntegerInput(scanner, "Enter the day (1-" + getDaysInMonth(month, year) + "): ");
            if (day < 1 || day > getDaysInMonth(month, year)) {
                System.out.println("Invalid day... Try again :(");
            }
        } while (day < 1 || day > getDaysInMonth(month, year));

        System.out.print("Enter event title: ");
        scanner.nextLine();  // Consume the newline
        String title = scanner.nextLine();

        System.out.print("Enter event description: ");
        String description = scanner.nextLine();

        Event newEvent = new Event(title, description, day, month, year);//creating a new event object
        events.add(newEvent);
        System.out.println("Event added: " + newEvent);
    }

    // Method to print all events for a given month
    public static void printEventsForMonth(int month, int year) {
        boolean found = false;
        System.out.println("Events for " + month + "/" + year + ":");
        for (Event event : events) {
            if (event.month == month && event.year == year) {
                System.out.println(event);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No events found.");
        }
    }

    // Method to ask if the user wants to replay or start over
    public static boolean replay(Scanner scanner) {
        while(true){
            System.out.print("Do you want to start over (enter 'yes' or 'no')? ");
            String response = scanner.next().toLowerCase();
            if(yesNo(response)){
                return response.equals("yes");
            }
            else{
                System.out.println("Invalid input......Try again:(");
            }
        }           
    }

    public static boolean yesNo(String response){
        if(response.equals("yes") || response.equals("no")){
            return true;
        }
        else{
            return false;
        }
    }

    public static void addEventforMonth(Scanner scanner,int month,int year){
        while(true){
            System.out.print("Do you want to add any events (enter 'yes' or 'no')? ");
            String response=scanner.next().toLowerCase();
            if(yesNo(response)){
                if(response.equals("yes")){
                    addEvent(scanner, month, year);
                }
                else{
                    break;
                }
            }
            else{
                System.out.println("Invalid input....Try again:(");
            }
        } 
    }


    // Main method with input validation and replay feature
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        do {
            // Input year with validation
            int year = getValidIntegerInput(scanner, "Enter the year: ");

            // Input choice (month or year) with validation
            int choice;
            do {
                choice = getValidIntegerInput(scanner, "Do you want to print a (1) month or (2) year calendar? Enter 1 or 2: ");
                if (choice != 1 && choice != 2) {
                    System.out.println("Invalid input... Try again :(");
                }
            } while (choice != 1 && choice != 2);

            // If month is chosen, ask for the month input with validation
            if (choice == 1) {
                int month;
                do {
                    month = getValidIntegerInput(scanner, "Enter the month (1-12): ");
                    if (month < 1 || month > 12) {
                        System.out.println("Invalid input... Try again :(");
                    }
                } while (month < 1 || month > 12);

                printMonthCalendar(month, year);
                printEventsForMonth(month, year);
                addEventforMonth(scanner,month,year);
            } else if (choice == 2) {
                navigateYearCalendar(year);
            }
        } while (replay(scanner));

        scanner.close();
    }
}
