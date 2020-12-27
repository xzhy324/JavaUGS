package drawer;
import m2.nbb.data.DataLoader;
import m2.nbb.debug.img.ImageReader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MyJFrame extends JFrame {

    private static DataLoader dataloader;
    BufferedImage image = new BufferedImage(560, 560, BufferedImage.TYPE_INT_BGR);
    Graphics2D g = (Graphics2D) image.getGraphics();
    MyCanvas canvas = new MyCanvas();
    Color foreColor = Color.black;
    Color backgroundColor = Color.white;

    int x = -1;int y = -1;
    private JToolBar toolBar;
    private JButton clearButton;
    private JButton saveButton;

    public MyJFrame() {
        setResizable(false);//设为不可更改大小
        setTitle("朴素贝叶斯手写数字识别");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(400, 200, 560, 560);
        component_init();//初始化画板
        addListener();//添加监听事件
    }

    private void component_init() {
        g.setColor(backgroundColor);
        g.fillRect(0, 0, 560, 560);//矩形覆盖填充
        g.setColor(foreColor);
        canvas.setImage(image);
        getContentPane().add(canvas);
        g.setStroke(new BasicStroke(30, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));//画笔粗细

        toolBar = new JToolBar();
        getContentPane().add(toolBar, BorderLayout.NORTH);//将工具栏放在窗体上边
        saveButton = new JButton("识别");
        toolBar.add(saveButton);
        toolBar.addSeparator();
        clearButton = new JButton("清除");
        toolBar.add(clearButton);
    }

    private void addListener() {
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(final MouseEvent e) {
                if (x > 0 && y > 0) //防止两次直接连线
                    g.drawLine(x, y, e.getX(), e.getY());
                x = e.getX();y = e.getY();
                canvas.repaint();
            }
        });
        canvas.addMouseListener(new MouseAdapter() {
            public void mouseReleased(final MouseEvent arg0) {
                x = -1;y = -1;//防止两次直接连线
            }
        });

        clearButton.addActionListener(e -> {
            g.setColor(backgroundColor);
            g.fillRect(0, 0, 560, 560);
            g.setColor(foreColor);
            canvas.repaint();

        });

        saveButton.addActionListener(e -> {
            int result=dataloader.getResult(ImageHandler.convertToByteArray(image));
            if(result==-1)JOptionPane.showMessageDialog(null,"Cannot Recognize!");
            else JOptionPane.showMessageDialog(null,"The Result is "+result+"!\n");
            ImageHandler.savePNG(image);
        });
    }
    public static void main(String[] args) {
        m2.nbb.debug.img.ImageReader ir=new ImageReader("./train-images.idx3-ubyte");
        //ir.printImg(50000);
        dataloader = new DataLoader("./train-labels.idx1-ubyte", "./train-images.idx3-ubyte", 10);
        MyJFrame frame = new MyJFrame();
        frame.setVisible(true);
    }
}