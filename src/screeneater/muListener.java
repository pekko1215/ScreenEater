package screeneater;

import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class muListener implements MouseListener{
	  public void mouseClicked(MouseEvent e){
		  if(e.isShiftDown()){
			  System.exit(0);
		  }else{
			ScreenEater.celllist.add(MouseInfo.getPointerInfo().getLocation());
		  }
	    /* 処理したい内容をここに記述する */
	  }

	  public void mouseEntered(MouseEvent e){
	    /* 処理したい内容をここに記述する */
	  }

	  public void mouseExited(MouseEvent e){
	    /* 処理したい内容をここに記述する */
	  }

	  public void mousePressed(MouseEvent e){
	    /* 処理したい内容をここに記述する */
	  }

	  public void mouseReleased(MouseEvent e){
	    /* 処理したい内容をここに記述する */
	  }
}