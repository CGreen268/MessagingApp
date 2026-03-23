package io.github.ehteam.messaging;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ProfilePage extends JPanel {

    public ProfilePage() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("My Profile");
        add(title, BorderLayout.NORTH);
    }
}