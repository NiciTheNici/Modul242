/**
 * VERBALE BESCHREIBUNG:
 * Erstellen Sie hier ihre verbale Beschreibung.
 * <p>
 * <p>
 * <p>
 * VIDEO URL: https://youtu.be/dQw4w9WgXcQ
 */
public class Robi {

    private final RobiAPI robi;


    // Used to start the Robi
    public static void main(String[] args) {
        Robi r = new Robi("localhost", 38835);   // Tragen Sie hier die Portnummer auf Ihrem Simulator ein.
        // hier Robi Methoden aufrufen

        r.followScreenEdge();

    }

    public Robi(String hostname, int portNummer) {
        robi = new RobiAPI(hostname, portNummer);

    }

    public void followScreenEdge() {
        robi.connect();
        robi.getDistSensorValues();
        Zustand currentZustand = Zustand.WALLFIND;
        while (true) {
            switch (currentZustand) {
                case WALLFIND -> {
                    if (touchesWall()) {
                        robi.drive(0);
                        turnParallellToWall();
                    } else {
                        robi.drive( 5);
                    }
                }
                case WALLFOLLOW -> {

                }

            }
        }
    }

    enum Zustand {
        WALLFIND,
        WALLFOLLOW,
        RECHTS,
    }

    public void curveAroundWall() {


    }

    public void turnParallellToWall() {
        while (robi.readSensor(6) + robi.readSensor(7) != 510) {
            robi.turn(10);
            robi.getDistSensorValues();
        }
        robi.turn(0);
        System.out.println("im parallell");
    }

    public boolean touchesWall() {
        for (int i = 4; i < 15; i++) {
            robi.getDistSensorValues();
            if (robi.readSensor(i) == 255) {
                return true;
            }
        }
        return false;

    }

    public void lineFollowTwoSensor() {
        // Variabel deklarieren
        final int GERADE = 0;
        final int LINKS = 1;
        final int RECHTS = 2;

        int zustand = GERADE;

        robi.connect();
        robi.drive(5);
        long startTime = System.currentTimeMillis();
        while (true) {
            switch (zustand) {
                case GERADE -> {
                    robi.getDistSensorValues();
                    if (robi.readSensor(0) > 100) {
                        zustand = LINKS;
                        System.out.println("LINKS");
                        robi.stop();
                        robi.turn(-40);
                    }
                    if (robi.readSensor(1) < 100) {
                        zustand = RECHTS;
                        System.out.println("RECHTS");
                        robi.stop();
                        robi.turn(40);
                    }
                }
                case LINKS -> {
                    robi.getDistSensorValues();
                    if (robi.readSensor(0) < 100) {
                        zustand = GERADE;
                        System.out.println("GERADE");
                        robi.stop();
                        robi.drive(50);
                    }
                }
                case RECHTS -> {
                    robi.getDistSensorValues();
                    if (robi.readSensor(1) > 100) {
                        zustand = GERADE;
                        System.out.println("GERADE");
                        robi.stop();
                        robi.drive(50);
                    }
                }
            }
        }
    }

    public void lineFollowOneSensor() {
        // Variabel deklarieren
        final int FOLLOW = 0;
        final int TURN = 1;

        int zustand = FOLLOW;

        robi.connect();
        robi.setLeftDriveSpeed(2);
        robi.setRightDriveSpeed(4);


        while (true) {
            switch (zustand) {
                case FOLLOW -> {
                    robi.getDistSensorValues();
                    if (robi.readSensor(0) < 100) {
                        zustand = TURN;
                        System.out.println("TURN");
                        robi.stop();
                        robi.setLeftDriveSpeed(80);
                        robi.setRightDriveSpeed(20);
                    }
                }
                case TURN -> {
                    robi.getDistSensorValues();
                    if (robi.readSensor(0) > 100) {
                        zustand = FOLLOW;
                        System.out.println("FOLLOW");
                        robi.stop();
                        robi.turn(-20);
                    }
                }
            }
        }
    }
}
