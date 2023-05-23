package bgu.spl.mics.application.passiveObjects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {

    private Ewok newewok;


    @BeforeEach
    public void setUp() {
        newewok = new Ewok(1);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void acquire() {
        assertTrue(newewok.available);
        newewok.acquire();
        assertFalse(newewok.available);
    }

    @Test
    public void release() {
        newewok.acquire();
        newewok.release();
        assertTrue(newewok.available);

    }
}