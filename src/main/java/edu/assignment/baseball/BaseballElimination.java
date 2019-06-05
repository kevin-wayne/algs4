/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.assignment.baseball;

import edu.princeton.cs.algs4.In;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author vahbuna
 */
public class BaseballElimination {
    private final int numTeams;
    private final String[] names;
    private HashMap<String, Integer> nameIdMap = new HashMap<>();
    private final int[] wins;
    private final int[] losses;
    private final int[] remains;
    private final int[][] games;
    /**
     * create a baseball division from given filename in format specified below.
     * @param filename 
     */
    public BaseballElimination(String filename) {
        In reader = new In(filename);
        numTeams = reader.readInt();
        names = new String[numTeams];
        wins = new int[numTeams];
        losses = new int[numTeams];
        remains = new int[numTeams];
        games = new int[numTeams][numTeams];
        for (int i = 0; i < numTeams; i++) {
            names[i] = reader.readString();
            nameIdMap.put(names[i], i);
            wins[i] = reader.readInt();
            losses[i] = reader.readInt();
            remains[i] = reader.readInt();
            for (int j = 0; j < numTeams; j++) {
                games[i][j] = reader.readInt();
            }
        }
    }
    
    /**
     * number of teams.
     * @return 
     */
    public int numberOfTeams() {
        return numTeams;
    }
    
    /**
     * all teams.
     * @return 
     */
    public Iterable<String> teams() {
        return Arrays.asList(names);
    }
    
    /**
     * number of wins for given team.
     * @param team
     * @return 
     */
    public int wins(String team) {
        return wins[nameIdMap.get(team)];
    }
    /**
     * number of losses for given team.
     * @param team
     * @return 
     */
    public int losses(String team) {
        return losses[nameIdMap.get(team)];
    }
    
    /**
     * number of remaining games for given team.
     * @param team
     * @return 
     */
    public int remaining(String team) {
        return remains[nameIdMap.get(team)];
    }
    
    /**
     * number of remaining games between team1 and team2.
     * @param team1
     * @param team2
     * @return 
     */
    public int against(String team1, String team2) {
        return games[nameIdMap.get(team1)][nameIdMap.get(team2)];
    }
    
    /**
     * is given team eliminated?.
     * @param team
     * @return 
     */
    public boolean isEliminated(String team) {
        return false;
    }

    /**
     * subset R of teams that eliminates given team; null if not eliminated.
     * @param team
     * @return 
     */
    public Iterable<String> certificateOfElimination(String team) {
        return null;
    }
}
