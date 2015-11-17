package Upload;

import static org.junit.Assert.*;

import org.junit.Test;

import uploads.UploadService;

public class UploadServiceTest {

	@Test
	public void getPIDTest() {
		UploadService service= new UploadService();
		assertEquals("ms:1",service.getPIDT("snaps", "changeme:1"));
		assertEquals("hv:1",service.getPIDT("harfield_village", "changeme:1"));
		assertEquals("sss:1",service.getPIDT("spring_queen", "changeme:1"));
		assertEquals("sss:1",service.getPIDT("miss_gay", "changeme:1"));
		
	}

}
