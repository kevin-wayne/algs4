/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.assignment.baseball;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author vahbuna
 */
public class BaseballElimination {
    private final int numTeams;
    private final String[] names;
    private final HashMap<String, Integer> nameIdMap = new HashMap<>();
    private final HashMap<String, ArrayList<String>> certificate = new HashMap<>();
    private final int[] wins;
    private final int[] losses;
    private final int[] remains;
    private final int[][] games;
    private final int[] eliminated;
    /**
     * create a baseball division from given filename in format specified below.
     * @param filename path of file
     */
    public BaseballElimination(final String filename) {
        In reader = new In(filename);
        numTeams = reader.readInt();
        names = new String[numTeams];
        wins = new int[numTeams];
        losses = new int[numTeams];
        remains = new int[numTeams];
        games = new int[numTeams][numTeams];
        eliminated = new int[numTeams];

        for (int i = 0; i < numTeams; i++) {
            names[i] = reader.readString();
            nameIdMap.put(names[i], i);
            wins[i] = reader.readInt();
            losses[i] = reader.readInt();
            remains[i] = reader.readInt();
            eliminated[i] = -1;
            for (int j = 0; j < numTeams; j++) {
                games[i][j] = reader.readInt();
            }
        }
    }

    /**
     * number of teams.
     * @return count
     */
    public int numberOfTeams() {
        return numTeams;
    }

    /**
     * all teams.
     * @return list
     */
    public Iterable<String> teams() {
        return Arrays.asList(names);
    }

    /**
     * number of wins for given team.
     * @param team name
     * @return wins
     */
    public int wins(final String team) {
        if (!nameIdMap.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }
        return wins[nameIdMap.get(team)];
    }
    /**
     * number of losses for given team.
     * @param team name
     * @return losses
     */
    public int losses(final String team) {
        if (!nameIdMap.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }
        return losses[nameIdMap.get(team)];
    }

    /**
     * number of remaining games for given team.
     * @param team name
     * @return remaining games
     */
    public int remaining(final String team) {
        if (!nameIdMap.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }
        return remains[nameIdMap.get(team)];
    }

    /**
     * number of remaining games between team1 and team2.
     * @param team1 name
     * @param team2 name
     * @return remaining games
     */
    public int against(final String team1, final String team2) {
        if (!nameIdMap.containsKey(team1)) {
            throw new java.lang.IllegalArgumentException();
        }
        if (!nameIdMap.containsKey(team2)) {
            throw new java.lang.IllegalArgumentException();
        }
        return games[nameIdMap.get(team1)][nameIdMap.get(team2)];
    }

    /**
     * is given team eliminated?.
     * @param team name
     * @return true or false
     */
    public boolean isEliminated(final String team) {
        if (!nameIdMap.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }
        int idx = nameIdMap.get(team);
        boolean answer = false;
        if (eliminated[idx] == -1) {
            answer = checkElimination(idx);
        } else if (eliminated[idx] == 1) {
            answer = true;
        }
        return answer;
    }

    /**
     * Update the certificate of elimination.
     * @param i team being eliminated
     * @param node team that eliminated
     */
    private void updateCertificate(final int i, final int node) {
        ArrayList<String> teams = certificate.get(names[i]);
        if (teams == null) {
            teams = new ArrayList<String>();
        }
        teams.add(names[node]);
        certificate.put(names[i], teams);
    }

    /**
     * checks if the given team can be eliminated.
     * @param i index of the team
     * @return true or false
     */
    private boolean checkElimination(final int i) {
        int best = wins[i] + remains[i];
        boolean eliminate = false;
        eliminated[i] = 0;
        for (int node = 0; node < numTeams; node++) {
            if (node != i) {
                if (best < wins[node]) {
                    eliminate = true;
                    updateCertificate(i, node);
                    eliminated[i] = 1;
                }
            }
        }
        if (!eliminate) {
            int layerOneNodes = (numTeams - 2) * (numTeams - 1) / 2;
            int numNodes = layerOneNodes + numTeams + 2;
            FlowNetwork fn = new FlowNetwork(numNodes);
            int layerOne = 1;
            for (int m = 0; m < numTeams; m++) {
                for (int n = m + 1; n < numTeams; n++) {
                    if (i != m && i != n) {
                        fn.addEdge(new FlowEdge(0, layerOne, games[m][n]));
                        fn.addEdge(new FlowEdge(layerOne,
                                layerOneNodes + m + 1,
                                Double.POSITIVE_INFINITY));
                        if (wins[i] + remains[i] - wins[m] >= 0) {
                            int found = 0;
                            for (FlowEdge e : fn.adj(numNodes - 1)) {
                                if (e.from() == (layerOneNodes + m + 1)) {
                                    found = 1;
                                    break;
                                }
                            }
                            if (found == 0) {
                            fn.addEdge(new FlowEdge(layerOneNodes + m + 1,
                                       numNodes - 1,
                                       wins[i] + remains[i] - wins[m]));
                            }
                        }
                        fn.addEdge(new FlowEdge(layerOne,
                                layerOneNodes + n + 1,
                                Double.POSITIVE_INFINITY));
                        if (wins[i] + remains[i] - wins[n] >= 0) {
                            int found = 0;
                            for (FlowEdge e : fn.adj(numNodes - 1)) {
                                if (e.from() == (layerOneNodes + n + 1)) {
                                    found = 1;
                                    break;
                                }
                            }
                            if (found == 0) {
                                fn.addEdge(new FlowEdge(layerOneNodes + n + 1,
                                            numNodes - 1,
                                            wins[i] + remains[i] - wins[n]));
                            }
                        }
                        layerOne++;
                    }
                }
            }
            FordFulkerson elimination = new FordFulkerson(fn, 0, numNodes - 1);
            for (int j = 1 + layerOneNodes; j < numNodes - 1; j++) {
                if (elimination.inCut(j)) {
                    eliminate = true;
                    eliminated[i] = 1;
                    updateCertificate(i, j - 1 - layerOneNodes);
                }
            }
        }
        return eliminate;
    }

    /**
     * subset R of teams that eliminates given team; null if not eliminated.
     * @param team team name
     * @return list of teams
     */
    public Iterable<String> certificateOfElimination(final String team) {
        if (!nameIdMap.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }
        int idx = nameIdMap.get(team);
        if (eliminated[idx] == -1) {
            boolean answer = checkElimination(idx);
            if (answer) {
                eliminated[idx] = 1;
            } else {
                eliminated[idx] = 0;
            }
        }
        return certificate.get(team);
    }
}
