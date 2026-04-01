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

// Panel used to view and edit a contacts profile
public class ContactProfile extends JPanel {

    // Form fields shown on the profile screen
    private JTextField nameField;
    private JTextField usernameField;
    private JTextArea bioField;
    private JTextField phoneField;

    public ContactProfile(Runnable onSave, Runnable onCancel) {
        // Main panel layout
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Contact Profile", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        // Form layout
        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));

        nameField = new JTextField();
        bioField = new JTextArea(3, 20);
        bioField.setLineWrap(true);
        bioField.setWrapStyleWord(true);
        phoneField = new JTextField();

        form.add(new JLabel("Full Name:"));
        form.add(nameField);
        form.add(new JLabel("Bio:"));
        form.add(new JScrollPane(bioField));
        form.add(new JLabel("Phone:"));
        form.add(phoneField);

        add(form, BorderLayout.CENTER);

        // Save and cancel button actions
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> onSave.run());
        cancelBtn.addActionListener(e -> onCancel.run());

        JPanel bottom = new JPanel();
        bottom.add(saveBtn);
        bottom.add(cancelBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    // Loads the selected contact data into the form fields
    public void loadContact(ContactLinkedList.Node node) {
        nameField.setText(node.name);
        bioField.setText(node.bio);
        phoneField.setText(node.phone);
    }

    // Returns trimmed values entered in the form
    public String getContactName()     { return nameField.getText().trim(); }
    public String getContactBio()      { return bioField.getText().trim(); }
    public String getContactPhone()    { return phoneField.getText().trim(); }
}
