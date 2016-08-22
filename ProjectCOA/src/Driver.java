import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Driver {
    JFrame frame;
    JFrame frame1;
    JButton runButton;
    JTextArea textArea;
    String[] instLib;
    String[] varSet;
    int[] varVal;
    double regG;
    JTextArea label;
    int loopCount;
    int loopIndex;
    public static void main(String [] args){
        Driver driver=new Driver();
        driver.start();
    }

    public void start(){
        frame= new JFrame("Language IDE");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        runButton=new JButton("Run");
        runButton.addActionListener(new RunButton());
        textArea=new JTextArea();
        textArea.setLineWrap(true);
        JScrollPane scrollPane=new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        frame.getContentPane().add(BorderLayout.CENTER,scrollPane);
        frame.getContentPane().add(BorderLayout.SOUTH,runButton);
        frame.setSize(500,500);
        frame.setVisible(true);
    }

    class RunButton implements ActionListener{
        public void actionPerformed(ActionEvent event){
            interpret();
        }
    }

    public void interpret(){
        String[] code=textArea.getText().split(";");
        for(int i = 0 ; i < code.length ; i++){
            code[i] = code[i].trim();
        }
        frame1=new JFrame("Program");
        label=new JTextArea("");
        label.setEditable(false);
        label.setBackground(frame1.getBackground());
        JPanel panel = new JPanel();
        panel.add(label);
        JScrollPane scrollPane1 = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        frame1.getContentPane().add(BorderLayout.CENTER, scrollPane1);
        if(code[0].equals("Calibrate InstLib")) {
            calibrateLib();
            if(code.length>1) {
                if (code[1].equals("Calibrate Reg")) {
                    calibrateReg();
                    for (int i = 2; i < code.length; i++) {
                        String[] line = code[i].split(" ");
                        int x = searchLib(line[0]);
                        if (x != 0) {
                            switch (x) {
                                case 1:
                                    int y=searchReg(line[1]);
                                    if(y!=-1) {
                                        try {
                                            varVal[y] = Integer.parseInt(line[2]);
                                            label.setText(label.getText() + "Register " + line[1] + " Contains Value: " + line[2] + "\n");
                                        } catch (Exception e) {
                                            JLabel label = new JLabel("Integer Required.");
                                            JPanel jpanel = new JPanel();
                                            jpanel.add(label);
                                            JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                            frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                                            i=code.length;
                                        }
                                    }
                                    else{
                                        JLabel label = new JLabel("Register "+line[1]+" Does Not Exists.\n");
                                        JPanel jpanel = new JPanel();
                                        jpanel.add(label);
                                        JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                        frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                                        i=code.length;
                                    }
                                    break;
                                case 2:
                                    for(int j=0;j<6;j++){
                                        if(varVal[j]==-1){
                                            label.setText(label.getText() + "Register " + varSet[j] + ": Not Initialised.\n");
                                        }
                                        else{
                                            label.setText(label.getText() + "Register " + varSet[j] + ": " + varVal[j] + "\n");
                                        }
                                    }
                                    break;
                                case 3:
                                    if(line.length>1) {
                                        int reg1 = searchReg(line[1]);
                                        int reg2 = searchReg(line[2]);
                                        if(reg1!=-1 && reg2!=-1){
                                            if(varVal[reg1]!=-1 && varVal[reg2]!=-1){
                                                varVal[reg1]+=varVal[reg2];
                                                label.setText(label.getText() + "Value Of " + varSet[reg1] + " Changed To: " + varVal[reg1] + "\n");
                                            }
                                            else{
                                                JLabel label = new JLabel("Add: Value Of Register Not Initialised.\n");
                                                JPanel jpanel = new JPanel();
                                                jpanel.add(label);
                                                JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                                frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                                                i=code.length;
                                            }
                                        }
                                        else{
                                            JLabel label = new JLabel("Add: Register Not Found");
                                            JPanel jpanel = new JPanel();
                                            jpanel.add(label);
                                            JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                            frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                                            i=code.length;
                                        }
                                    }
                                    else{
                                        JLabel label = new JLabel("Add: Not Enough Arguments.\n");
                                        JPanel jpanel = new JPanel();
                                        jpanel.add(label);
                                        JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                        frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                                        i=code.length;
                                    }
                                    break;
                                case 4:
                                    if(line.length>1) {
                                        int reg1 = searchReg(line[1]);
                                        int reg2 = searchReg(line[2]);
                                        if(reg1!=-1 && reg2!=-1){
                                            if(varVal[reg1]!=-1 && varVal[reg2]!=-1){
                                                if(varVal[reg1]-varVal[reg2]<0){
                                                    varVal[reg1]=0;
                                                    label.setText(label.getText() + "Value Of " + varSet[reg1] + " Changed To: 0\n");
                                                }
                                                else {
                                                    varVal[reg1] -= varVal[reg2];
                                                    label.setText(label.getText() + "Value Of " + varSet[reg1] + " Changed To: " + varVal[reg1] + "\n");
                                                }
                                            }
                                            else{
                                                JLabel label = new JLabel("Sub: Value Of Register Not Initialised.\n");
                                                JPanel jpanel = new JPanel();
                                                jpanel.add(label);
                                                JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                                frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                                                i=code.length;
                                            }
                                        }
                                        else{
                                            JLabel label = new JLabel("Sub: Register Not Found");
                                            JPanel jpanel = new JPanel();
                                            jpanel.add(label);
                                            JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                            frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                                            i=code.length;
                                        }
                                    }
                                    else{
                                        JLabel label = new JLabel("Sub: Not Enough Arguments.\n");
                                        JPanel jpanel = new JPanel();
                                        jpanel.add(label);
                                        JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                        frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                                        i=code.length;
                                    }
                                    break;
                                case 5:
                                    if(line.length>1) {
                                        int reg1 = searchReg(line[1]);
                                        int reg2 = searchReg(line[2]);
                                        if(reg1!=-1 && reg2!=-1){
                                            if(varVal[reg1]!=-1 && varVal[reg2]!=-1){
                                                varVal[reg1]*=varVal[reg2];
                                                label.setText(label.getText() + "Value Of " + varSet[reg1] + " Changed To: " + varVal[reg1] + "\n");
                                            }
                                            else{
                                                JLabel label = new JLabel("Mul: Value Of Register Not Initialised.\n");
                                                JPanel jpanel = new JPanel();
                                                jpanel.add(label);
                                                JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                                frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                                                i=code.length;
                                            }
                                        }
                                        else{
                                            JLabel label = new JLabel("Mul: Register Not Found");
                                            JPanel jpanel = new JPanel();
                                            jpanel.add(label);
                                            JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                            frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                                            i=code.length;
							}
                                    }
                                    else{
                                        JLabel label = new JLabel("Mul: Not Enough Arguments.\n");
                                        JPanel jpanel = new JPanel();
                                        jpanel.add(label);
                                        JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                        frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                                        i=code.length;
                                    }
                                    break;
                                case 6:
                                    if(line.length>1) {
                                        int reg1 = searchReg(line[1]);
                                        int reg2 = searchReg(line[2]);
                                        if(reg1!=-1 && reg2!=-1){
                                            if(varVal[reg1]!=-1 && varVal[reg2]!=-1){
                                                regG=varVal[reg1]/varVal[reg2];
                                                label.setText(label.getText() + "Register G Set To: " + regG + "\n");
                                            }
                                            else{
                                                JLabel label = new JLabel("Div: Value Of Register Not Initialised.\n");
                                                JPanel jpanel = new JPanel();
                                                jpanel.add(label);
                                                JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                                frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                                                i=code.length;
                                            }
                                        }
                                        else{
                                            JLabel label = new JLabel("Div: Register Not Found");
                                            JPanel jpanel = new JPanel();
                                            jpanel.add(label);
                                            JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                            frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                                            i=code.length;
                                        }
                                    }
                                    else{
                                        JLabel label = new JLabel("Div: Not Enough Arguments.\n");
                                        JPanel jpanel = new JPanel();
                                        jpanel.add(label);
                                        JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                        frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                                        i=code.length;
                                    }
                                    break;
                                case 7:
                                    if(regG==-1)
                                        label.setText(label.getText() + "Register G Is Not Initialised\n");
                                    else
                                        label.setText(label.getText() + "Register G: " + regG + "\n");
                                    break;
                                case 8:
                                    if(line.length==1){
                                        for(int j=0;j<6;j++) {
                                            varVal[j] = -1;
                                        }
                                        label.setText(label.getText() + "All Registers Are Reset\n");
                                    }
                                    else {
                                        int reg = searchReg(line[1]);
                                        if (reg != -1) {
                                            varVal[reg]=-1;
                                        } else {
                                            JLabel label = new JLabel("Reset: Register Not Found\n");
                                            JPanel jpanel = new JPanel();
                                            jpanel.add(label);
                                            JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                            frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                                            i=code.length;
                                        }
                                    }
                                    break;
                                case 9:
                                    int reg = searchReg(line[1]);
                                    if(reg!=-1){
                                        regG=Math.sqrt((double)varVal[reg]);
                                        label.setText(label.getText() + "Register G Set To: " + regG + "\n");
                                    }
                                    else{
                                        JLabel label = new JLabel("Sqrt: Register Not Found\n");
                                        JPanel jpanel = new JPanel();
                                        jpanel.add(label);
                                        JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                        frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                                        i=code.length;
                                    }
                                    break;
                                case 10:
                                    try {
                                        loopCount = Integer.parseInt(line[1]);
                                        if (loopCount < 0) {
                                            JLabel label = new JLabel("Loop: Loop Count Should Not Be Negative\n");
                                            JPanel jpanel = new JPanel();
                                            jpanel.add(label);
                                            JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                            frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                                            i = code.length;
                                        } else {
                                            loopIndex = i;
                                        }
                                    } catch(Exception e){
                                        JLabel label = new JLabel("Integer Required.");
                                        JPanel jpanel = new JPanel();
                                        jpanel.add(label);
                                        JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                        frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                                        i=code.length;
                                    }
                                    break;
                                case 11:
                                    if(loopIndex == 0){
                                        JLabel label = new JLabel("Invalid: End Without Loop");
                                        JPanel jpanel = new JPanel();
                                        jpanel.add(label);
                                        JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                        frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                                        i=code.length;
                                    }
                                    else{
                                        loopCount--;
                                        if(loopCount!=0){
                                            i = loopIndex;
                                        }
                                    }
                                    break;
                            }
                        } else {
                            JLabel label = new JLabel("Instruction \"" + line[0] + "\" Not Found In The Library");
                            JPanel jpanel = new JPanel();
                            jpanel.add(label);
                            JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                            frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                            break;
                        }
                        if(i==code.length-1){
                            if(loopCount!=0){
                                JLabel label = new JLabel("Loop: End Not Found To Terminate Loop");
                                JPanel jpanel = new JPanel();
                                jpanel.add(label);
                                JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
                            }
                        }
                    }
                }
            }
            else{
                JLabel label = new JLabel("Register Library Not Calibrated");
                JPanel jpanel = new JPanel();
                jpanel.add(label);
                JScrollPane scrollPane2 = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                frame1.getContentPane().add(BorderLayout.NORTH, scrollPane2);
            }
            frame1.setSize(500,500);
            frame1.setVisible(true);
        }
        else{
            JLabel label=new JLabel("Instruction Library Not Calibrated");
            JPanel jpanel=new JPanel();
            jpanel.add(label);
            JScrollPane scrollPane2=new JScrollPane(jpanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            frame1.getContentPane().add(BorderLayout.NORTH,scrollPane2);
            frame1.setSize(500,500);
            frame1.setVisible(true);
        }
    }

    public void calibrateLib(){
        instLib=new String[11];
        instLib[0]="Init";
        instLib[1]="Show";
        instLib[2]="Add";
        instLib[3]="Sub";
        instLib[4]="Mul";
        instLib[5]="Div";
        instLib[6]="GetG";
        instLib[7]="Reset";
        instLib[8]="Sqrt";
        instLib[9]="Loop";
        instLib[10]="End";
    }

    public void calibrateReg(){
        varSet = new String[7];
        varVal = new int[6];
        varSet[0]="A";
        varSet[1]="B";
        varSet[2]="C";
        varSet[3]="D";
        varSet[4]="E";
        varSet[5]="F";
        varSet[6]="G";
        for(int i=0;i<6;i++)
            varVal[i]=-1;
        regG=-1;
        loopCount=0;
        loopIndex=0;
    }

    public int searchLib(String s){
        for(int i=0;i<instLib.length;i++){
            if(s.equals(instLib[i]))
                return i+1;
        }
        return 0;
    }

    public int searchReg(String s){
        for(int i=0;i<varSet.length-1;i++){
            if(varSet[i].equals(s))
                return i;
        }
        return -1;
    }
}