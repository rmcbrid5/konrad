package com.konrad.mail;

import com.konrad.mail.models.Message;
import com.konrad.mail.utils.MessageUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MessageUtilsTest {

    //<editor-fold desc="MessageUtils.combineSortedMessages tests">

    //TODO : Write your Unit tests (Question #4) for the combineSortedMessages here.

    //</editor-fold>


    //<editor-fold desc="MessageUtils.sortMessages tests">
    @Test
    public void sortMessages_sortsUnsortedInDescendingOrder() {
        Long curTime = System.currentTimeMillis();
        List<Message> messages = Arrays.asList(
                new Message("a", curTime + 1),
                new Message("b", curTime + 2),
                new Message("c", curTime - 1),
                new Message("d", curTime)
        );
        MessageUtils.sortMessages(messages);
        assertEquals("b", messages.get(0).getId());
        assertEquals("a", messages.get(1).getId());
        assertEquals("d", messages.get(2).getId());
        assertEquals("c", messages.get(3).getId());
    }

    @Test
    public void sortMessages_leavesSortedUntouched() {
        Long curTime = System.currentTimeMillis();
        List<Message> messages = Arrays.asList(
                new Message("a", curTime + 3),
                new Message("b", curTime + 2),
                new Message("c", curTime + 1),
                new Message("d", curTime)
        );
        MessageUtils.sortMessages(messages);
        assertEquals("a", messages.get(0).getId());
        assertEquals("b", messages.get(1).getId());
        assertEquals("c", messages.get(2).getId());
        assertEquals("d", messages.get(3).getId());
    }

    @Test
    public void sortMessages_doesntChangeOrderOfSameDateElements() {
        Long curTime = System.currentTimeMillis();
        List<Message> messages = Arrays.asList(
                new Message("a", curTime),
                new Message("b", curTime),
                new Message("c", curTime),
                new Message("d", curTime)
        );
        MessageUtils.sortMessages(messages);
        assertEquals("a", messages.get(0).getId());
        assertEquals("b", messages.get(1).getId());
        assertEquals("c", messages.get(2).getId());
        assertEquals("d", messages.get(3).getId());
    }

    @Test
    public void sortMessages_handlesEmptyOrSingleItem() {
        Long curTime = System.currentTimeMillis();
        List<Message> emptyMessages = new ArrayList<>();
        MessageUtils.sortMessages(emptyMessages);

        List<Message> oneMessage = Arrays.asList(
                new Message("a", curTime)
        );
        MessageUtils.sortMessages(oneMessage);
        assertEquals("a", oneMessage.get(0).getId());
    }
    //</editor-fold>

}