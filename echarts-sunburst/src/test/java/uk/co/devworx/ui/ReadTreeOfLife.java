package uk.co.devworx.ui;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadTreeOfLife
{
	public static ObjectMapper objectMapper = new ObjectMapper().configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);

	public static final int max_depth = Integer.MAX_VALUE;

	public static void main(String... args) throws  Exception
	{

		JsonNode jsonNode = objectMapper.readTree(Paths.get("/home/js/.config/JetBrains/IntelliJIdea2021.2/scratches/treeoflife.json").toFile());

		Map<String, LifeNode> allLifeNodes = new HashMap<>();

		ArrayNode lifeNodes = (ArrayNode)jsonNode.get("nodes");
		int size = lifeNodes.size();

		for (int i = 0; i < size; i++)
		{
			JsonNode lfNode = lifeNodes.get(i);
			LifeNode life = new LifeNode(lfNode.get("id").asText(),lfNode.get("name").asText());
			allLifeNodes.put(life.getId(), life);
		}

		System.out.println("Got a total of : " + allLifeNodes.size());

		ArrayNode linkNodes = (ArrayNode)jsonNode.get("links");
		for (int i = 0; i < linkNodes.size(); i++)
		{
			JsonNode linkNode = linkNodes.get(i);

			if(linkNode.get("source") == null) continue;
			if(linkNode.get("target") == null) continue;

			String sourceId = linkNode.get("source").asText();
			String targetId = linkNode.get("target").asText();

			LifeNode sourceNode = allLifeNodes.get(sourceId);
			LifeNode targetNode = allLifeNodes.get(targetId);
			sourceNode.addChild(targetNode);
		}

		System.out.println("Set up all the links... now write out ");

		LifeNode rootLifeNode = allLifeNodes.get("1");
		ArrayNode arrayNode = objectMapper.createArrayNode();
		ObjectNode rootNode = objectMapper.createObjectNode();
		arrayNode.add(rootNode);

		System.out.println(rootLifeNode);

		recursivelyAddLifeTree(rootLifeNode, rootNode, 0);

		BufferedWriter bw = Files.newBufferedWriter(Paths.get("/home/js/git/devworx/ui-bundles/echarts-sunburst/src/main/webapp/tree-of-life-output.json"));
		objectMapper.writerWithDefaultPrettyPrinter().writeValue(bw, arrayNode);
	}

	private static void recursivelyAddLifeTree(final LifeNode node, final ObjectNode jsonNode, final int depth)
	{
		jsonNode.put("name",node.getName());
		int val = node.getNodeSizeValue();
		 jsonNode.put("size", val);

		List<LifeNode> children = node.getChildren();

		if(! (max_depth < (depth+1)) && children.size() > 0)
		{

			ArrayNode childrenNodes = jsonNode.putArray("children");
			for (LifeNode child : children)
			{

				ObjectNode subNode = childrenNodes.addObject();
				recursivelyAddLifeTree(child, subNode, depth + 1);
			}
		}


		jsonNode.put("value", val);
	}

	static class LifeNode
	{
		private final String id;
		private final String name;
		private final List<LifeNode> children;

		public LifeNode(String id, String name)
		{
			this.id = id;
			this.name = name;
			this.children = new ArrayList<>();
		}

		public String getId()
		{
			return id;
		}

		public String getName()
		{
			return name;
		}

		public List<LifeNode> getChildren()
		{
			return children;
		}

		public void addChild(LifeNode targetNode)
		{
			children.add(targetNode);
		}

		@Override public String toString()
		{
			return "LifeNode{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", children=" + children.size() + '}';
		}

		public int getNodeSizeValue()
		{
			AtomicInteger intVal = new AtomicInteger(1);
			for(LifeNode n : children)
			{
				recursivelyGetValue(n, intVal);
				intVal.incrementAndGet();
			}
			return intVal.intValue();
		}

		private void recursivelyGetValue(LifeNode n, AtomicInteger intVal)
		{
			for(LifeNode x : n.getChildren())
			{
				recursivelyGetValue(x, intVal);
				intVal.incrementAndGet();
			}
		}
	}



}
