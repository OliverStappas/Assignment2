import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Currency;
import java.util.Random;

public class TrainLine {

	private TrainStation leftTerminus;
	private TrainStation rightTerminus;
	private String lineName;
	private boolean goingRight;
	public TrainStation[] lineMap;
	public static Random rand;

	public TrainLine(TrainStation leftTerminus, TrainStation rightTerminus, String name, boolean goingRight) {
		this.leftTerminus = leftTerminus;
		this.rightTerminus = rightTerminus;
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;

		this.lineMap = this.getLineArray();
	}

	public TrainLine(TrainStation[] stationList, String name, boolean goingRight)
		/*
		 * Constructor for TrainStation input: stationList - An array of TrainStation
		 * containing the stations to be placed in the line name - Name of the line
		 * goingRight - boolean indicating the direction of travel
		 */ {
		TrainStation leftT = stationList[0];
		TrainStation rightT = stationList[stationList.length - 1];

		stationList[0].setRight(stationList[stationList.length - 1]);
		stationList[stationList.length - 1].setLeft(stationList[0]);

		this.leftTerminus = stationList[0];
		this.rightTerminus = stationList[stationList.length - 1];
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;

		for (int i = 1; i < stationList.length - 1; i++) {
			this.addStation(stationList[i]);
		}

		this.lineMap = this.getLineArray();
	}

	public TrainLine(String[] stationNames, String name,
					 boolean goingRight) {/*
	 * Constructor for TrainStation. input: stationNames - An array of String
	 * containing the name of the stations to be placed in the line name - Name of
	 * the line goingRight - boolean indicating the direction of travel
	 */
		TrainStation leftTerminus = new TrainStation(stationNames[0]);
		TrainStation rightTerminus = new TrainStation(stationNames[stationNames.length - 1]);

		leftTerminus.setRight(rightTerminus);
		rightTerminus.setLeft(leftTerminus);

		this.leftTerminus = leftTerminus;
		this.rightTerminus = rightTerminus;
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;
		for (int i = 1; i < stationNames.length - 1; i++) {
			this.addStation(new TrainStation(stationNames[i]));
		}

		this.lineMap = this.getLineArray();


	}

	// adds a station at the last position before the right terminus
	public void addStation(TrainStation stationToAdd) {
		TrainStation rTer = this.rightTerminus;
		TrainStation beforeTer = rTer.getLeft();
		rTer.setLeft(stationToAdd);
		stationToAdd.setRight(rTer);
		beforeTer.setRight(stationToAdd);
		stationToAdd.setLeft(beforeTer);

		stationToAdd.setTrainLine(this);

		this.lineMap = this.getLineArray();
	}

	public String getName() {
		return this.lineName;
	}

	public int getSize() {

		// YOUR CODE GOES HERE
		if (this.rightTerminus.equals(null)) {
			return 0;
		}
		int size = 1;
		TrainStation current = this.leftTerminus;
		while (current.getRight() != null) {
			current = current.getRight();
			size += 1;
		}
		return size;
	}

	public void reverseDirection() {
		this.goingRight = !this.goingRight;
	}

	// You can modify the header to this method to handle an exception. You cannot make any other change to the header.
	public TrainStation travelOneStation(TrainStation current, TrainStation previous) {


		if (current.getLine().equals(this)) {

			// YOUR CODE GOES HERE
			if (current.hasConnection && !current.getTransferStation().equals(previous)) {

				return current.getTransferStation();
			} else {

				return getNext(current);

			}


		}
		throw new StationNotFoundException("Station not found");
	}

	// You can modify the header to this method to handle an exception. You cannot make any other change to the header.
	public TrainStation getNext(TrainStation station) {

		// YOUR CODE GOES HERE
		if (station.getLine().equals(this)) {

			if (this.goingRight) {
				if (station.isRightTerminal()) {
					this.reverseDirection();
					return station.getLeft();
				}
				return station.getRight();
			}
			if (station.isLeftTerminal()) {
				this.reverseDirection();
				return station.getRight();
			}
			return station.getLeft();
		}
		throw new StationNotFoundException("Station not found");

	}

	// You can modify the header to this method to handle an exception. You cannot make any other change to the header.
	public TrainStation findStation(String name) {

		// YOUR CODE GOES HERE
		for (TrainStation tStation : this.lineMap) {
			if (tStation.getName().equalsIgnoreCase(name)) {
				return tStation;
			}
		}
		throw new StationNotFoundException("Station not found");
	}


    public void sortLine() {

        int swapped;
        TrainStation current;
        TrainStation previous;

        do {
            swapped = 0;
            current = leftTerminus;
            previous = null;

            while (current.getRight() != null) {

                if (current.getName().compareTo(current.getRight().getName()) > 0) {
                    TrainStation temp = current.getRight();
                    current.setRight(current.getRight().getRight());
                    current.setLeft(temp);
                    temp.setLeft(previous);
                    temp.setRight(current);

                    if (previous != null) {
						previous.setRight(temp);
					}
                    previous = temp;
                    previous.setLeft(temp.getLeft());
                    previous.setRight(temp.getRight());

                    swapped = 1;
                }

                else {
					TrainStation temp = current.getLeft();
					previous = current;
                    current = current.getRight();
					previous.setLeft(temp);
					previous.setRight(current);
                }

				if (previous.getLeft() == null) {
					previous.setLeftTerminal();
					leftTerminus = previous;

				} else if (previous.getLeft() != null && previous.getLeft().getLeft() == null) {
					previous.getLeft().setLeftTerminal();
					leftTerminus = previous.getLeft();
				}

				if (current.getRight() == null) {
					current.setRightTerminal();
					this.rightTerminus = current;
				}
            }
        } while (swapped != 0);

      	this.lineMap = this.getLineArray();

    }













//	public void swap(TrainStation trainStation1, TrainStation trainStation2) {
//	    TrainStation temp = trainStation1;
//	    trainStation1 = trainStation2;
//	    trainStation2 = temp;
//	}

	public TrainStation[] getLineArray() {

		// YOUR CODE GOES HERE
		TrainStation[] trainStation = new TrainStation[this.getSize()];
		trainStation[0] = leftTerminus;
		TrainStation current = trainStation[0];
		int counter = 1;
		while (current.getRight() != null) { // 		while (!current.equals(rightTerminus)) {
			trainStation[counter] = current.getRight();
			current = current.getRight();
			counter++;
		}

		return trainStation;



//		return Arrays.copyOf(this.lineMap, this.lineMap.length); // change this

		//		int size = this.lineMap.length;
		//		int count = 0;
		//		TrainStation[] lineArray = new TrainStation[this.lineMap.length];
		//		while (count < size) {
		//			lineArray[count] = this.lineMap[count];
		//			count++;
		//		}
		//		return  lineArray;
	}

	private TrainStation[] shuffleArray(TrainStation[] array) {
		Random rand = new Random();

		for (int i = 0; i < array.length; i++) {
			int randomIndexToSwap = rand.nextInt(array.length);
			TrainStation temp = array[randomIndexToSwap];
			array[randomIndexToSwap] = array[i];
			array[i] = temp;
		}
		this.lineMap = array;
		return array;
	}

	public void shuffleLine() {

		// you are given a shuffled array of trainStations to start with
		TrainStation[] lineArray = this.getLineArray();
		TrainStation[] shuffledArray = shuffleArray(lineArray);

		// YOUR CODE GOES HERE

		this.leftTerminus.setNonTerminal();

		this.rightTerminus.setNonTerminal();
		this.leftTerminus = shuffledArray[0];
		this.rightTerminus = shuffledArray[shuffledArray.length - 1];

		(shuffledArray[shuffledArray.length - 1]).setRightTerminal();
        shuffledArray[0].setLeftTerminal();
        this.leftTerminus.setLeft(null);
        this.rightTerminus.setRight(null);

//		this.leftTerminus.setLeftTerminal();
//		this.rightTerminus.setNonTerminal();
//		this.rightTerminus = shuffledArray[shuffledArray.length - 1];
//		this.leftTerminus = shuffledArray[0];
//		this.leftTerminus.setLeft(null);
//		this.rightTerminus.setRight(null);
//		this.leftTerminus.setNonTerminal();
//		this.rightTerminus.setRightTerminal();



		TrainStation current = this.rightTerminus;
        for (int i = shuffledArray.length - 2; i >= 0; i--) {
            current.setLeft(shuffledArray[i]);
            current = current.getLeft();
        }

        current = this.leftTerminus;

        for (int i = 1; i < shuffledArray.length; i++) {
            current.setRight(shuffledArray[i]);
            current = current.getRight();
        }

        this.lineMap = shuffledArray;

	}

	public String toString() {
		TrainStation[] lineArr = this.getLineArray();
		String[] nameArr = new String[lineArr.length];
		for (int i = 0; i < lineArr.length; i++) {
			nameArr[i] = lineArr[i].getName();
		}

		return Arrays.deepToString(nameArr);
	}

	public boolean equals(TrainLine line2) {

		// check for equality of each station
		TrainStation current = this.leftTerminus;
		TrainStation curr2 = line2.leftTerminus;

		try {
			while (current != null) {
				if (!current.equals(curr2))
					return false;
				else {
					current = current.getRight();
					curr2 = curr2.getRight();
				}
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public TrainStation getLeftTerminus() {
		return this.leftTerminus;
	}

	public TrainStation getRightTerminus() {
		return this.rightTerminus;
	}
}

//Exception for when searching a line for a station and not finding any station of the right name.
class StationNotFoundException extends RuntimeException {
	String name;

	public StationNotFoundException(String n) {
		name = n;
	}

	public String toString() {
		return "StationNotFoundException[" + name + "]";
	}
}
