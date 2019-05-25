import jdk.nashorn.internal.ir.Terminal;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import javax.swing.*;
public class MainScene extends JFrame {

    private JPanel systemTextPanel;
    private JSplitPane ioDivision;
    private JTextArea outPutArea;
    private JScrollPane outputScroll;
    private JTextArea inputArea;
    private JScrollPane inputScroll;

    private StringBuilder outPut = new StringBuilder();


    JPanel SubmitPanel;
    JButton submitButtom;

    public static void main(String[] args) {
        MainScene a = new MainScene();
    }

    public MainScene()
    {
        myConsole.initialConsole();
        systemTextPanel = new JPanel();
        outPutArea = new JTextArea();
        outPutArea.setLineWrap(true);
        outputScroll = new JScrollPane(outPutArea);
        inputArea = new JTextArea();
        inputArea.setLineWrap(true);
        inputScroll = new JScrollPane(inputArea);
        ioDivision = new JSplitPane(JSplitPane.VERTICAL_SPLIT, outputScroll, inputScroll);
        ioDivision.setDividerLocation(200);
        ioDivision.setDividerSize(1);

        SubmitPanel = new JPanel();
        submitButtom = new JButton("Submit");

        systemTextPanel.setLayout(new BorderLayout());
        SubmitPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        systemTextPanel.add(ioDivision);
        SubmitPanel.add(submitButtom);

        this.add(systemTextPanel, BorderLayout.CENTER);
        this.add(SubmitPanel, BorderLayout.SOUTH);

        this.setTitle("File Manager");
        this.setSize(400, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        inputArea.setFont(new Font("Courier",Font.PLAIN,16));
        outPutArea.setFont(new Font("Courier",Font.PLAIN,16));

        submitButtom.addActionListener(event -> whatWriting());



    }

    private void whatWriting() {
        //System.out.println((new Date()).toString().replaceAll(" ","-"));
        //System.out.println(UUID.randomUUID().toString());
        String command = inputArea.getText();
        if(command.indexOf("nanoo")==0){
            String out = myConsole.interpret(command);
            inputArea.setText(out);
        }else {
            String out = myConsole.interpret(command);
            if(out.lastIndexOf("\n")==out.length()-1){
                outPut.append(out);
            }else {
            outPut.append(out+"\n");
            }
            outPutArea.setText(outPut.toString());
            inputArea.setText("");
        }




    }


}

