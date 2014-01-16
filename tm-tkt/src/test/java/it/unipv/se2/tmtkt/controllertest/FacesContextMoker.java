package it.unipv.se2.tmtkt.controllertest;

import javax.faces.context.FacesContext;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public abstract class FacesContextMoker extends FacesContext {

	private FacesContextMoker() { }
	
	private static final Release RELEASE = new Release();

	private static class Release implements Answer<Void> {
	    @Override
	    public Void answer(InvocationOnMock invocation) throws Throwable {
	        setCurrentInstance(null);
	        return null;
	    }
	}

	public static FacesContext mockFacesContext() {
	    FacesContext context = Mockito.mock(FacesContext.class);
	    setCurrentInstance(context);
	    Mockito.doAnswer(RELEASE).when(context).release();
	    return context;
	}
	
	/*
	@Override
	public void addMessage(String arg0, FacesMessage arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Application getApplication() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<String> getClientIdsWithMessages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExternalContext getExternalContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Severity getMaximumSeverity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<FacesMessage> getMessages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<FacesMessage> getMessages(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RenderKit getRenderKit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getRenderResponse() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getResponseComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResponseStream getResponseStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseWriter getResponseWriter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UIViewRoot getViewRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub

	}

	@Override
	public void renderResponse() {
		// TODO Auto-generated method stub

	}

	@Override
	public void responseComplete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setResponseStream(ResponseStream arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setResponseWriter(ResponseWriter arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setViewRoot(UIViewRoot arg0) {
		// TODO Auto-generated method stub

	}
	*/

}
