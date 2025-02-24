/*******************************************************************************
 * Copyright (c) 2010, 2019 VectorGraphics2D project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Erich Seifert - initial API and implementation
 * Michael Seifert - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.vectorgraphics2d.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.swtchart.vectorgraphics2d.core.TestUtils.XMLFragment;
import org.eclipse.swtchart.vectorgraphics2d.core.TestUtils.XMLFragment.FragmentType;
import org.junit.Test;

public class TestUtilsTest {

	@Test
	public void testParseXmlStartTag() throws Exception {

		String xmlTagName = "foo:bar.baz_tag";
		String xmlString;
		XMLFragment frag;
		xmlString = "<" + xmlTagName + ">";
		frag = XMLFragment.parse(xmlString, 0);
		assertEquals(xmlTagName, frag.name);
		assertEquals(FragmentType.START_TAG, frag.type);
		assertTrue(frag.attributes.isEmpty());
		assertEquals(0, frag.matchStart);
		assertEquals(xmlString.length(), frag.matchEnd);
		xmlString = "< " + xmlTagName + "  >";
		frag = XMLFragment.parse(xmlString, 0);
		assertEquals(xmlTagName, frag.name);
		assertEquals(FragmentType.START_TAG, frag.type);
		assertTrue(frag.attributes.isEmpty());
		assertEquals(0, frag.matchStart);
		assertEquals(xmlString.length(), frag.matchEnd);
	}

	@Test
	public void testParseXmlEndTag() throws Exception {

		String xmlTagName = "foo:bar.baz_tag";
		String xmlString;
		XMLFragment frag;
		xmlString = "</" + xmlTagName + ">";
		frag = XMLFragment.parse(xmlString, 0);
		assertEquals(xmlTagName, frag.name);
		assertEquals(FragmentType.END_TAG, frag.type);
		assertTrue(frag.attributes.isEmpty());
		assertEquals(0, frag.matchStart);
		assertEquals(xmlString.length(), frag.matchEnd);
		xmlString = "</ " + xmlTagName + "  >";
		frag = XMLFragment.parse(xmlString, 0);
		assertEquals(xmlTagName, frag.name);
		assertEquals(FragmentType.END_TAG, frag.type);
		assertTrue(frag.attributes.isEmpty());
		assertEquals(0, frag.matchStart);
		assertEquals(xmlString.length(), frag.matchEnd);
	}

	@Test
	public void testParseXmlEmptyElement() throws Exception {

		String xmlTagName = "foo:bar.baz_tag";
		String xmlString;
		XMLFragment frag;
		xmlString = "<" + xmlTagName + "/>";
		frag = XMLFragment.parse(xmlString, 0);
		assertEquals(xmlTagName, frag.name);
		assertEquals(FragmentType.EMPTY_ELEMENT, frag.type);
		assertTrue(frag.attributes.isEmpty());
		assertEquals(0, frag.matchStart);
		assertEquals(xmlString.length(), frag.matchEnd);
		xmlString = "< " + xmlTagName + "  />";
		frag = XMLFragment.parse(xmlString, 0);
		assertEquals(xmlTagName, frag.name);
		assertEquals(FragmentType.EMPTY_ELEMENT, frag.type);
		assertTrue(frag.attributes.isEmpty());
		assertEquals(0, frag.matchStart);
		assertEquals(xmlString.length(), frag.matchEnd);
	}

	@Test
	public void testParseXmlCDATA() throws Exception {

		String xmlString;
		XMLFragment frag;
		xmlString = "<![CDATA[foo bar]]>";
		frag = XMLFragment.parse(xmlString, 0);
		assertEquals("", frag.name);
		assertEquals(FragmentType.CDATA, frag.type);
		assertEquals("foo bar", frag.attributes.get("value"));
		assertEquals(0, frag.matchStart);
		assertEquals(xmlString.length(), frag.matchEnd);
		xmlString = "<![CDATA[<nested>foo bar</nested>]]>";
		frag = XMLFragment.parse(xmlString, 0);
		assertEquals("", frag.name);
		assertEquals(FragmentType.CDATA, frag.type);
		assertEquals("<nested>foo bar</nested>", frag.attributes.get("value"));
		assertEquals(0, frag.matchStart);
		assertEquals(xmlString.length(), frag.matchEnd);
	}

	@Test
	public void testParseXmlDeclaration() throws Exception {

		String xmlString;
		XMLFragment frag;
		xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		frag = XMLFragment.parse(xmlString, 0);
		assertEquals("xml", frag.name);
		assertEquals(FragmentType.DECLARATION, frag.type);
		assertEquals("1.0", frag.attributes.get("version"));
		assertEquals("UTF-8", frag.attributes.get("encoding"));
		assertEquals(0, frag.matchStart);
		assertEquals(xmlString.length(), frag.matchEnd);
	}

	@Test
	public void testParseXmlDoctype() throws Exception {

		String xmlString;
		XMLFragment frag;
		xmlString = "<!DOCTYPE html>";
		frag = XMLFragment.parse(xmlString, 0);
		assertEquals(FragmentType.DOCTYPE, frag.type);
		assertEquals("html", frag.attributes.get("doctype 00"));
		assertEquals(0, frag.matchStart);
		assertEquals(xmlString.length(), frag.matchEnd);
		xmlString = "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1 Tiny//EN\"\n" + "\t\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11-tiny.dtd\">";
		frag = XMLFragment.parse(xmlString, 0);
		assertEquals(FragmentType.DOCTYPE, frag.type);
		assertEquals("svg", frag.attributes.get("doctype 00"));
		assertEquals("PUBLIC", frag.attributes.get("doctype 01"));
		assertEquals("-//W3C//DTD SVG 1.1 Tiny//EN", frag.attributes.get("doctype 02"));
		assertEquals("http://www.w3.org/Graphics/SVG/1.1/DTD/svg11-tiny.dtd", frag.attributes.get("doctype 03"));
		assertEquals(0, frag.matchStart);
		assertEquals(xmlString.length(), frag.matchEnd);
	}

	@Test
	public void testParseXmlComment() throws Exception {

		String xmlString;
		XMLFragment frag;
		xmlString = "<!-- foo bar -->";
		frag = XMLFragment.parse(xmlString, 0);
		assertEquals("", frag.name);
		assertEquals(FragmentType.COMMENT, frag.type);
		assertEquals("foo bar", frag.attributes.get("value"));
		assertEquals(0, frag.matchStart);
		assertEquals(xmlString.length(), frag.matchEnd);
		xmlString = "<!-- <nested>foo bar</nested> -->";
		frag = XMLFragment.parse(xmlString, 0);
		assertEquals("", frag.name);
		assertEquals(FragmentType.COMMENT, frag.type);
		assertEquals("<nested>foo bar</nested>", frag.attributes.get("value"));
		assertEquals(0, frag.matchStart);
		assertEquals(xmlString.length(), frag.matchEnd);
	}

	@Test
	public void testParseXMLAttributesTag() throws Exception {

		String xmlTagName = "foo:bar.baz_tag";
		String xmlString;
		XMLFragment frag;
		xmlString = "<" + xmlTagName + " foo='bar'>";
		frag = XMLFragment.parse(xmlString, 0);
		assertEquals(xmlTagName, frag.name);
		assertEquals("bar", frag.attributes.get("foo"));
		assertEquals(0, frag.matchStart);
		assertEquals(xmlString.length(), frag.matchEnd);
		xmlString = "<" + xmlTagName + " foo=\"bar\">";
		frag = XMLFragment.parse(xmlString, 0);
		assertEquals(xmlTagName, frag.name);
		assertEquals("bar", frag.attributes.get("foo"));
		assertEquals(0, frag.matchStart);
		assertEquals(xmlString.length(), frag.matchEnd);
		xmlString = "<" + xmlTagName + " foo=\"bar\" baz='qux'>";
		frag = XMLFragment.parse(xmlString, 0);
		assertEquals(xmlTagName, frag.name);
		assertEquals("bar", frag.attributes.get("foo"));
		assertEquals("qux", frag.attributes.get("baz"));
		assertEquals(0, frag.matchStart);
		assertEquals(xmlString.length(), frag.matchEnd);
	}
}
