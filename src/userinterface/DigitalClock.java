package userinterface;

import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.util.Duration;

//@@author Morgan
/**
 * Creates a digital clock display as a simple label.
 * Format of the clock display is hh:mm:ss aa, where:
 * hh Hour in am/pm (1-12)
 * mm Minute in hour
 * ss Second in minute
 * aa Am/pm marker
 * Time is the system time for the local timezone.
 */
class DigitalClock extends Label {
	
	public DigitalClock() {
    bindToTime();
  }

  // the digital clock updates once a second.
  private void bindToTime() {
    Timeline timeline = new Timeline(
      new KeyFrame(Duration.seconds(0),
        new EventHandler<ActionEvent>() {
          @Override public void handle(ActionEvent actionEvent) {
            Calendar time = Calendar.getInstance();
            String hourString = StringUtilities.pad(2, ' ', time.get(Calendar.HOUR) == 0 ? "12" : time.get(Calendar.HOUR) + "");
            String minuteString = StringUtilities.pad(2, '0', time.get(Calendar.MINUTE) + "");
            String secondString = StringUtilities.pad(2, '0', time.get(Calendar.SECOND) + "");
            String ampmString = time.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
            LocalDate localDate = new LocalDate();
            setText("          " + hourString + ":" + minuteString + ":" + secondString + " " + ampmString + "            " + localDate.toString());
          }
        }
      ),
      new KeyFrame(Duration.seconds(1))
    );
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }
}

class StringUtilities {
  /**
   * Creates a string left padded to the specified width with the supplied padding character.
   * @param fieldWidth the length of the resultant padded string.
   * @param padChar a character to use for padding the string.
   * @param s the string to be padded.
   * @return the padded string.
   */
  public static String pad(int fieldWidth, char padChar, String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = s.length(); i < fieldWidth; i++) {
      sb.append(padChar);
    }
    sb.append(s);

    return sb.toString();
  }
}