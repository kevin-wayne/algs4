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

/**
 *
 * @author vahbuna
 */
public class BaseballEliminationTest {
    private BaseballElimination baseballGames;
    
    @Before
    public void readFile() {
        baseballGames = new BaseballElimination("data/baseball/teams4.txt");
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
        Assert.assertEquals(2, baseballGames.against("Montreal", "Philadelphia"));
    }   
}
