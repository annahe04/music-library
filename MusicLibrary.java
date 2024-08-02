package labs.lab9;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;
import java.util.Map;

public class MusicLibrary extends JFrame {
    private double totalrunningtime = 0;
    private static final int FRAME_WIDTH = 1000;
    private static final int FRAME_HEIGHT = 600;
    private final int TITLE_FIELD_WIDTH = 20;

    private Map<String, Song> songs = new TreeMap<>();

    DefaultListModel<String> songlistmodel = new DefaultListModel<>();
    JList<String> songlist = new JList<>(songlistmodel);
    JLabel totalrunningtimelabel = new JLabel();
    final JTextField titlefield = new JTextField(TITLE_FIELD_WIDTH);
    final JTextField artistfield = new JTextField(TITLE_FIELD_WIDTH);
    JCheckBox friendscheckbox = new JCheckBox("Friends");
    JCheckBox familycheckbox = new JCheckBox("Family");
    JCheckBox coworkerscheckbox = new JCheckBox("Coworkers");
    JComboBox<String> categorycombobox = new JComboBox<>();
    final JTextField runningtimefield = new JTextField(10);
    JTextArea notesarea = new JTextArea(3, 22);

    JButton savesongbutton = new JButton("Save Song");
    JButton newsongbutton = new JButton("New Song");
    JButton deletebutton = new JButton("Delete");

    Song selectedsong;
    int selectedindex;
    boolean userselection = true;

    
    public MusicLibrary() {
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        createComponents();
        makeactionlistener();
    }
    
    private void makeactionlistener() {
        // SAVE SONG BUTTON
        class savelistenerclass implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                String retrievetitle = titlefield.getText();
                String retrieveartist = artistfield.getText();
                String retrieverunningtime = runningtimefield.getText();
                boolean retrievefriends = friendscheckbox.isSelected(); 
                boolean retrievefamily = familycheckbox.isSelected();
                boolean retrievecoworkers = coworkerscheckbox.isSelected();
                String retrievecategory = (String)categorycombobox.getSelectedItem();
                String retrievenotes = notesarea.getText();

                // check if running time is a valid double
                Double doublerunningtime;
                try {
                    doublerunningtime = Double.valueOf(retrieverunningtime);
                }
                catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (doublerunningtime < 0) {
                    JOptionPane.showMessageDialog(null, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // check if title isnt empty and artist isnt empty
                if ((retrievetitle.equals("")) || (retrieveartist.equals(""))) {
                    JOptionPane.showMessageDialog(null, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // check if we already added a song with that title (assuming we're not editing)
                if ((selectedsong == null) && (songs.get(retrievetitle) != null)) {
                    JOptionPane.showMessageDialog(null, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if ((selectedsong != null) && (!selectedsong.getTitle().equals(retrievetitle)) && (songs.get(retrievetitle) != null)) {
                    JOptionPane.showMessageDialog(null, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // add song to the map
                userselection = false;
                if (selectedsong == null)
                {
                    songs.put(retrievetitle, new Song(retrievetitle, retrieveartist, retrievefriends,
                          retrievefamily, retrievecoworkers, retrievecategory, doublerunningtime, retrievenotes)); 
                    songlistmodel.removeAllElements();
                    for (String songkey : songs.keySet()) {
                        songlistmodel.addElement(songkey);
                    }
                }
                else {
                    songs.remove(selectedsong.getTitle());
                    songs.put(retrievetitle, new Song(retrievetitle, retrieveartist, retrievefriends,
                          retrievefamily, retrievecoworkers, retrievecategory, doublerunningtime, retrievenotes)); 
                    songlistmodel.removeAllElements();
                    for (String songkey : songs.keySet()) {
                        songlistmodel.addElement(songkey);
                    }
                }
                userselection = true;

                selectedsong = songs.get(retrievetitle);
                songlist.setSelectedValue(retrievetitle, false);
                selectedindex = songlist.getSelectedIndex();

                double sumrunningtime = 0;
                for (String songkey : songs.keySet()) {
                    sumrunningtime += songs.get(songkey).getRunningTime();
                }
                totalrunningtimelabel.setText("Total running time: " + String.valueOf(sumrunningtime));

                JOptionPane.showMessageDialog(null, "Song saved!", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        ActionListener savelistener = new savelistenerclass();
        savesongbutton.addActionListener(savelistener);

        // NEW SONG BUTTON
        class newlistenerclass implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                titlefield.setText("");
                artistfield.setText("");
                friendscheckbox.setSelected(false);
                familycheckbox.setSelected(false);
                coworkerscheckbox.setSelected(false);
                categorycombobox.setSelectedIndex(0);
                runningtimefield.setText("0.0");
                notesarea.setText("");

                selectedsong = null;
                selectedindex = -1;
                userselection = false;
                songlist.clearSelection();
                userselection = true;
            }
        }
        ActionListener newlistener = new newlistenerclass();
        newsongbutton.addActionListener(newlistener);

        // DELETE BUTTON
        class deletelistenerclass implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                if (selectedsong != null) {
                    userselection = false;
                    songs.remove(selectedsong.getTitle());
                    songlistmodel.removeAllElements();
                    for (String songkey : songs.keySet()) {
                        songlistmodel.addElement(songkey);
                    }
                    songlist.setModel(songlistmodel);
                    selectedsong = null;
                    selectedindex = -1;
                    userselection = true;

                    titlefield.setText("");
                    artistfield.setText("");
                    friendscheckbox.setSelected(false);
                    familycheckbox.setSelected(false);
                    coworkerscheckbox.setSelected(false);
                    categorycombobox.setSelectedIndex(0);
                    runningtimefield.setText("0.0");
                    notesarea.setText("");
                }
            }
        }
        ActionListener deletelistener = new deletelistenerclass();
        deletebutton.addActionListener(deletelistener);

        // LIST SELECTION
        class listselectionclass implements ListSelectionListener {
            public void valueChanged(ListSelectionEvent event) {
                if (userselection && !event.getValueIsAdjusting()) {
                    String selectedtitle = songlist.getSelectedValue();
                    selectedsong = songs.get(selectedtitle);
                    selectedindex = songlist.getSelectedIndex();
                    
                    titlefield.setText(selectedsong.getTitle());
                    artistfield.setText(selectedsong.getArtist());
                    friendscheckbox.setSelected(selectedsong.getFriends());
                    familycheckbox.setSelected(selectedsong.getFamily());
                    coworkerscheckbox.setSelected(selectedsong.getCoworkers());
                    categorycombobox.setSelectedItem(selectedsong.getCategory());
                    runningtimefield.setText(String.valueOf(selectedsong.getRunningTime()));
                    notesarea.setText(selectedsong.getNotes());
                }
            }
        }
        ListSelectionListener songlistlistener = new listselectionclass();
        songlist.addListSelectionListener(songlistlistener);

    }
    
    private void createComponents() {

        // EMPTY PANEL
        JPanel emptypanel = new JPanel();

        // LEFT PANEL
        totalrunningtimelabel.setText("Total running time: " + totalrunningtime);
        //songlist.setVisibleRowCount(30);
        JScrollPane songlistscroll = new JScrollPane(songlist);
        songlistscroll.setPreferredSize(new Dimension(450, 500));
        JPanel leftsmallpanel = new JPanel();
        leftsmallpanel.add(deletebutton);
        leftsmallpanel.add(totalrunningtimelabel);
        JPanel leftsmallpanel2 = new JPanel();
        leftsmallpanel2.add(songlistscroll);
        JPanel leftpanel = new JPanel();
        leftpanel.setLayout(new BorderLayout());
        leftpanel.add(leftsmallpanel2, BorderLayout.NORTH);
        leftpanel.add(leftsmallpanel, BorderLayout.CENTER);
        

        // RIGHT PANEL
        JLabel titlelabel = new JLabel("Title: ");
        JLabel artistlabel = new JLabel("Artist: ");
        JLabel sharewith = new JLabel("Share with: ");
        JLabel categorylabel = new JLabel("Category: ");
        categorycombobox.addItem("Work");
        categorycombobox.addItem("Exercise");
        categorycombobox.addItem("Party");
        categorycombobox.addItem("Sleep");
        categorycombobox.addItem("Driving");
        categorycombobox.addItem("Other");
        JLabel runningtimelabel = new JLabel("Running time: ");
        runningtimefield.setText("0.0");
        JLabel noteslabel = new JLabel("Notes: ");
        notesarea.setLineWrap(true);
        notesarea.setWrapStyleWord(true);
        notesarea.setRows(3);
        JScrollPane notesscroll= new JScrollPane(notesarea);

        JPanel titlepanel = new JPanel();
        titlepanel.add(titlelabel);
        titlepanel.add(titlefield);
        JPanel artistpanel = new JPanel();
        artistpanel.add(artistlabel);
        artistpanel.add(artistfield);
        JPanel checkboxpanel = new JPanel();
        checkboxpanel.add(sharewith);
        checkboxpanel.add(friendscheckbox);
        checkboxpanel.add(familycheckbox);
        checkboxpanel.add(coworkerscheckbox);
        JPanel categorypanel = new JPanel();
        categorypanel.add(categorylabel);
        categorypanel.add(categorycombobox);
        JPanel runningtimepanel = new JPanel();
        runningtimepanel.add(runningtimelabel);
        runningtimepanel.add(runningtimefield);
        JPanel notespanel = new JPanel();
        notespanel.add(noteslabel);
        notespanel.add(notesscroll);
        JPanel savenewsongpanel = new JPanel();
        savenewsongpanel.add(savesongbutton);
        savenewsongpanel.add(newsongbutton);

        JPanel rightpanel = new JPanel();
        rightpanel.setLayout(new GridLayout(9, 1));
        rightpanel.add(emptypanel);
        rightpanel.add(titlepanel);
        rightpanel.add(artistpanel);
        rightpanel.add(checkboxpanel);
        rightpanel.add(categorypanel);
        rightpanel.add(runningtimepanel);
        rightpanel.add(notespanel);
        rightpanel.add(savenewsongpanel);


        // FRAME LAYOUT
        setLayout(new GridLayout(1, 2));;
        add(leftpanel);
        add(rightpanel);
    }

    public static void main(String[] args) {
        JFrame frame = new MusicLibrary();
        frame.setTitle("Anna He - 47086936");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
