package drawer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

public class ImageHandler {
    //将560*560的图片压缩为28*28的图像
    public static byte[] convertToByteArray(BufferedImage srcImage){
        byte[] outPut=new byte[784];
        int[] dataINT=((DataBufferInt) srcImage.getData().getDataBuffer()).getData();
        //白色：16777215 黑色：0
        int[][] data2D=new int[560][560];
        for(int i=0;i<313600;i++){
            int x=i/560;//确定行数
            int y=i%560;//确定列数
            data2D[x][y]=dataINT[i];
        }//转化为二维int数组
        for(int i=0;i<560;i+=20){
            for(int j=0;j<560;j+=20){
                //(i,j)为右上顶点，分块处理
                int blackCounter=0;
                for(int m=i;m<i+20;m++){
                    for(int n=j;n<j+20;n++){
                        if(data2D[m][n]==0)blackCounter++;
                    }
                }
                int x=i/20,y=j/20;//转化为2D坐标情况
                if(blackCounter>=FILL_RATE) outPut[x*28+y]=(byte)(-254*blackCounter/400);
                else outPut[x*28+y]=0;
            }
        }
        for(int x=0;x<28;x++){//打印output
            for(int y=0;y<28;y++){
                if(outPut[x*28+y]==0) System.out.print('□');
                else System.out.print('■');
            }System.out.println();
        }
        return outPut;
    }
    //将画板保存为.png图像
    public static void savePNG(BufferedImage srcImage){
        String filename= String.valueOf(System.currentTimeMillis());
        filename+=".png";
        File outputfile = new File(filename);
        try {
            ImageIO.write(srcImage,"png",outputfile);
            JOptionPane.showMessageDialog(null,"Saved as PNG in the RootDirectory!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static final int FILL_RATE=5;//     FIll_Rate越大，则一个有效块需要的黑色像素越多
}
