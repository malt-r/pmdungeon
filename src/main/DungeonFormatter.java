package main;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class DungeonFormatter extends Formatter {

  @Override
  public String format(LogRecord record) {
    DateTimeFormatter timeFormatter =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            .ofPattern("k:mm:s:S00", Locale.GERMANY)
            .withZone(ZoneId.systemDefault());

    return timeFormatter.format(record.getInstant())
        + ": ("
        + record.getSourceClassName()
        + "."
        + record.getSourceMethodName()
        + ") "
        + record.getLevel()
        + "-"
        + record.getMessage()
        + "\n";
  }
}
