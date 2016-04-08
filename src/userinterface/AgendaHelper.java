package userinterface;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import org.joda.time.DateTime;
import javafx.util.Callback;
import jfxtras.internal.scene.control.skin.agenda.AgendaDaySkin;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;
import logic.SearchCommand;
import logic.Todo;
import storage.Memory;

/**
 * Creates an agenda, similar to Google cal's, which can be affixed to arbitrary locations.
 * Using a selected date from the calendar view, the agenda will automatically
 * fetch all todos that are listed under that particular day.
 */

//@@author Morgan
public class AgendaHelper {
	
	private Agenda agenda = new Agenda();
	private LocalDate selectedDate;
	
	private AgendaHelper(LocalDate selectedDate) {
		this.selectedDate = selectedDate;
	}
	
	//Load all of the events into the agenda view that correlate to the week of the selected date
	public void loadEvents() {
		//Configure an alternate Agenda skin to confine table to selected date
		agenda.setSkin(new AgendaDaySkin(agenda));
	    LocalDateTime localDateTime = LocalDateTime.of(selectedDate, LocalTime.now());
		agenda.setDisplayedLocalDateTime(localDateTime);
		
		// setup appointment groups
        final Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
        for (Agenda.AppointmentGroup lAppointmentGroup : agenda.appointmentGroups()) {
        	lAppointmentGroupMap.put(lAppointmentGroup.getDescription(), lAppointmentGroup);
        }
        
        //Fetch events and deadlines of selected date
        Date jodaCompatDate = Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        DateTime dateTime = new DateTime(jodaCompatDate);
        
       //reference memory to search according to selected date
        Memory memory = Controller.getLogic().getMemory();
        Collection<Todo> selectedTasksOfDate = SearchCommand.getTodosOfSameDay(null, dateTime, memory);
        
        //format the tasks into appointments for the agenda display
        ArrayList<Appointment> todoSpans = formatTasksIntoAgenda(selectedTasksOfDate, lAppointmentGroupMap);

        // accept new appointments
		agenda.newAppointmentCallbackProperty().set(new Callback<Agenda.LocalDateTimeRange, Agenda.Appointment>()
		{
			@Override
			public Appointment call(LocalDateTimeRange dateTimeRange)
			{
				return new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime( dateTimeRange.getStartLocalDateTime() )
				.withEndLocalDateTime( dateTimeRange.getEndLocalDateTime() )
				.withSummary("new")
				.withDescription("new")
				.withAppointmentGroup(lAppointmentGroupMap.get("group01"));
			}
		});
		
		//add spans constructed from live memory into agenda view
		Appointment[] agendaSpans = convertAppointmentListToArray(todoSpans);
		if(!todoSpans.isEmpty()){
			agenda.appointments().addAll(agendaSpans);
		}
			
		// action
		agenda.setActionCallback( (appointment) -> {
			System.out.println("Testing user interaction with agenda");
			return null;
		});

	}
	
	/**
	   * Formats a list of tasks into spans that the Agenda can understand as events that span over a space
	   * @param Tasks that have been queried from memory to match the calendar's selected date
	   * @return A list of appointments that the agenda can understand
	*/
	private ArrayList<Appointment> formatTasksIntoAgenda(Collection<Todo> selectedTasksOfDate, 
			Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap) {
		
		ArrayList<Appointment> agendaSpans = new ArrayList<Appointment>();
		
		for(Todo item : selectedTasksOfDate) {
			LocalDateTime startTime = null;
			LocalDateTime endTime = null;
			
			//format the start and end times based of the type of Todo item
			switch(item.getType()){
		
				case DEADLINE:
					LocalDateTime javaUtilCompatDeadline = item.getEndTime().toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
					endTime = javaUtilCompatDeadline;
					startTime = javaUtilCompatDeadline.minusHours(2);
					break;
					
				case EVENT:
					LocalDateTime javaUtilCompatEventStart = item.getStartTime().toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
					LocalDateTime javaUtilCompatEventEnd = item.getEndTime().toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();;
					startTime = javaUtilCompatEventStart;
					endTime = javaUtilCompatEventEnd;
					break;
					
				default:
					//if the item is not a deadline or event, it should not span space within the agenda
					continue;
					
			}
			
			Appointment span = new Agenda.AppointmentImplLocal()
					.withStartLocalDateTime(startTime)
					.withEndLocalDateTime(endTime)
					.withSummary(item.getName())
					.withDescription(item.getName())
					.withAppointmentGroup(lAppointmentGroupMap.get("group20"));
			
			agendaSpans.add(span);
		}
	
		return agendaSpans;
	}
	
   /**
   * Converts ArrayList of appointments into an array of appointments.
   * Conversion in this format is necessary to construct the agenda view.
   * @param list of appointments in a collection or array list
   * @return a statically sized array of the same appointments
   */
	private Appointment[] convertAppointmentListToArray(ArrayList<Appointment> apts) {
		Appointment[] agendaSpans = new Appointment[apts.size()];
		for(int i=0; i<apts.size(); i++) {
			agendaSpans[i] = apts.get(i);
		}
		return agendaSpans;
	}
	
	public Agenda getAgenda() {
		return agenda;
	}
	
   /**
   * Creates a new agenda view showing task of selected calendar date
   * @param date selected from UI within the calendar layout
   * @return a new Agenda node that can be affixed to any Pane location
   */
	public static Agenda generateAgendaHelperView(LocalDate selectedDate) {
		AgendaHelper agendaHelper = new AgendaHelper(selectedDate);
		agendaHelper.loadEvents();
		return agendaHelper.getAgenda();
	}	
}
