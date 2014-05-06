package net.scholagest.app.utils;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class HtmlPageBuilder {
	private String attributeName = null;
	private String attributeValue = null;
	
	public HtmlPageBuilder(String attributeName, String attributeValue) {
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	}
	
	public String inject(String basePage, List<String> toAdd) {
		try {
			return buildPage(basePage, toAdd);
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	private String buildPage(String basePage, List<String> toAdd) throws ParserException {
		Parser htmlParser = new Parser(basePage);
		
		NodeList root = htmlParser.parse(null);
		NodeList nodeList = root.extractAllNodesThatMatch(new HasAttributeFilter(attributeName, attributeValue), true);
		if (nodeList.size() > 0) {
			Node node = nodeList.elementAt(0);
			
			NodeList children = node.getChildren();
			if (children == null) {
				children = new NodeList();
				node.setChildren(children);
			}
			List<Node> convertedNodes = stringToNodes(toAdd);
			addListToNodeList(children, convertedNodes);
			node.setChildren(children);
		}
		
		return root.toHtml();
	}
	
	private NodeList addListToNodeList(NodeList destination, List<Node> nodes) {
		for (Node node : nodes) {
			destination.add(node);
		}
		
		return destination;
	}
	
	private List<Node> stringToNodes(List<String> nodesAsString) {
		List<Node> nodes = new ArrayList<>();
		
		for (String s : nodesAsString) {
			try {
				Node node = parseNode(s);
				nodes.add(node);
			} catch (ParserException e) {
				e.printStackTrace();
			}
		}
		
		return nodes;
	}
	
	private Node parseNode(String nodeAsString) throws ParserException {
		Parser nodeParser = new Parser(nodeAsString);
		return nodeParser.parse(null).elementAt(0);
	}
}
