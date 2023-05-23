package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBusImplTest {

    private MessageBusImpl messagebus;
    private MicroService testerMS;
    private MicroService second;

    @BeforeEach
    public void setUp() {
        messagebus = MessageBusImpl.getInstance();

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void complete() {
        AttackEvent attackE = new AttackEvent(null);
        Future future = messagebus.sendEvent(attackE);
        assertTrue(future.isDone());
    }

    @Test
    public void senAndSubscribeBroadcast() throws InterruptedException {
        messagebus.register(testerMS);
        messagebus.register(second);

        Message one = messagebus.awaitMessage(testerMS);
        Message two = messagebus.awaitMessage(second);
        assertSame(one,two);
    }

    @Test
    public void sendAndSubscribeEvent() throws InterruptedException {
        AttackEvent attackE = new AttackEvent(null);
        messagebus.register(testerMS);
        messagebus.subscribeEvent(attackE.getClass(),testerMS);
        Future future = messagebus.sendEvent(attackE);
        Message messageE = messagebus.awaitMessage(testerMS);
        assertNotNull(future);
        assertSame(attackE,messageE);
    }

    @Test
    public void register() {
        try {
            messagebus.register(testerMS);
            AttackEvent attackE = new AttackEvent(null);
            messagebus.sendEvent(attackE);
        } catch (Exception notRegister){
            fail();
        }
    }

    @Test
    public void awaitMessage() throws InterruptedException {
        messagebus.register (testerMS);
        AttackEvent attackE = new AttackEvent(null);
        messagebus.sendEvent(attackE);
        Message expected = messagebus.awaitMessage(testerMS);
        assertEquals(attackE,expected);

    }
}