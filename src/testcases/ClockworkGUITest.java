package testcases;

import static org.junit.Assert.*;

import org.junit.Test;

import javafx.scene.text.Text;
import userinterface.model.TopDisplay;

public class ClockworkGUITest {
	public TopDisplay testHBox = new TopDisplay();

	//All not working yet though
	@Test
	public void testGetDisplayText() {
		Text testText = new Text("Welcome to Clockwork (:");
		assertEquals(testText, TopDisplay.getDisplayText());
	}
	
	@Test
	public void testChangeDisplayText(){
		String testString = "Am I detected?";
		TopDisplay.changeDisplayText(testString);
		assertEquals(testString, TopDisplay.getDisplayText());
	}

}
