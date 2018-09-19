package peppervalleypoolscheduler2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.lang.Character;

public class scheduler2 {
	public static void main(String[] args) throws FileNotFoundException {
		// this is the file that the availability is being read from
		File inFile = new File("staffAvailibility.txt");
		// this is the file where the schedule is being written to
		File outFile = new File("staffSchedule.txt");
		Scanner in = new Scanner(inFile);
		PrintWriter out = new PrintWriter(outFile);
		// this is a counter that keeps track of how many workers are currently assigned
		// to the shift
		int numPerShift = 0;
		// this is an array of counters that each hold the number of shifts for each
		// worker respective to the array below
		// the first element has a value of 31 to make sure that the brackets are only
		// put into the schedule when no one else can work
		int[] numShifts = { 31, 0, 0, 0, 0, 0, 0, 0, 0 };
		// this is an array of each of my workers, however the first element is brackets
		// to represent that no one is able to work that shift
		String[] names = { "[]", "XT", "JC", "DA", "BG", "NH", "MW", "LK", "AM" };
		// this is a string that flips from "o" to "c" depending on whether the shift
		// that is currently being scheduled is and opening or closing shift
		// this variable isn't a boolean because it is also use in combination with date
		// to specify the shift
		String openClose = "o";
		// this variable specifies the date
		int date = 1;
		// this is a string that is a combination of the date and the openClose variable
		// to come up with the shift name
		String shift = date + openClose;
		// this value represent the last line number in the input file that the program
		// looked through so the program knows what worker it was referencing in the
		// input file
		int lineNum = 0;
		// this is a boolean that identifies whether the worker is available for the
		// shift
		boolean isAvail = true;
		// this variable represent the element number in the names array that is
		// currently going to be schedule to work
		int onDeckNum = 0;
		// this temporarily holds the .next() of the scanner so it can be compared to
		// the shift variable
		String next = "";
		// this represents the element number in names that is already working in the
		// current shift
		int alreadyWorking = 0;
		// this is a counter in the while loop to go through every shift of the month
		int i = 0;
		// this keeps track of which row in the schedule array is currently being filled
		int scheduleRowNum = 0;
		// this array keeps track of the element number of the workers in the opening
		// shift to avoid them from working double shift unless they are the only one
		// available
		int openWorker[] = { 0, 0 };
		// this represents the element number in names that is working a doublesShift
		int doubleShift = 0;
		// this two dimensional array stores the data of the schedule
		String[][] schedule = new String[68][3];
		// iterates for all shifts assuming that the month is 31
		while (i < 68) {
			// if there is more file left to read
			if (in.hasNext() == true) {
				next = in.next();
				// if the next item scanned is the new line
				if (next.charAt(0) == '.') {
					lineNum = lineNum + 1;
					if (isAvail == true && (numShifts[lineNum] < numShifts[onDeckNum]) && (lineNum != alreadyWorking)) {
						// if this person isn't working the first shift
						if (lineNum != openWorker[0] && lineNum != openWorker[1]) {
							// assign the worker to the person on shift
							onDeckNum = lineNum;
							// if this person is working the first shift
						} else {
							// if this person has less shifts than the other person who would pull the
							// double shift
							if (numShifts[lineNum] < numShifts[doubleShift]) {
								doubleShift = lineNum;
							}
						}
					}
					isAvail = true;
				}
				// if not, compare with the shift to see if the currently scanned component is
				// the same as the shift your looking for
				else {
					if (next.equals(shift)) {
						isAvail = false;
					}
				}
			}
			// when the entire file has been read and you want to move on to the next shift
			else {
				if (onDeckNum == 0) {
					onDeckNum = doubleShift;
				}
				doubleShift = 0;
				if (numPerShift == 0) {
					schedule[scheduleRowNum][0] = shift;
				}
				if (numPerShift == 1) {
					schedule[scheduleRowNum][2] = names[onDeckNum];
					if (shift.contains("o")) {
						openWorker[1] = onDeckNum;
					}
					scheduleRowNum = scheduleRowNum + 1;
					alreadyWorking = 99;
				} else {
					schedule[scheduleRowNum][1] = names[onDeckNum];
					if (shift.contains("o")) {
						openWorker[0] = onDeckNum;
					}
					alreadyWorking = onDeckNum;
					if (alreadyWorking == 0) {
						alreadyWorking = 99;
					}
				}
				numPerShift = numPerShift + 1;
				if (onDeckNum != 0) {
					numShifts[onDeckNum] = numShifts[onDeckNum] + 1;
				}
				onDeckNum = 0;
				if (numPerShift == 2) {
					if (openClose == "c") {
						openClose = "o";
						openWorker[0] = 0;
						openWorker[1] = 0;
						date++;
					} else {
						openClose = "c";
					}
					shift = date + openClose;
					i++;
					numPerShift = 0;
				}
				in = new Scanner(inFile);
				lineNum = 0;
			}
		}
		for (int k = 0; k < 68; k++) {
			for (int m = 0; m < 3; m++) {
				out.print(schedule[k][m] + " ");
			}
			out.println(" ");
		}
		out.println(" ");
		for (int j = 1; j < 9; j++) {
			out.print(names[j] + ": ");
			out.println(numShifts[j]);
		}
		in.close();
		out.close();

	}
}
