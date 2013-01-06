package net.scholagest.app.rest.object;

public class RestProperty {
	private Object value;
	private String type;
	private String displayText;
	private boolean isHtmlList;
	private boolean isHtmlGroup;
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getDisplayText() {
		return displayText;
	}
	
	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public boolean isHtmlList() {
		return isHtmlList;
	}

	public void setHtmlList(boolean isHtmlList) {
		this.isHtmlList = isHtmlList;
	}

	public boolean isHtmlGroup() {
		return isHtmlGroup;
	}

	public void setHtmlGroup(boolean isHtmlGroup) {
		this.isHtmlGroup = isHtmlGroup;
	}
}
