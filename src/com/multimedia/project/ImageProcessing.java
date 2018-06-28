package com.multimedia.project;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

/*** 
 * @description 文件处理相关操作
 * @version 1.0
*/
public class ImageProcessing {

	public static boolean isGrey = false;
	public static boolean isColor = false;
	 /**
	 * 把byte[]数组处理成int形式
	 * 
	 * @param in
	 * @param offset
	 * @return
	 */
	public static int convertByteToInt(byte[] in, int offset) {
		
		int ret = ((int) in[offset + 3] & 0xff);
		ret = (ret << 8) | ((int) in[offset + 2] & 0xff);
		ret = (ret << 8) | ((int) in[offset + 1] & 0xff);
		ret = (ret << 8) | ((int) in[offset + 0] & 0xff);
		return (ret);
	}

	/**
	 * 把byte[]数组处理成int形式（对高位数进行处理）
	 * 
	 * @param in
	 * @param offset
	 * @return
	 */
	public static int convertByteToInt32(byte[] in, int offset) {

		int ret = 0xff;
		ret = (ret << 8) | ((int) in[offset + 2] & 0xff);
		ret = (ret << 8) | ((int) in[offset + 1] & 0xff);
		ret = (ret << 8) | ((int) in[offset + 0] & 0xff);
		return (ret);
	}

	/**
	 * 把byte[]数组处理成int形式
	 * 
	 * @param in
	 * @param offset
	 * @return
	 */
	public static long convertByteToLong(byte[] in, int offset) {

		long ret = ((long) in[offset + 7] & 0xff);
		ret |= (ret << 8) | ((long) in[offset + 6] & 0xff);
		ret |= (ret << 8) | ((long) in[offset + 5] & 0xff);
		ret |= (ret << 8) | ((long) in[offset + 4] & 0xff);
		ret |= (ret << 8) | ((long) in[offset + 3] & 0xff);
		ret |= (ret << 8) | ((long) in[offset + 2] & 0xff);
		ret |= (ret << 8) | ((long) in[offset + 1] & 0xff);
		ret |= (ret << 8) | ((long) in[offset + 0] & 0xff);
		return (ret);
	}

	/**
	 * 把byte[]数组处理成int形式
	 * 
	 * @param in
	 * @param offset
	 * @return
	 */
	public static double convertByteToDouble(byte[] in, int offset) {

		long ret = convertByteToLong(in, offset);
		return (Double.longBitsToDouble(ret));
	}

	/**
	 * 把byte[]数组处理成int形式
	 * 
	 * @param in
	 * @param offset
	 * @return
	 */
	public static short convertByteToShort(byte[] in, int offset) {

		short ret = (short) ((short) in[offset + 1] & 0xff);
		ret = (short) ((ret << 8) | (short) ((short) in[offset + 0] & 0xff));
		return (ret);
	}
	
	/**
	 * BMP文件头
	 */
	 static class BitmapHeader {

		public int nsize;
		public int nbisize;
		public int nwidth;
		public int nheight;
		public int nplanes;
		public int nbitcount;
		public int ncompression;
		public int nsizeimage;
		public int nxpm;
		public int nypm;
		public int nclrused;
		public int nclrimp;

		/**
		 * 读取BMP文件
		 * 
		 * @param fs
		 * @throws IOException
		 */
		public void read(FileInputStream fs) throws IOException

		{

			final int bflen = 14; // 14 byte BMP文件头
			byte bf[] = new byte[bflen];
			fs.read(bf, 0, bflen);
			final int bilen = 40; // 40-byte 位图信息头
			byte bi[] = new byte[bilen];
			fs.read(bi, 0, bilen);

			nsize = convertByteToInt(bf, 2);// 位图文件的大小，以字节为单位 (2-5 字节 )
			nbisize = convertByteToInt(bi, 2);//?
			nwidth = convertByteToInt(bi, 4);// 位图的宽度，以像素为单位 (18-21 字节 )
			nheight = convertByteToInt(bi, 8); // 位图的高度，以像素为单位 (22-25 字节 )
			nplanes = convertByteToShort(bi, 12); // 目标设备的级别，必须为 1(26-27 字节 ) 
			nbitcount = convertByteToShort(bi, 14); // 每个像素所需的位数，必须是 1(双色),(28-29 字节) 4(16 色 ) ， 8(256 色 ) 或 24(// 真彩色 ) 之一　
			ncompression = convertByteToInt(bi, 16);// 位图压缩类型，必须是 0( 不压缩 ),(30-33 字节 ) 1(BI_RLE8 压缩类型 ) 或// 2(BI_RLE4 压缩类型 ) 之一　
			nsizeimage = convertByteToInt(bi, 20); // 位图的大小，以字节为单位 (34-37 字节 )
			nxpm = convertByteToInt(bi, 24);// 位图水平分辨率，每米像素数 (38-41 字节 )
			nypm = convertByteToInt(bi, 28);// 位图垂直分辨率，每米像素数 (42-45 字节 )
			nclrused = convertByteToInt(bi, 32);// 位图实际使用的颜色表中的颜色数 (46-49 字节 ) 
			nclrimp = convertByteToInt(bi, 36);// 位图显示过程中重要的颜色数 (50-53 字节 
		}
	}
	
	 /**
	  * 读取文件
	  * @param fs
	  * @return
	  */
	public static Image read(FileInputStream fs)
	{
		try {
			BitmapHeader bh = new BitmapHeader();
			bh.read(fs);
			if (bh.nbitcount == 32)
			{
				isColor = true;
				isGrey = false;
				return (readMap32(fs, bh));
			}
			if (bh.nbitcount == 24) {
				isColor = true;
				isGrey = false;
				return (readMap24(fs, bh));
			}
			if (bh.nbitcount == 8) {
				isColor = false;
				isGrey = true;
				return (readMap8(fs, bh));
			}	
			fs.close();
		} catch (IOException e) {
			System.out.println("Caught exception in loadbitmap!");
		}
		isColor = false;
		isGrey = false;
		return (null);
	}
	
	/**
	 * 读取32位bmp文件
	 * @param fs
	 * @param bh
	 * @return
	 * @throws IOException
	 */
	protected static Image readMap32(FileInputStream fs, BitmapHeader bh)
			throws IOException

	{
		Image image;
		//int xwidth = bh.nsizeimage / bh.nheight;
		int ndata[] = new int[bh.nheight * bh.nwidth];
		byte brgb[] = new byte[bh.nwidth * 4 * bh.nheight];
		fs.read(brgb, 0, bh.nwidth * 4 * bh.nheight);
		int nindex = 0;
		for (int j = 0; j < bh.nheight; j++)
		{
			for (int i = 0; i < bh.nwidth; i++)
			{
				ndata[bh.nwidth * (bh.nheight - j - 1) + i] = convertByteToInt32(
						brgb, nindex);
				nindex += 4;
			}
		}
		image = Toolkit.getDefaultToolkit().createImage
		(new MemoryImageSource(bh.nwidth, bh.nheight,
		ndata, 0, bh.nwidth));
		fs.close();
		return (image);
	}

	/**
	 * 读取24位bmp文件
	 * @param fs
	 * @param bh
	 * @return
	 * @throws IOException
	 */
	protected static Image readMap24(FileInputStream fs, BitmapHeader bh)
			throws IOException

	{
		Image image;
		int npad = (bh.nsizeimage / bh.nheight) - bh.nwidth * 3;
		int ndata[] = new int[bh.nheight * bh.nwidth];
		byte brgb[] = new byte[(bh.nwidth + npad) * 3 * bh.nheight];
		fs.read(brgb, 0, (bh.nwidth + npad) * 3 * bh.nheight);
		int nindex = 0;
		for (int j = 0; j < bh.nheight; j++)
		{
			for (int i = 0; i < bh.nwidth; i++)
			{
				ndata[bh.nwidth * (bh.nheight - j - 1) + i] = convertByteToInt32(
						brgb, nindex);
				nindex += 3;
			}
			nindex += npad;
		}
		image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(
				bh.nwidth, bh.nheight,ndata, 0, bh.nwidth));
		fs.close();
		return (image);
	}

	/**
	 * 读取8位bmp文件
	 * @param fs
	 * @param bh
	 * @return
	 * @throws IOException
	 */
	protected static Image readMap8(FileInputStream fs, BitmapHeader bh)
			throws IOException

	{
		Image image;

		int nNumColors = 0;

		if (bh.nclrused > 0)
		{
			nNumColors = bh.nclrused;
		}
		else
		{
			nNumColors = (1 & 0xff) << bh.nbitcount;
		}
		
		if (bh.nsizeimage == 0)
		{
			bh.nsizeimage = ((((bh.nwidth * bh.nbitcount) + 31) & ~31) >> 3);
			bh.nsizeimage *= bh.nheight;
		}
		int npalette[] = new int[nNumColors];
		byte bpalette[] = new byte[nNumColors * 4];
		fs.read(bpalette, 0, nNumColors * 4);
		int nindex8 = 0;
		
		for (int n = 0; n < nNumColors; n++)
		{
			npalette[n] = convertByteToInt32(bpalette, nindex8);
			nindex8 += 4;
		}
		int npad8 = (bh.nsizeimage / bh.nheight) - bh.nwidth;
		int ndata8[] = new int[bh.nwidth * bh.nheight];
		byte bdata[] = new byte[(bh.nwidth + npad8) * bh.nheight];
		fs.read(bdata, 0, (bh.nwidth + npad8) * bh.nheight);
		nindex8 = 0;
		
		for (int j8 = 0; j8 < bh.nheight; j8++)
		{
			for (int i8 = 0; i8 < bh.nwidth; i8++)
			{
				ndata8[bh.nwidth * (bh.nheight - j8 - 1) + i8] =
				npalette[((int) bdata[nindex8] & 0xff)];
				nindex8++;
			}
			nindex8 += npad8;
		}
		image = Toolkit.getDefaultToolkit().createImage
		(new MemoryImageSource(bh.nwidth, bh.nheight,
		ndata8, 0, bh.nwidth));
		return (image);
	}
	
	public static Image load(String sdir, String sfile) {

		return (load(sdir + sfile));

	}
	public static Image load(String sdir)
	{
		try
		{
			FileInputStream fs = new FileInputStream(sdir);
			return (read(fs));
		}
		catch (IOException ex) {
			return (null);
		}
	}
	
	public static Image Write(int width,int height,Image image, String file,int type ) throws java.io.IOException {
	    try {
	      /**
	       * ceate graphic
	       */
	    	BufferedImage bi = null;
	    switch (type) {
		case 1:
			bi = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_BINARY);
			break;
		case 8:
			bi = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
			break;
		case 24:
			bi = new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
			break;
		default:
			break;
		}
	      
	      Graphics g = bi.getGraphics();
	      g.drawImage(image, 0, 0, null);
	      /*  open file */
	      File iFile= new File(file+".bmp");
	      ImageIO.write(bi, "bmp", iFile);
	      } catch (IOException e) {
	        return null;
	      }
	     return image;
	  }
}
