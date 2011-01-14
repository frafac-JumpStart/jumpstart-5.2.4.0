package jumpstart.web.pages.examples.javascript;

import java.util.Arrays;
import java.util.List;

import jumpstart.web.pages.Index;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

public class AJAXSelect1 {
	
		public enum CarMaker
		{
			MERCEDES, AUDI, BMW, HONDA, TOYOTA, PEUGEOT, CITROEN ;
		}
	
	
		@Inject
	    private Messages messages;
	   
	    @Property
	    @Persist
	    private CarMaker carMaker;
	   
	    @Property
	    @Persist
	    private String carModel;

	    @InjectComponent
	    private Zone modelZone;
	   
	    @Property
	    @Persist
	    private List<String> availableModels;
	    
	    // Other pages
		@InjectPage
		private AJAXSelect2 _page2;
		
		private String _carMaker;
		
		
	    
	    public Object onValueChanged(CarMaker maker) 
	    {
	       availableModels = findAvailableModels(maker);
	       
	       return modelZone.getBody();
	    }
	    
	    public List<String> findAvailableModels(final CarMaker maker) 
	    {
	      switch (maker) 
	      {
	         case AUDI:
	            return Arrays.asList("A4", "A6", "A8");
	         case BMW:
	            return Arrays.asList("3 Series", "5 Series", "7 Series");
	         case MERCEDES:
	            return Arrays.asList("C-Class", "E-Class", "S-Class");
	         case HONDA:
	        	return Arrays.asList("Accord", "Civic", "Jazz");
	         case TOYOTA:	 
	        	return Arrays.asList("Camry", "Corolla");
	         case PEUGEOT:
		        return Arrays.asList("207", "307", "407");
		     case CITROEN:	 
		        return Arrays.asList("C3", "C4","C5");	

	         default:
	            return Arrays.asList();
	       }
	    }    
	
	

	

	
}
