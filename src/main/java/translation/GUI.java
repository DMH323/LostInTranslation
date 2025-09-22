package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.Arrays;

public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Translator translator = new JSONTranslator();
            LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
            CountryCodeConverter countryCodeConverter = new CountryCodeConverter();

            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));
            JComboBox<String> languageComboBox = new JComboBox<>();
            for(String langCode : translator.getLanguageCodes()) {
                languageComboBox.addItem(languageCodeConverter.fromLanguageCode(langCode));
            }
            languagePanel.add(languageComboBox);

            JPanel countryPanel = new JPanel();
            String[] items = new String[translator.getCountryCodes().size()];
            for (int i = 0; i < items.length; i++) {
                items[i] = countryCodeConverter.fromCountryCode(translator.getCountryCodes().get(i));
            }
            JList<String> list = new JList<>(items);
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            JScrollPane scrollPane = new JScrollPane(list);

            countryPanel.add(new JLabel("Country:"));
            countryPanel.add(scrollPane);

            JPanel buttonPanel = new JPanel();

            JLabel resultLabelText = new JLabel("Translation:");
            buttonPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            buttonPanel.add(resultLabel);

            languageComboBox.addItemListener(new ItemListener() {

                /**
                 * Invoked when an item has been selected or deselected by the user.
                 * The code written for this method performs the operations
                 * that need to occur when an item is selected (or deselected).
                 *
                 * @param e the event to be processed
                 */
                @Override
                public void itemStateChanged(ItemEvent e) {

                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String lang = languageComboBox.getSelectedItem().toString();
                        int[] indices = list.getSelectedIndices();
                        String item = list.getModel().getElementAt(indices[0]);
                        String country = countryCodeConverter.fromCountry(item);
                        String result = translator.translate(country.toLowerCase(), languageCodeConverter.fromLanguage(lang));
                        if (result == null) {
                            result = "no translation found!";}
                        resultLabel.setText(result);
                    }
                }


            });

            list.addListSelectionListener(new ListSelectionListener() {

                /**
                 * Called whenever the value of the selection changes.
                 *
                 * @param e the event that characterizes the change.
                 */
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    String lang = languageComboBox.getSelectedItem().toString();
                    int[] indices = list.getSelectedIndices();
                    String item = list.getModel().getElementAt(indices[0]);
                    String country = countryCodeConverter.fromCountry(item);
                    String result = translator.translate(country.toLowerCase(), languageCodeConverter.fromLanguage(lang));
                    if (result == null) {
                        result = "no translation found!";}
                    resultLabel.setText(result);


                }
            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(countryPanel);
            mainPanel.add(buttonPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);


        });
    }
}
