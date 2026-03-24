package io.github.ehteam.messaging;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ProfilePage extends JPanel {

    private String displayName = "Jane Doe";
    private String username = "janedoe";
    private String bio = "Hey there! I'm using MessagingApp.";
    private String phone = "+44 7700 900123";

    private JTextField nameField;
    private JTextField usernameField;
    private JTextArea bioField;
    private JTextField phoneField;

    public ProfilePage() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("My Profile", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));

        nameField = new JTextField(displayName);
        usernameField = new JTextField(username);
        bioField = new JTextArea(bio, 3, 20);
        bioField.setLineWrap(true);
        bioField.setWrapStyleWord(true);
        phoneField = new JTextField(phone);

        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Username:"));
        form.add(usernameField);
        form.add(new JLabel("Bio:"));
        form.add(new JScrollPane(bioField));
        form.add(new JLabel("Phone:"));
        form.add(phoneField);

        add(form, BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            displayName = nameField.getText();
            username = usernameField.getText();
            bio = bioField.getText();
            phone = phoneField.getText();

            JOptionPane.showMessageDialog(this, "Profile saved!");
        });

        JPanel bottom = new JPanel();
        bottom.add(saveBtn);
        add(bottom, BorderLayout.SOUTH);
    }
}