package innopolis.tabletennis.util;

import innopolis.tabletennis.exception.BadRequestException;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Timer;
import java.util.TimerTask;

public final class Util {
    @Getter
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static String getStringFromLocalDate(LocalDate date) {
        return date.format(formatter);
    }

    public static LocalDate getLocalDateFromString(String date) throws DateTimeParseException {
        if (date == null)
            throw new BadRequestException("You did not provide any string formatted date. The date is null.");
        return LocalDate.parse(date, formatter);
    }

    public static void setTimeout(Runnable runnable, long delay) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        }, delay);
    }

    private Util() {
    }
}
