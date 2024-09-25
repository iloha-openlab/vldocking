package com.vlsolutions.swing.docking;

import com.vlsolutions.swing.TestBase;
import com.vlsolutions.swing.sample.MyEditorApp;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.timing.Condition;
import org.assertj.swing.timing.Timeout;
import org.junit.Test;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.swing.timing.Pause.pause;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DockViewTitleBarTest extends TestBase {


    @Override
    protected void onSetUp() {
        application = createDockedApplication(MyEditorApp.class);
        window = new FrameFixture(robot(), application);
        window.show();
    }

    @Test
    public void testTitleBarNotify() {
        final AtomicInteger counter = new AtomicInteger(0);
        final AtomicBoolean cleared = new AtomicBoolean(false);
        SwingUtilities.invokeLater(() -> {
            application.setVisible(true);
            window.button("OkButton").target().setEnabled(false);
        });
        window.panel("myTextEditorPanel").requireVisible();
        SwingUtilities.invokeLater(() -> {
            Dockable dockable = (Dockable)(window.scrollPane().target());
            dockable.getDockKey().addPropertyChangeListener(evt -> {
                if (evt.getPropertyName().equals("notification")) {
                    if (counter.getAndIncrement() >= 2) {
                        window.button("OkButton").target().setEnabled(true);
                    }
                    if (!dockable.getDockKey().isNotification()) {
                        cleared.set(true);
                    }
                }
            });
            application.notification();
        });
        JButton target = window.button("OkButton").target();
        pause(new Condition("OK button to be enabled") {
            public boolean test() {
                return target.isEnabled();
            }
        }, Timeout.timeout(10000));
        assertEquals(5, counter.get());
        assertTrue(cleared.get());
    }
}
