import javax.swing.*;

public class MyFrame extends JFrame {
    public MyFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(200,200,800,600);
        setTitle("Brick Breaker");
        MyPanel myPanel = new MyPanel();
        setResizable(false);
        setVisible(true);
        add(myPanel);
    }
}
