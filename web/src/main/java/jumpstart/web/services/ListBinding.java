// From http://wiki.apache.org/tapestry/Tapestry5HowToAddBindingPrefix

package jumpstart.web.services;

import java.util.List;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.internal.bindings.AbstractBinding;

public class ListBinding extends AbstractBinding {
	private final List<Binding> delegates;
	private final boolean _invariant;

	public ListBinding(List<Binding> delegates, boolean invariant) {
		this.delegates = delegates;
		_invariant = invariant;
	}

	public Object get() {
		Object[] valuesFromDelegates = new Object[delegates.size()];

		for (int i = 0; i < delegates.size(); i++) {
			valuesFromDelegates[i] = delegates.get(i).get();
		}

		return valuesFromDelegates;
	}

	@Override
	public boolean isInvariant() {
		return _invariant;
	}

	@Override
	public Class<Object[]> getBindingType() {
		return Object[].class;
	}
}
