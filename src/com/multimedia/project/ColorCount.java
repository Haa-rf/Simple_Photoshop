package com.multimedia.project;

public class ColorCount {

	private  int[] colorCount;
	int valueOfColor;
	
	public ColorCount(int[] colorData) {
		colorCount=new int[256];
		for(int i=0;i<colorData.length;i++){
			valueOfColor = colorData[i] & 0x0000ff;
			colorCount[valueOfColor] = colorCount[valueOfColor] + 1;
		}
	}
	
	public int[] getCounts() {
		return this.colorCount;
	}
}
