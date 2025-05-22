import java.awt.*;
import java.awt.geom.Point2D;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Canvas boyutu iste
        System.out.print("Enter canvas width: ");
        // For fixing input error if user enter different variable than int.
        int width = readIntNoPrompt(scanner, 1, Integer.MAX_VALUE);

        System.out.print("Enter canvas height: ");
        // For fixing input error if user enter different variable than int.
        int height = readIntNoPrompt(scanner, 1, Integer.MAX_VALUE);

        World world = new World(width, height);
        Turtle turtle = new Turtle(world, 0, 0);
        turtle.setDelay(0.009);  // Çizim hızı

        while (true) {
            System.out.println("\nHome Screen");
            System.out.println("1) Add Shape");
            System.out.println("2) Save Image");
            System.out.println("0) Exit");
            System.out.print("Enter choice: ");
            int choice = readInt(scanner, 0, 2);

            switch (choice) {
                case 1 -> addShape(scanner, turtle);
                case 2 -> {
                    System.out.print("Enter filename to save (e.g., drawing.png): ");
                    scanner.nextLine(); // önceki satırı temizle
                    String filename = scanner.nextLine();
                    world.saveAs(filename);
                    System.out.println("Image saved as " + filename);
                }
                case 0 -> {
                    System.out.println("Exiting application...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addShape(Scanner scanner, Turtle turtle) {
        System.out.println("\nWhich shape do you want to draw?");
        System.out.println("1) Square");
        System.out.println("2) Circle");
        System.out.println("3) Triangle");

        int shapeChoice = readInt(scanner, 1, 3);

        double sideLength = 0;
        double radius = 0;

        if (shapeChoice == 1 || shapeChoice == 3) {
            sideLength = readDouble(scanner, "Enter side length (> 0): ", 0.0);
        } else if (shapeChoice == 2) {
            radius = readDouble(scanner, "Enter radius (> 0): ", 0.0);
        }

        double border = readDouble(scanner, "Enter border width (> 0): ", 0.0);

        System.out.print("Enter RGB color values (e.g., 255 0 0): ");
        int r = 0, g = 0, b = 0;
        boolean validColor = false;
        while (!validColor) {
            try {
                r = scanner.nextInt();
                g = scanner.nextInt();
                b = scanner.nextInt();
                if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
                    System.out.println("RGB values must be between 0 and 255. Try again:");
                } else {
                    validColor = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid RGB format. Enter three numbers like: 255 0 0");
                scanner.nextLine(); // hatalı satırı temizle
            }
        }
        Color color = new Color(r, g, b);

        double x = readDouble(scanner, "Enter X coordinate: ", null);
        double y = readDouble(scanner, "Enter Y coordinate: ", null);
        Point2D location = new Point2D.Double(x, y);

        Shape shape = switch (shapeChoice) {
            case 1 -> new Square(turtle, location, color, border, sideLength);
            case 2 -> new Circle(turtle, location, color, border, radius);
            case 3 -> new Triangle(turtle, location, color, border, sideLength);
            default -> null;
        };

        if (shape != null) {
            shape.paint();
            Point2D pos = turtle.getLocation();
            System.out.printf("Turtle's location (%.1f, %.1f).\n", pos.getX(), pos.getY());
        }
    }

    // Güvenli double okuma
    private static double readDouble(Scanner scanner, String prompt, Double minValue) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = scanner.nextDouble();
                if (minValue != null && value <= minValue) {
                    System.out.println("Value must be greater than " + minValue);
                    continue;
                }
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid number. Please enter a numeric value.");
                scanner.nextLine(); // hatalı inputu temizle
            }
        }
    }

    // Güvenli int okuma
    private static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = scanner.nextInt();
                if (value < min || value > max) {
                    System.out.println("Value must be between " + min + " and " + max);
                    continue;
                }
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid number. Please enter an integer.");
                scanner.nextLine();
            }
        }
    }

    // Overload version
    private static int readInt(Scanner scanner, int min, int max) {
        return readInt(scanner, "Enter choice: ", min, max);
    }
    // For fixing input error if user enter different variable than int.
    private static int readIntNoPrompt(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int value = scanner.nextInt();
                if (value < min || value > max) {
                    System.out.println("Value must be between " + min + " and " + max);
                    continue;
                }
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid number. Please enter a valid integer.");
                scanner.nextLine();
            }
        }
    }
}
