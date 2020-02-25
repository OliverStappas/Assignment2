public class TrainNetwork {
	final int swapFreq = 2;
	TrainLine[] networkLines;

    public TrainNetwork(int nLines) {
    	this.networkLines = new TrainLine[nLines];
    }

    public void addLines(TrainLine[] lines) {
    	this.networkLines = lines;
    }

    public TrainLine[] getLines() {
    	return this.networkLines;
    }

    public void dance() {
    	System.out.println("The tracks are moving!");
    	for (TrainLine line: networkLines) {
    		line.shuffleLine();
		}
    }

    public void undance() {
		for (TrainLine line: networkLines) {
			line.sortLine();
		}
	}

	public int travel(String startStation, String startLine, String endStation, String endLine) {

		TrainLine curLine = null;
		TrainStation curStation = null;
		TrainLine finalLine;
		TrainStation finalStation;


		try {
			curLine = this.getLineByName(startLine); //use this variable to store the current line. // = null
		}

		catch (LineNotFoundException e) {
			System.out.println("Departing line not on network");
		}

		try {
			curStation = curLine.findStation(startStation);
		}

		catch (StationNotFoundException e) {
			System.out.println("Departing station not on network");
		}

		//	curStation = curLine.findStation(startStation);

		try {
			finalLine = this.getLineByName(endLine);
		}

		catch (LineNotFoundException e) {
			System.out.println("Jumped off after spending a full week on the train. Might as well walk.");
			return 168;
		}

		//	finalLine = this.getLineByName(endLine);

		try {
			finalStation = finalLine.findStation(endStation);
		}

		catch (StationNotFoundException e) {
			System.out.println("Jumped off after spending a full week on the train. Might as well walk.");
			return 168;
		}

		TrainStation prevStation = null;

		TrainStation tempCurrent;

		int hoursCount = 0;
		System.out.println("Departing from "+startStation);

		while(!endStation.equalsIgnoreCase(curStation.getName()) && !endLine.equalsIgnoreCase(curStation.getName())) {

			if(hoursCount == 168) {
				System.out.println("Jumped off after spending a full week on the train. Might as well walk.");
				return 168;
			}


			if (hoursCount % 2 == 0 && hoursCount != 0) {
				this.dance();
				curLine = curStation.getLine();
			}

			hoursCount += 1;


			tempCurrent = curStation;

			try {
				curStation = curLine.travelOneStation(curStation, prevStation);
			}

			catch (StationNotFoundException e) {
				return hoursCount;
			}

			curLine = curStation.getLine();

			prevStation = tempCurrent;

			//prints an update on your current location in the network.
			System.out.println("Traveling on line "+curLine.getName()+":"+curLine.toString());
			System.out.println("Hour "+hoursCount+". Current station: "+curStation.getName()+" on line "+curLine.getName());
			System.out.println("=============================================");

		}

		System.out.println("Arrived at destination after "+hoursCount+" hours!");
		return hoursCount;

	}

    //you can extend the method header if needed to include an exception. You cannot make any other change to the header.
    public TrainLine getLineByName(String lineName){
    	//YOUR CODE GOES HERE
		for (TrainLine line: this.networkLines) {
			if (line.getName().equalsIgnoreCase(lineName)) {
				return line;
			}
		}

		throw new LineNotFoundException("Line not found");
    }

  //prints a plan of the network for you.
    public void printPlan() {
    	System.out.println("CURRENT TRAIN NETWORK PLAN");
    	System.out.println("----------------------------");
    	for(int i=0;i<this.networkLines.length;i++) {
    		System.out.println(this.networkLines[i].getName()+":"+this.networkLines[i].toString());
    		}
    	System.out.println("----------------------------");
    }
}

//exception when searching a network for a LineName and not finding any matching Line object.
class LineNotFoundException extends RuntimeException {
	   String name;

	   public LineNotFoundException(String n) {
	      name = n;
	   }

	   public String toString() {
	      return "LineNotFoundException[" + name + "]";
	   }
	}