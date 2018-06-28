package com.multimedia.project;
import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import org.omg.CORBA.PRIVATE_MEMBER;

import com.multimedia.project.AlgorithmCoding;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

/*** 
 * @description 主窗口
 * @version 4.0
*/
public class MainFrame extends JFrame {

	private JFrame frame;
	
	int i, iw, ih;
	int[] pixels;
	int[][] DCT_grey;
	boolean isLoad = false;
	boolean isGrey = false;
	boolean isColor = false;
	boolean isDCT = false;
	final BMPPanel mp = new BMPPanel();
	int type=24;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setSize(900, 600);  
	      
	    //The following code ensure the window shown on the center of screen.  
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();  
	    frame.setLocation((dim.width - frame.getWidth()) / 2, (dim.height - frame.getHeight()) / 2);  
		frame.setTitle("Simple Photoshop");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 try {  
	            UIManager  
	                .setLookAndFeel(new NimbusLookAndFeel());  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu FileMenu = new JMenu("文件");
		FileMenu.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 15));
		menuBar.add(FileMenu);
		
		JMenu openMenu = new JMenu("打开");
		openMenu.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		FileMenu.add(openMenu);
		
		JMenuItem openMenuItem_bmp = new JMenuItem("打开bmp文件");
		openMenuItem_bmp.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		openMenu.add(openMenuItem_bmp);
		
		JMenu saveMenu = new JMenu("保存");
		saveMenu.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		FileMenu.add(saveMenu);
		
		JMenuItem saveMenuItem_bmp = new JMenuItem("保存bmp文件");
		saveMenuItem_bmp.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		saveMenu.add(saveMenuItem_bmp);
		
		JMenu ModeSwitchMenu = new JMenu("模式转换");
		ModeSwitchMenu.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 15));
		menuBar.add(ModeSwitchMenu);
		
		JMenu toBinaryMenu = new JMenu("转换为二进制图像");
		toBinaryMenu.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		ModeSwitchMenu.add(toBinaryMenu);
		
		JMenuItem SingleThresholdsMenuItem = new JMenuItem("单阈值法");
		SingleThresholdsMenuItem.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		toBinaryMenu.add(SingleThresholdsMenuItem);
		
		JMenuItem DitherMenuItem = new JMenuItem("Dither");
		DitherMenuItem.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		toBinaryMenu.add(DitherMenuItem);
		
		JMenuItem OrderedDitherMenuItem = new JMenuItem("Ordered Dither");
		OrderedDitherMenuItem.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		toBinaryMenu.add(OrderedDitherMenuItem);
		
		JMenu toGrayMenu = new JMenu("转换为灰度图像");
		toGrayMenu.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		ModeSwitchMenu.add(toGrayMenu);
		
		JMenuItem toGrayMenuItemHSI = new JMenuItem("RGB—HSI");
		toGrayMenuItemHSI.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		toGrayMenu.add(toGrayMenuItemHSI);
		
		JMenuItem toGrayMenuItemYCbCr = new JMenuItem("RGB--YCbCr");
		toGrayMenuItemYCbCr.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		toGrayMenu.add(toGrayMenuItemYCbCr);
		
		JMenu ImageEnhancementMenu = new JMenu("图像增强");
		ImageEnhancementMenu.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 15));
		menuBar.add(ImageEnhancementMenu);
		
		JMenu HistogramEqualizationMenu = new JMenu("直方图均衡");
		HistogramEqualizationMenu.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 15));
		ImageEnhancementMenu.add(HistogramEqualizationMenu);
		
		JMenuItem HistogramEqualizationMenuItemGray = new JMenuItem("Gray");
		HistogramEqualizationMenu.add(HistogramEqualizationMenuItemGray);
		HistogramEqualizationMenuItemGray.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		
		JMenuItem HistogramEqualizationMenuItemColor = new JMenuItem("Color");
		HistogramEqualizationMenuItemColor.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		HistogramEqualizationMenu.add(HistogramEqualizationMenuItemColor);
		
		JMenu CompressMenu = new JMenu("图像压缩");
		CompressMenu.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		menuBar.add(CompressMenu);
		
		JMenu PredictorMenu = new JMenu("预测器处理");
		PredictorMenu.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		CompressMenu.add(PredictorMenu);
		
		JMenuItem OnePredictorMenuItem = new JMenuItem("根据左侧值预测");
		PredictorMenu.add(OnePredictorMenuItem);
		OnePredictorMenuItem.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		
		JMenuItem TwoPredictorMenuItem = new JMenuItem("根据左侧和上方值预测");
		PredictorMenu.add(TwoPredictorMenuItem);
		TwoPredictorMenuItem.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		
		JMenuItem ThreePredictorMenuItem = new JMenuItem("根据左侧上方与左上值预测");
		PredictorMenu.add(ThreePredictorMenuItem);
		ThreePredictorMenuItem.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		
		JMenuItem AlgorithmCoding = new JMenuItem("进行算术编码");
		PredictorMenu.add(AlgorithmCoding);
		AlgorithmCoding.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		
		JMenu QuantizationMenu = new JMenu("量化");
		CompressMenu.add(QuantizationMenu);
		QuantizationMenu.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		
		JMenuItem UniformScaleQuantizationMenuItem = new JMenuItem("均匀量化");
		QuantizationMenu.add(UniformScaleQuantizationMenuItem);
		UniformScaleQuantizationMenuItem.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		
		JMenuItem UniformScaleQuantizationIGSMenuItem = new JMenuItem("均匀量化改进版IGS");
		QuantizationMenu.add(UniformScaleQuantizationIGSMenuItem);
		UniformScaleQuantizationIGSMenuItem.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		
		JMenu DCTMenu = new JMenu("DCT");
		DCTMenu.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		CompressMenu.add(DCTMenu);
		
		JMenuItem DCTTransformMenuItem = new JMenuItem("DCT变换");
		DCTTransformMenuItem.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		DCTMenu.add(DCTTransformMenuItem);
		
		JMenuItem DCTInverseTransformationMenuItem_50 = new JMenuItem("DCT变换(去掉50%)");
		DCTInverseTransformationMenuItem_50.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		DCTMenu.add(DCTInverseTransformationMenuItem_50);
		
		JMenuItem DCTInverseTransformationMenuItem = new JMenuItem("DCT反变换");
		DCTInverseTransformationMenuItem.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		DCTMenu.add(DCTInverseTransformationMenuItem);
		
		JMenuItem RefreshMenuItem = new JMenuItem("刷新");
		RefreshMenuItem.setForeground(Color.RED);
		RefreshMenuItem.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 15));
		menuBar.add(RefreshMenuItem);
		
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(mp);
	
		//打开文件
		openMenuItem_bmp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser =new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("bmp文件(*.bmp)", "bmp");
				chooser.setFileFilter(filter);
				chooser.setCurrentDirectory(new File("C:\\Users\\Renfu Gou\\Documents\\项目测试图像"));
				String path_name;
				
				int result = chooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION){
					File fl = chooser.getSelectedFile();
					try {
						BufferedImage Bi = ImageIO.read(fl);
						iw = Bi.getWidth();
						ih = Bi.getHeight();
						mp.img = ImageProcessing.read(new FileInputStream(fl));
						getReferences(mp.img);
						mp.setPreferredSize(new Dimension(iw, ih));
						repaint();
						frame.setSize(iw, ih);
						frame.pack();
						isColor = ImageProcessing.isColor;
						isGrey = ImageProcessing.isGrey;
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException ei) {
						// TODO Auto-generated catch block
						ei.printStackTrace();
					}
				}
			}
		});
		
		//保存文件
		saveMenuItem_bmp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { 
				JFileChooser chooser =new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("bmp文件(*.bmp)", "bmp");
				chooser.setFileFilter(filter);
				chooser.setCurrentDirectory(new File("C:\\Users\\Renfu Gou\\Documents\\项目测试图像"));
				String path_name;
				int result = chooser.showSaveDialog(null);
				if(result == JFileChooser.APPROVE_OPTION){
					path_name = chooser.getSelectedFile().getPath();
					try {
						ImageProcessing.Write(iw,ih,mp.img,path_name,type);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			
		});
		
		//转为二进制图像（单阈值法）
		SingleThresholdsMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String inputValue = JOptionPane.showInputDialog("Please input the Threshold(0~255)"); 
				int Threshold = Integer.parseInt(inputValue);
				SingleThreshold(e, mp.img,Threshold);	
				repaint();
				frame.setSize(iw, ih);
				frame.pack();
			}
		});
		
		//灰度转为二进制（Dither）
		DitherMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String inputValue = JOptionPane.showInputDialog("Please input the dither matrix(2的幂)"); 
				int DitherRank = Integer.parseInt(inputValue);
				Dither(e, mp.img,DitherRank);
				repaint();
				frame.setSize(iw, ih);
				frame.pack();
			}
		});
		
		//灰度转为二进制（OrderedDither）
		OrderedDitherMenuItem.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				String inputValue = JOptionPane.showInputDialog("Please input the dither matrix(2的幂)"); 
				int DitherRank = Integer.parseInt(inputValue);
				OrderedDither(e, mp.img,DitherRank);
				repaint();
				frame.setSize(iw, ih);
				frame.pack();			}
		});
		
		//转为灰度图像HSI
		toGrayMenuItemHSI.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				toGrayHSI(e, mp.img);
				repaint(); 
				frame.setSize(iw, ih);
				frame.pack();
			}
		});
		
		//转为灰度图像YCbCr
		toGrayMenuItemYCbCr.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				toGrayYCbCr(e, mp.img);
				repaint();
				frame.setSize(iw, ih);
				frame.pack();
			}
		});
	
		//直方图均衡（灰度）
		HistogramEqualizationMenuItemGray.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				HistogramEqualizationGray(e);
				repaint();
				frame.setSize(iw, ih);
				frame.pack();
			}
		});
		
		//直方图均衡（color）
		HistogramEqualizationMenuItemColor.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				HistogramEqualizationColor(e,mp.img);
				repaint();
				frame.setSize(iw, ih);
				frame.pack();
			}
		});
		
		//预测器处理
		OnePredictorMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { 
			if (isLoad && isGrey) {
				double Coefficient = 0;
				String inputCoe;
				int [] mGrey = new int[iw*ih];
				while(true) {
					inputCoe = JOptionPane.showInputDialog("请输入f(x-1,y)的系数"); 
					Coefficient = Double.parseDouble(inputCoe);
					if(Coefficient<=1&&Coefficient>0) {
						break;
					}
					else {
						JOptionPane.showMessageDialog(null,"系数错误！系数范围为(0,1],请重新输入");
					}
				}
				Predictor mOnePredictor = new Predictor(pixels,iw,ih);
				System.arraycopy(mOnePredictor.OnePredictor(e, Coefficient), 0, mGrey, 0, mGrey.length);
				ImageProducer ip = new MemoryImageSource(iw, ih, mGrey, 0, iw);
				mp.img = createImage(ip);
				isGrey = true;
				isColor = false;
				type = 8;
			} else {
				JOptionPane.showMessageDialog(null, "请打开一张图片", "Alert",
						JOptionPane.WARNING_MESSAGE);
			}
				repaint();
				frame.setSize(iw, ih);
				frame.pack();
			}
			
		});
		
		TwoPredictorMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { 
			if (isLoad && isGrey) {
				int [] mGrey = new int[iw*ih];
				String inputCoe1; 
				double Coefficient1;
				String inputCoe2; 
				double Coefficient2;
				double sumOfCoe;
				while(true) {
					
					inputCoe1 = JOptionPane.showInputDialog("请输入f(x-1,y)的系数"); 
					Coefficient1 = Double.parseDouble(inputCoe1);
					inputCoe2 = JOptionPane.showInputDialog("请输入f(x,y-1)的系数"); 
					Coefficient2 = Double.parseDouble(inputCoe2);
					sumOfCoe = Coefficient1 + Coefficient2;
					if(sumOfCoe<=1&&sumOfCoe>0) {
						break;
					}
					else {
						JOptionPane.showMessageDialog(null,"系数错误！所有系数的和范围应为(0,1],请重新输入");
					}
				}
				Predictor mOnePredictor = new Predictor(pixels,iw,ih);
				System.arraycopy(mOnePredictor.TwoPredictor(e, Coefficient1, Coefficient2), 0, mGrey, 0, mGrey.length);
				ImageProducer ip = new MemoryImageSource(iw, ih, mGrey, 0, iw);
				mp.img = createImage(ip);
				isGrey = true;
				isColor = false;
				type = 8;
			} else {
				JOptionPane.showMessageDialog(null, "请打开一张图片", "Alert",
						JOptionPane.WARNING_MESSAGE);
			}
				repaint();
				frame.setSize(iw, ih);
				frame.pack();
			}
			
		});
		
		ThreePredictorMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { 
			if (isLoad && isGrey) {
				int [] mGrey = new int[iw*ih];
				String inputCoe1; 
				double Coefficient1;
				String inputCoe2; 
				double Coefficient2;
				String inputCoe3; 
				double Coefficient3;
				double sumOfCoe;
				while(true) {
					
					inputCoe1 = JOptionPane.showInputDialog("请输入f(x-1,y)的系数"); 
					Coefficient1 = Double.parseDouble(inputCoe1);
					inputCoe2 = JOptionPane.showInputDialog("请输入f(x,y-1)的系数"); 
					Coefficient2 = Double.parseDouble(inputCoe2);
					inputCoe3 = JOptionPane.showInputDialog("请输入f(x-1,y-1)的系数"); 
					Coefficient3 = Double.parseDouble(inputCoe3);
					sumOfCoe = Coefficient1 + Coefficient2 + Coefficient3;
					if(sumOfCoe<=1&&sumOfCoe>0) {
						break;
					}
					else {
						JOptionPane.showMessageDialog(null,"系数错误！所有系数的和范围应为(0,1],请重新输入");
					}
				}
				Predictor mOnePredictor = new Predictor(pixels,iw,ih);
				System.arraycopy(mOnePredictor.ThreePredictor(e, Coefficient1, Coefficient2,Coefficient3), 0, mGrey, 0, mGrey.length);
				ImageProducer ip = new MemoryImageSource(iw, ih, mGrey, 0, iw);
				mp.img = createImage(ip);
				isGrey = true;
				isColor = false;
				type = 8;
			} else {
				JOptionPane.showMessageDialog(null, "请打开一张图片", "Alert",
						JOptionPane.WARNING_MESSAGE);
			}
				repaint();
				frame.setSize(iw, ih);
				frame.pack();
			}
			
		});
		
		//算术编码
		AlgorithmCoding.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { 
				if (isLoad && isGrey) {
					AlgorithmCoding newCode = new AlgorithmCoding(pixels);
					String str = new String(newCode.ACoding(e).toString());
					FileWriter fwriter = null;
					
					JFileChooser chooser =new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("txt文件(*.txt)", "txt");
					chooser.setFileFilter(filter);
					chooser.setCurrentDirectory(new File("C:\\Users\\Renfu Gou\\Documents\\项目测试图像"));
					String path_name;
					int result = chooser.showSaveDialog(null);
					if(result == JFileChooser.APPROVE_OPTION){
						path_name = chooser.getSelectedFile().getPath();
						path_name = path_name +".txt";
						try {
							  fwriter = new FileWriter(path_name);
							  fwriter.write(str);
						} 
						catch (IOException ex) {
							  ex.printStackTrace();
						} 
						finally {
							  try {
								   fwriter.flush();
								   fwriter.close();	
							  } 
							  catch (IOException ex) {
								  ex.printStackTrace();
							  }
						 }
					}

					//JOptionPane.showMessageDialog(null, "编码结果:"+str, "Alert", JOptionPane.INFORMATION_MESSAGE);
				} 
				else {
					JOptionPane.showMessageDialog(null, "请打开一张图片", "Alert",
							JOptionPane.WARNING_MESSAGE);
				}
				
				
			}
			
		});
		
		//均匀量化
		UniformScaleQuantizationMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String inputValue = JOptionPane.showInputDialog("Please input value x to set the compression ratio--- 8:x(example:x=4,compression ratio=2)"); 
				int ratio_value = Integer.parseInt(inputValue);
				UniformScaleQuantization(e, mp.img,ratio_value);	
				repaint();
				frame.setSize(iw, ih);
				frame.pack();
			}
		});
		
		//均匀量化改进版IGS
		UniformScaleQuantizationIGSMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				UniformScaleQuantizationIGS(e, mp.img);	
				repaint();
				frame.setSize(iw, ih);
				frame.pack();
			}
		});
		
		//DCT变换
		DCTTransformMenuItem.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {
				String inputValue = JOptionPane.showInputDialog("Please input the block size(rank)"); 
				int BlockRank = Integer.parseInt(inputValue);
				DCTTransform(e, mp.img,BlockRank);
				repaint();
				frame.setSize(iw, ih);
				frame.pack();
			}
		});
		
		//DCT变换,去掉50%
		DCTInverseTransformationMenuItem_50.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {
				String inputValue = JOptionPane.showInputDialog("Please input the block size(rank)"); 
				int BlockRank = Integer.parseInt(inputValue);
				DCTTransform_50(e, mp.img, BlockRank);
				repaint();
				frame.setSize(iw, ih);
				frame.pack();
			}
		});
		
		//DCT反变换
		DCTInverseTransformationMenuItem.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {
				String inputValue = JOptionPane.showInputDialog("Please input the block size(rank)"); 
				int BlockRank = Integer.parseInt(inputValue);
				DCTInverseTransform(e, mp.img,BlockRank);
				repaint();
				frame.setSize(iw, ih);
				frame.pack();
			}  
		});
		
		//刷新界面
		RefreshMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//frame.pack();
				//窗口最小化后还原
				frame.setSize(iw, ih);
				frame.pack();
			}
		});
		
	}
	
	
	/*********************函数功能实现*****************************/
	public void getReferences(Image img) {
		pixels = new int[iw * ih];

		MediaTracker tracker = new MediaTracker(this);
		tracker.addImage(img, 0);
		try {
			tracker.waitForID(0);
		} catch (Exception e) {

		}
		

		try {
			PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels, 0, iw);
			pg.grabPixels();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 获得图像的RGB值和Alpha值
		ColorModel cm = ColorModel.getRGBdefault();
		for (int i = 0; i < iw * ih; i++) {
			int alpha = cm.getAlpha(pixels[i]);
			int red = cm.getRed(pixels[i]);
			int green = cm.getGreen(pixels[i]);
			int blue = cm.getBlue(pixels[i]);
			this.pixels[i] = alpha << 24 | red << 16 | green << 8 | blue;
		}

		// 将数组中的像素产生一个图像
		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		mp.img = createImage(ip);
		this.isLoad = true;
	}
	
	//灰度转为二进制（单阈值法）
	public void SingleThreshold(ActionEvent e, Image img,int Threshold) {
		if (this.isLoad && this.isGrey) {
			try {
				PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels,0, iw);
				pg.grabPixels();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			ColorModel cm = ColorModel.getRGBdefault();
			for (int i = 0; i < ih; i++) {
				for (int j = 0; j < iw; j++) {
					int alpha = cm.getAlpha(pixels[i * iw + j]);
					int grey = this.pixels[i * iw + j] & 0x0000ff;
					if (grey<Threshold) {
						grey = 0;
					} else {
						grey = 255;
					}
					this.pixels[i * iw + j] = alpha << 24 | grey << 16 | grey << 8 | grey;
				}
			}
	
			// 将数组中的像素产生一个图像
			ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
			mp.img = createImage(ip);
			this.isGrey = false;
			this.type = 1;
		} else {
			JOptionPane.showMessageDialog(null, "请打开一张灰度图片！", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//灰度转为二进制（Dither）
	public void Dither(ActionEvent e,Image img,int rank) {
		//生成dither矩阵
		if (this.isLoad && this.isGrey) {
			try {
			PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels,0, iw);
			pg.grabPixels();
				} catch (Exception exception) {
			exception.printStackTrace();
			}
			ColorModel cm = ColorModel.getRGBdefault();
			int [][]dither=new int[rank][rank];
			dither=setDither(rank);
			int tempP=ih*iw;
			int []pixels=new int[tempP];
			int i;
			int j;
			int nh=ih*rank;
			int nw=iw*rank;
			int grey;
			int alpha;
			//线性拉伸
			int cell=(int) (256.0/(rank*rank+1));
			for(int k=0;k<tempP;k++) {
				pixels[k]=this.pixels[k] & 0x0000ff;
				pixels[k]=pixels[k]/cell;
			}
			int[]tempPixels=new int[nw*nh*rank*rank];
			for(int x=0;x<iw;x++) {
				for(int y=0;y<ih;y++) {
					i=x*rank;
					j=y*rank;
					alpha=cm.getAlpha(pixels[y * iw + x]);
					for(int ii=0;ii<rank;ii++) {
						for(int jj=0;jj<rank;jj++) {
							if(pixels[y*iw+x]-dither[ii][jj]>0) {
								tempPixels[(jj+j)*nw+ii+i]=-1;
							}
							else {
								tempPixels[(jj+j)*nw+ii+i]=-16777216;
							}
						}
					}
				}
			}
			// 将数组中的像素产生一个图像
			ImageProducer ip = new MemoryImageSource(nw, nh, tempPixels, 0, nw);
			mp.img = createImage(ip);
			this.isGrey = false;
			this.type = 1;
		} else {
			JOptionPane.showMessageDialog(null, "请打开一张灰度图片！", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//灰度转为二进制（OrderedDither）
	public void OrderedDither(ActionEvent e,Image img,int rank) {
		//生成dither矩阵
		if (this.isLoad && this.isGrey) {
			try {
			PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels,0, iw);
			pg.grabPixels();
				} catch (Exception exception) {
			exception.printStackTrace();
			}
			ColorModel cm = ColorModel.getRGBdefault();
			int [][]dither=new int[rank][rank];
			dither=setDither(rank);
			int tempP=ih*iw;
			int []pixels=new int[tempP];
			int i;
			int j;
			int grey;
			int alpha;
			//线性拉伸
			int cell=(int) (256.0/(rank*rank+1));
			for(int k=0;k<tempP;k++) {
				pixels[k]=this.pixels[k] & 0x0000ff;
				pixels[k]=pixels[k]/cell;
			}
			for(int x=0;x<iw;x++) {
				i=x%rank;
				for(int y=0;y<ih;y++) {
					j=y%rank;
					if(pixels[y*iw+x]>dither[i][j]) {
						pixels[y*iw+x]=-1;
					}
					else {
						pixels[y*iw+x]=-16777216;
					}
				}
			}
			// 将数组中的像素产生一个图像
			ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
			mp.img = createImage(ip);
			this.isGrey = false;
			this.type = 1;
		} else {
			JOptionPane.showMessageDialog(null, "请打开一张灰度图片！", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//生成Dither矩阵
	public int[][] setDither(int rank) {
		int half=rank/2;
		int [][]result=new int[rank][rank];
		if(rank==2) {
			result[0][0]=0;
			result[0][1]=2;
			result[1][0]=3;
			result[1][1]=1;
			return result;
		}
		int [][]d=new int[half][half];
		d=setDither(half);
		for(int i=0;i<half;i++) {
			for(int j=0;j<half;j++) {
				result[i][j]=4*d[i][j];
				result[i+half][j]=4*d[i][j]+3;
				result[i][j+half]=4*d[i][j]+2;
				result[i+half][j+half]=4*d[i][j]+1;
			}
		}
		return result;
		
	}
	
	//转为灰度图像HSI
	public void toGrayHSI(ActionEvent e,Image img) {
		// 必须保证图像进行了加载，然后才可以进行灰度化
		if (this.isLoad && this.isColor) {
			try {
				PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels,0, iw);
				pg.grabPixels();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
	
			ColorModel cm = ColorModel.getRGBdefault();
			for (int i = 0; i < ih; i++) {
				for (int j = 0; j < iw; j++) {
					int alpha = cm.getAlpha(pixels[i * iw + j]);
					int red = cm.getRed(pixels[i * iw + j]);
					int green = cm.getGreen(pixels[i * iw + j]);
					int blue = cm.getBlue(pixels[i * iw + j]);
	
					int intensity = (int) ((red + green + blue)/3);
	
					this.pixels[i * iw + j] = alpha << 24 | intensity << 16
							| intensity << 8 | intensity;
				}
			}
	
			// 将数组中的像素产生一个图像
			ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
			mp.img = createImage(ip);
			this.isColor = false;
			this.isGrey = true;
			this.type = 8;
		} else {
			JOptionPane.showMessageDialog(null, "请打开一张彩色图片！", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//转为灰度图像YCbCr
	public void toGrayYCbCr(ActionEvent e,Image img) {
		// 必须保证图像进行了加载，然后才可以进行灰度化
		if (this.isLoad && this.isColor) {
			try {
				PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels,0, iw);
				pg.grabPixels();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
	
			ColorModel cm = ColorModel.getRGBdefault();
			for (int i = 0; i < ih; i++) {
				for (int j = 0; j < iw; j++) {
					int alpha = cm.getAlpha(pixels[i * iw + j]);
					int red = cm.getRed(pixels[i * iw + j]);
					int green = cm.getGreen(pixels[i * iw + j]);
					int blue = cm.getBlue(pixels[i * iw + j]);
	
					int grey = (int) (0.2990 * red + 0.5870 * green + 0.1140 * blue);
	
					this.pixels[i * iw + j] = alpha << 24 | grey << 16
							| grey << 8 | grey;
				}
			}
	
			// 将数组中的像素产生一个图像
			ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
			mp.img = createImage(ip);
			this.isColor = false;
			this.isGrey = true;
			this.type = 8;
		} else {
			JOptionPane.showMessageDialog(null, "请打开一张彩色图片！", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//直方图均衡（灰度）
	public void HistogramEqualizationGray(ActionEvent e) {
		if (this.isLoad && this.isGrey) {
			// 获取图像的直方图
			int[] histogram = new int[256];
			for (int i = 0; i < ih; i++) {
				for (int j = 0; j < iw; j++) {
					int grey = this.pixels[i * iw + j] & 0xff;
					histogram[grey]++;
				}
			}
			// 直方图均匀化处理
			double a = (double) 255 / (iw * ih);
			double[] c = new double[256];
			c[0] = a * histogram[0];
			for (int i = 1; i < 256; i++) {
				c[i] = c[i - 1] + (int) (a * histogram[i]);
			}
			ColorModel cm = ColorModel.getRGBdefault();
			for (int i = 0; i < ih; i++) {
				for (int j = 0; j < iw; j++) {
					int alpha = cm.getAlpha(pixels[i * iw + j]);
					int grey = this.pixels[i * iw + j] & 0x0000ff;
					int hist = (int) c[grey];

					this.pixels[i * iw + j] = alpha << 24 | hist << 16
							| hist << 8 | hist;
				}
			}
			// 将数组中的像素产生一个图像
			ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
			mp.img = createImage(ip);
			this.type = 8;
		} else {
			JOptionPane.showMessageDialog(null, "请打开一张灰度图片！", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//直方图均衡（Color）
	public void HistogramEqualizationColor(ActionEvent e,Image img) {
		// 必须保证图像进行了加载，然后才可以进行灰度化		
		if (this.isLoad && this.isColor) {
			//先灰度化
			try {
				PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels,0, iw);
				pg.grabPixels();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
	
			int[] gray_pixels = new int[iw * ih];
			
			ColorModel cm = ColorModel.getRGBdefault();
			for (int i = 0; i < ih; i++) {
				for (int j = 0; j < iw; j++) {
					int alpha = cm.getAlpha(pixels[i * iw + j]);
					int red = cm.getRed(pixels[i * iw + j]);
					int green = cm.getGreen(pixels[i * iw + j]);
					int blue = cm.getBlue(pixels[i * iw + j]);
					
					int Y = (int) (0.2990*red + 0.5870*green + 0.1140*blue + 0);
					int Cb = (int) ((-0.1687)*red + (-0.3313)*green + (0.500)*blue + 128);
					int Cr = (int) ((0.500)*red + (-0.4187)*green + (-0.0813)*blue + 128);
	
					gray_pixels[i * iw + j] = alpha << 24 | Y << 16
							| Cb << 8 | Cr;
				}
			}
			// 获取图像的直方图
			int[] histogram = new int[256];
			for (int i = 0; i < ih; i++) {
				for (int j = 0; j < iw; j++) {
					int grey = (gray_pixels[i * iw + j] >> 16) & 0xff;
					histogram[grey]++;
				}
			}
			// 直方图均匀化处理
			double a = (double) 255 / (iw * ih);
			double[] c = new double[256];
			c[0] = a * histogram[0];
			for (int i = 1; i < 256; i++) {
				c[i] = c[i - 1] + (int) (a * histogram[i]);
			}
			for (int i = 0; i < ih; i++) {
				for (int j = 0; j < iw; j++) {
					int alpha = (gray_pixels[i * iw + j] >> 24) & 0xff;
					int grey = (gray_pixels[i * iw + j] >> 16) & 0xff;
					int temp_Cb = (gray_pixels[i * iw + j] >> 8) & 0xff;
					int temp_Cr = (gray_pixels[i * iw + j] ) & 0xff;
					int hist = (int) c[grey];
					
					gray_pixels[i * iw + j] = alpha << 24 | hist << 16
							| temp_Cb << 8 | temp_Cr;
				}
			}
			int[] result_pixels = new int[iw * ih];
			for (int i = 0; i < ih; i++) {
				for (int j = 0; j < iw; j++) {
					int alpha = (gray_pixels[i * iw + j] >> 24) & 0xff;
					int temp_Y = (gray_pixels[i * iw + j] >> 16) & 0xff;
					int temp_Cb = (gray_pixels[i * iw + j] >> 8) & 0xff;
					int temp_Cr = (gray_pixels[i * iw + j] ) & 0xff;
					
					int red = (int)(1*temp_Y + 0*(temp_Cb-128) + 1.40200*(temp_Cr-128));
					int green = (int)(1*temp_Y + (-0.34414)*(temp_Cb-128) + (-0.71414)*(temp_Cr-128));
					int blue = (int)(1*temp_Y + 1.77200*(temp_Cb-128) + 0*(temp_Cr-128));
					
					this.pixels[i * iw + j] = alpha << 24 | red << 16
							| green << 8 | blue;
				}
			}
			// 将数组中的像素产生一个图像
			ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
			mp.img = createImage(ip);
			this.type = 24;
		} else {
			JOptionPane.showMessageDialog(null, "请打开一张彩色图片！", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//均匀量化
	public void UniformScaleQuantization(ActionEvent e, Image img,int ratio_value) {
		if (this.isLoad && this.isGrey) {
			try {
				PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels,0, iw);
				pg.grabPixels();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			ColorModel cm = ColorModel.getRGBdefault();
			for (int i = 0; i < ih; i++) {
				for (int j = 0; j < iw; j++) {
					int alpha = cm.getAlpha(pixels[i * iw + j]);
					int grey = this.pixels[i * iw + j] & 0x0000ff;
					int shift = 8-ratio_value;
					int temp = 0xff<<shift;
					int result = grey & temp;
					this.pixels[i * iw + j] = alpha << 24 | result << 16 | result << 8 | result;
				}
			}
	
			// 将数组中的像素产生一个图像
			ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
			mp.img = createImage(ip);
			this.type = 8;
		} else {
			JOptionPane.showMessageDialog(null, "请打开一张灰度图片！", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//均匀量化IGS
	public void UniformScaleQuantizationIGS(ActionEvent e, Image img) {
		if (this.isLoad && this.isGrey) {
			try {
				PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels,0, iw);
				pg.grabPixels();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			int sum = 0x00000000;
			int result;
			int temp;
			int re;
			int random;
			Random r = new Random();
			ColorModel cm = ColorModel.getRGBdefault();
			for (int i = 0; i < ih; i++) {
				for (int j = 0; j < iw; j++) {
					int alpha = cm.getAlpha(pixels[i * iw + j]);
					int grey = this.pixels[i * iw + j] & 0x0000ff;
					if (grey>=240) {
						result = grey;
						temp = (result<<4)&0xff;
						sum = (temp>>4)&0xff;
					} else {
						result = grey + sum;
						temp = (result<<4)&0xff;
						sum = (temp>>4)&0xff;
					}	
					result = result & 0xf0;
					random =r.nextInt(16);
					result = random + result;
					this.pixels[i * iw + j] = alpha << 24 | result << 16 | result << 8 | result;
				}
			}
	
			// 将数组中的像素产生一个图像
			ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
			mp.img = createImage(ip);
			this.type = 8;
		} else {
			JOptionPane.showMessageDialog(null, "请打开一张灰度图片！", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	//DCT变换
	public void DCTTransform(ActionEvent e,Image img,int blockRank) {
		if (this.isLoad && this.isGrey) {
			try {
			PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels,0, iw);
			pg.grabPixels();
				} catch (Exception exception) {
			exception.printStackTrace();
			}
			ColorModel cm = ColorModel.getRGBdefault();
			int molW=blockRank-iw%blockRank;
			int molH=blockRank-ih%blockRank;
			int [][]grey=new int[iw+molW][ih+molH];
			for(int i=0;i<iw;i++) {
				for(int j=0;j<ih;j++) {
					grey[i][j] = this.pixels[j * iw + i] & 0x0000ff;
				}
			}
			int [][]DCT=new int[blockRank][blockRank];
			for(int i=0;i<iw+molW;i=i+blockRank) {
				for(int j=0;j<ih+molH;j=j+blockRank) {
					for(int x=0;x<blockRank;x++) {
						for(int y=0;y<blockRank;y++) {
							DCT[x][y]=grey[i+x][j+y];
						}
					}
				
				DCT=DCTtrans(DCT,blockRank);
					for(int m=0;m<blockRank;m++) {
					for(int n=0;n<blockRank;n++) {
						grey[i+m][j+n]=DCT[m][n];
						}
				}
				}
			}
			
			this.DCT_grey=grey;
			
			for(int i=0;i<iw;i++) {
				for(int j=0;j<ih;j++) {
					int alpha=(this.pixels[i * iw + j] >> 24) & 0xff;
					this.pixels[j * iw + i] = alpha << 24 | grey[i][j]<< 16 | grey[i][j] << 8 | grey[i][j];
				}
			}
			ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
			mp.img = createImage(ip);
			this.isDCT = true;
			this.type = 8;
		}
		else {
			JOptionPane.showMessageDialog(null, "请打开一张灰度图片！", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//DCT变换,去掉50%
	public void DCTTransform_50(ActionEvent e,Image img,int blockRank) {
		if (this.isLoad && this.isGrey) {
			try {
			PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels,0, iw);
			pg.grabPixels();
				} catch (Exception exception) {
			exception.printStackTrace();
			}
			ColorModel cm = ColorModel.getRGBdefault();
			int molW=blockRank-iw%blockRank;
			int molH=blockRank-ih%blockRank;
			int [][]grey=new int[iw+molW][ih+molH];
			for(int i=0;i<iw;i++) {
				for(int j=0;j<ih;j++) {
					grey[i][j] = this.pixels[j * iw + i] & 0x0000ff;
				}
			}
			int [][]DCT=new int[blockRank][blockRank];
			for(int i=0;i<iw+molW;i=i+blockRank) {
				for(int j=0;j<ih+molH;j=j+blockRank) {
					for(int x=0;x<blockRank;x++) {
						for(int y=0;y<blockRank;y++) {
							DCT[x][y]=grey[i+x][j+y];
						}
					}
				
				DCT=DCTtrans(DCT,blockRank);
					for(int m=0;m<blockRank;m++) {
					for(int n=0;n<blockRank;n++) {
						if (m+n>blockRank) {
							grey[i+m][j+n]=0;
						}else {
							grey[i+m][j+n]=DCT[m][n];
						}
						
					}
				}
				}
			}
			
			this.DCT_grey=grey;
			
			for(int i=0;i<iw;i++) {
				for(int j=0;j<ih;j++) {
					int alpha=(this.pixels[i * iw + j] >> 24) & 0xff;
					this.pixels[j * iw + i] = alpha << 24 | grey[i][j]<< 16 | grey[i][j] << 8 | grey[i][j];
				}
			}
			ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
			mp.img = createImage(ip);
			this.isDCT = true;
			this.type = 8;
		}
		else {
			JOptionPane.showMessageDialog(null, "请打开一张灰度图片！", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//反DCT变换
	public void DCTInverseTransform(ActionEvent e,Image img,int blockRank) {
		if (this.isLoad && this.isGrey) {
			if (this.isDCT) {
				try {
					PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels,0, iw);
					pg.grabPixels();
						} catch (Exception exception) {
					exception.printStackTrace();
					}
					ColorModel cm = ColorModel.getRGBdefault();
					int molW=blockRank-iw%blockRank;
					int molH=blockRank-ih%blockRank;
					int [][]grey=this.DCT_grey;
					
					
					int [][]DCT=new int[blockRank][blockRank];
					for(int i=0;i<iw+molW;i=i+blockRank) {
						for(int j=0;j<ih+molH;j=j+blockRank) {
							for(int x=0;x<blockRank;x++) {
								for(int y=0;y<blockRank;y++) {
									DCT[x][y]=grey[i+x][j+y];
								}
							}
						
						DCT=IDCInverseTtrans(DCT,blockRank);
						for(int x=0;x<blockRank;x++) {
							for(int y=0;y<blockRank;y++) {
								grey[i+x][j+y]=DCT[x][y];
								}
						}
						}
					}
					
					
					for(int i=0;i<iw;i++) {
						for(int j=0;j<ih;j++) {
							int alpha=(this.pixels[i * iw + j] >> 24) & 0xff;
							this.pixels[j * iw + i] = alpha << 24 | grey[i][j]<< 16 | grey[i][j] << 8 | grey[i][j];
						}
					}
					ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
					mp.img = createImage(ip);
					this.isDCT = false;
					this.type = 8;
			}else {
				JOptionPane.showMessageDialog(null, "请先进行DCT变换！", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "请打开一张灰度图片！", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//分块DCT
	public int[][] DCTtrans(int matrix[][],int blockRank) {
		int N=blockRank;
		int output[][] = new int[N][N];
        double temp[][] = new double[N][N];
        double temp1;
        int i;
        int j;
        int k;
        /**
         * Cosine matrix. N * N.
         */
        double c[][]        = new double[N][N];

        /**
         * Transformed cosine matrix, N*N.
         */
        double cT[][]       = new double[N][N];
        for (j = 0; j < N; j++)
        {
            double nn = (double)(N);
            c[0][j]  = 1.0 / Math.sqrt(nn);
            cT[j][0] = c[0][j];
        }

        for (i = 1; i < N; i++)
        {
            for (j = 0; j < N; j++)
            {
                double jj = (double)j;
                double ii = (double)i;
                c[i][j]  = Math.sqrt(2.0/8.0) * Math.cos(((2.0 * jj + 1.0) * ii * Math.PI) / (2.0 * 8.0));
                cT[j][i] = c[i][j];
            }
        }

        for (i = 0; i < N; i++)
        {
            for (j = 0; j < N; j++)
            {
                temp[i][j] = 0.0;
                for (k = 0; k < N; k++)
                {
                    temp[i][j] += (((int)(matrix[i][k]) - 128) * cT[k][j]);
                }
            }
        }

        for (i = 0; i < N; i++)
        {
            for (j = 0; j < N; j++)
            {
                temp1 = 0.0;

                for (k = 0; k < N; k++)
                {
                    temp1 += (c[i][k] * temp[k][j]);
                }

                output[i][j] = (int)Math.round(temp1);
            }
        }

        return output;
	}
	
	//分块反DCT
	public int[][] IDCInverseTtrans(int matrix[][],int blockRank) {
		int N=blockRank;
		int output[][] = new int[N][N];
        double temp[][] = new double[N][N];
        double temp1;
        int i;
        int j;
        int k;
        /**
         * Cosine matrix. N * N.
         */
        double c[][]        = new double[N][N];

        /**
         * Transformed cosine matrix, N*N.
         */
        double cT[][]       = new double[N][N];
        for (j = 0; j < N; j++)
        {
            double nn = (double)(N);
            c[0][j]  = 1.0 / Math.sqrt(nn);
            cT[j][0] = c[0][j];
        }

        for (i = 1; i < N; i++)
        {
            for (j = 0; j < N; j++)
            {
                double jj = (double)j;
                double ii = (double)i;
                c[i][j]  = Math.sqrt(2.0/8.0) * Math.cos(((2.0 * jj + 1.0) * ii * Math.PI) / (2.0 * 8.0));
                cT[j][i] = c[i][j];
            }
        }

        for (i=0; i<N; i++)
        {
            for (j=0; j<N; j++)
            {
                temp[i][j] = 0.0;

                for (k=0; k<N; k++)
                {
                    temp[i][j] += matrix[i][k] * c[k][j];
                }
            }
        }

        for (i=0; i<N; i++)
        {
            for (j=0; j<N; j++)
            {
                temp1 = 0.0;

                for (k=0; k<N; k++)
                {
                    temp1 += cT[i][k] * temp[k][j];
                }

                temp1 += 128.0;

                if (temp1 < 0)
                {
                    output[i][j] = 0;
                }
                else if (temp1 > 255)
                {
                    output[i][j] = 255;
                }
                else
                {
                     output[i][j] = (int)Math.round(temp1);
                }
            }
        }
        return output;
	}
}

class BMPPanel extends JPanel {

	public Image img;

	public BMPPanel() {
	}

	public BMPPanel(Image img) {
		this.img = img;
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this);
	}
}
