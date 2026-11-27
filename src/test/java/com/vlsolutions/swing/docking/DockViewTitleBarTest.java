package com.vlsolutions.swing.docking;

import com.vlsolutions.swing.TestBase;
import com.vlsolutions.swing.sample.DockableScrollPane;
import com.vlsolutions.swing.sample.MyEditorApp;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.timing.Condition;
import org.assertj.swing.timing.Timeout;
import org.junit.Test;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.swing.timing.Pause.pause;
import static org.junit.Assert.assertEquals;

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
        SwingUtilities.invokeLater(() -> {
            application.setVisible(true);
            window.button("OkButton").target().setEnabled(false);
        });
        window.panel("myTreePanel").focus();
        SwingUtilities.invokeLater(() -> {
            Dockable dockable = (Dockable) (window.panel("myTextEditorPanel").target());
            dockable.getDockKey().addPropertyChangeListener(evt -> {
                if (evt.getPropertyName().equals("notification")) {
                    counter.getAndIncrement();
                    window.button("OkButton").target().setEnabled(true);
                }
            });
        });
        DockableScrollPane dockable = (DockableScrollPane) (window.scrollPane("myTextEditorScrollPane").target());
        dockable.setNotification(true);
        JButton target = window.button("OkButton").target();
        pause(new Condition("OK button to be enabled") {
            public boolean test() {
                return target.isEnabled();
            }
        }, Timeout.timeout(10000));
        assertEquals(1, counter.get());
    }
}
