package task1;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageProcessor {
	BufferedImage load;
	ImageIcon icon;
	String ext;
	String stringedImage;
	public ImageProcessor(File f){
		load = null;
		try {  
			ext = f.getName().substring(f.getName().indexOf(".")+1);
			load = ImageIO.read(f);
		} catch (IOException e) {}
		returnImageIcon();
	}
	public ImageProcessor(ImageIcon i){
		icon = i;
		BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.createGraphics();
		icon.paintIcon(null, g, 0,0);
		g.dispose();
		load = bi;
		makeString();
	}
	public ImageProcessor(String si){
		stringedImage = si;
		byte[] b = MyBase64.decode(si);
		icon = new ImageIcon(b);
	}
	
	public ImageIcon returnImageIcon(){
		ImageIcon ii = new ImageIcon(load);
		icon = ii;
		return ii;
	}
	public void size(int maxDimen){
		int w = load.getWidth();
		int h = load.getHeight();
		
		float nw = 0;
		float nh = 0;
		
		if(w>h){
			nw = 200;
			nh = h*200/w;
		} else {
			nh = 200;
			nw = w*200/h;
		}
		float sw = nw/w;
		float sh = nh/h;
		BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(sw, sh);
		AffineTransformOp scaleOp =  new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(load, after);
		after = after.getSubimage( 0, 0, (int)Math.floor(nw), (int)Math.floor(nh));
		load = after;
	}
	public String makeString(){
		String a="";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(load, ext, baos);
			byte[] res=baos.toByteArray();
			a = MyBase64.encode(res);
			stringedImage = a;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return a;
	}
}
