package org.f4a.annotation.processing.html;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;

import org.f4a.annotation.processing.json.JSONElement;
import org.f4a.annotation.processing.html.HTMLTag;
import org.f4a.ioc.Injector;
import org.f4a.utils.ClassUtils;
import org.f4a.utils.StringUtils;

@SupportedAnnotationTypes("org.f4a.annotation.processing.html.*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HTMLProcessor extends AbstractProcessor {
	public HTMLProcessor() {
	}

	private Filer filer;
	private Messager messager;
	private Types types;

	@Override
	public void init(ProcessingEnvironment env) {
		filer = env.getFiler();
		messager = env.getMessager();
		types = env.getTypeUtils();
	}

	@Override
	public boolean process(Set<? extends TypeElement> input, RoundEnvironment roundEnvironment) {
		Set<? extends Element> elements = roundEnvironment.getRootElements();
		if (elements.size() <= 0) {
			// not interested
			return false;
		}

		int count = 0;
		long t = System.currentTimeMillis();
		for (Element element : elements) {
			if (handleAnnotatedElement(element)) {
				count++;
			}
		}
		long te = System.currentTimeMillis() - t;
		messager.printMessage(Diagnostic.Kind.NOTE, String.format("Java2HTML processed %d file%s in %d ms (generated %d file%s) ...", elements.size(), StringUtils.getPlural(elements.size()), te, count, StringUtils.getPlural(count)));

		return true;
	}



	private boolean handleAnnotatedElement(Element element) {
		try {
			Annotation topAnnotation = element.getAnnotation(HTMLElement.class);
			if (topAnnotation == null) {
				// not interested
				return false;
			}
			String name = element.getSimpleName().toString();
			List<? extends Element> subElements = element.getEnclosedElements();
			int count = subElements.size();
			if (count <= 0) {
				return false;
			}

			List<HTMLTag> tags = new ArrayList<HTMLTag>();
			for (Element e : subElements) {
				extractTag(e, tags);
			}

			StringBuilder out = new StringBuilder();
			out.append("<form><table>");
			for (HTMLTag tag : tags) {
				out.append("<tr><td>");
				if (tag.hasLabel()) {
					out.append("<label for=\"").append(tag.id).append("\"/>").append(tag.name).append("</td><td>");
				}
				tag.writeTo(out);
				out.append("</td></tr>");
			}
			out.append("</table></form>");
			writeAsFile(name, out);

			messager.printMessage(Diagnostic.Kind.NOTE, String.format("Generated %s.html (%d element%s).", name, count, StringUtils.getPlural(count)), element);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private void extractTag(Element e, List<HTMLTag> tags) {
		Annotation annotation = e.getAnnotation(HTMLElement.class);
		if (annotation == null) {
			return;
		}
		HTMLElement eAnnotation = (HTMLElement) annotation;
		String tagName = eAnnotation.tagName();
		String style = eAnnotation.style();
		String varName = e.getSimpleName().toString();
		String id = eAnnotation.id();
		if (StringUtils.isNullOrEmpty(id)) {
			id = "id-"+varName;
		}
		String varType = e.asType().toString();
		boolean hasLabel = false;
		Class<?> varClass = Injector.impl().createClass(varType);
		hasLabel = ClassUtils.isSimpleClass(varClass);
		if (varClass == null) {
			varType += "?";
		}
		int order = eAnnotation.order();

		StringBuilder tagAttributes = new StringBuilder();
		if (!StringUtils.isNullOrEmpty(style)) {
			tagAttributes.append(" style=\"").append(style).append("\"");
		}
		tagAttributes.append(" java-type=\"").append(varType).append("\"");
		for (String attributePair : eAnnotation.attributes()) {
			tagAttributes.append(" ").append(attributePair);
		}
		if ("input".equals(tagName)) {
			if ("java.lang.Double".equals(varType)) {
				tagAttributes.append(" dojo-type=\"").append("Currency").append("\"");
			} else if ("java.lang.Integer".equals(varType)) {
				tagAttributes.append(" dojo-type=\"").append("Number").append("\"");
			} else if ("java.lang.Boolean".equals(varType)) {
				tagAttributes.append(" dojo-type=\"").append("Checkbox").append("\"");
			} else if (varType.startsWith("(")) {
				
			}
		}
		HTMLTag tag = new HTMLTag(order, id, varName, tagName, tagAttributes, hasLabel);
		tags.add(tag);
	}

	private void writeAsFile(String fileName, StringBuilder buffer) throws IOException {
		String htmlFileName = fileName + ".html";
		JavaFileManager.Location location = StandardLocation.SOURCE_OUTPUT;
		FileObject fo = filer.createResource(location, "", htmlFileName);
		OutputStream os = fo.openOutputStream();
		PrintWriter pw = new PrintWriter(os);

		pw.append(buffer);

		pw.flush();
		os.flush();
		pw.close();
		os.close();
	}


}
