import java.io.BufferedWriter;
import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.BorderLayout;

public class KeyLogger extends JPanel implements KeyListener {
    String filePath = "C:\\Users\\New\\Desktop\\1\\output.txt";
    BufferedWriter bw;
    boolean capsLockOn = false; // Флаг для отслеживания состояния Caps Lock
    boolean shiftPressed = false; // Флаг для отслеживания состояния Shift
    JTextArea textArea; // Область для отображения вводимых символов

    public KeyLogger() {
        this.setFocusable(true);
        this.addKeyListener(this);

        textArea = new JTextArea();
        textArea.setEditable(false); // Запрет на редактирование пользователем
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(textArea), BorderLayout.CENTER);

        try {
            bw = new BufferedWriter(new FileWriter(filePath, true)); // 'true' для добавления в конец файла
        } catch (IOException e) {
            System.out.println("Ошибка при открытии файла: " + e.getMessage());
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            String keyText = "";
            // Проверяем клавишу Backspace
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                String currentText = textArea.getText();
                if (currentText.length() > 0) {
                    textArea.setText(currentText.substring(0, currentText.length() - 1));
                }
            } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                keyText = "\n"; // Переход на новую строку
                textArea.append(keyText);
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                keyText = " "; // Пробел
                textArea.append(keyText);
            } else if (e.getKeyCode() == KeyEvent.VK_TAB) {
                keyText = "     "; // 5 пробелов
                textArea.append(keyText);
            } else if (e.getKeyCode() == KeyEvent.VK_CAPS_LOCK) {
                capsLockOn = !capsLockOn; // Меняем состояние Caps Lock
                return; // Возвращаемся, так как ничего не записываем в файл
            } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                shiftPressed = true; // Устанавливаем флаг нажатия Shift
                return; // Возвращаемся, так как ничего не записываем в файл
            } else {
                keyText = KeyEvent.getKeyText(e.getKeyCode());
                if (capsLockOn || shiftPressed) {
                    keyText = keyText.toUpperCase(); // Переводим в верхний регистр
                } else {
                    keyText = keyText.toLowerCase(); // Переводим в нижний регистр
                }
                textArea.append(keyText);
            }
            bw.write(keyText); // Записываем в файл
            bw.flush(); // Сбрасываем данные в файл
        } catch (IOException ex) {
            System.out.println("Ошибка при записи в файл: " + ex.getMessage());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Сбрасываем флаг нажатия Shift при отпускании клавиши
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shiftPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Можно оставить пустым, если не требуется обработка ввода символа клавиатуры
    }

    // Метод для закрытия BufferedWriter, когда программа завершает работу
    public void close() {
        try {
            if (bw != null) {
                bw.close();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при закрытии файла: " + e.getMessage());
        }
    }
}
