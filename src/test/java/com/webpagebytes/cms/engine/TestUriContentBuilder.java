package com.webpagebytes.cms.engine;

import static org.junit.Assert.*;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.webpagebytes.cms.WPBForward;
import com.webpagebytes.cms.WPBModel;
import com.webpagebytes.cms.WPBRequestHandler;
import com.webpagebytes.cms.cmsdata.WPBUri;
import com.webpagebytes.cms.engine.FileContentBuilder;
import com.webpagebytes.cms.engine.InternalModel;
import com.webpagebytes.cms.engine.ModelBuilder;
import com.webpagebytes.cms.engine.PageContentBuilder;
import com.webpagebytes.cms.engine.UriContentBuilder;
import com.webpagebytes.cms.exception.WPBException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UriContentBuilder.class})
public class TestUriContentBuilder {

WPBCacheInstances cacheInstancesMock;
ModelBuilder modelBuilderMock;
UriContentBuilder uriContentBuilder;
PageContentBuilder pageContentBuilder;
FileContentBuilder fileContentBuilder;

@Before
public void setUp()
{
cacheInstancesMock = EasyMock.createMock(WPBCacheInstances.class);
modelBuilderMock = EasyMock.createMock(ModelBuilder.class);
pageContentBuilder = EasyMock.createMock(PageContentBuilder.class);
fileContentBuilder = EasyMock.createMock(FileContentBuilder.class);
uriContentBuilder = new UriContentBuilder(cacheInstancesMock, modelBuilderMock, fileContentBuilder, pageContentBuilder);

}

@Test
public void test_initialize()
{
	EasyMock.replay(cacheInstancesMock, modelBuilderMock);
	uriContentBuilder.initialize();
	EasyMock.verify(cacheInstancesMock, modelBuilderMock);
}

@Test
public void test_buildUriContent()
{
	HttpServletResponse responseMock = EasyMock.createMock(HttpServletResponse.class);
	HttpServletRequest requestMock = EasyMock.createMock(HttpServletRequest.class);
	WPBUri uriMock = EasyMock.createMock(WPBUri.class);
	
	String controllerClass = "com.webpagebytes.cms.engine.DummyRequestHandler";
	EasyMock.expect(uriMock.getControllerClass()).andReturn(controllerClass);
	
	InternalModel model = new InternalModel();
	WPBForward forward = new WPBForward();
	try
	{
	
		EasyMock.replay(responseMock, requestMock, uriMock, cacheInstancesMock, modelBuilderMock);
		uriContentBuilder.buildUriContent(requestMock, responseMock, uriMock, model, forward);
		EasyMock.verify(responseMock, requestMock, uriMock, cacheInstancesMock, modelBuilderMock);
		
		//verify that the controller is in customControllers map
		Map<String, WPBRequestHandler> controllers =  Whitebox.getInternalState(uriContentBuilder, "customControllers");
		assertTrue (controllers.get(controllerClass) != null);
		
	}catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_buildUriContent_wrongController()
{
	HttpServletResponse responseMock = EasyMock.createMock(HttpServletResponse.class);
	HttpServletRequest requestMock = EasyMock.createMock(HttpServletRequest.class);
	WPBUri uriMock = EasyMock.createMock(WPBUri.class);
	
	EasyMock.expect(uriMock.getControllerClass()).andReturn("com.webpagebytes.cms.DoesNotExist");
	
    InternalModel model = new InternalModel();
	WPBForward forward = new WPBForward();
	try
	{
	
		EasyMock.replay(responseMock, requestMock, uriMock, cacheInstancesMock, modelBuilderMock);
		uriContentBuilder.buildUriContent(requestMock, responseMock, uriMock, model, forward);
		EasyMock.verify(responseMock, requestMock, uriMock, cacheInstancesMock, modelBuilderMock);
		
	}catch (WPBException e)
	{
		//OK
	}
	catch (Exception e)
	{
		assertTrue (false);
	}
}

@Test
public void test_buildUriContent_controller_already_exists()
{
	HttpServletResponse responseMock = EasyMock.createMock(HttpServletResponse.class);
	HttpServletRequest requestMock = EasyMock.createMock(HttpServletRequest.class);
	WPBUri uriMock = EasyMock.createMock(WPBUri.class);
	
	String controllerClass = "com.webpagebytes.cms.engine.DummyRequestHandler";
	
	WPBRequestHandler controllerInst = null;
	try
	{
		controllerInst = (WPBRequestHandler) Class.forName(controllerClass).newInstance();
	} catch (Exception e)
	{
		assertTrue(false);
	}
	Map<String, WPBRequestHandler> controllers = Whitebox.getInternalState(uriContentBuilder, "customControllers");
	controllers.put(controllerClass, controllerInst);
	
	EasyMock.expect(uriMock.getControllerClass()).andReturn(controllerClass);
	
    InternalModel model = new InternalModel();
	WPBForward forward = new WPBForward();
	try
	{
	
		EasyMock.replay(responseMock, requestMock, uriMock, cacheInstancesMock, modelBuilderMock);
		uriContentBuilder.buildUriContent(requestMock, responseMock, uriMock, model, forward);
		EasyMock.verify(responseMock, requestMock, uriMock, cacheInstancesMock, modelBuilderMock);
		
		//verify that the controller is in customControllers map
		Map<String, WPBRequestHandler> controllers_ =  Whitebox.getInternalState(uriContentBuilder, "customControllers");
		assertTrue (controllers_.get(controllerClass) == controllerInst);
		assertTrue (controllers_.size() == 1);
		
	}catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_buildUriContent_empty_controller()
{
	HttpServletResponse responseMock = EasyMock.createMock(HttpServletResponse.class);
	HttpServletRequest requestMock = EasyMock.createMock(HttpServletRequest.class);
	WPBUri uriMock = EasyMock.createMock(WPBUri.class);
	
	String controllerClass = "";
	
	EasyMock.expect(uriMock.getControllerClass()).andReturn(controllerClass);	
    InternalModel model = new InternalModel();
	WPBForward forward = new WPBForward();
	try
	{
	
		EasyMock.replay(responseMock, requestMock, uriMock, cacheInstancesMock, modelBuilderMock);
		uriContentBuilder.buildUriContent(requestMock, responseMock, uriMock, model, forward);
		EasyMock.verify(responseMock, requestMock, uriMock, cacheInstancesMock, modelBuilderMock);
				
	}catch (Exception e)
	{
		assertTrue(false);
	}
}

@Test
public void test_buildUriContent_null_controller()
{
	HttpServletResponse responseMock = EasyMock.createMock(HttpServletResponse.class);
	HttpServletRequest requestMock = EasyMock.createMock(HttpServletRequest.class);
	WPBUri uriMock = EasyMock.createMock(WPBUri.class);
	
	String controllerClass = null;
	
	EasyMock.expect(uriMock.getControllerClass()).andReturn(controllerClass);	
    InternalModel model = new InternalModel();
	WPBForward forward = new WPBForward();
	try
	{
	
		EasyMock.replay(responseMock, requestMock, uriMock, cacheInstancesMock, modelBuilderMock);
		uriContentBuilder.buildUriContent(requestMock, responseMock, uriMock, model, forward);
		EasyMock.verify(responseMock, requestMock, uriMock, cacheInstancesMock, modelBuilderMock);
				
	}catch (Exception e)
	{
		assertTrue(false);
	}
}

}
