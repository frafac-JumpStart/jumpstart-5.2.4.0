package jumpstart.web.state.examples.state;

public class Count {
	private int _timesVisited = 0;

	public int getTimesVisited() {
		return _timesVisited;
	}

	public void incrementTimesVisited() {
		_timesVisited += 1;
	}
}
