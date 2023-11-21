package helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WriteToFile {
	private final String outputDir;
	private final String mimeType;
	private final String fileName;
	private final String content;

	public WriteToFile(WriteToFileBuilder builder) { 
		this.outputDir = builder.outputDir;
		this.mimeType = builder.mimeType;
		this.fileName = builder.fileName;
		this.content = builder.content;
	}
	
	public void write() throws IOException {		
		Path dir = Paths.get(outputDir);
		try {
			if (!Files.isDirectory(dir)) {
				Files.createDirectory(dir);
			}
		} catch (IOException e) {
			System.out.println("Cannot create direcotry: " + e.getMessage());
		}
		Path file = Paths.get(dir.getFileName().toString(), fileName);
		try {
			Files.write(file, content.getBytes());			
		} catch (IOException e) {
			System.out.println("Cannot write to file: " + e.getMessage());
		}
	}

	public String getOutputDir() {
		return outputDir;
	}
	
	public String getMimeType() {
		return mimeType;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getContent() {
		return content;
	}

	public static final class WriteToFileBuilder {
		private String outputDir;
		private String mimeType;
		private String fileName;
		private String content;
		
		public WriteToFileBuilder() { }
		
		public WriteToFileBuilder outputDir(String outputDir) {
			this.outputDir = outputDir;
			return this;
		}
		
		public WriteToFileBuilder mimeType(String mimeType) {
			this.mimeType = mimeType;
			return this;
		}
		
		public WriteToFileBuilder fileName(String fileName) {
			this.fileName = fileName;
			return this;
		}
		
		public WriteToFileBuilder content(String content) {
			this.content = content;
			return this;
		}
		
		public WriteToFile build() {
			return new WriteToFile(this);
		}
	}
}
