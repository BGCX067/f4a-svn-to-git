package org.f4a.annotation.processing.html;

import org.f4a.utils.StringUtils;

public class HTMLTag {
	public int order;
	public String id;
	public String name;

	public String tagName;
	public StringBuilder attributes = new StringBuilder();
	public boolean hasLabel;
	public String javaType;

	public HTMLTag(int order, String id, String name, String tagName, String javaType, StringBuilder attributes, boolean hasLabel) {
		super();
		this.order = order;
		this.id = id;
		this.name = name;
		this.tagName = tagName;
		this.javaType = javaType;
		this.attributes = attributes;
		this.hasLabel =hasLabel;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public StringBuilder getAttributes() {
		return attributes;
	}

	public void setAttributes(StringBuilder attributes) {
		this.attributes = attributes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void writeTo(StringBuilder out) {
		out.append("<").append(tagName);
		if (!StringUtils.isNullOrEmpty(name)) {
			out.append(" name=\"").append(name).append("\"");
		}
		if (!StringUtils.isNullOrEmpty(id)) {
			out.append(" id=\"").append(id).append("\"");
		}
		if (attributes.length() > 0) {
			out.append(" ").append(attributes);
		}
		out.append("></").append(tagName).append(">");
	}

	public boolean hasLabel() {
		return hasLabel;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public boolean isNumberType() {
		return "Integer".equals(this.javaType) || "Double".equals(this.javaType);
	}
	
	
}
