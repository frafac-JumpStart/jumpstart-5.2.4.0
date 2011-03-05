package jumpstart.web.pages.examples.javascript;

import java.util.Date;

import org.apache.tapestry5.ajax.MultiZoneUpdate;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.DateField;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;

public class AJAXForm {

	// Screen fields

	@Property
	private String _firstName;

	@Property
	private String _lastName;
	
	@Property
	private Date _birthday;

	// Generally useful bits and pieces
	
	@Component
	private Form _ajaxForm;
    
	@Component(id = "birthday",parameters ={"validate=required"})
	private DateField df;
    
	@Component(id = "firstName")
	private TextField _firstNameField;
    
	@Component(id = "lastName")
	private TextField _lastNameField;

	@Component(id="nameZone")
	private Zone _nameZone;
	
	@Component(id="formZone")
	private Zone _formZone;
	
	
	// The code
	
	void setupRender() {
		if (_firstName == null && _lastName == null) {
			_firstName = "Humpty";
			_lastName = "Dumpty";
		}
	}
	
	void onValidateForm() {
		
		if (_firstName == null || _firstName.trim().equals("")) {
			_ajaxForm.recordError(_firstNameField, "First Name is required.");
		}
		if (_lastName == null || _lastName.trim().equals("")) {
			_ajaxForm.recordError(_lastNameField, "Last Name is required.");
		}
		Date now = new Date(); 
		if(_birthday.after(now))
		{
			_ajaxForm.recordError(df, "invalid birthday");
		}
	}


	Object onSuccess() {
		return  new MultiZoneUpdate(_nameZone).add(_formZone);
	}
	
	Object onFailure() {
		return  new MultiZoneUpdate(_nameZone).add(_formZone);
		
	}

	public String getName() {
		return _firstName + " " + _lastName;
	}

	public Date getTime() {
		return new Date();
	}

}
