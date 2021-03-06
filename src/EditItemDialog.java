import com.thoughtworks.xstream.XStream;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

public class EditItemDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox kindPar1CB;
    private JComboBox kindPar2CB;
    private JComboBox kindPar3CB;
    private JTextField initValuePar1TF;
    private JTextField initValuePar2TF;
    private JTextField initValuePar3TF;
    private JComboBox incrementPar1TF;
    private JComboBox incrementPar2TF;
    private JComboBox incrementPar3TF;
    private JComboBox levelCB;
    private JTextField nameTF;
    private JComboBox placeCB;
    private JComboBox maxLevelCB;
    private JCheckBox equippedCB;
    private int position;
    private ArrayList<Item> itemList;
    private XStream xStream;


    public EditItemDialog(int pos, ArrayList<Item> list) {

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setTitle("Изменить");
        setBounds(100, 100, 650, 400);
        getRootPane().setDefaultButton(buttonOK);
        position = pos;

        xStream = new XStream();
        xStream.alias("items", ItemList.class);
        xStream.alias("item", Item.class);
        xStream.alias("parameter", Parameter.class);
        xStream.addImplicitCollection(ItemList.class, "list");

        itemList = list;//(ArrayList<Item>)xStream.fromXML(xml);
        Item temp = itemList.get(position);

        if (temp.isEquipped()){
            equippedCB.setSelected(true);
        }

        placeCB.addItem("Шапка");
        placeCB.addItem("Артефакт");
        placeCB.addItem("Оружие");
        placeCB.addItem("Доспех");
        placeCB.setSelectedIndex(temp.getPlacement());

        kindPar1CB = addKinds(kindPar1CB);
        kindPar1CB.setSelectedIndex(temp.getPar1().getKind());
        kindPar2CB = addKinds(kindPar2CB);
        kindPar2CB.setSelectedIndex(temp.getPar2().getKind());
        kindPar3CB = addKinds(kindPar3CB);
        kindPar3CB.setSelectedIndex(temp.getPar3().getKind());

        incrementPar1TF.addItem(0.8);
        incrementPar1TF.addItem(0.7);
        incrementPar1TF.addItem(0.6);
        incrementPar1TF.addItem(0.5);
        incrementPar1TF.addItem(0.4);
        incrementPar2TF.addItem(0.4);
        incrementPar2TF.addItem(0.3);
        incrementPar2TF.addItem(0.2);
        incrementPar2TF.addItem(0.1);
        incrementPar3TF.addItem(0.4);
        incrementPar3TF.addItem(0.3);
        incrementPar3TF.addItem(0.2);
        incrementPar3TF.addItem(0.1);
        int ind;
        switch (Math.round(temp.getPar1().getIncrement()*10)){
            case 8: incrementPar1TF.setSelectedIndex(0);
                break;
            case 7: incrementPar1TF.setSelectedIndex(1);
                break;
            case 6: incrementPar1TF.setSelectedIndex(2);
                break;
            case 5: incrementPar1TF.setSelectedIndex(3);
                break;
            case 4: incrementPar1TF.setSelectedIndex(4);
                break;
            default: incrementPar1TF.setSelectedIndex(0);
                break;
        }
        //incrementPar1TF.setSelectedIndex(ind);

        switch (Math.round(temp.getPar2().getIncrement()*10)){
            case 4: incrementPar2TF.setSelectedIndex(0);
                break;
            case 3: incrementPar2TF.setSelectedIndex(1);
                break;
            case 2: incrementPar2TF.setSelectedIndex(2);
                break;
            case 1: incrementPar2TF.setSelectedIndex(3);
                break;
            default: incrementPar2TF.setSelectedIndex(0);
                break;
        }

        switch (Math.round(temp.getPar3().getIncrement()*10)){
            case 4: incrementPar3TF.setSelectedIndex(0);
                break;
            case 3: incrementPar3TF.setSelectedIndex(1);
                break;
            case 2: incrementPar3TF.setSelectedIndex(2);
                break;
            case 1: incrementPar3TF.setSelectedIndex(3);
                break;
            default: incrementPar3TF.setSelectedIndex(0);
                break;
        }


        levelCB = addLevels(levelCB);
        levelCB.setSelectedIndex(temp.getPar1().getLevel());
        maxLevelCB.addItem(16);
        maxLevelCB.addItem(20);
        if (temp.getPar1().getMaxLevel()==16){
            ind = 0;
        }else {
            ind = 1;
        }
        maxLevelCB.setSelectedIndex(ind);


        nameTF.setText(temp.getName());
        initValuePar1TF.setText(temp.getPar1().getInitValue()+"");
        initValuePar2TF.setText(temp.getPar2().getInitValue()+"");
        initValuePar3TF.setText(temp.getPar3().getInitValue()+"");


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
// add your code here
        Number init1= null;
        Number init2= null;
        Number init3= null;
        Number incr1= null;
        Number incr2= null;
        Number incr3= null;
        int maxLevel;
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        try {
            init1 = format.parse(initValuePar1TF.getText());
            init2 = format.parse(initValuePar2TF.getText());
            init3 = format.parse(initValuePar3TF.getText());
        } catch (ParseException eh){

        }
        if (maxLevelCB.getSelectedIndex()==0){
            maxLevel = 16;
        } else {
            maxLevel = 20;
        }
        format = NumberFormat.getInstance(Locale.US);
        try {
            incr1 = format.parse(incrementPar1TF.getSelectedItem().toString());
            incr2 = format.parse(incrementPar2TF.getSelectedItem().toString());
            incr3 = format.parse(incrementPar3TF.getSelectedItem().toString());

        } catch (ParseException eh){

        }

        Parameter par1 = new Parameter(kindPar1CB.getSelectedIndex(), init1.floatValue(), levelCB.getSelectedIndex(), incr1.floatValue(), maxLevel);
        Parameter par2 = new Parameter(kindPar2CB.getSelectedIndex(), init2.floatValue(), levelCB.getSelectedIndex(), incr2.floatValue(), maxLevel);
        Parameter par3 = new Parameter(kindPar3CB.getSelectedIndex(), init3.floatValue(), levelCB.getSelectedIndex(), incr3.floatValue(), maxLevel);
        Item item= new Item(placeCB.getSelectedIndex(), par1, par2, par3, nameTF.getText(), equippedCB.isSelected());
        itemList.remove(position);
        itemList.add(position,item);
        String xml1 = xStream.toXML(itemList);

        try{
            java.io.FileWriter fw = new java.io.FileWriter("items.xml");
            fw.write(xml1);
            fw.close();
        } catch (IOException e){

        }

        System.out.println("name " + item.getName() + "       " + item.getPars()[0] + "         " + item.getPars()[1] + "       " + item.getPars()[2] + "          " + item.getPars()[3]
                + "         " + item.getPars()[4] + "          " + item.getPars()[5] + "          " + item.getPars()[6] + "          " + item.getPars()[7]);
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    private JComboBox addKinds(JComboBox jComboBox){
        JComboBox result = jComboBox;
        result.addItem("Сила ББ");
        result.addItem("Сила СС");
        result.addItem("Стена");
        result.addItem("Ров");
        result.addItem("Ворота");
        result.addItem("Скорость");
        result.addItem("Обнаружение");
        result.addItem("Мародер");
        result.addItem("Огонь");
        result.addItem("Честь");
        result.addItem("Слава");
        result.addItem("Уничтожение");
        result.addItem("Снаряжение");
        result.addItem("Эконом");
        return result;
    }

    private JComboBox addLevels(JComboBox jComboBox){
        JComboBox result = jComboBox;
        for (int i=0;i<21;i++){
            result.addItem(i);
        }

        return result;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
