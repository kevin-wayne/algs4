/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.assignment.baseball;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author vahbuna
 */
public class BaseballEliminationTest {
    private static BaseballElimination baseballGames;
    private static BaseballElimination baseballGamesV2;

    @BeforeClass
    public static void readFile() {
        baseballGames = new BaseballElimination("data/baseball/teams4.txt");
        baseballGamesV2 = new BaseballElimination("data/baseball/teams5.txt");
    }

    @Test
    public void testNumberOfTeams() {
        Assert.assertEquals(4, baseballGames.numberOfTeams());
    }

    @Test
    public void testWins() {
        Assert.assertEquals(80, baseballGames.wins("Philadelphia"));
    }

    @Test
    public void testLosses() {
        Assert.assertEquals(71, baseballGames.losses("Atlanta"));
    }

    @Test
    public void testRemaining() {
        Assert.assertEquals(3, baseballGames.remaining("Montreal"));
    }

    @Test
    public void testAgainst() {
        Assert.assertEquals(2, baseballGames.against("Montreal",
                "Philadelphia"));
    }

    @Test
    public void testElimination() {
        Assert.assertTrue(baseballGamesV2.isEliminated("Detroit"));
        Assert.assertFalse(baseballGamesV2.isEliminated("Boston"));
        Assert.assertFalse(baseballGamesV2.isEliminated("Baltimore"));
        for (String team : baseballGames.teams()) {
            if (baseballGames.isEliminated(team)) {
                System.out.print(team + " is eliminated by the subset R = { ");
                for (String t : baseballGames.certificateOfElimination(team)) {
                    System.out.print(t + " ");
                }
                System.out.println("}");
            } else {
                System.out.println(team + " is not eliminated");
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void teamNotFound() {
        baseballGames.losses("California");
    }
}
