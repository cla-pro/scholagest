package net.scholagest.rest.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.scholagest.app.utils.HtmlPageBuilder;

import org.junit.Test;

public class HtmlPageBuilderTest {
	public String readFile(String fileName) {
		BufferedReader buf = null;
		
		String result = "";
		try {
			buf = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + fileName)));
			result = readStream(buf);
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				buf.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return result;
	}
	
	private String readStream(BufferedReader buf) throws IOException {
		StringBuilder builder = new StringBuilder();
		
		String line = buf.readLine();
		while (line != null) {
			builder.append(line);
			line = buf.readLine();
		}
		
		return builder.toString();
	}
	
	@Test
	public void testInjection() {
		HtmlPageBuilder builder = new HtmlPageBuilder("id", "idToDetect");
		List<String> toAdd = new ArrayList<>();
		toAdd.add("<div></div>");
		String result = builder.inject("<div id=\"idToDetect\"></div>", toAdd);
		
		assertEquals("<div id=\"idToDetect\"><div></div></div>", result);
		System.out.println(result);
	}
}
