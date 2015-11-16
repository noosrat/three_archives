package downloads;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import downloads.Download;

public class DownloadTest {
	
	@Test
	public void addToCartTest() {
		Download test= new  Download();
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<String> orig = new ArrayList<String>();
	
		orig.add("ms:4");
		orig.add("ms:6");
		
		temp.add("ms:4");
		temp.add("ms:6");
		temp.add("ms:1");
		temp.add("ms:2");
		temp.add("ms:3");
		assertEquals(temp,test.addToCart("ms:1,ms:2,ms:3", orig));
	}
}
