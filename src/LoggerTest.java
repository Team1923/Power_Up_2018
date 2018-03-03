import org.usfirst.frc.team1923.robot.utils.logger.Logger;

import java.util.Random;

public class LoggerTest {

    public static void main(String[] args) {
        Logger logger = new Logger();

        logger.addDataSource("RandomData", () -> new Random().nextInt(100) + "");

        logger.initialize();

        for (int i = 0; i < 100; i++) {
            logger.periodic();
        }

        logger.save();
    }

}
