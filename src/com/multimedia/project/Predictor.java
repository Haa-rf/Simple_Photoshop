package com.multimedia.project;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.ColorModel;


public class Predictor {
	private int[] m_Img;
	private int m_Width;
	private int m_Height;


	
	public Predictor(int[] m_Img,int m_Width,int m_Height) {
		this.m_Img = m_Img;
		this.m_Width = m_Width;
		this.m_Height  = m_Height;
	}
	
	public int[] OnePredictor(ActionEvent e,double C) {   //系数C
		int[] m_PData = new int[m_Width * m_Height];//预测的数据
		//int[] m_Pixels=new int[this.m_Width*this.m_Height];
		int[] m_E = new int[m_Width * m_Height];    //差值
		int[] m_G_E = new int[m_Width*m_Height];
		int grey;
		int alpha;
		ColorModel cm = ColorModel.getRGBdefault();
		//通过左侧值预测

		for(int i=0;i<m_Height;i++) {
			for(int j=0;j<m_Width;j++) {
				alpha = cm.getAlpha(m_Img[i * m_Width + j]);
				grey = m_Img[i * m_Width + j] & 0x0000ff;
				if(j==0) {
					m_PData[m_Width*i+j]= grey;
				}
				else {
					m_PData[m_Width*i+j]=(int)(C*(m_PData[i * m_Width + j-1]+m_G_E[i * m_Width + j-1]));
				}
				m_G_E[i*m_Width+j] = EQuantizer(grey-m_PData[m_Width*i+j]);
				m_E[i*m_Width+j] = alpha<<24|m_G_E[i*m_Width+j]<<16|m_G_E[i*m_Width+j]<<8|m_G_E[i*m_Width+j];
			}
		}		
		return m_E;
		
	}
	
	public int[] TwoPredictor(ActionEvent e,double C1,double C2) {   //系数C
		int[] m_PData = new int[m_Width * m_Height];//预测的数据
		int[] m_E = new int[m_Width * m_Height];    //差值
		int[] m_G_E = new int[m_Width*m_Height];
		int grey;
		int alpha;
		
		ColorModel cm = ColorModel.getRGBdefault();
		//通过左侧和上方值预测
		for(int i=0;i<m_Height;i++) {
			for(int j=0;j<m_Width;j++) {
				alpha = cm.getAlpha(m_Img[i * m_Width + j]);
				grey = m_Img[i * m_Width + j] & 0x0000ff;
				if(j==0||i==0) {
					m_PData[m_Width*i+j]= grey;
				}
				else {
					m_PData[m_Width*i+j]=(int)(C1*(m_PData[i * m_Width + j-1]+m_G_E[i * m_Width + j-1])+
							C2*(m_PData[(i-1) * m_Width + j]+m_G_E[(i-1) * m_Width +j]));
				}
				m_G_E[i*m_Width+j] = EQuantizer(grey-m_PData[m_Width*i+j]);
				m_E[i*m_Width+j] = alpha<<24|m_G_E[i*m_Width+j]<<16|m_G_E[i*m_Width+j]<<8|m_G_E[i*m_Width+j];
			}
		}		
		
		return m_E;
	}
	
	public int[] ThreePredictor(ActionEvent e, double C1, double C2, double C3) {   //系数C
		int[] m_PData = new int[m_Width * m_Height];//预测的数据
		int[] m_E = new int[m_Width * m_Height];    //差值
		int[] m_G_E = new int[m_Width*m_Height];
		int grey;
		int alpha;
		
		ColorModel cm = ColorModel.getRGBdefault();
		//通过左侧和上方值预测
		for(int i=0;i<m_Height;i++) {
			for(int j=0;j<m_Width;j++) {
				alpha = cm.getAlpha(m_Img[i * m_Width + j]);
				grey = m_Img[i * m_Width + j] & 0x0000ff;
				if(j==0||i==0) {
					m_PData[m_Width*i+j]= grey;
				}
				else {
					m_PData[m_Width*i+j]=(int)(C1*(m_PData[i * m_Width + j-1]+m_G_E[i * m_Width + j-1])+
							C2*(m_PData[(i-1) * m_Width + j]+m_G_E[(i-1) * m_Width +j])+
							C3*(m_PData[(i-1) * m_Width + j-1]+m_G_E[(i-1) * m_Width +j-1]));
				}
				m_G_E[i*m_Width+j] = EQuantizer(grey-m_PData[m_Width*i+j]);
				m_E[i*m_Width+j] = alpha<<24|m_G_E[i*m_Width+j]<<16|m_G_E[i*m_Width+j]<<8|m_G_E[i*m_Width+j];
			}
		}		
		
		return m_E;
	}
	
	
	private int EQuantizer(int data) {
		if(data>255) {
			data=255;
		}
		else if(data<-255){
			data=-255;
		}
		
		data=(int)((data+255)/2);
		return data;
	}
}
