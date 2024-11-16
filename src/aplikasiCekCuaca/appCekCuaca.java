
package aplikasiCekCuaca;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;


public class appCekCuaca {
   private JFrame frame;
    private JTextField cityField;
    private JLabel weatherIconLabel;
    private JLabel tempLabel, descriptionLabel;
    private JComboBox<String> locationComboBox;
    private JButton checkWeatherButton;

    // API Key OpenWeatherMap
    private final String API_KEY = "593b96a162ff9dabbf6dd230f586bca9";

    // URL API
    private final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); // Tema modern
               appCekCuaca window = new appCekCuaca();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public appCekCuaca() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Aplikasi Cek Cuaca");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(new Color(70, 130, 180)); // Warna latar biru muda
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Label untuk kota
        JLabel selectLabel = new JLabel("Pilih Kota:");
        selectLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        frame.getContentPane().add(selectLabel, gbc);
        
        // ComboBox untuk lokasi
        locationComboBox = new JComboBox<>();
        locationComboBox.addItem("Jakarta");
        locationComboBox.addItem("Surabaya");
        locationComboBox.addItem("Bandung");
        locationComboBox.addItem("Yogyakarta");
        locationComboBox.addItem("Medan");
        gbc.gridx = 1;
        gbc.gridy = 0;
        frame.getContentPane().add(locationComboBox, gbc);
        
        // Label untuk input manual
        JLabel manualLabel = new JLabel("Atau Masukkan Kota:");
        manualLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.getContentPane().add(manualLabel, gbc);
        
        // TextField untuk kota
        cityField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        frame.getContentPane().add(cityField, gbc);
        
        // Tombol cek cuaca
        checkWeatherButton = new JButton("Cek Cuaca");
        checkWeatherButton.setBackground(new Color(30, 144, 255));
        checkWeatherButton.setForeground(Color.WHITE);
        checkWeatherButton.addActionListener(e -> fetchWeatherData());
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.getContentPane().add(checkWeatherButton, gbc);
        
        // Panel untuk hasil cuaca
        JPanel weatherPanel = new JPanel(new GridBagLayout());
        weatherPanel.setBackground(new Color(70, 130, 180)); // Warna sama dengan latar utama
        
        GridBagConstraints weatherGbc = new GridBagConstraints();
        weatherGbc.insets = new Insets(10, 10, 10, 10);
        
        weatherIconLabel = new JLabel("Cuaca: ");
        weatherIconLabel.setForeground(Color.WHITE);
        weatherGbc.gridx = 0;
        weatherGbc.gridy = 0;
        weatherPanel.add(weatherIconLabel, weatherGbc);
        
        tempLabel = new JLabel("Suhu: ");
        tempLabel.setForeground(Color.WHITE);
        weatherGbc.gridx = 0;
        weatherGbc.gridy = 1;
        weatherPanel.add(tempLabel, weatherGbc);
        
        descriptionLabel = new JLabel("Deskripsi: ");
        descriptionLabel.setForeground(Color.WHITE);
        weatherGbc.gridx = 0;
        weatherGbc.gridy = 2;
        weatherPanel.add(descriptionLabel, weatherGbc);
        
        // Tambahkan panel hasil ke frame utama
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        frame.getContentPane().add(weatherPanel, gbc);
    }

    private void fetchWeatherData() {
        String location = (String) locationComboBox.getSelectedItem();
        if (!cityField.getText().trim().isEmpty()) {
            location = cityField.getText().trim();
        }

        try {
            String urlString = BASE_URL + location + "&appid=" + API_KEY + "&units=metric";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Membaca respon dari API
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse JSON dari API
            JSONObject myResponse = new JSONObject(response.toString());

            // Mendapatkan informasi cuaca
            double temp = myResponse.getJSONObject("main").getDouble("temp");
            String description = myResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            String icon = myResponse.getJSONArray("weather").getJSONObject(0).getString("icon");

            // Mengupdate UI dengan data cuaca
            tempLabel.setText("Suhu: " + temp + " °C");
            descriptionLabel.setText("Deskripsi: " + description);

            // Menampilkan ikon cuaca
            String iconUrl = "http://openweathermap.org/img/wn/" + icon + "@2x.png";
            weatherIconLabel.setText("Cuaca: ");
            weatherIconLabel.setIcon(new ImageIcon(new URL(iconUrl)));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Terjadi kesalahan saat mengambil data cuaca.");
            e.printStackTrace();
        }
    }
}
