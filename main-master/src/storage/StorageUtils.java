package storage;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import com.google.gson.JsonSyntaxException;

import logic.ClockWork;

/**
 * Processes the custom file directory path specified by user.
 * 
 * Since the storage file at the default location can also be corrupt or invalid,
 * it is also checked in the StorageHandler class.
 */
public class StorageUtils {

		private static final String SETTINGS_FILE_NAME = "settings.txt";
		private static final String STORAGE_FILE_NAME = "storageFile.json";
		
		private static final String MESSAGE_INCORRECT_COMMAND = "Incorrect command is given.";
		private static final String MESSAGE_INVALID_DIRECTORY = "User-specified directory for storage is invalid. Storage file location is reverted to the default : %1$s";
		private static final String MESSAGE_COPIED_TO_LOCATION = "Storage file copied to specified location: %1$s";
		private static final String MESSAGE_DIRECTORY_DEFAULT = "Storage file location is reverted to the default: %1$s";
		private static final String MESSAGE_DIRECTORY_UPDATED = "Directory of the storage file is updated to: %1$s";
		private static final String MESSAGE_CORRUPT_FILE = "An unreadable storage file exists at the user-specified location. \n"
				+ "Do you wish to : \n"
				+ "\t 1. Copy over an existing version from the default location (C) \n"
				+ "\t 2. Overwrite the file with a blank file (O) \n"
				+ "\t 3. Revert to the default location (R)\n"
				+ "\t 4. Exit the program (E)";
		
		private static String defaultFileDirectory;
		private static String fileDirectory;
		private static String settingsFilePath;
		private static File settingsFile;
		
	/**
	 * Processes the user-specified path.
	 * 
	 * If the path is valid, and there exists a Storage file (storageFile.json)
	 * at that user-specified path, the file will be checked if it is a valid
	 * JSON file. If it is, it will be used automatically.
	 * 
	 * If the file at the user-specified path is corrupt or invalid, the user
	 * will be asked to choose the following options, which would be executed accordingly.
	 * 1. Copy the storage file from the default location 
	 * 2. Create and use a new storage file at the user-specified path 
	 * 3. Revert back to using the storage file at the default location 
	 * 4. Exit
	 * 
	 * @param String customFileDirPath
	 * @return String Path of storageFile.json after processing
	 */
		public static String processStorageDirectory(String customFileDirPath) {
			Scanner scn = ClockWork.scn;
			// If valid: copy any existing storageFile from its current location to
			// the
			// new user-specified location. Change the settings file.
			if (isValidDirPath(customFileDirPath)) {
				// Check if there is a file at customFileDirPath and select
				// overwrite option.
				String customFilePath = customFileDirPath + "/" + STORAGE_FILE_NAME;
				File newStorageFile = new File(customFilePath);

				if (newStorageFile.exists()) {
					// newStorageFile is not in Json format
					if(!isFileInJsonFormat(newStorageFile)){
						System.out
						.println(MESSAGE_CORRUPT_FILE);
						String command;
						do{
							command = scn.next().toUpperCase().trim();
							switch(command){
							case "C":
								String storageFilePath = fileDirectory + "/"
										+ STORAGE_FILE_NAME;
								File currentStorageFile = new File(storageFilePath);
								
								if (currentStorageFile.exists()) {
									copyStorageFile(storageFilePath, customFileDirPath);
									System.out
											.println("Storage file copied to specified location: "
													+ customFileDirPath);
								}
								// No existing storageFile.json at the default location
								// Create a blank storageFile.json at the user-specified location
								else{
									newStorageFile.delete();
									try{
										newStorageFile.createNewFile();
									} catch(IOException e){
										e.printStackTrace();
									}
									StorageHandler.createFileIfNonExistent(newStorageFile);
									StorageHandler.storeMemoryToFile(new Memory(), newStorageFile);
									
								}
								// Update settings file and file directory
								modifySettingsFile(customFileDirPath);
								fileDirectory = customFileDirPath;
								System.out
										.println(String.format(MESSAGE_DIRECTORY_UPDATED, customFileDirPath));
								break;
							case "O":
								newStorageFile.delete();
								try{
									newStorageFile.createNewFile();
								} catch(IOException e){
									e.printStackTrace();
								}
								StorageHandler.createFileIfNonExistent(newStorageFile);
								StorageHandler.storeMemoryToFile(new Memory(), newStorageFile);
								
								// Update settings file and file directory
								modifySettingsFile(customFileDirPath);
								fileDirectory = customFileDirPath;
								System.out
										.println(String.format(MESSAGE_DIRECTORY_UPDATED, customFileDirPath));
								break;
							case "R":
								// Do nothing; fileDirectory is already default
								System.out
								.println(String.format(MESSAGE_DIRECTORY_DEFAULT, fileDirectory));
								break;
							case "E":
								System.exit(0);
							default:
								System.out.println(MESSAGE_INCORRECT_COMMAND);
							break;
							}
							scn.nextLine();
						}while(!command.equals("C")&&!command.equals("O")&&!command.equals("R")&&!command.equals("E"));
					}
					// newStorageFile is in Json format
					// Use the existing file at user-specified location automatically
					else{
					 modifySettingsFile(customFileDirPath);
					 fileDirectory = customFileDirPath;
					}
				}
				// newStorageFile does not exist.
				else {
					// If there exists a storageFile.json in the current directory
					// as
					// specified in the settings file, copy it to the custom
					// directory
					String storageFilePath = fileDirectory + "/"
							+ STORAGE_FILE_NAME;
					File currentStorageFile = new File(storageFilePath);
					if (currentStorageFile.exists()) {
						copyStorageFile(storageFilePath, customFileDirPath);
						System.out
								.println(String.format(MESSAGE_COPIED_TO_LOCATION, customFileDirPath));
					}
					// Update regardless of existence of storageFile.json
					modifySettingsFile(customFileDirPath);
					fileDirectory = customFileDirPath;
					System.out
							.println(String.format(MESSAGE_DIRECTORY_UPDATED, customFileDirPath));
				}

			}
			// If customFileDirPath is invalid: revert back to default directory
			else {
				System.out
						.println(String.format(MESSAGE_INVALID_DIRECTORY, fileDirectory));
			}
			return fileDirectory;
		}
		
		/**
		 * Checks if the file passed as parameter is in Json format.
		 * 
		 * @param File
		 * @return Boolean
		 */
		static Boolean isFileInJsonFormat(File file){
			try{
				Scanner reader = new Scanner(file);
				StringBuilder builder = new StringBuilder();

				while (reader.hasNextLine()) {
					builder.append(reader.nextLine() + "\n");
				}
				String jsonString = builder.toString();
				StorageHandler.importFromJson(jsonString);
				reader.close();
			} catch(FileNotFoundException e){
				e.printStackTrace();
			} catch(JsonSyntaxException e){
				return false;
			}
			return true;
		}
		/**
		 * Copies storageFile.json from the storageFilePath to customFileDirPath 
		 * 
		 * @param String storageFilePath
		 * @param String customFileDirPath
		 */
		static void copyStorageFile(String storageFilePath,
				String customFileDirPath) {
			String customFilePath = customFileDirPath + "/" + STORAGE_FILE_NAME;
			try {
				Files.copy(Paths.get(storageFilePath), Paths.get(customFilePath),
						REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Modifies the Settings file (settings.txt) by overriding the file with
		 * the path specified by customFileDirPath.
		 * 
		 * @param String customFileDirPath
		 */
		static void modifySettingsFile(String customFileDirPath) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						settingsFile, false));
				writer.write(customFileDirPath);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
	
		/**
		 * Attempt to read settings.txt in the default directory.
		 * 
		 * If settings.txt does not exist, it will be created and the default
		 * directory path string will be written into it.
		 * 
		 * If settings.txt exists, the directory for saving storageFile.json will be
		 * read from the file. If the directory read is invalid, the directory of
		 * storageFile.json will be reverted to the default.
		 * 
		 */
		public static String readSettingsFile() {
			// Set default file directory
			setDefaultFileDirectory();

			// Build settings file path
			settingsFilePath = defaultFileDirectory + "/" + SETTINGS_FILE_NAME;

			// Check if settings file exists
			settingsFile = new File(settingsFilePath);
			BufferedWriter writer;
			try {
				if (!settingsFile.exists()) {
					settingsFile.createNewFile();
					// Write default storage file directory path to settings file
					writer = new BufferedWriter(new FileWriter(settingsFile));
					writer.write(defaultFileDirectory);
					fileDirectory = defaultFileDirectory;
					writer.close();
				}
				// Settings file exists. Read storage file directory path from file.
				else {
					BufferedReader reader = new BufferedReader(new FileReader(
							settingsFile));
					// Read storage directory file path
					String fileDirectoryString = reader.readLine();

					// If storage directory file path is invalid, overwrite settings
					// file
					// with default directory path and set the storage file
					// directory path to default
					if (!isValidDirPath(fileDirectoryString)) {
						writer = new BufferedWriter(new FileWriter(settingsFile,
								false));
						writer.write(defaultFileDirectory);
						writer.close();
						fileDirectory = defaultFileDirectory;
					}
					// If storage file path is valid, set it as file directory
					else {
						fileDirectory = fileDirectoryString;
					}

					reader.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			return fileDirectory;
		}

		/**
		 * Sets the default file directory where storageFile.json is saved as the
		 * same directory the program is being run from.
		 */
		static void setDefaultFileDirectory() {
			defaultFileDirectory = new File("").getAbsolutePath();
		}

		/**
		 * Checks if the string is a valid file directory.
		 * 
		 * @param String fileDirectoryString
		 * @return Boolean if string is a valid directory, false otherwise.
		 */
		static Boolean isValidDirPath(String fileDirectoryString) {
			if (fileDirectoryString == null || fileDirectoryString == "") {
				return false;
			}

			return new File(fileDirectoryString).isDirectory();

		}

}
