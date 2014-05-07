package task1;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ImageTypeFilter extends FileFilter{
	@Override
	public boolean accept(File f) {
		
		String s = f.getName();

		return s.toLowerCase().endsWith(".png") || s.endsWith(".jpg") || f.isDirectory();
	}
	
	@Override
	public String getDescription() {
		return "Images - png, jpg";
	}

}
