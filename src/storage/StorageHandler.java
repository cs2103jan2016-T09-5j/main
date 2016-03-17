package storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import logic.ClockWork;



/**
 * Handles the storing of an instance of Memory into a file in JSON formatting,
 * as well as the retrieving of an instance of Memory from a file in JSON
 * formatting.
 */

public class StorageHandler {
	private static final String MESSAGE_CORRUPT_FILE = "Storage file is unreadable or corrupt. Do you wish to : \n"
			+ "\t1. Replace it with a blank file (R)\n"
			+ "\t2. Exit (E)";
	private static final String FILE_NAME = "storageFile.json";
	
	private static PrintWriter writer;
	private static Scanner reader;
	private static String filePath;
	
    private File storageFile;
	
	/**
	 * Builder inner class for creating instances of StorageHandler with
	 * the option of setting the filePath variable.
	 */
	public static class Builder{
		 String fileDirectory;
		 String filePath;
		
		/**
		 * Set directory path with the given string.
		 * 
		 * @param fileDirectory
		 * @return Builder
		 */
		public Builder setDirectoryPath(String fileDirectory){
			this.fileDirectory = fileDirectory;
			return this;
		}
		
		/**
		 * Set file path with the directory path and file name.
		 * 
		 * @return Builder
		 */
		public Builder setFilePath(){
			this.filePath = fileDirectory + "/" + FILE_NAME;
			return this;
		}
		
		/**
		 * Overloaded method: 
		 * Set file path with the absolute path of the given file.
		 * 
		 * @param File 
		 * @return Builder
		 */
		public Builder setFilePath(File file){
			this.filePath = file.getAbsolutePath();
			return this;
		}
		
		/**
		 * Returns a StorageHandler instance.
		 * 
		 * @return StorageHandler
		 */
		public StorageHandler build(){
			return new StorageHandler(this);
		}
	}
		
	/**
	 * Constructor for StorageHandler, which takes in a Builder object
	 * to initialise variables. 
	 * 
	 * @param builder 
	 */
	private StorageHandler(Builder builder) {
	    filePath = builder.filePath;
		storageFile = new File(filePath);
		createFileIfNonExistent(storageFile);
		checkFileFormat();
	}
	
	/**
	 * Stores the memory object passed as a parameter into a file in JSON formatting
	 * 
	 * @param memoryToStore
	 */
	public void storeMemoryToFile(Memory memoryToStore) {
		initialiseWriter(storageFile);
		String jsonString = exportAsJson(memoryToStore);
		writer.println(jsonString);
		tearDownWriter();
	}
	
	/**
	 * Overloaded method:
	 * Stores the memory object that is passed as a parameter into the file that 
	 * is passed as the other parameter, in JSON formatting
	 * 
	 * @param memoryToStore
	 * @param file
	 */
	public static void storeMemoryToFile(Memory memoryToStore, File file) {
		initialiseWriter(file);
		String jsonString = exportAsJson(memoryToStore);
		writer.println(jsonString);
		tearDownWriter();
	}
	
	/**
	 * Retrieves a Memory object from the JSON file.
	 * 
	 * @return Memory
	 */
	public Memory retrieveMemoryFromFile() throws JsonSyntaxException{
		initialiseReader(storageFile);
		StringBuilder builder = new StringBuilder();

		while (reader.hasNextLine()) {
			builder.append(reader.nextLine() + "\n");
		}
		String jsonString = builder.toString();
		tearDownReader();
		Memory retrievedMemory = null;
		retrievedMemory = importFromJson(jsonString);

		return retrievedMemory;
	}
	
	/**
	 * 
	 * Checks that storageFile.json is readable. If it is not, 
	 * the user will be asked to replace the file with a new blank
	 * file, or to exit the program.
	 * 
	 */
	private void checkFileFormat(){
		if(!StorageUtils.isFileInJsonFormat(storageFile)){
			String command;
			Scanner scn = ClockWork.scn;
			do{
				System.out.println(MESSAGE_CORRUPT_FILE);
				command = scn.nextLine().toUpperCase().trim();
				if(command.equals("R")){
					storageFile.delete();
					createFileIfNonExistent(storageFile);
				}
				else if(command.equals("E")){
					System.exit(0);
				}
				else{
					System.out.println("Incorrect command given.");
				}
			}while(!command.equals("R")&&!command.equals("E"));
		}
	}
	/**
	 * Initialise the file reader.
	 * 
	 */
	private static void initialiseReader(File file) {
		try {
			reader = new Scanner(file);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialise the file writer.
	 * 
	 */
	private static void initialiseWriter(File file) {
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(
					file, false)));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tear down the file reader.
	 * 
	 */
	private static void tearDownReader() {
		reader.close();
	}
	/**
	 * Tear down the file writer.
	 * 
	 */
	private static void tearDownWriter() {
		writer.close();
	}
	
	/**
	 * Create file with the specified file path if it does not exist.
	 * 
	 */
	public static void createFileIfNonExistent(File file) {
		try {
			if (!file.exists()) {
				file.createNewFile();
				//write a null Memory in JSON format to file
                storeMemoryToFile(new Memory(), file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes testStorageFile for tearing-down in system tests.
	 * 
	 */
	public static void deleteStorageFileIfExists(String fileDirectory){
		String filePath = fileDirectory + "/" + FILE_NAME;
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
	}

	/**
	 * Method to export the current memory into a JSON String for external
	 * storage in a file
	 * 
	 * @return String json
	 */
	public static String exportAsJson(Memory mem) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(DateTime.class,
				new DateTimeTypeConverter());
		gsonBuilder.registerTypeAdapter(LocalDate.class,
				new LocalDateTypeConverter());
		gsonBuilder.registerTypeAdapter(LocalTime.class,
				new LocalTimeTypeConverter());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String jsonString = gson.toJson(mem);
		return jsonString;
	}

	/**
	 * Method to parse a json String representing an instance of memory into an
	 * instance of Memory class
	 * 
	 * @param jsonString JSON representation of an instance of memory as String
	 * @return an instance of Memory class
	 */
	public static Memory importFromJson(String jsonString) throws JsonSyntaxException {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(DateTime.class,
				new DateTimeTypeConverter());
		gsonBuilder.registerTypeAdapter(LocalDate.class,
				new LocalDateTypeConverter());
		gsonBuilder.registerTypeAdapter(LocalTime.class,
				new LocalTimeTypeConverter());
		gsonBuilder.registerTypeAdapter(DurationFieldType.class, new DurationFieldTypeDeserialiser());
		Gson gson = gsonBuilder.create();
		
		return gson.fromJson(jsonString, Memory.class);
	}

    
	/**
	 * Converter to serialize and deserialize between org.joda.time.DateTime and
	 * com.google.gson.JsonElement
	 * 
	 * With reference to:
	 * 
	 * https://sites.google.com/site/gson/gson-user-guide
	 * 
	 *
	 */
	private static class DateTimeTypeConverter implements
			JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
		@Override
		public JsonElement serialize(DateTime src, Type srcType,
				JsonSerializationContext context) {
			return new JsonPrimitive(src.toString());
		}

		@Override
		public DateTime deserialize(JsonElement json, Type type,
				JsonDeserializationContext context) throws JsonParseException {
			try {
				return new DateTime(json.getAsJsonPrimitive().getAsString());
			} catch (IllegalArgumentException e) {
				// Try parsing as java.util.Date instead
				Date date = context.deserialize(json, Date.class);
				return new DateTime(date);
			}
		}

	}


	/**
	 * Converter to serialize and deserialize between org.joda.time.DateTime and
	 * com.google.gson.JsonElement
	 *
	 * With reference to:
	 * 
	 * https://sites.google.com/site/gson/gson-user-guide
	 */

	private static class LocalDateTypeConverter implements
			JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
		@Override
		public JsonElement serialize(LocalDate src, Type typeOfSrc,
				JsonSerializationContext context) {
			return new JsonPrimitive(src.toString());
		}

		@Override
		public LocalDate deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			return new LocalDate(json.getAsJsonPrimitive().getAsString());
		}

	}

	/**
	 * Converter class to serialize and deserialize between
	 * org.joda.time.LocalTime and com.google.gson.JsonElement
	 * 
	 * With reference to:
	 * 
	 * https://sites.google.com/site/gson/gson-user-guide
	 *
	 */
	private static class LocalTimeTypeConverter implements
			JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
		@Override
		public JsonElement serialize(LocalTime src, Type typeOfSrc,
				JsonSerializationContext context) {
			return new JsonPrimitive(src.toString());
		}

		@Override
		public LocalTime deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			return new LocalTime(json.getAsJsonPrimitive().getAsString());
		}

	}
	
	/**
	 * Deserialiser for JodaTime's DurationFieldType for proper 
	 * Json deserialisation
	 */
	private static class DurationFieldTypeDeserialiser implements JsonDeserializer<DurationFieldType> {

		  @Override
		  public DurationFieldType deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
		      throws JsonParseException {
		    final JsonObject jsonObject = json.getAsJsonObject();

		    final String iName = jsonObject.get("iName").getAsString();
		    
		    switch(iName){
		    
		    case "years":
		    return DurationFieldType.years();
		    
		    case "months":
		    return DurationFieldType.months();
		    
		    case "weeks":
		    return DurationFieldType.weeks();
		    
		    case "days":
		    return DurationFieldType.days();
		    
		    case "hours":
		    return DurationFieldType.hours();
		    
		    case "minutes":
		    return DurationFieldType.minutes();
		    
		    case "seconds":
		    return DurationFieldType.seconds();
		    
		    case "millis":
		    return DurationFieldType.millis();
		    
		    // Should not be reached
		    default:
		    throw new JsonParseException("No suitable iName found.");
		    }
		    
		  }
		}

}
