import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Window extends JFrame {
    private JTextField coefficientsField;
    private JTextField modelField;
    private List<JTextField> constraintsFields; // Список полів для обмежень
    private JTextArea resultsTextArea;
    private JPanel constraintsPanel; // Панель для полів обмежень

    private equation targetEquation;
    private ArrayList<equation> funcList;
    private ArrayList<Point> intersectionPoint;

    public int count = 0;
    public Window() {
        setTitle("Linear Programming Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);

        // Поле для вводу коефіцієнтів цільової функції
        coefficientsField = new JTextField(60);
        coefficientsField.setBorder(BorderFactory.createTitledBorder("Цільова функція F(x,y) = _x1+_x2+_y"));

        modelField = new JTextField(60);
        modelField.setBorder(BorderFactory.createTitledBorder("Модель процесу y = _x1+_x2"));

        // Список полів для обмежень
        constraintsFields = new ArrayList<>();
        constraintsPanel = new JPanel();
        constraintsPanel.setLayout(new BoxLayout(constraintsPanel, BoxLayout.Y_AXIS));

        // Додати перше поле для обмеження
        addConstraintField();

        // Кнопка "Знайти"
        JButton solveButton = new JButton("Знайти");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Отримати дані з полів для вводу
                String coefficients = coefficientsField.getText();
                String model = modelField.getText();
                String constraints = getConstraintsString();

                // Виконати обчислення і отримати результати
                solveLinearProgramming(coefficients, model, constraints);
                String resultText =Calculation.printPoint(intersectionPoint);
                ODZ();
                resultText = resultText.concat( Calculation.printValue(intersectionPoint, targetEquation));

                String results = "\nМінімальне значення = "+ Calculation.getMin(targetEquation, intersectionPoint)+
                        "\nМаксимальне значення = " + Calculation.getMax(targetEquation, intersectionPoint);

                resultText =  resultText.concat(results);
                // Вивести результати в текстову область

                resultsTextArea.setText(resultText);

            }
        });

        // Кнопка "+"
        JButton addButton = new JButton("+");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addConstraintField();
                revalidate();
                repaint();
            }
        });

        resultsTextArea = new JTextArea(30, 90);
        resultsTextArea.setEditable(false);
        resultsTextArea.setBorder(BorderFactory.createTitledBorder("Результати"));

        // Додайте компоненти в JSplitPane для розділення вікна на дві частини
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createInputPanel());
        splitPane.setRightComponent(new JScrollPane(resultsTextArea));
        splitPane.setDividerLocation(500); // Розміщення розділювача між панелями

        getContentPane().add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.add(coefficientsField);
        inputPanel.add(modelField);
        inputPanel.add(constraintsPanel);
        inputPanel.add(createButtonsPanel());

        return inputPanel;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(createButtonBox());
        return buttonsPanel;
    }

    private Box createButtonBox() {
        Box buttonBox = Box.createHorizontalBox();
        JButton addButton = new JButton("+");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addConstraintField();
                revalidate();
                repaint();
            }
        });

        JButton solveButton = new JButton("Знайти");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Отримати дані з полів для вводу
                String coefficients = coefficientsField.getText();
                String model = modelField.getText();
                String constraints = getConstraintsString();

                // Виконати обчислення і отримати результати
                solveLinearProgramming(coefficients, model, constraints);
                String resultText =Calculation.printPoint(intersectionPoint);
                ODZ();
                resultText = resultText.concat( Calculation.printValue(intersectionPoint, targetEquation));

                String results = "\nМінімальне значення = "+ Calculation.getMin(targetEquation, intersectionPoint)+
                        "\nМаксимальне значення = " + Calculation.getMax(targetEquation, intersectionPoint);

                resultText =  resultText.concat(results);
                // Вивести результати в текстову область

                resultsTextArea.setText(resultText);
            }
        });

        buttonBox.add(addButton);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(solveButton);

        return buttonBox;
    }


    // Метод для створення і додавання нового поля для обмеження
    private void addConstraintField() {
        JTextField constraintField = new JTextField(60);
        count++;
        constraintField.setBorder(BorderFactory.createTitledBorder( count +". Обмеження _x1+_x2 <= _"));
        constraintsFields.add(constraintField);
        constraintsPanel.add(constraintField); // Додати на панель з полями обмежень
    }

    // Метод для отримання обмежень у вигляді рядка
    private String getConstraintsString() {
        StringBuilder constraints = new StringBuilder();
        for (JTextField field : constraintsFields) {
            constraints.append(field.getText()).append(" + ");
        }
        // Видаляємо останній знак "+"
        if (constraints.length() >= 3) {
            constraints.delete(constraints.length() - 3, constraints.length());
        }
        return constraints.toString();
    }

    // Функція для обчислення мінімуму і максимуму
    private String solveLinearProgramming(String coefficients, String model, String constraints) {
        try {
            // Розбиваємо рядки на коефіцієнти
            String[] coeffArray = coefficients.split(" ");
            String[] modelArray = model.split(" ");
            String[] constraintArray = constraints.split("'+'" );

            // Ініціалізуємо масиви для зберігання коефіцієнтів
            double[] coefficientsArray = new double[coeffArray.length];
            double[] modelArrayValues = new double[modelArray.length];
            double[][] constraintsArray = new double[count][coeffArray.length];

            // Перетворюємо рядки у числа
            for (int i = 0; i < coeffArray.length; i++) {
                coefficientsArray[i] = Double.parseDouble(coeffArray[i]);
            }
            for (int i = 0; i < modelArray.length; i++) {
                modelArrayValues[i] = Double.parseDouble(modelArray[i]);
            }
            targetEquation = new equation(coefficientsArray[0]+coefficientsArray[2]*modelArrayValues[0],coefficientsArray[1]+coefficientsArray[2]*modelArrayValues[1],0,false);


            String[] constraintCoefficients = constraintArray[0].split(" ");
            int k =0;
            funcList = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                for (int j = 0; j < coeffArray.length; j++, k++) {
                    if(constraintCoefficients[k].equals("+"))
                        k++;
                    constraintsArray[i][j] = Double.parseDouble(constraintCoefficients[k]);
                }
                equation e = new equation(constraintsArray[i]);
                funcList.add(e);

            }

            intersectionPoints();
            return "";

        } catch (Exception e) {
            return "Помилка введення даних. Перевірте введені значення.";
        }
    }

    public void ODZ() {

        for (int i = 0; i < intersectionPoint.size(); i++) {
            boolean flag = true;
            for (equation equation : funcList) {
                if (!equation.isInArea(intersectionPoint.get(i).getX(), intersectionPoint.get(i).getY())) {
                    flag = false;
                    break;
                }
            }
            if (!flag) {
                intersectionPoint.remove(i);
                i--;
            }
        }

    }



    public void intersectionPoints() {
        intersectionPoint = new ArrayList<>();
        for (int i = 0; i < funcList.size(); i++) {
            for (int j = i+1; j < funcList.size(); j++) {
                intersectionPoint.add(Calculation.intersection(funcList.get(i), funcList.get(j)));
            }
        }
    }

}
