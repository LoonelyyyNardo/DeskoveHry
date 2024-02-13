package BoardGames;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BoardGamesGUI extends JFrame {
    private JTextField txtName;
    private JCheckBox CBOwned;
    private JRadioButton RB3;
    private JRadioButton RB2;
    private JRadioButton RB1;
    private JButton prevBtn;
    private JButton nxtBtn;
    private JButton saveBtn;
    private JPanel panel;
    private final List<BoardGame> BGList = new ArrayList<>();
    private int index = 0;
    private final int[] selectedScore = {1};

    public static void main(String[] args) {
        BoardGamesGUI bgGUI = new BoardGamesGUI();
        bgGUI.setContentPane(bgGUI.panel);
        bgGUI.setSize(500, 250);
        bgGUI.setDefaultCloseOperation(EXIT_ON_CLOSE);
        bgGUI.setTitle("Board Games");
        bgGUI.setVisible(true);
    }
    public BoardGame getBoG(int i){
        return BGList.get(i);
    }
    public BoardGamesGUI() {
        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(RB1);
        btnGroup.add(RB2);
        btnGroup.add(RB3);
        RB1.addItemListener(e -> handleRadioButtonClick(1));
        RB2.addItemListener(e -> handleRadioButtonClick(2));
        RB3.addItemListener(e -> handleRadioButtonClick(3));

        prevBtn.addActionListener(e -> {
            if (index > 0){
                index--;
                displayBG(getBoG(index));
            }
        });
        nxtBtn.addActionListener(e -> {
            if (index < BGList.size() - 1) {
                index++;
                displayBG(getBoG(index));
            }
        });
        saveBtn.addActionListener(e -> saveToFile());
        readingFromFIle();
        displayBG(getBoG(index));
    }

    public void readingFromFIle() {
        try (Scanner sc = new Scanner(new BufferedReader(new FileReader("BoardGames.txt")))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(";");
                String name = (parts[0]);
                boolean owned = parts[1].equals("owned");
                int score = Integer.parseInt(parts[2]);
                BoardGame bg = new BoardGame(name, owned, score);
                BGList.add(bg);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getLocalizedMessage());
        } catch (NumberFormatException e) {
            System.err.println("Wrongly formatted number: " + e.getLocalizedMessage());
        }
    }

    public void saveToFile() {
        int selectedIndex = index;
        BoardGame selectedBG = BGList.get(selectedIndex);
        selectedBG.setName(txtName.getText());
        selectedBG.setOwned(CBOwned.isSelected());
        selectedBG.setScore(selectedScore[0]);

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("BoardGames.txt")))) {
            for (BoardGame bg : BGList) {
                writer.println(bg.getName() + ";" + (bg.isOwned() ? "owned" : "not owned") + ";" + bg.getScore());
            }
            JOptionPane.showMessageDialog(this, "Changes saved to file.", "Message saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getLocalizedMessage());
        }
    }
    public void displayBG(BoardGame bg){
        txtName.setText(bg.getName());
        CBOwned.setSelected(bg.isOwned());
        switch (bg.getScore()){
            case 1 -> RB1.setSelected(true);
            case 2 -> RB2.setSelected(true);
            case 3 -> RB3.setSelected(true);
        }
    }
    private void handleRadioButtonClick(int score) {
        selectedScore[0] = score;
    }

}

