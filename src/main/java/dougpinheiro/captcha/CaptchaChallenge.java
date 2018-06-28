package dougpinheiro.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Locale;

import javax.imageio.ImageIO;

public class CaptchaChallenge {

	protected String key;
	
	public CaptchaChallenge() {
		this.key = generateChallenge();
	}
	
	public static void main(String[] args) throws IOException {
		CaptchaChallenge cc = new CaptchaChallenge();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File("d:\\tempImage.png"));
			fos.write(cc.getImage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			fos.flush();
			fos.close();
		}
		
	}
	
	
	public byte[] getImage() {
		this.key = CaptchaChallenge.generateChallenge();
		
		ClassLoader classLoader = this.getClass().getClassLoader();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			File fontFile = new File(classLoader.getResource("ASS.TTF").getFile());
			Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
			Font font2 = font.deriveFont(Font.BOLD, 40);
			
			BufferedImage bi = new BufferedImage(200, 100, BufferedImage.TYPE_INT_ARGB);
			Graphics2D b = (Graphics2D) bi.getGraphics();
			b.setFont(font2);
			b.setBackground(Color.orange);
			b.setColor(Color.white);
			b.drawRect(0, 0, 200, 100);
			b.setColor(Color.black);
			b.drawLine(0, (int)Math.round(Math.random()*60), 200, (int)Math.round(Math.random()*80));
			b.setColor(Color.red);
			b.drawLine(0, (int)Math.round(Math.random()*60), 200, (int)Math.round(Math.random()*80));
			b.setColor(Color.black);
			b.drawLine(0, (int)Math.round(Math.random()*60), 200, (int)Math.round(Math.random()*80));
			b.drawString(this.key, 31, 60);
		    ImageIO.write(bi, "png", baos); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
	
	private static String generateChallenge() {
		String key = "5gh9d6";
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			
			byte[] digest = md.digest(String.valueOf(Math.random()*Integer.MAX_VALUE).getBytes());
			Locale.setDefault(new Locale("pt", "br"));
			key = Base64.getEncoder().encodeToString(digest).substring(0, 6);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		
		return key;
	}
	
	public boolean validateToken(String token) {
		if(token.equalsIgnoreCase(this.key)) {
			return true;
		}
		return false;
	}

}
