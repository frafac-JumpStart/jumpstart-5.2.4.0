package jumpstart.web.pages.examples.javascript;

import jumpstart.web.pages.Index;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

public class AJAXSelect1 {
	static final private String[] ALL_MAKES = new String[] {"Honda", "Toyota"};
	static final private String[] NO_MODELS = new String[] {};
	static final private String[] HONDA_MODELS = new String[] {"Accord", "Civic", "Jazz"};
	static final private String[] TOYOTA_MODELS = new String[] {"Camry", "Corolla"};

	// Screen fields

	@Property
	@Persist
	private String _carMake;

	@Property
	@Persist
	private String _carModel;

	@Property
	@Persist
	private String _keyWords;
	
	@Property
	@Persist
	private String[] _carMakes;
	
	@SuppressWarnings("unused")
	@Property
	@Persist
	private String[] _carModels;
	
	// Other pages

	@InjectPage
	private AJAXSelect2 _page2;

	// Generally useful bits and pieces

	@InjectComponent
	private Zone _searchZone;

	@Component(id = "searchCriteria")
	private Form _searchCriteriaForm;

	@Inject
	private Request _request;

	@Inject
	private ComponentResources _resources;
	
	// The code

	void setupRender() {
		if (_carMakes == null) {
			_carMakes = ALL_MAKES;
			_carModels = NO_MODELS;
		}
	}

	Object onChangeOfCarMake() {
		_carMake = _request.getParameter("param");
		
		if (_carMake == null) {
			_carModels = NO_MODELS;
		}
		else if (_carMake.equals(_carMakes[0])) {
			_carModels = HONDA_MODELS;
		}
		else if (_carMake.equals(_carMakes[1])) {
			_carModels = TOYOTA_MODELS;
		}
		else {
			_carModels = NO_MODELS;
		}
		_carModel = null;
		
		return _searchZone.getBody();
	}

	Object onChangeOfCarModel() {
		_carModel = _request.getParameter("param");
		return _searchZone.getBody();
	}

	void onChangeOfKeyWords() {
		_keyWords = _request.getParameter("param");
	}
	
	void onValidateForm() {
		if (_carMake == null) {
			_searchCriteriaForm.recordError("Choose a car make.");
			return;
		}
		if (_carModel == null) {
			_searchCriteriaForm.recordError("Choose a car model.");
			return;
		}
	}

	Object onSuccess() {
		_page2.set(_carMake, _carModel, _keyWords);
		_resources.discardPersistentFieldChanges();
		return _page2;
	}

	Object onActionFromGoHome() {
		_resources.discardPersistentFieldChanges();
		return Index.class;
	}
}
