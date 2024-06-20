package org.scrum.psd.battleship.ascii;

import com.microsoft.applicationinsights.core.dependencies.apachecommons.lang3.math.NumberUtils;
import org.scrum.psd.battleship.controller.GameController;
import org.scrum.psd.battleship.controller.dto.Letter;
import org.scrum.psd.battleship.controller.dto.Position;
import org.scrum.psd.battleship.controller.dto.Ship;

import java.util.*;
import java.util.stream.Collectors;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

public class Main {
  private static final Telemetry telemetry = new Telemetry();
  private static List<Ship> myFleet;
  private static List<Ship> enemyFleet;

  public static void main(String[] args) {
    telemetry.trackEvent("ApplicationStarted", "Technology", "Java");
    System.out.println(colorize("                                     |__", MAGENTA_TEXT()));
    System.out.println(colorize("                                     |\\/", MAGENTA_TEXT()));
    System.out.println(colorize("                                     ---", MAGENTA_TEXT()));
    System.out.println(colorize("                                     / | [", MAGENTA_TEXT()));
    System.out.println(colorize("                              !      | |||", MAGENTA_TEXT()));
    System.out.println(colorize("                            _/|     _/|-++'", MAGENTA_TEXT()));
    System.out.println(colorize("                        +  +--|    |--|--|_ |-", MAGENTA_TEXT()));
    System.out.println(colorize("                     { /|__|  |/\\__|  |--- |||__/", MAGENTA_TEXT()));
    System.out.println(colorize("                    +---------------___[}-_===_.'____                 /\\", MAGENTA_TEXT()));
    System.out.println(colorize("                ____`-' ||___-{]_| _[}-  |     |_[___\\==--            \\/   _", MAGENTA_TEXT()));
    System.out.println(colorize(" __..._____--==/___]_|__|_____________________________[___\\==--____,------' .7", MAGENTA_TEXT()));
    System.out.println(colorize("|                        Welcome to Battleship                         BB-61/", MAGENTA_TEXT()));
    System.out.println(colorize(" \\_________________________________________________________________________|", MAGENTA_TEXT()));
    System.out.println();

    InitializeGame();

    StartGame();
  }

  private static void StartGame() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("\033[2J\033[;H");
    System.out.println("                  __");
    System.out.println("                 /  \\");
    System.out.println("           .-.  |    |");
    System.out.println("   *    _.-'  \\  \\__/");
    System.out.println("    \\.-'       \\");
    System.out.println("   /          _/");
    System.out.println("  |      _  /\" \"");
    System.out.println("  |     /_'");
    System.out.println("   \\    \\_/");
    System.out.println("    \" \"\" \"\" \"\" \"");

    List<Position> listPlayer1Hit = new ArrayList<>();
    List<Position> listPlayer1Miss = new ArrayList<>();
    boolean userWon = false;
    do {
      System.out.println();
      System.out.println("Player, it's your turn");
      if (listPlayer1Hit.size() > 0) {
        String valores = "";
        for (Position l : listPlayer1Hit) {
          valores += (String.valueOf(l.getColumn()) + l.getRow() + ",");
        }
        System.out.println("Posicion barcos tocados: " + valores);
      }
      if (listPlayer1Miss.size() > 0) {
        String valores = "";
        for (Position l : listPlayer1Miss) {
          valores += (String.valueOf(l.getColumn()) + l.getRow() + ",");
        }
        System.out.println("Posicion barcos fallados: " + valores);
      }
      String barcosHundidos = "";
      String barcosRestantes = "";
      for (Ship ship : enemyFleet) {
        if (ship.getIsSunk()) {
          String positions = "";
          for (Position p : ship.getPositions()) {
            positions += (String.valueOf(p.getColumn()) + p.getRow() + ",");
          }
          barcosHundidos += ship.getName() + "(" + positions + ")" + "\n";
        } else {
          barcosRestantes += ship.getName() + "\n";
        }
      }

      if (!barcosRestantes.equals("")) {
        System.out.println("Barcos restantes: " + barcosRestantes);
      }
      if (!barcosHundidos.equals("")) {
        System.out.println("Barcos hundidos: " + barcosHundidos);
      }
      boolean positionValid = false;
      Position position = new Position();
      do {
        try {
          System.out.println("Enter coordinates for your shot :");
          position = parsePosition(scanner.next());
          positionValid = true;
        } catch (Exception e) {
          System.out.println("Coordenada erronea");
        }
      } while (!positionValid);

      boolean isHit = GameController.checkIsHit(enemyFleet, position);
      if (isHit) {
        beep();

        System.out.println("                \\         .  ./");
        System.out.println("              \\      .:\" \";'.:..\" \"   /");
        System.out.println("                  (M^^.^~~:.'\" \").");
        System.out.println("            -   (/  .    . . \\ \\)  -");
        System.out.println("               ((| :. ~ ^  :. .|))");
        System.out.println("            -   (\\- |  \\ /  |  /)  -");
        System.out.println("                 -\\  \\     /  /-");
        System.out.println("                   \\  \\   /  /");


        boolean win = checkStatusGame(enemyFleet);
        if (win) {
          userWon = true;
          break;
        }
      }
      if (isHit) {
        listPlayer1Hit.add(position);
      } else {
        listPlayer1Miss.add(position);
      }

      System.out.println(isHit ? colorize("Yeah ! Nice hit !", GREEN_TEXT()) : colorize("Miss", CYAN_TEXT()));
      telemetry.trackEvent("Player_ShootPosition", "Position", position.toString(), "IsHit", Boolean.valueOf(isHit).toString());


      System.out.println("---------------------------------------------------------");
      System.out.println();
      System.out.println();
      System.out.println();
      System.out.println("--------------------------------------------------------");
      position = getRandomPosition();
      isHit = GameController.checkIsHit(myFleet, position);
      System.out.println();
      System.out.printf("Computer shoot in %s%s and %s%n", position.getColumn(), position.getRow(), isHit ? colorize("hit your ship !", RED_TEXT()) : colorize("Miss", CYAN_TEXT()));
      telemetry.trackEvent("Computer_ShootPosition", "Position", position.toString(), "IsHit", Boolean.valueOf(isHit).toString());
      if (isHit) {
        beep();

        System.out.println("                \\         .  ./");
        System.out.println("              \\      .:\" \";'.:..\" \"   /");
        System.out.println("                  (M^^.^~~:.'\" \").");
        System.out.println("            -   (/  .    . . \\ \\)  -");
        System.out.println("               ((| :. ~ ^  :. .|))");
        System.out.println("            -   (\\- |  \\ /  |  /)  -");
        System.out.println("                 -\\  \\     /  /-");
        System.out.println("                   \\  \\   /  /");


        boolean win = checkStatusGame(enemyFleet);
        if (win) {
          break;
        }

      }
      System.out.println("---------------------------------------------------------");
      System.out.println();
      System.out.println();
      System.out.println();
      System.out.println("--------------------------------------------------------");
    } while (true);


    if (userWon) {
      System.out.println(" .----------------.  .----------------.  .----------------.   .----------------.  .----------------.  .-----------------.\n" +
          "| .--------------. || .--------------. || .--------------. | | .--------------. || .--------------. || .--------------. |\n" +
          "| |  ____  ____  | || |     ____     | || | _____  _____ | | | | _____  _____ | || |     _____    | || | ____  _____  | |\n" +
          "| | |_  _||_  _| | || |   .'    `.   | || ||_   _||_   _|| | | ||_   _||_   _|| || |    |_   _|   | || ||_   \\|_   _| | |\n" +
          "| |   \\ \\  / /   | || |  /  .--.  \\  | || |  | |    | |  | | | |  | | /\\ | |  | || |      | |     | || |  |   \\ | |   | |\n" +
          "| |    \\ \\/ /    | || |  | |    | |  | || |  | '    ' |  | | | |  | |/  \\| |  | || |      | |     | || |  | |\\ \\| |   | |\n" +
          "| |    _|  |_    | || |  \\  `--'  /  | || |   \\ `--' /   | | | |  |   /\\   |  | || |     _| |_    | || | _| |_\\   |_  | |\n" +
          "| |   |______|   | || |   `.____.'   | || |    `.__.'    | | | |  |__/  \\__|  | || |    |_____|   | || ||_____|\\____| | |\n" +
          "| |              | || |              | || |              | | | |              | || |              | || |              | |\n" +
          "| '--------------' || '--------------' || '--------------' | | '--------------' || '--------------' || '--------------' |\n" +
          " '----------------'  '----------------'  '----------------'   '----------------'  '----------------'  '----------------' ");
    } else {
      System.out.println(" /$$     /$$ /$$$$$$  /$$   /$$       /$$        /$$$$$$   /$$$$$$  /$$$$$$$$\n" +
          "|  $$   /$$//$$__  $$| $$  | $$      | $$       /$$__  $$ /$$__  $$|__  $$__/\n" +
          " \\  $$ /$$/| $$  \\ $$| $$  | $$      | $$      | $$  \\ $$| $$  \\__/   | $$   \n" +
          "  \\  $$$$/ | $$  | $$| $$  | $$      | $$      | $$  | $$|  $$$$$$    | $$   \n" +
          "   \\  $$/  | $$  | $$| $$  | $$      | $$      | $$  | $$ \\____  $$   | $$   \n" +
          "    | $$   | $$  | $$| $$  | $$      | $$      | $$  | $$ /$$  \\ $$   | $$   \n" +
          "    | $$   |  $$$$$$/|  $$$$$$/      | $$$$$$$$|  $$$$$$/|  $$$$$$/   | $$   \n" +
          "    |__/    \\______/  \\______/       |________/ \\______/  \\______/    |__/   ");
    }

  }

  private static boolean checkStatusGame(List<Ship> fleet) {
    List barcosHundidos = new ArrayList();
    for (Ship ship : fleet) {
      if (ship.getIsSunk()) {
        barcosHundidos.add(ship);
      }
    }

    return barcosHundidos.size() == fleet.size();
  }

  private static void beep() {
    System.out.print("\007");
  }

  protected static Position parsePosition(String input) throws Exception {
    Letter letter = Letter.valueOf(input.toUpperCase().substring(0, 1));
    int number = Integer.parseInt(input.substring(1));
    if (number <= 0 || number > 9) {
      throw new Exception("");
    }
    return new Position(letter, number);
  }

  private static Position getRandomPosition() {
    int rows = 8;
    int lines = 8;
    Random random = new Random();
    Letter letter = Letter.values()[random.nextInt(lines)];
    int number = random.nextInt(rows);
    Position position = new Position(letter, number);
    //TODO: Combrobar si la posicion ya ha sido usada
    return position;
  }

  private static void DebugInitializeMyFleet() {
    myFleet = GameController.initializeShips();

    System.out.println("Please position your fleet (Game board has size from A to H and 1 to 8) :");

    for (Ship ship : myFleet) {
      System.out.println();
      System.out.printf("Please enter the positions for the %s (size: %s)%n", ship.getName(), ship.getSize());
      for (int i = 1; i <= ship.getSize(); i++) {
        // Position position = new Position(Letter.A, 1);
        ship.addPosition("A1");
        telemetry.trackEvent("Player_PlaceShipPosition", "Position", "A1", "Ship", ship.getName(), "PositionInShip", Integer.valueOf(i).toString());
      }
    }
  }

  private static void InitializeGame() {
    InitializeMyFleet();
    //DebugInitializeMyFleet();
    InitializeEnemyFleet();
  }

  private static void InitializeMyFleet() {
    Scanner scanner = new Scanner(System.in);
    myFleet = GameController.initializeShips();
    System.out.println("Please position your fleet (Game board has size from A to H and 1 to 8) :");

    for (Ship ship : myFleet) {
      System.out.println();
      System.out.printf("Please enter the positions for the %s (size: %s)%n", ship.getName(), ship.getSize());

      for (int i = 1; i <= ship.getSize(); i++) {
        placePosition(scanner, ship, i, myFleet);
      }
    }
  }


  private static void placePosition(Scanner scanner, Ship ship, int i, List<Ship> myFleet) {
    System.out.printf("Enter position %s of %s (i.e A3):%n", i, ship.getSize());

    String positionInput = scanner.next();
    if (isPositionValid(positionInput)) {
      if (!isPositionRepeated(positionInput, ship, myFleet)) {
        if (isAdjacent(positionInput, ship.getPositions())) {
          ship.addPosition(positionInput);
          telemetry.trackEvent("Player_PlaceShipPosition", "Position", positionInput, "Ship", ship.getName(), "PositionInShip", Integer.valueOf(i).toString());
        } else {
          System.out.println("Error. Position not available !!");
          placePosition(scanner, ship, i, myFleet);
        }

      } else {
        System.out.println("Error. Position already filled !!");
        placePosition(scanner, ship, i, myFleet);

      }
    } else {
      System.out.println("Error. Out ouf range !!");
      placePosition(scanner, ship, i, myFleet);
    }
  }

  private static boolean isAdjacent(String newPosition1, List<Position> shipPositions) {
    Position position = new Position(Letter.valueOf(newPosition1.substring(0, 1).toUpperCase().trim()), Integer.parseInt(newPosition1.substring(1, 2)));
    if (shipPositions.isEmpty()) {
      return true;
    }

    Position positionShip1 = shipPositions.get(0);

    if (shipPositions.size() == 1) {
      boolean adjacentColumn = position.getColumn().ordinal() == positionShip1.getColumn().ordinal() - 1 || position.getColumn().ordinal() == positionShip1.getColumn().ordinal() + 1;
      boolean adjacentRow = position.getRow() == positionShip1.getRow() - 1 || position.getRow() == positionShip1.getRow() + 1;

      return adjacentColumn || adjacentRow;
    }


    Position positionShip2 = shipPositions.get(1);

    boolean boatIsColumn = positionShip1.getColumn().ordinal() == positionShip2.getColumn().ordinal() - 1 || positionShip1.getColumn().ordinal() == positionShip2.getColumn().ordinal() + 1;
    boolean boatIsRow = positionShip1.getRow() == positionShip2.getRow() - 1 || positionShip1.getRow() == positionShip2.getRow() + 1;

    if (boatIsColumn) {
      for (Position boatPosition : shipPositions) {
        if (position.getRow() == boatPosition.getRow() && (position.getColumn().ordinal() == boatPosition.getColumn().ordinal() - 1 || position.getColumn().ordinal() == boatPosition.getColumn().ordinal() + 1)) {
          return true;
        }
      }
    } else if (boatIsRow) {
      for (Position boatPosition : shipPositions) {
        if ((position.getRow() == boatPosition.getRow() - 1 || position.getRow() == boatPosition.getRow() + 1) && position.getColumn() == boatPosition.getColumn()) {
          return true;
        }
      }

    }

    return false;
  }


  private static boolean isPositionRepeated(String positionInput, Ship ship, List<Ship> myFleet) {
    List<Position> positionsList = myFleet.stream()
        .map(Ship::getPositions)
        .filter(Objects::nonNull)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
    //positionsList.addAll(ship.getPositions());
    Position newPosition = new Position(Letter.valueOf(positionInput.substring(0, 1).toUpperCase().trim()), Integer.parseInt(positionInput.substring(1, 2)));
    return positionsList.stream().filter(p -> p.getRow() == newPosition.getRow() && p.getColumn() == newPosition.getColumn()).collect(Collectors.toList()).size() != 0;
    /*positionsList.add(newPosition);

    Set<Position> positionsSet = new HashSet<>(positionsList);
    return positionsSet.size() < positionsList.size();*/
  }

  private static boolean isPositionValid(String positionInput) {
    if (positionInput.length() > 2) {
      return false;
    }
    if (NumberUtils.isParsable(positionInput.substring(1, 2))
        && (Integer.parseInt(positionInput.substring(1, 2)) < 1
        || (Integer.parseInt(positionInput.substring(1, 2)) > 8))) {
      return false;
    }
    String letter = positionInput.substring(0, 1);
    return isInEnum(letter, Letter.class);
  }

  private static boolean isInEnum(String value, Class<Letter> enumClass) {
    try {
      for (Letter e : enumClass.getEnumConstants()) {
        if (e.name().equals(value.toUpperCase().trim())) {
          return true;
        }
      }
    } catch (IllegalArgumentException e) {
      return false;
    }
    return false;
  }


  private static void InitializeEnemyFleet() {
    enemyFleet = GameController.initializeShips();

    enemyFleet.get(0).getPositions().add(new Position(Letter.B, 4));
    enemyFleet.get(0).getPositions().add(new Position(Letter.B, 5));
    enemyFleet.get(0).getPositions().add(new Position(Letter.B, 6));
    enemyFleet.get(0).getPositions().add(new Position(Letter.B, 7));
    enemyFleet.get(0).getPositions().add(new Position(Letter.B, 8));

    enemyFleet.get(1).getPositions().add(new Position(Letter.E, 5));
    enemyFleet.get(1).getPositions().add(new Position(Letter.E, 6));
    enemyFleet.get(1).getPositions().add(new Position(Letter.E, 7));
    enemyFleet.get(1).getPositions().add(new Position(Letter.E, 8));

    enemyFleet.get(2).getPositions().add(new Position(Letter.A, 3));
    enemyFleet.get(2).getPositions().add(new Position(Letter.B, 3));
    enemyFleet.get(2).getPositions().add(new Position(Letter.C, 3));

    enemyFleet.get(3).getPositions().add(new Position(Letter.F, 8));
    enemyFleet.get(3).getPositions().add(new Position(Letter.G, 8));
    enemyFleet.get(3).getPositions().add(new Position(Letter.H, 8));

    enemyFleet.get(4).getPositions().add(new Position(Letter.C, 5));
    enemyFleet.get(4).getPositions().add(new Position(Letter.C, 6));
  }
}
