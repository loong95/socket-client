package io.loong95.light.chat.layout;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import io.loong95.light.chat.ThemeItem;
import io.loong95.light.chat.socket.SocketClient;
import net.beeger.squareness.SquarenessLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * @author linyunlong
 */
public class Main {
    private JLabel hostLabel;
    private JTextField hostTextField;
    private JTextField portTextField;
    private JLabel portLabel;
    private JButton tcpButton;
    private JPanel mainPanel;
    private JPanel connectInfoPanel;
    private JTextPane logPane;
    private JComboBox<ThemeItem> themeComboBox;
    private JTextField msgTextField;
    private JButton sendBtn;
    private JLabel rawTextInputLabel;
    private JPanel rawTextPanel;
    private JLabel binaryStringLabel;
    private JTextField binaryTextField;
    private JButton sendBinaryBtn;
    private JLabel hexMsgLabel;
    private JTextField hexTextField;
    private JButton sendHexBtn;
    private JLabel logLabel;
    private JScrollPane logScrollPane;
    private JButton exportLogBtn;
    private JPanel additionPanel;
    private SocketClient socketClient;

    public Main() {
        tcpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String host = hostTextField.getText();
                int port = Integer.parseInt(portTextField.getText());
                if (socketClient == null) {
                    socketClient = new SocketClient(logPane);
                }
                new Thread(() -> socketClient.reconnect(host, port)).start();
            }
        });
        sendBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String msg = msgTextField.getText();
                socketClient.send(msg);
            }
        });
        sendBinaryBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String msg = binaryTextField.getText();
                socketClient.sendBinary(msg);
            }
        });
        sendHexBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String msg = hexTextField.getText();
                socketClient.sendHex(msg);
            }
        });
        setTheme(SquarenessLookAndFeel.class.getName());
        portTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                int keyChar = e.getKeyChar();
                if (keyChar < KeyEvent.VK_0 || keyChar > KeyEvent.VK_9) {
                    //关键，屏蔽掉非法输入
                    e.consume();
                }
            }
        });
        exportLogBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 日志导出
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showSaveDialog(mainPanel);
                File file = fileChooser.getSelectedFile();
                String text = logPane.getText();
                if (file.exists()) {
                    file = new File(file.getAbsolutePath() + "_" + System.currentTimeMillis());
                }
                try {
                    boolean newFile = file.createNewFile();
                    if (!newFile) {
                        logPane.setText(logPane.getText() + "\n" + "导出失败：创建文件失败");
                        return;
                    }
                } catch (IOException ex) {
                    logPane.setText(logPane.getText() + "\n" + "导出失败：创建文件失败：" + ex.getMessage());
                    return;
                }
                try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                    byte[] wrap = "\n".getBytes();
                    for (String line : text.split("\n")) {
                        fileOutputStream.write(line.getBytes(StandardCharsets.UTF_8));
                        fileOutputStream.write(wrap);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private String getThemeName(String className) {
        String lastPart = className.substring(className.lastIndexOf('.') + 1);
        return lastPart.replace("LookAndFeel", "");
    }

    private void setTheme(String className) {
        LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(mainPanel);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            setTheme(lookAndFeel.getClass().getName());
        }
    }

    private void setFont(Font font, String itemName) {
        UIManager.put(itemName + ".font", font);
        SwingUtilities.updateComponentTreeUI(mainPanel);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Socket客户端");
        Main main = new Main();
        frame.setContentPane(main.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(5, 3, new Insets(5, 5, 5, 5), -1, -1));
        connectInfoPanel = new JPanel();
        connectInfoPanel.setLayout(new GridLayoutManager(1, 3, new Insets(1, 1, 1, 1), -1, -1));
        mainPanel.add(connectInfoPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        hostTextField = new JTextField();
        hostTextField.setText("127.0.0.1");
        connectInfoPanel.add(hostTextField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        portLabel = new JLabel();
        Font portLabelFont = this.$$$getFont$$$("Microsoft JhengHei UI", -1, -1, portLabel.getFont());
        if (portLabelFont != null) portLabel.setFont(portLabelFont);
        this.$$$loadLabelText$$$(portLabel, ResourceBundle.getBundle("i18n/label").getString("port"));
        connectInfoPanel.add(portLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        portTextField = new JTextField();
        portTextField.setText("8080");
        connectInfoPanel.add(portTextField, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        rawTextPanel = new JPanel();
        rawTextPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(rawTextPanel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        msgTextField = new JTextField();
        rawTextPanel.add(msgTextField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        binaryTextField = new JTextField();
        panel1.add(binaryTextField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        hexTextField = new JTextField();
        panel2.add(hexTextField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        binaryStringLabel = new JLabel();
        Font binaryStringLabelFont = this.$$$getFont$$$("Microsoft YaHei UI", -1, -1, binaryStringLabel.getFont());
        if (binaryStringLabelFont != null) binaryStringLabel.setFont(binaryStringLabelFont);
        this.$$$loadLabelText$$$(binaryStringLabel, ResourceBundle.getBundle("i18n/label").getString("binInput"));
        mainPanel.add(binaryStringLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        hexMsgLabel = new JLabel();
        Font hexMsgLabelFont = this.$$$getFont$$$("Microsoft YaHei UI", -1, -1, hexMsgLabel.getFont());
        if (hexMsgLabelFont != null) hexMsgLabel.setFont(hexMsgLabelFont);
        this.$$$loadLabelText$$$(hexMsgLabel, ResourceBundle.getBundle("i18n/label").getString("hexInput"));
        mainPanel.add(hexMsgLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rawTextInputLabel = new JLabel();
        Font rawTextInputLabelFont = this.$$$getFont$$$("Microsoft YaHei UI", -1, -1, rawTextInputLabel.getFont());
        if (rawTextInputLabelFont != null) rawTextInputLabel.setFont(rawTextInputLabelFont);
        this.$$$loadLabelText$$$(rawTextInputLabel, ResourceBundle.getBundle("i18n/label").getString("textInput"));
        mainPanel.add(rawTextInputLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        hostLabel = new JLabel();
        Font hostLabelFont = this.$$$getFont$$$("Microsoft YaHei UI", -1, -1, hostLabel.getFont());
        if (hostLabelFont != null) hostLabel.setFont(hostLabelFont);
        this.$$$loadLabelText$$$(hostLabel, ResourceBundle.getBundle("i18n/label").getString("host"));
        mainPanel.add(hostLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sendBtn = new JButton();
        this.$$$loadButtonText$$$(sendBtn, ResourceBundle.getBundle("i18n/label").getString("sendPacket"));
        mainPanel.add(sendBtn, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sendBinaryBtn = new JButton();
        this.$$$loadButtonText$$$(sendBinaryBtn, ResourceBundle.getBundle("i18n/label").getString("sendPacket"));
        mainPanel.add(sendBinaryBtn, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sendHexBtn = new JButton();
        this.$$$loadButtonText$$$(sendHexBtn, ResourceBundle.getBundle("i18n/label").getString("sendPacket"));
        mainPanel.add(sendHexBtn, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        logLabel = new JLabel();
        Font logLabelFont = this.$$$getFont$$$("Microsoft YaHei UI", -1, -1, logLabel.getFont());
        if (logLabelFont != null) logLabel.setFont(logLabelFont);
        this.$$$loadLabelText$$$(logLabel, ResourceBundle.getBundle("i18n/label").getString("log"));
        mainPanel.add(logLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tcpButton = new JButton();
        this.$$$loadButtonText$$$(tcpButton, ResourceBundle.getBundle("i18n/label").getString("tcpConnectBtn"));
        mainPanel.add(tcpButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        logScrollPane = new JScrollPane();
        mainPanel.add(logScrollPane, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        logPane = new JTextPane();
        logPane.setEditable(false);
        logScrollPane.setViewportView(logPane);
        additionPanel = new JPanel();
        additionPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(additionPanel, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        exportLogBtn = new JButton();
        this.$$$loadButtonText$$$(exportLogBtn, ResourceBundle.getBundle("i18n/label").getString("exportLog"));
        additionPanel.add(exportLogBtn, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        additionPanel.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        portLabel.setLabelFor(portTextField);
        binaryStringLabel.setLabelFor(binaryTextField);
        hexMsgLabel.setLabelFor(hexTextField);
        rawTextInputLabel.setLabelFor(msgTextField);
        hostLabel.setLabelFor(hostTextField);
        logLabel.setLabelFor(logScrollPane);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
