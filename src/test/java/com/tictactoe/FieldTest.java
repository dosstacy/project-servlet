package com.tictactoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {
    private Field field;

    @BeforeEach
    public void setUp() {
        field = new Field();
    }

    @Test
    void testGetFieldNotNull(){
        assertNotNull(field, "Field can not be null");
    }

    @Test
    void testGetFieldSize(){
        assertEquals(9, field.getField().size());
    }

    @Test
    void testGetFieldContent(){
        for (int i = 0; i < 9; i++) {
            assertEquals(Sign.EMPTY, field.getField().get(i));
        }
    }

    @Test
    public void testGetEmptyFieldIndexWhenEmptyExists() {
        assertEquals(0, field.getEmptyFieldIndex());
    }

    @Test
    public void testGetEmptyFieldIndexWhenEmptyNotExists() {
        for (int i = 0; i < 9; i++) {
            field.getField().put(i, Sign.CROSS);
        }
        assertEquals(-1, field.getEmptyFieldIndex());
    }

    @Test
    void testGetFieldDataNotNull() {
        assertNotNull(field.getFieldData(), "Field data can not be null");
    }

    @Test
    void testGetFieldDataSize() {
        assertEquals(9, field.getFieldData().size());
    }

    @Test
    void testGetFieldDataContent() {
        field.getField().put(0, Sign.CROSS);
        field.getField().put(1, Sign.NOUGHT);
        field.getField().put(2, Sign.EMPTY);
        field.getField().put(3, Sign.CROSS);
        field.getField().put(4, Sign.NOUGHT);
        field.getField().put(5, Sign.EMPTY);
        field.getField().put(6, Sign.CROSS);
        field.getField().put(7, Sign.NOUGHT);
        field.getField().put(8, Sign.EMPTY);

        List<Sign> expected = Arrays.asList(Sign.CROSS, Sign.NOUGHT, Sign.EMPTY, Sign.CROSS, Sign.NOUGHT, Sign.EMPTY, Sign.CROSS, Sign.NOUGHT, Sign.EMPTY);
        List<Sign> actual = field.getFieldData();

        assertEquals(expected, actual, "The field data should match the expected values");
    }

    @Test
    void testHorizontalWin() {
        field.getField().put(0, Sign.CROSS);
        field.getField().put(1, Sign.CROSS);
        field.getField().put(2, Sign.CROSS);

        assertEquals(Sign.CROSS, field.checkWin(), "X should win with a horizontal line");
    }

    @Test
    void testVerticalWin() {
        field.getField().put(0, Sign.NOUGHT);
        field.getField().put(3, Sign.NOUGHT);
        field.getField().put(6, Sign.NOUGHT);

        assertEquals(Sign.NOUGHT, field.checkWin(), "O should win with a vertical line");
    }

    @Test
    void testDiagonalWin() {
        field.getField().put(0, Sign.CROSS);
        field.getField().put(4, Sign.CROSS);
        field.getField().put(8, Sign.CROSS);

        assertEquals(Sign.CROSS, field.checkWin(), "X should win with a diagonal line");
    }

    @Test
    void testNoWinContinueGame() {
        // Немає виграшної комбінації, гра продовжується
        field.getField().put(0, Sign.CROSS);
        field.getField().put(1, Sign.NOUGHT);
        field.getField().put(2, Sign.CROSS);
        field.getField().put(3, Sign.CROSS);
        field.getField().put(4, Sign.NOUGHT);
        field.getField().put(5, Sign.NOUGHT);
        field.getField().put(6, Sign.NOUGHT);
        field.getField().put(7, Sign.CROSS);
        field.getField().put(8, Sign.CROSS);

        assertEquals(Sign.EMPTY, field.checkWin(), "There should be no winner yet, continue game");
    }

    @Test
    void testNoWinDraw() {
        // Немає виграшної комбінації, гра завершена нічиєю
        field.getField().put(0, Sign.CROSS);
        field.getField().put(1, Sign.NOUGHT);
        field.getField().put(2, Sign.CROSS);
        field.getField().put(3, Sign.CROSS);
        field.getField().put(4, Sign.CROSS);
        field.getField().put(5, Sign.NOUGHT);
        field.getField().put(6, Sign.NOUGHT);
        field.getField().put(7, Sign.CROSS);
        field.getField().put(8, Sign.NOUGHT);

        assertEquals(Sign.EMPTY, field.checkWin(), "The game should end in a draw");
    }

}