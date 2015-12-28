package org.f4a.annotation.processing.html;

public @interface HTMLElement {

	String tagName() default "input";
	String style() default "";
	String id() default "";
	int order() default 0;
	String[] attributes();

}
