package common;

import java.util.ArrayList;
import java.util.Arrays;

import userinterface.Main;

public class UserInterfaceObjectListTest {
	
	//create ArrayList<UserInterfaceObject> before populating by passing in parameter
	public static ArrayList<UserInterfaceObject> populateTestObjectList(ArrayList<UserInterfaceObject> list) {
		list.clear();
		
		String testIndex = "0.";
		String testName = "TEST NAME"; 
		String testTime = "12:00 - 15:00";
		String testDate = "12 December 2016";
		String testTypeAdd = "ADD";
		String testTypeEdit = "EDIT";
		String testTypeDelete = "DELETE";
		String testTypeMark = "MARK";
		String testTypeUndo = "UNDO";
		String testTypeRedo = "REDO";
		String testTypeSearch = "SEARCH";
		String testTypeClash = "CLASH";
		String testTypeNone = "NONE";

		UserInterfaceObject testAdd = new UserInterfaceObject(testIndex, testName, testTime, testTypeAdd, testDate);
		UserInterfaceObject testEdit = new UserInterfaceObject(testIndex, testName, testTime, testTypeEdit, testDate);
		UserInterfaceObject testDelete = new UserInterfaceObject(testIndex, testName, testTime, testTypeDelete, testDate);
		UserInterfaceObject testMark = new UserInterfaceObject(testIndex, testName, testTime, testTypeMark, testDate);
		UserInterfaceObject testUndo = new UserInterfaceObject(testIndex, testName, testTime, testTypeUndo, testDate);
		UserInterfaceObject testRedo = new UserInterfaceObject(testIndex, testName, testTime, testTypeRedo, testDate);
		UserInterfaceObject testSearch = new UserInterfaceObject(testIndex, testName, testTime, testTypeSearch, testDate);
		UserInterfaceObject testClash = new UserInterfaceObject(testIndex, testName, testTime, testTypeClash, testDate);
		UserInterfaceObject testNone = new UserInterfaceObject(testIndex, testName, testTime, testTypeNone, testDate);
		
		list.add(testAdd);
		list.add(testEdit);
		list.add(testDelete);
		list.add(testMark);
		list.add(testUndo);
		list.add(testRedo);
		list.add(testSearch);
		list.add(testClash);
		list.add(testNone);
		
		return list;
	}
}
