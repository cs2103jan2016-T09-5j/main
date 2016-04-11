package userinterface;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import jfxtras.scene.control.agenda.Agenda;

//@@author A0149671W
public class LayoutCalendar extends BorderPane {
	
	private Label currentTime = new DigitalClock();
	private LocalDate selectedDate = LocalDate.now();
	private LocalDateTime agendaScrollLocation = LocalDateTime.now();
	private DatePicker datePicker = new DatePicker();
	private BorderPane centralPane = new BorderPane();
	private TextField cliTextField;
	private Node calendarControl;
	private Agenda agendaControl;
	
	public LayoutCalendar() {
		setDisplayRegions();
	}

	/** POPULATING LAYOUT */
	
	private void setDisplayRegions() {
		setBottomRegion();
		setTopRegion();
		setCenterRegion();
	}
	
	/** Set top region to display available shortcuts */
	private void setTopRegion() {
		BoxHeader headerBox = new BoxHeader();
		this.setTop(headerBox);
	}

   /****************CALENDAR IMPLEMENTATION **********/
	private void setCenterRegion() {
		//Utilize DatePicker JavaFX component to extract internal calendar component
		datePicker.setValue(LocalDate.now());

		//Isolate internal component, which was previously the component that "popped up"
		DatePickerSkin skin = new DatePickerSkin(datePicker);
		calendarControl = skin.getPopupContent();
		calendarControl.setId("calendar-control");
		calendarControl.setStyle("-fx-padding: 1;  -fx-background-insets: 0, 100;  "
				+ "-fx-background-radius: 0, 0; -fx-background-color: rgba(0, 100, 100, 0.1);");
		
		//Set node in focus so that attached listeners are able to be utilized
		Platform.runLater(new Runnable() {
		     @Override
		     public void run() {
		         calendarControl.requestFocus();
		     }
		});
		
		installEventHandler(calendarControl);
		
		//Preparation of current time  
		this.currentTime.setFont(Font.font("Cambria", 50));
		this.currentTime.setTextFill(Color.WHITE);
		this.currentTime.setTextAlignment(TextAlignment.CENTER);
		
		//Affix isolated node along with the current time
		centralPane.setCenter(calendarControl);
		centralPane.setBottom(this.currentTime);
		centralPane.setStyle("-fx-background-color: #182733;");
		centralPane.setPadding(new Insets(25));

		//Exploit this component as our stand-alone calendar widget
		this.setCenter(centralPane);
	}
	
	/************* END IMPLEMENTATION ***************/
	
	private void setBottomRegion() {
		cliTextField = implementTextField();
		this.setBottom(cliTextField);
	}

	private TextField implementTextField() {
		BoxInput textField = new BoxInput();
		textField.setEditable(false);
		return textField;
	}
	
	private void clearChildren() {
		//refresh internal components
		datePicker = new DatePicker();
		centralPane = new BorderPane();
		selectedDate = LocalDate.now();
		agendaScrollLocation = LocalDateTime.now();
		
		//clear all children from top-level
		this.getChildren().removeAll();
		this.getChildren().clear();
	}
	
	/***********************AGENDA VIEW***********************/
	private void paintAgendaView() {
		
		//Utilize AgendaHelper factory method for necessary setup
		agendaControl = AgendaHelper.generateAgendaHelperView(selectedDate);
		
		//Set node in focus so that attached listeners are able to be utilized
		Platform.runLater(new Runnable() {
		     @Override
		     public void run() {
		    	 agendaControl.setOnMouseClicked(null);
		    	 agendaControl.setOnMousePressed(null);
		         agendaControl.requestFocus();
		     }
		});
		
		//Override default mouse scroll with keypress scroll for the agenda
		installEventAgendarHandler(agendaControl);
		this.setCenter(agendaControl);
	}
	
	/**
	  * Helper function for converting LocalDate objects to LocalDateTime objects that are compatible with the Agenda view
	  * @param LocalDate objects intended to be casted
	  * @return LocalDateTime object fully-compatible with the Agenda View, specifically for scroll location
	*/
	private LocalDateTime convertToLocalDateTime(LocalDate date) {
	    Date javaCompatDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	    LocalDateTime agendaScrollCompatible = javaCompatDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	    return agendaScrollCompatible;
	}
		
	/**************DECLARING NODE EVENT HANDLERS**************/
	
	private void installEventHandler(final Node calendar) {
	    // handler for enter key press / release events, other keys are
	    // handled by the parent (keyboard) node handler
	    final EventHandler<KeyEvent> keyEventHandler =
	        new EventHandler<KeyEvent>() {
	    		@Override
	            public void handle(final KeyEvent keyEvent) throws NullPointerException {
	            	
	            	//select date above
	                if (keyEvent.getCode() == KeyCode.UP) {
	                    setPressed(keyEvent.getEventType() == KeyEvent.KEY_PRESSED);
	                    selectedDate = selectedDate.minusDays(7);
	                    datePicker.setValue(selectedDate);
	                    keyEvent.consume();
	                }
	                
	                //select date below
	                if (keyEvent.getCode() == KeyCode.DOWN) {
	                    setPressed(keyEvent.getEventType() == KeyEvent.KEY_PRESSED);
	                    selectedDate = selectedDate.plusDays(7);
	                    datePicker.setValue(selectedDate);
	                    keyEvent.consume();
	                }
	                
	                //select date to right
	                if (keyEvent.getCode() == KeyCode.RIGHT) {
	                    setPressed(keyEvent.getEventType() == KeyEvent.KEY_PRESSED);
	                    selectedDate = selectedDate.plusDays(1);
	                    datePicker.setValue(selectedDate);
	                    keyEvent.consume();
	                }
	                
	                //select date to left
	                if (keyEvent.getCode() == KeyCode.LEFT) {
	                    setPressed(keyEvent.getEventType() == KeyEvent.KEY_PRESSED);
	                    selectedDate = selectedDate.minusDays(1);
	                    datePicker.setValue(selectedDate);
	                    keyEvent.consume();
	                }
	                
	                //trigger agenda view with events contained in currently selected day
	                if (keyEvent.getCode() == KeyCode.ENTER) {
	                    setPressed(keyEvent.getEventType() == KeyEvent.KEY_PRESSED);              
	                    //remove handlers when clearing component
	                    calendar.setOnKeyPressed(null);
	                    //we need to delay removing the component until we are outside of the component's
	                    // handler, otherwise we would run into a null pointer exception
                		Platform.runLater(new Runnable() {
               		     @Override
               		     public void run() {
               		    	centralPane.getChildren().remove(calendarControl);
    	                    agendaScrollLocation = convertToLocalDateTime(selectedDate);
               		    	paintAgendaView();
               		     }
	               		});
	                }
	                
	                //Rewind to previous view upon pressing escape
	                if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
	                    setPressed(keyEvent.getEventType() == KeyEvent.KEY_PRESSED);
						Controller.processEnter("DISPLAY");
	                    keyEvent.consume();
					}
					Controller.executeKeyPress(cliTextField, keyEvent);
	            }
	        };
	 
	    calendar.setOnKeyPressed(keyEventHandler);
	}
	
	private void installEventAgendarHandler(final Node agendaListener) {
	    // handler for enter key press / release events, other keys are
	    // handled by the parent (keyboard) node handler
	    final EventHandler<KeyEvent> keyEventHandler =
	        new EventHandler<KeyEvent>() {
	    		@Override
	            public void handle(final KeyEvent keyEvent){
	    			//Scrolls the agenda view up
	    			if(keyEvent.getCode() == KeyCode.UP) {
		    			setPressed(keyEvent.getEventType() == KeyEvent.KEY_PRESSED);
		    			//limit scrolling to the single day selected
		    			if(agendaScrollLocation.getHour() > 0){
			    			agendaScrollLocation = agendaScrollLocation.minusHours(1);
			    			agendaControl.setDisplayedLocalDateTime(agendaScrollLocation);
		    			}
		    			keyEvent.consume();
	    			} 
	    			
	    			//Scrolls the agenda view down
	    			if(keyEvent.getCode() == KeyCode.DOWN) {
		    			setPressed(keyEvent.getEventType() == KeyEvent.KEY_PRESSED);
		    			//limit scrolling to the single day selected
		    			if(agendaScrollLocation.getHour() < 23) {
		    				agendaScrollLocation = agendaScrollLocation.plusHours(1);
		    				agendaControl.setDisplayedLocalDateTime(agendaScrollLocation);
		    			}
		    			keyEvent.consume();
	    			}   
	    			
	                //Rewind to previous view upon pressing escape
	                if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
	                    setPressed(keyEvent.getEventType() == KeyEvent.KEY_PRESSED);	                    
	                    //we need to delay removing the component until we are outside of the component's
	                    // handler, otherwise we would run into a null pointer exception
                		Platform.runLater(new Runnable() {
               		     @Override
               		     public void run() {
     	                    clearChildren();
    	                    setDisplayRegions();
               		     }
	               		});
	                    keyEvent.consume();
					}
	            }
	        };
	 
	    agendaListener.setOnKeyPressed(keyEventHandler);
	}
}
