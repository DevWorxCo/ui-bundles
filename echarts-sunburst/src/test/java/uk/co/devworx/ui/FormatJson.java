package uk.co.devworx.ui;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FormatJson
{
	@Test
	public void formatJson() throws Exception
	{
		ObjectMapper objectMapper = new ObjectMapper().configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
		JsonNode jsonNode = objectMapper.readTree(Paths.get("/home/js/.config/JetBrains/IntelliJIdea2021.2/scratches/treeoflife.json").toFile());
		BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get("/home/js/.config/JetBrains/IntelliJIdea2021.2/scratches/treeoflife-formatted.json"));
		objectMapper.writerWithDefaultPrettyPrinter().writeValue(bufferedWriter, jsonNode);


	}



}
