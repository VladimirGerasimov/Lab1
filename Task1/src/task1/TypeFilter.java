package task1;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class TypeFilter extends FileFilter{

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()){
			return false;
		}

		String s = f.getName();

		return s.endsWith(".gerdb");
	}
	
	@Override
	public String getDescription() {
		return ".gerdb";
	}

}
