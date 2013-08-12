package Data;

/**
 * This class represents a result that users submit and stores all the
 * information about the result as mentioned earlier.
 * 
 * @author Vishal
 * 
 */
public class Result {

	private String user1;
	private String user2;
	private int score1;
	private int score2;
	private String team1;
	private String team2;

	public Result(String u1, String u2, int s1, int s2, String t1, String t2) {
		user1 = u1;
		user2 = u2;
		score1 = s1;
		score2 = s2;
		team1 = t1;
		team2 = t2;
	}

	public String getUser1() {
		return user1;
	}

	public String getUser2() {
		return user2;
	}

	public int getScore1() {
		return score1;
	}

	public int getScore2() {
		return score2;
	}

	public String getTeam1() {
		return team1;
	}

	public String getTeam2() {
		return team2;
	}

	public String getResult(String user) {
		String s = "";
		if (user.equals(user1)) {
			s = user1 + " (" + team1 + ") : " + score1;
			s += " - " + score2 + " : " + user2 + " (" + team2 + ")";
		} else {
			s = user2 + " (" + team2 + ") : " + score2;
			s += " - " + score1 + " : " + user1 + " (" + team1 + ")";
		}
		return s;
	}

}
