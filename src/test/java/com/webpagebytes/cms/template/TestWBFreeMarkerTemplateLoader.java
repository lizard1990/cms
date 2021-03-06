package com.webpagebytes.cms.template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;

import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.easymock.EasyMock;

import com.webpagebytes.cms.WPBArticlesCache;
import com.webpagebytes.cms.WPBFilesCache;
import com.webpagebytes.cms.WPBMessagesCache;
import com.webpagebytes.cms.WPBPageModulesCache;
import com.webpagebytes.cms.WPBPagesCache;
import com.webpagebytes.cms.WPBParametersCache;
import com.webpagebytes.cms.WPBProjectCache;
import com.webpagebytes.cms.WPBUrisCache;
import com.webpagebytes.cms.cmsdata.WPBPage;
import com.webpagebytes.cms.cmsdata.WPBPageModule;
import com.webpagebytes.cms.engine.WPBCacheInstances;
import com.webpagebytes.cms.exception.WPBIOException;
import com.webpagebytes.cms.template.WPBFreeMarkerTemplateEngine;
import com.webpagebytes.cms.template.FreeMarkerTemplateLoader;
import com.webpagebytes.cms.template.FreeMarkerTemplateObject;
import com.webpagebytes.cms.template.WPBTemplateEngine;
import com.webpagebytes.cms.template.FreeMarkerTemplateObject.TemplateType;

@RunWith(PowerMockRunner.class)
public class TestWBFreeMarkerTemplateLoader {

private WPBUrisCache wbUriCacheMock;
private WPBPagesCache wbWebPageCacheMock;
private WPBPageModulesCache wbWebPageModuleCacheMock;
private WPBParametersCache wbParameterCacheMock;
private WPBFilesCache wbImageCacheMock;
private WPBArticlesCache wbArticleCacheMock;
private WPBMessagesCache wbMessageCacheMock;
private WPBProjectCache wbProjectCacheMock;

WPBCacheInstances cacheInstances;

@Before
public void setUp()
{
	wbUriCacheMock = PowerMock.createMock(WPBUrisCache.class);
	wbWebPageCacheMock = PowerMock.createMock(WPBPagesCache.class);
	wbWebPageModuleCacheMock = PowerMock.createMock(WPBPageModulesCache.class);
	wbParameterCacheMock = PowerMock.createMock(WPBParametersCache.class);
	wbImageCacheMock = PowerMock.createMock(WPBFilesCache.class);
	wbArticleCacheMock = PowerMock.createMock(WPBArticlesCache.class);
	wbMessageCacheMock = PowerMock.createMock(WPBMessagesCache.class);
	wbProjectCacheMock = PowerMock.createMock(WPBProjectCache.class);
	cacheInstances = new WPBCacheInstances(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock);
	
}

@Test
public void test_findTemplateSource_page_ok()
{
	try
	{

		String templatePage = WPBTemplateEngine.WEBPAGES_PATH_PREFIX + "test";
	
		WPBPage webPageMock = PowerMock.createMock(WPBPage.class);
		Date date = new Date();
		EasyMock.expect(webPageMock.getLastModified()).andReturn(date);
		EasyMock.expect(wbWebPageCacheMock.getByExternalKey("test")).andReturn(webPageMock);
	
		PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, webPageMock);
		FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
		FreeMarkerTemplateObject object = (FreeMarkerTemplateObject) templateLoader.findTemplateSource(templatePage);

		PowerMock.verify(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, webPageMock);
		
		assertTrue(object.getExternalKey().equals("test"));
		assertTrue(object.getType() == TemplateType.TEMPLATE_PAGE);
		assertTrue(object.getLastModified() == date.getTime());
		
	} catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_findTemplateSource_page_notfound()
{
	try
	{

		String templatePage = WPBTemplateEngine.WEBPAGES_PATH_PREFIX + "test";
	
		EasyMock.expect(wbWebPageCacheMock.getByExternalKey("test")).andReturn(null);	
		PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock);
		FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
		FreeMarkerTemplateObject object = (FreeMarkerTemplateObject) templateLoader.findTemplateSource(templatePage);
		PowerMock.verify(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock);		
		assertTrue(object == null);
		
	} catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_findTemplateSource_page_exceptionGetFromCache()
{
	try
	{

		String templatePage = WPBTemplateEngine.WEBPAGES_PATH_PREFIX + "test";
	
		EasyMock.expect(wbWebPageCacheMock.getByExternalKey("test")).andThrow(new WPBIOException(""));
	
		PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock);
		FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
		FreeMarkerTemplateObject object = (FreeMarkerTemplateObject) templateLoader.findTemplateSource(templatePage);
		PowerMock.verify(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock);

		assertTrue(object == null);
		
	} catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_findTemplateSource_module_ok()
{
	try
	{
		String templatePage = WPBTemplateEngine.WEBMODULES_PATH_PREFIX + "test";
	
		WPBPageModule webPageModuleMock = PowerMock.createMock(WPBPageModule.class);
		Date date = new Date();
		EasyMock.expect(webPageModuleMock.getLastModified()).andReturn(date);
		EasyMock.expect(wbWebPageModuleCacheMock.getByExternalKey("test")).andReturn(webPageModuleMock);
	
		PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, webPageModuleMock);
		FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
		FreeMarkerTemplateObject object = (FreeMarkerTemplateObject) templateLoader.findTemplateSource(templatePage);

		PowerMock.verify(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, webPageModuleMock);
		assertTrue(object.getExternalKey().equals("test"));
		assertTrue(object.getType() == TemplateType.TEMPLATE_MODULE);
		assertTrue(object.getLastModified() == date.getTime());
		
	} catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_findTemplateSource_module_notfound()
{
	try
	{

		String templatePage = WPBTemplateEngine.WEBMODULES_PATH_PREFIX + "test";
	
		EasyMock.expect(wbWebPageModuleCacheMock.getByExternalKey("test")).andReturn(null);
	
		PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock);
		FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
		FreeMarkerTemplateObject object = (FreeMarkerTemplateObject) templateLoader.findTemplateSource(templatePage);
		PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock);
		assertTrue(object == null);
		
	} catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_findTemplateSource_module_exceptionGetFromCache()
{
	try
	{

		String templatePage = WPBTemplateEngine.WEBMODULES_PATH_PREFIX + "test";
	
		EasyMock.expect(wbWebPageModuleCacheMock.getByExternalKey("test")).andThrow(new WPBIOException(""));
	
		PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock);
		FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
		FreeMarkerTemplateObject object = (FreeMarkerTemplateObject) templateLoader.findTemplateSource(templatePage);
		PowerMock.verify(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock);

		assertTrue(object == null);
		
	} catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_findTemplateSource_wrongNameFormat()
{
	try
	{
		String templatePage =  "random test";
		PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock);
		FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
		FreeMarkerTemplateObject object = (FreeMarkerTemplateObject) templateLoader.findTemplateSource(templatePage);
		PowerMock.verify(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock);

		assertTrue(object == null);
		
	} catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_getLastModified_module()
{
	try
	{
	String templateName = "testX";
	Date date = new Date();
	
	FreeMarkerTemplateObject templateObjectMock = PowerMock.createMock(FreeMarkerTemplateObject.class);
	EasyMock.expect(templateObjectMock.getType()).andReturn(TemplateType.TEMPLATE_MODULE);
	
	//EasyMock.expect(templateObject.getLastModified()).andReturn(date.getTime());
	EasyMock.expect(templateObjectMock.getExternalKey()).andReturn(templateName);
	
	WPBPageModule pageModuleMock = PowerMock.createMock(WPBPageModule.class);
	EasyMock.expect(pageModuleMock.getLastModified()).andReturn(date);
	EasyMock.expect(wbWebPageModuleCacheMock.getByExternalKey(templateName)).andReturn(pageModuleMock);
	
	PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, pageModuleMock, templateObjectMock);

	FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
	
	long lastmodified = templateLoader.getLastModified(templateObjectMock);
	PowerMock.verify(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, templateObjectMock);
	
	assertTrue (lastmodified == date.getTime());
	
	}catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_getLastModified_module_exception()
{
	try
	{
	String templateName = "testX";
	
	FreeMarkerTemplateObject templateObjectMock = PowerMock.createMock(FreeMarkerTemplateObject.class);
	EasyMock.expect(templateObjectMock.getType()).andReturn(TemplateType.TEMPLATE_MODULE);
	
	//EasyMock.expect(templateObject.getLastModified()).andReturn(date.getTime());
	EasyMock.expect(templateObjectMock.getExternalKey()).andReturn(templateName);
	
	EasyMock.expect(wbWebPageModuleCacheMock.getByExternalKey(templateName)).andThrow(new WPBIOException(""));
	
	PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, templateObjectMock);
	FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);

	long lastmodified = templateLoader.getLastModified(templateObjectMock);
	PowerMock.verify(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, templateObjectMock);
	
	assertTrue (lastmodified == 0L);
	
	}catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_getLastModified_module_notfound()
{
	try
	{
	String templateName = "testX";
	
	FreeMarkerTemplateObject templateObjectMock = PowerMock.createMock(FreeMarkerTemplateObject.class);
	EasyMock.expect(templateObjectMock.getType()).andReturn(TemplateType.TEMPLATE_MODULE);
	
	//EasyMock.expect(templateObject.getLastModified()).andReturn(date.getTime());
	EasyMock.expect(templateObjectMock.getExternalKey()).andReturn(templateName);
	
	EasyMock.expect(wbWebPageModuleCacheMock.getByExternalKey(templateName)).andReturn(null);
	
	PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, templateObjectMock);
	FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);

	long lastmodified = templateLoader.getLastModified(templateObjectMock);
	PowerMock.verify(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, templateObjectMock);
	
	assertTrue (lastmodified == 0L);
	
	}catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_getLastModified_page()
{
	try
	{
	String templateName = "testX";
	Date date = new Date();
	
	FreeMarkerTemplateObject templateObjectMock = PowerMock.createMock(FreeMarkerTemplateObject.class);
	EasyMock.expect(templateObjectMock.getType()).andReturn(TemplateType.TEMPLATE_PAGE);
	
	//EasyMock.expect(templateObject.getLastModified()).andReturn(date.getTime());
	EasyMock.expect(templateObjectMock.getExternalKey()).andReturn(templateName);
	
	WPBPage pageMock = PowerMock.createMock(WPBPage.class);
	EasyMock.expect(pageMock.getLastModified()).andReturn(date);
	EasyMock.expect(wbWebPageCacheMock.getByExternalKey(templateName)).andReturn(pageMock);

	PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, pageMock, templateObjectMock);
	FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
	
	long lastmodified = templateLoader.getLastModified(templateObjectMock);
	PowerMock.verify(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, pageMock, templateObjectMock);	
	assertTrue (lastmodified == date.getTime());
	
	}catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_getLastModified_page_exception()
{
	try
	{
	String templateName = "testX";
	
	FreeMarkerTemplateObject templateObjectMock = PowerMock.createMock(FreeMarkerTemplateObject.class);
	EasyMock.expect(templateObjectMock.getType()).andReturn(TemplateType.TEMPLATE_PAGE);
	
	//EasyMock.expect(templateObject.getLastModified()).andReturn(date.getTime());
	EasyMock.expect(templateObjectMock.getExternalKey()).andReturn(templateName);
	
	EasyMock.expect(wbWebPageCacheMock.getByExternalKey(templateName)).andThrow(new WPBIOException(""));
	
	PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, templateObjectMock);

	FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
	long lastmodified = templateLoader.getLastModified(templateObjectMock);
	
	PowerMock.verify(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, templateObjectMock);
	assertTrue (lastmodified == 0L);
	
	}catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_getLastModified_page_notfound()
{
	try
	{
	String templateName = "testX";
	
	FreeMarkerTemplateObject templateObjectMock = PowerMock.createMock(FreeMarkerTemplateObject.class);
	EasyMock.expect(templateObjectMock.getType()).andReturn(TemplateType.TEMPLATE_PAGE);
	
	//EasyMock.expect(templateObject.getLastModified()).andReturn(date.getTime());
	EasyMock.expect(templateObjectMock.getExternalKey()).andReturn(templateName);
	
	EasyMock.expect(wbWebPageCacheMock.getByExternalKey(templateName)).andReturn(null);
	
	PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, templateObjectMock);
	FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
	
	long lastmodified = templateLoader.getLastModified(templateObjectMock);
	
	PowerMock.verify(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, templateObjectMock);

	assertTrue (lastmodified == 0L);
	
	}catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_getLastModified_null()
{
	try
	{
	
	PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock);
	FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
	
	long lastmodified = templateLoader.getLastModified(null);
	
	assertTrue (lastmodified == 0L);
	
	}catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_getReader_page()
{
	try
	{
	String templateName = "testX";
	String htmlSource = "<b>abc</b>";
	
	FreeMarkerTemplateObject templateObjectMock = PowerMock.createMock(FreeMarkerTemplateObject.class);
	EasyMock.expect(templateObjectMock.getType()).andReturn(TemplateType.TEMPLATE_PAGE);
	
	EasyMock.expect(templateObjectMock.getExternalKey()).andReturn(templateName);
	
	WPBPage pageMock = PowerMock.createMock(WPBPage.class);
	EasyMock.expect(pageMock.getHtmlSource()).andReturn(htmlSource);
	
	EasyMock.expect(wbWebPageCacheMock.getByExternalKey(templateName)).andReturn(pageMock);
	
	PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, templateObjectMock, pageMock);
	FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
	
	Reader reader = templateLoader.getReader(templateObjectMock, "");
	BufferedReader strReader = new BufferedReader(reader);
	assertTrue(strReader.readLine().equals(htmlSource));
	PowerMock.verify(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, templateObjectMock, pageMock);
	
	}catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_getReader_module()
{
	try
	{
	String templateName = "testX";
	String htmlSource = "<b>abc</b>";
	
	FreeMarkerTemplateObject templateObjectMock = PowerMock.createMock(FreeMarkerTemplateObject.class);
	EasyMock.expect(templateObjectMock.getType()).andReturn(TemplateType.TEMPLATE_MODULE);
	
	EasyMock.expect(templateObjectMock.getExternalKey()).andReturn(templateName);
	
	WPBPageModule pageModuleMock = PowerMock.createMock(WPBPageModule.class);
	EasyMock.expect(pageModuleMock.getHtmlSource()).andReturn(htmlSource);
	
	EasyMock.expect(wbWebPageModuleCacheMock.getByExternalKey(templateName)).andReturn(pageModuleMock);
	
	PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, templateObjectMock, pageModuleMock);
	FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
	
	Reader reader = templateLoader.getReader(templateObjectMock, "");
	BufferedReader strReader = new BufferedReader(reader);
	assertTrue(strReader.readLine().equals(htmlSource));
	PowerMock.verify(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, templateObjectMock, pageModuleMock);

	}catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_getReader_module_exception()
{
	try
	{
	String templateName = "testX";
	
	FreeMarkerTemplateObject templateObjectMock = PowerMock.createMock(FreeMarkerTemplateObject.class);
	EasyMock.expect(templateObjectMock.getType()).andReturn(TemplateType.TEMPLATE_MODULE);
	
	EasyMock.expect(templateObjectMock.getExternalKey()).andReturn(templateName);
		
	EasyMock.expect(wbWebPageModuleCacheMock.getByExternalKey(templateName)).andThrow(new WPBIOException(""));
	
	PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, templateObjectMock);
	FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
	
	templateLoader.getReader(templateObjectMock, "");
	assertTrue(false);
	
	
	}
	catch (IOException e)
	{
		
	}
	catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_getReader_page_exception()
{
	try
	{
	String templateName = "testX";
	
	FreeMarkerTemplateObject templateObjectMock = PowerMock.createMock(FreeMarkerTemplateObject.class);
	EasyMock.expect(templateObjectMock.getType()).andReturn(TemplateType.TEMPLATE_PAGE);
	
	EasyMock.expect(templateObjectMock.getExternalKey()).andReturn(templateName);
		
	EasyMock.expect(wbWebPageCacheMock.getByExternalKey(templateName)).andThrow(new WPBIOException(""));
	
	PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, templateObjectMock);
	FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
	
	templateLoader.getReader(templateObjectMock, "");
	assertTrue(false);
	
	}
	catch (IOException e)
	{
		
	}
	catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_getReader_page_notfound()
{
	try
	{
	String templateName = "testX";
	
	FreeMarkerTemplateObject templateObjectMock = PowerMock.createMock(FreeMarkerTemplateObject.class);
	EasyMock.expect(templateObjectMock.getType()).andReturn(TemplateType.TEMPLATE_PAGE);
	
	EasyMock.expect(templateObjectMock.getExternalKey()).andReturn(templateName);
		
	EasyMock.expect(wbWebPageCacheMock.getByExternalKey(templateName)).andReturn(null);
	
	PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, templateObjectMock);
	FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
	
	templateLoader.getReader(templateObjectMock, "");
	assertTrue(false);
	
	}
	catch (IOException e)
	{
		
	}
	catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_getReader_module_notfound()
{
	try
	{
	String templateName = "testX";
	
	FreeMarkerTemplateObject templateObjectMock = PowerMock.createMock(FreeMarkerTemplateObject.class);
	EasyMock.expect(templateObjectMock.getType()).andReturn(TemplateType.TEMPLATE_MODULE);
	
	EasyMock.expect(templateObjectMock.getExternalKey()).andReturn(templateName);
		
	EasyMock.expect(wbWebPageModuleCacheMock.getByExternalKey(templateName)).andReturn(null);
	
	PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, templateObjectMock);
	FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
	
	templateLoader.getReader(templateObjectMock, "");
	assertTrue(false);
	
	}
	catch (IOException e)
	{
		
	}
	catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_getReader_null()
{
	try
	{
	
		PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock);
		FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
	
		templateLoader.getReader(null, "");
		assertTrue(false);
	
	}
	catch (IOException e)
	{
		
	}
	catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_getPrefix()
{
	
	PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock);
	FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
	
	assertTrue (templateLoader.getWebModulesPathPrefix().equals(WPBFreeMarkerTemplateEngine.WEBMODULES_PATH_PREFIX));
	assertTrue (templateLoader.getWebPagesPathPrefix().equals(WPBFreeMarkerTemplateEngine.WEBPAGES_PATH_PREFIX));
		
}

@Test
public void test_closeTemplateSource()
{
	FreeMarkerTemplateObject templateObjectMock = PowerMock.createMock(FreeMarkerTemplateObject.class);	
	PowerMock.replay(wbUriCacheMock, wbWebPageCacheMock, wbWebPageModuleCacheMock, wbParameterCacheMock, wbImageCacheMock, wbArticleCacheMock, wbMessageCacheMock, wbProjectCacheMock, templateObjectMock);
	FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader(cacheInstances);
	try
	{
		templateLoader.closeTemplateSource(templateObjectMock);
	} catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_defaultConstructor()
{
	FreeMarkerTemplateLoader templateLoader = new FreeMarkerTemplateLoader();
	assertTrue(templateLoader != null);
}

}
