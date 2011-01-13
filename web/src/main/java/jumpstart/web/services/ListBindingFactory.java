// From http://wiki.apache.org/tapestry/Tapestry5HowToAddBindingPrefix

package jumpstart.web.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.BindingSource;

/**
 * Implementation of the list: binding prefix -- we parse list of bindings and generate delegate bindings for each
 * element<br>
 * default binding is prop, except when surrounded with '' or is a numeric value ([0-9.]*)
 */
public class ListBindingFactory implements BindingFactory {
	private final BindingSource _bindingSource;

	public ListBindingFactory(BindingSource source) {
		this._bindingSource = source;
	}

	public Binding newBinding(String description, ComponentResources container, ComponentResources component,
			String expression, Location location) {
		List<Binding> delegates = new ArrayList<Binding>();
		String[] bindingNames = expression.split(",");
		boolean isInvariant = true;

		for (String bindingName : bindingNames) {
			String defaultBinding = BindingConstants.LITERAL;

			if (bindingName.charAt(0) == '\'') {
				// translate "'something'" to "literal:something"
				bindingName = bindingName.substring(1, bindingName.length() - 1);

			}
			else {
				// if value is numeric, we leave literal binding prefix as default
				for (int i = 0; i < bindingName.length(); i++) {
					char ch = bindingName.charAt(i);
					if (ch != '.' && !Character.isDigit(ch)) {
						defaultBinding = BindingConstants.PROP;
						break;
					}
				}
			}

			Binding binding = _bindingSource.newBinding(description, container, component, defaultBinding, bindingName,
					location);
			isInvariant = isInvariant && binding.isInvariant();
			delegates.add(binding);
		}

		return new ListBinding(delegates, isInvariant);
	}
}
