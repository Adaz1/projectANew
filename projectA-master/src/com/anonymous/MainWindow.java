package com.anonymous;

import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.server.ExportException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;


/**
 * Created by Adam on 10.04.2018.
 */
public class MainWindow extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("Inuidisse Alert System");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 5, 5, 5));

        Platform.setImplicitExit(false);
        javax.swing.SwingUtilities.invokeLater(this::addAppToTray);

        Label dirConf = new Label("Choose directory:");
        dirConf.setFont(Font.font("default", FontWeight.BOLD, 12));
        grid.add(dirConf, 0, 0, 2,1);

        Label pathLabel = new Label("Path:");
        grid.add(pathLabel, 0, 1);

        TextField pathTextField = new TextField();
        pathTextField.setMinWidth(280);
        grid.add(pathTextField, 1, 1);

        Button browseBtn = new Button("browse");
        browseBtn.setMinSize(60, 20);
        browseBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                } catch (Exception ex){
                    System.err.println("Browse directory error");
                }

                JFileChooser f = new JFileChooser();

                try {
                    f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    f.showSaveDialog(null);
                } catch (Exception ex) {
                    return;
                }

                pathTextField.setText(f.getSelectedFile().toString());
            }
        });
        grid.add(browseBtn, 2, 1);

        Button runBtn = new Button("Run");
        runBtn.setMinSize(100, 20);
        runBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if (runBtn.getText() == "Run")
                {
                    runBtn.setText("Stop");
                }
                else
                {
                    runBtn.setText("Run");
                }

                File dir = new File(System.getProperty("user.home"), "AppData/Local/Inuidisse");
                dir.mkdir();

                List<String> lines = Arrays.asList(pathTextField.getText());
                Path file = Paths.get(dir.toString() + "/config.txt");
                try {
                    Files.write(file, lines, Charset.forName("UTF-8"));
                } catch (IOException ex) {
                    System.err.println("Config file write error");
                }

                DirWatcher dirWatcher = new DirWatcher(pathTextField.getText());
                dirWatcher.start();
            }
        });
        grid.add(runBtn, 2, 7);

        Label settings = new Label("Choose directory:");
        settings.setFont(Font.font("default", FontWeight.BOLD, 12));
        grid.add(settings, 0, 3, 2,1);

        CheckBox trayBox = new CheckBox();
        trayBox.setText("Keep in tray");
        CheckBox autoStartBox = new CheckBox();
        autoStartBox.setText("Autorun when computer starts");

        grid.add(trayBox, 0, 4, 4, 1);
        grid.add(autoStartBox, 0, 5, 4, 1);

        Scene scene = new Scene(grid, 500, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        /*primaryStage.setOnCloseRequest(new EventHandler<javafx.stage.WindowEvent>() {
            public void handle(WindowEvent we) {
                //System.out.println("Stage is closing");
                we.consume();
                //startTray();
                if (Platform.isImplicitExit())
                {
                    System.out.println("WTF?");
                }
                primaryStage.close();

                if (!SystemTray.isSupported()) {
                    System.out.println("SystemTray is not supported");
                    return;
                }

                SystemTray tray = SystemTray.getSystemTray();
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Image image = toolkit.getImage("D:/crap/green.png");

                PopupMenu menu = new PopupMenu();

                java.awt.MenuItem messageItem = new java.awt.MenuItem("Show Message");
                messageItem.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        //JOptionPane.showMessageDialog(null, "www.java2s.com");
                        primaryStage.show();
                    }
                });
                menu.add(messageItem);

                java.awt.MenuItem closeItem = new java.awt.MenuItem("Close");
                closeItem.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        System.exit(0);
                    }
                });
                menu.add(closeItem);
                TrayIcon icon = new TrayIcon(image, "SystemTray Demo", menu);
                icon.setImageAutoSize(true);

                try {
                    tray.add(icon);
                } catch (AWTException ex) {
                    System.err.println("Tray icon error");
                }
            }
        });*/



        //primaryStage.setOnCloseRequest(event -> Platform.exit());

        /*StackPane root = new StackPane();
        root.getChildren().add(btn);
        root.getChildren().add(menuBar);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();*/

        /*MailSender mailSender = new MailSender();
                mailSender.init(emailAddrTextField.getText(), emailPassTextField.getText());
                mailSender.sendMessage("harnas15@poczta.onet.pl", "Hello Adam", "blablabla\nblablabla");*/
    }

    private void addAppToTray() {
        try {
            java.awt.Toolkit.getDefaultToolkit();

            if (!java.awt.SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            }

            java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
            java.awt.Image image = null;
            try {
                image = ImageIO.read(getClass().getResource("/resources/green.png"));
            }
            catch (IOException ex) {
                System.err.println("Getting trayIcon from resource failed");
            }

            java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);
            trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

            java.awt.MenuItem openItem = new java.awt.MenuItem("Open");
            openItem.addActionListener(event -> Platform.runLater(this::showStage));

            java.awt.MenuItem exitItem = new java.awt.MenuItem("Exit");
            exitItem.addActionListener(event -> {
                Platform.exit();
                tray.remove(trayIcon);
            });

            final java.awt.PopupMenu popup = new java.awt.PopupMenu();
            popup.add(openItem);
            popup.addSeparator();
            popup.add(exitItem);
            trayIcon.setImageAutoSize(true);
            trayIcon.setPopupMenu(popup);
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("Unable to init system tray");
            //e.printStackTrace();
        }
    }

    private void showStage() {
        if (primaryStage != null) {
            primaryStage.show();
            primaryStage.toFront();
        }
    }
}
