package com.boluozhai.snowflake.tool.projectmanager.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import com.boluozhai.snowflake.tool.projectmanager.model.ProjectInfo;
import com.boluozhai.snowflake.tool.projectmanager.model.WorkspaceContext;

public class ProjectNameNormalizeController {

	private final WorkspaceContext _working_context;

	public ProjectNameNormalizeController(WorkspaceContext wc) {
		this._working_context = wc;
	}

	public void exe() {
		List<ProjectInfo> projects = this._working_context.getProjects();
		for (ProjectInfo info : projects) {
			this.normalize(info);
		}
	}

	final String debug_name = "snowflake-lib-template";

	private void normalize_pom(ProjectInfo info, File file)
			throws SAXException, IOException, ParserConfigurationException {

		final String name2 = info.getName();

		Document doc = this.load(file);
		Element root = doc.getDocumentElement();
		Element tag_artifactId = (Element) root.getElementsByTagName(
				"artifactId").item(0);
		Element tag_name = (Element) root.getElementsByTagName("name").item(0);
		Element tag_finalName = (Element) root
				.getElementsByTagName("finalName").item(0);

		int cnt = 0;
		cnt += this.make_element_text_content_as(tag_artifactId, name2);
		cnt += this.make_element_text_content_as(tag_name, name2);
		cnt += this.make_element_text_content_as(tag_finalName, name2);

		if (cnt > 0) {
			this.save(file, doc);
		}

	}

	private int make_element_text_content_as(Element element, String text) {
		final String t1 = element.getTextContent();
		if (text.equals(t1)) {
			return 0;
		} else {
			element.setTextContent(text);
			return 1;
		}
	}

	private void normalize_eclipse(ProjectInfo info, File file)
			throws SAXException, IOException, ParserConfigurationException {

		Document doc = this.load(file);
		Element root = doc.getDocumentElement();
		Element tag_name = (Element) root.getElementsByTagName("name").item(0);

		String name1 = tag_name.getTextContent();
		String name2 = info.getName();

		if (!name1.equals(name2)) {
			// System.out.format("modify '%s' to '%s'\n", name1, name2);
			tag_name.setTextContent(name2);
			this.save(file, doc);
		}

	}

	private Document load(File file) throws SAXException, IOException,
			ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(file);
		return doc;
	}

	private void save(File file, Document doc) {

		System.out.println("write DOM to " + file);

		DOMImplementation impl = doc.getImplementation();
		DOMImplementationLS ls = (DOMImplementationLS) impl.getFeature("LS",
				"3.0");
		LSSerializer ser = ls.createLSSerializer();
		String uri = file.toURI().toString();
		ser.writeToURI(doc, uri);

	}

	private void normalize(ProjectInfo info) {

		File eclipse = info.getEclipseProjectFile();
		File pom = info.getPomXmlFile();

		try {
			this.normalize_pom(info, pom);
			this.normalize_eclipse(info, eclipse);

		} catch (SAXException e) {
			throw new RuntimeException(e);

		} catch (IOException e) {
			throw new RuntimeException(e);

		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);

		} finally {

		}

	}

}
