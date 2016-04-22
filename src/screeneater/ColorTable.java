package screeneater;

import java.awt.Point;
import java.util.Calendar;

public class ColorTable {
	Byte tables[][];
	Point size;

	ColorTable(int x, int y){ //バイト列 ??を最適化{
		tables = new Byte[x][y/2];
		size = new Point(x, y);
		reset();
	}

	public void reset() {
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y/2; y++) {
				tables[x][y] = 0;
			}
		}
	}

	public void setTable(int x, int y, int p) {
		if(y%2==0){
			tables[x][y/2] = (byte) ((byte) (0x0000000f & (p))+(tables[x][y/2] & 0x000000f0));
		}else{
			tables[x][y/2] = (byte) ((byte) ((0x0000000f & (p)) * 0x00000010)+(tables[x][y/2] & 0x0000000f));
		}
	}

	public Byte getTable(int x, int y) {
		return (byte) (y%2==0 ? tables[x][y/2] & 0x0000000f : (tables[x][y/2] & 0x000000f0) / 0x00000010);
	}

	public void setTable(Point point, int p) {
		setTable(point.x,point.y,p);
	}

	public Byte getTable(Point point) {
		return getTable(point.x,point.y);
	}
	
	public String toString(){
		String tmps = "";
		Calendar now = Calendar.getInstance();
		int oldTime = now.get(Calendar.SECOND);
		int nowTime = oldTime;
		for(int x=0;x<size.x;x++){
			for(int y=0;y<size.y/2;y++){
				String tmps2 = "";
				tmps2 += "" + (tables[x][y/2] & 0x000000ff) + "";
				tmps+= tmps2;
				now = Calendar.getInstance();
				nowTime = now.get(Calendar.SECOND);
				//System.out.println("" + nowTime + ","+oldTime);
				if(Math.abs(oldTime-nowTime)>5){
					oldTime = nowTime;
					System.out.println(System.out.printf("現在 %.2f％進行中・・・",((float)((x*size.y+y*2)*100)/(size.x*size.y))));
				}
				
			}
			tmps += "\n";
		}
		return tmps;
	}

}