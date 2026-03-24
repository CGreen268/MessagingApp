package io.github.ehteam.messaging;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ProfilePage extends JPanel {

    private String displayName = "Jane Doe";
    private String bio = "Hey there! I'm using MessagingApp.";
    private String phone = "+44 7700 900123";

    private JTextField nameField;
    private JTextArea bioField;
    private JTextField phoneField;

    public ProfilePage(Runnable onSave) {
        this(onSave, "New Contact");
    }

    public ProfilePage(Runnable onSave, String title) {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));

        nameField = new JTextField(displayName);
        bioField = new JTextArea(bio, 3, 20);
        bioField.setLineWrap(true);
        bioField.setWrapStyleWord(true);
        phoneField = new JTextField(phone);

        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Bio:"));
        form.add(new JScrollPane(bioField));
        form.add(new JLabel("Phone:"));
        form.add(phoneField);

        add(form, BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            displayName = nameField.getText();
            bio = bioField.getText();
            phone = phoneField.getText();
            onSave.run();
        });

        JPanel bottom = new JPanel();
        bottom.add(saveBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    public void clearFields() {
        nameField.setText("");
        bioField.setText("");
        phoneField.setText("");
    }

    public String getDisplayName() {
        return nameField.getText().trim();
    }

    public String getContactBio() {
        return bioField.getText().trim();
    }

    public String getContactPhone() {
        return phoneField.getText().trim();
    }

    public void setDisplayName(String name) {
        nameField.setText(name);
    }

    public void setContactBio(String bio) {
        bioField.setText(bio);
    }

    public void setContactPhone(String phone) {
        phoneField.setText(phone);
    }
}