package jumpstart.util.properties;

public class PropertyValueException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final int ALLOWED_VALUE_TYPE_DISCRETE = 1;
	private static final int ALLOWED_VALUE_TYPE_RANGE = 2;
	private static final int ALLOWED_VALUE_TYPE_DESCRIBED = 3;

	private String _propertyName;
	private String _propertyValue;
	private String _resourceName;

	private int _allowedValueType;
	private String[] _allowedValues;
	private String _fromValue;
	private String _toValue;
	private String _description;

	PropertyValueException(String propertyName, String resourceName, String propertyValue, String[] allowedValues) {
		_propertyName = propertyName;
		_resourceName = resourceName;

		_propertyValue = propertyValue;
		_allowedValueType = ALLOWED_VALUE_TYPE_DISCRETE;
		_allowedValues = allowedValues;
	}

	PropertyValueException(String propertyName, String resourceName, String propertyValue, String fromValue,
			String toValue) {
		_propertyName = propertyName;
		_resourceName = resourceName;

		_propertyValue = propertyValue;
		_allowedValueType = ALLOWED_VALUE_TYPE_RANGE;
		_fromValue = fromValue;
		_toValue = toValue;
	}

	PropertyValueException(String propertyName, String resourceName, String propertyValue, String description) {
		_propertyName = propertyName;
		_resourceName = resourceName;

		_propertyValue = propertyValue;
		_allowedValueType = ALLOWED_VALUE_TYPE_DESCRIBED;
		_description = description;
	}

	@Override
	public String getMessage() {
		switch (_allowedValueType) {

		case ALLOWED_VALUE_TYPE_DISCRETE:
			String s = "{ ";
			for (int i = 0; i < _allowedValues.length; i++) {
				if (i > 0) {
					s += ", ";
				}
				s += "\"" + _allowedValues[i] + "\"";
			}
			s += " }";
			return "Property \"" + _propertyName + "\" in resource \"" + _resourceName + "\" has invalid value \""
					+ _propertyValue + "\". Allowed values are " + s + ".";

		case ALLOWED_VALUE_TYPE_RANGE:
			return "Property \"" + _propertyName + "\" in resource \"" + _resourceName + "\" has invalid value \""
					+ _propertyValue + "\". Allowed values is from " + _fromValue + " to " + _toValue + ".";

		case ALLOWED_VALUE_TYPE_DESCRIBED:
			return "Property \"" + _propertyName + "\" in resource \"" + _resourceName + "\" has invalid value \""
					+ _propertyValue + "\". Allowed values is " + _description + ".";

		default:
			throw new IllegalStateException("Should never reach here.");
		}
	}
}
