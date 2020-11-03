package com.cg.employeepayroll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;
import org.junit.Assert;
import org.junit.Test;

public class NIOFileAPITest {
	private static String HOME = System.getProperty("user.home");
	private static String PLAY_WITH_NIO = "TempPlayGround";

	@Test
	public void givenPathWhenCheckedThenConfirm() throws IOException {
		// check if file exits
		Path homePath = Paths.get(HOME);
		Assert.assertTrue(Files.exists(homePath));

		// delete file and check file does not exists
		Path playPath = Paths.get(HOME + "/" + PLAY_WITH_NIO);
		if (Files.exists(playPath)) {
			FileUtils.deleteFiles(playPath.toFile());
		}
		Assert.assertTrue(Files.notExists(playPath));

		// create directory
		Files.createDirectory(playPath);
		Assert.assertTrue(Files.exists(playPath));

		// create file
		IntStream.range(1, 10).forEach(cntr -> {
			Path tempFile = Paths.get(playPath + "/temp" + cntr);
			Assert.assertTrue(Files.notExists(tempFile));
			try {
				Files.createFile(tempFile);
			} catch (IOException e) {
			}
			Assert.assertTrue(Files.exists(tempFile));
		});
		Files.list(playPath).filter(Files::isRegularFile).forEach(System.out::println);
		Files.newDirectoryStream(playPath).forEach(System.out::println);
		Files.newDirectoryStream(playPath, path -> path.toFile().isFile() && path.toString().startsWith("temp"))
				.forEach(System.out::println);
	}

	@Test
	public void givenADirectoryWhenWatchedListsAllTheActivities() throws IOException, InterruptedException {
		Path dir = Paths.get(HOME + "/" + PLAY_WITH_NIO);
		Files.list(dir).filter(Files::isRegularFile).forEach(System.out::println);
		new java8WatchServiceExample(dir).processEvents();
	}
}
