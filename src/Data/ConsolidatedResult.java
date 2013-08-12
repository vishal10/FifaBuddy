package Data;

/**
 * This class represents the consolidated result data for a user from all his
 * results that have been submitted to the application. This is extremely useful
 * when setting up the table.
 * 
 * @author Vishal
 * 
 */
public class ConsolidatedResult {

	private String user;
	private int wins;
	private int losses;
	private int draws;
	private int games;
	private int points;
	private int goalsFor;
	private int goalsAgainst;

	public ConsolidatedResult(String user) {
		this.user = user;
		this.wins = 0;
		this.losses = 0;
		this.draws = 0;
		this.points = 0;
		this.games = 0;
	}

	public void addResult(Result result) {
		if (result.getUser1().equals(user)) {
			games++;
			goalsFor += result.getScore1();
			goalsAgainst += result.getScore2();
			if (result.getScore1() > result.getScore2()) {
				wins++;
			} else if (result.getScore1() == result.getScore2()) {
				draws++;
			} else {
				losses++;
			}
		} else if (result.getUser2().equals(user)) {
			games++;
			goalsFor += result.getScore2();
			goalsAgainst += result.getScore1();
			if (result.getScore1() > result.getScore2()) {
				losses++;
			} else if (result.getScore1() == result.getScore2()) {
				draws++;
			} else {
				wins++;
			}
		}
		update();
	}

	private void update() {
		this.points = wins * 3 + draws * 1;
	}

	public String getName() {
		return user;
	}

	public int getGames() {
		return games;
	}

	public int getPoints() {
		return points;
	}

	public int getWins() {
		return wins;
	}

	public int getLosses() {
		return losses;
	}

	public int getDraws() {
		return draws;
	}

	public int getGoalsFor() {
		return goalsFor;
	}

	public int getGoalsAgainst() {
		return goalsAgainst;
	}

}
