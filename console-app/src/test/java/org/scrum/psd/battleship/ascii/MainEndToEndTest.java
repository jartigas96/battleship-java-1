package org.scrum.psd.battleship.ascii;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import java.util.NoSuchElementException;

import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

public class MainEndToEndTest {
    @ClassRule
    public static final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
    @ClassRule
    public static final TextFromStandardInputStream gameInput = emptyStandardInputStream();

    @Test
    public void testPlayGameShotHits() {
        try {
            gameInput.provideLines("b4", "b5", "b6", "b7", "b8", "e5", "e6", "e7", "e8", "a3", "b3", "c3", "f8", "g8", "h8", "c5","c6","b4", "a1", "b5", "b6", "b7", "b8", "e5", "e6", "e7", "e8", "a3", "b3", "c3", "f8", "g8", "h8", "c5","c6");
            Main.main(new String[]{});
        } catch(NoSuchElementException e) {
            Assert.assertTrue(systemOutRule.getLog().contains("Welcome to Battleship"));
            Assert.assertTrue(systemOutRule.getLog().contains("Yeah ! Nice hit !"));
        }
    }

    @Test
    public void testPlayGameShotMisses() {
        try {
            gameInput.provideLines("b4", "b5", "b6", "b7", "b8", "e5", "e6", "e7", "e8", "a3", "b3", "c3", "f8", "g8", "h8", "c5","c6","b4", "a1", "b5", "b6", "b7", "b8", "e5", "e6", "e7", "e8", "a3", "b3", "c3", "f8", "g8", "h8", "c5","c6");
            Main.main(new String[]{});
        } catch(NoSuchElementException e) {
            Assert.assertTrue(systemOutRule.getLog().contains("Welcome to Battleship"));
            Assert.assertTrue(systemOutRule.getLog().contains("Miss"));
        }
    }
}
