package common;

import java.util.ArrayList;
import java.util.Arrays;

import userinterface.Main;

public class UserInterfaceObjectListTest {
	
	//create ArrayList<UserInterfaceObject> before populating by passing in parameter
	public static ArrayList<UserInterfaceObject> populateTestObjectList(ArrayList<UserInterfaceObject> list) {
		list.clear();
		
		String testIndex = "0.";
		String testName = "TEST NAME DJFNSKFNDJSKFNJKDSNFJKDNFJKDSNFJKDSNFJKDSNFJKNDFSDHHJFNDSJFNJKDSNFJKDSNFJKDSFNJKDSFNJKF"; 
		String testTime = "12:00 - 15:00";
		String testTypeAdd = "ADD";
		String testTypeEdit = "EDIT";
		String testTypeDelete = "DELETE";
		String testTypeMark = "MARK";
		String testTypeUndo = "UNDO";
		String testTypeRedo = "REDO";
		String testTypeSearch = "SEARCH";
		String testTypeClash = "CLASH";
		String testTypeNone = "NONE";

		UserInterfaceObject testAdd = new UserInterfaceObject(testIndex, testName, testTime, testTypeAdd);
		UserInterfaceObject testEdit = new UserInterfaceObject(testIndex, testName, testTime, testTypeEdit);
		UserInterfaceObject testDelete = new UserInterfaceObject(testIndex, testName, testTime, testTypeDelete);
		UserInterfaceObject testMark = new UserInterfaceObject(testIndex, testName, testTime, testTypeMark);
		UserInterfaceObject testUndo = new UserInterfaceObject(testIndex, testName, testTime, testTypeUndo);
		UserInterfaceObject testRedo = new UserInterfaceObject(testIndex, testName, testTime, testTypeRedo);
		UserInterfaceObject testSearch = new UserInterfaceObject(testIndex, testName, testTime, testTypeSearch);
		UserInterfaceObject testClash = new UserInterfaceObject(testIndex, testName, testTime, testTypeClash);
		UserInterfaceObject testNone = new UserInterfaceObject(testIndex, testName, testTime, testTypeNone);
		
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
