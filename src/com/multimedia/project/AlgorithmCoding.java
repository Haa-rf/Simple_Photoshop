package com.multimedia.project;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;

//算术编码类
public class AlgorithmCoding {
	private int colorData;
	private int[] EData;
	private int[] colorCount;
	private double[] p;  //各灰度值粘的比例
	private double[] lowOfData; //每个数据的概率区间下界
	private double[] upOfData;  //每个数据概率区间上界
	private BigDecimal low;
	private BigDecimal high;
	private BigDecimal temp;
	private BigDecimal temp1;
	private BigDecimal Range;
	
	public AlgorithmCoding(int[] EData) {
		//System.arraycopy(EData, 0, this.EData, 0, EData.length);
		this.EData = EData;
		this.colorCount = new int[256];
		System.arraycopy(new ColorCount(EData).getCounts(), 0, this.colorCount, 0, 256);
	}
	
	public BigDecimal ACoding(ActionEvent e) {
		
		double L = 0;
		double H = 1;
		low = new BigDecimal(L);
		high = new BigDecimal(H);
		
		IntervalCul();
		for(int i=0;i<EData.length;i++) {
			colorData = EData[i] & 0x0000ff;
		
			Range = new BigDecimal(L);       //初始化区间大小为零
			
			//用high-low得到区间大小
			Range=Range.add(high);
			Range=Range.subtract(low);
			//temp用来计算下界增加的值
			temp = new BigDecimal(Double.toString(lowOfData[colorData]));
			
			temp=temp.multiply(Range);
			low=low.add(temp);
			
			//temp1用来计算新上界与旧的下界的差值
			temp1 = new BigDecimal(Double.toString(upOfData[colorData]));
			temp1=temp1.multiply(Range);
			high=high.subtract(Range);
			high=high.add(temp1);
			
		}
		
		
		return low;
	}
	
	private void IntervalCul() {
		lowOfData = new double[256];
		upOfData = new double[256];
		p = new double[256];
		for(int i=0;i<256;i++) {
			p[i] = (double)colorCount[i]/(double)EData.length;
			if(i==0) {
				lowOfData[i] = 0;
				upOfData[i] = p[i];
			}
			else {
				lowOfData[i] = upOfData[i-1];
				upOfData[i] = lowOfData[i] + p[i];
			}
		}
	}
	
}
