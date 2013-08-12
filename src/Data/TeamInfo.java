package Data;

import java.util.HashMap;

/**
 * This class stores all the information about all the teams that the user can
 * select while submitting a result. I populated this class from my own
 * experiences in the game and chose all of the extremely popular teams as well
 * as some of the not so popular ones. This class can easily be edited in order
 * to increase or decrease the choices for the users.
 * 
 * @author Vishal
 * 
 */
public class TeamInfo {

	private String[] leagues = { "Barclays Premier League", "France Ligue 1",
			"German Bundesliga", "Liga BBVA", "Italian Serie A",
			"International" };
	private String[] english = { "Arsenal", "Chelsea", "Everton", "Liverpool",
			"Manchester City", "Manchester United", "Tottenham Hotspur" };
	private String[] spanish = { "Atletico Madrid", "Barcelona", "Malaga",
			"Real Madrid", "Sevilla", "Valencia" };
	private String[] french = { "Lille", "Lyon", "Marseille",
			"Paris Saint-Germain" };
	private String[] german = { "Borussia Dortmund", "Bayer Leverkusen",
			"Bayern Munich", "Schalke 04" };
	private String[] italian = { "AC Milan", "AS Roma", "Fiorentina",
			"Inter Milan", "Juventus", "Lazio", "Napoli" };
	private String[] international = { "Argentina", "Belgium", "Brazil",
			"England", "France", "Germany", "Italy", "Ivory Coast", "Mexico",
			"Netherlands", "Portugal", "Russia", "Spain", "USA", "Uruguay" };
	private HashMap<String, String[]> teams;

	public TeamInfo() {
		teams = new HashMap<String, String[]>();
		teams.put(leagues[0], english);
		teams.put(leagues[1], french);
		teams.put(leagues[2], german);
		teams.put(leagues[3], spanish);
		teams.put(leagues[4], italian);
		teams.put(leagues[5], international);
	}

	public String[] getLeagues() {
		return this.leagues;
	}

	public String[] getTeams(String league) {
		return teams.get(league);
	}
}
