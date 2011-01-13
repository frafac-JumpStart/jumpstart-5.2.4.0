// Adadpted from http://wiki.apache.org/tapestry/Tapestry5HowtoSelectWithObjects
package jumpstart.web.commons;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.util.AbstractSelectModel;

/**
 * A generic selection model whose value is the id of the chosen object, not the object itself.
 */
public class IdSelectModel<T> extends AbstractSelectModel {

	private List<T> list;
	private PropertyAdapter labelFieldAdapter;
	private PropertyAdapter idFieldAdapter;

	/**
	 * @param list the list of objects you want modeled in a Select component. These objects MUST implement
	 *        equals(Object obj) and hashCode(). If the objects are JPA entities, ensure their implementations of
	 *        equals(Object obj) and hashCode() return the same thing for different instances of the same detached
	 *        entity.
	 * @param clazz the class of objects in the list.
	 * @param labelField the name of the field you want displayed as the label in the selection list, eg. "name".
	 * @param idField the name of the field which is the unique identifier of each object in the list, eg. "id". This is
	 *        used in the value property of the Select component.
	 * @param access Declare a PropertyAccess injected into your page (eg.
	 * @Inject private PropertyAccess _access) then pass it in here.
	 * 
	 */
	public IdSelectModel(List<T> list, Class<T> clazz, String labelField, String idField, PropertyAccess access) {
		if (clazz == null) {
			throw new IllegalArgumentException("clazz is required.");
		}
		if (idField == null) {
			throw new IllegalArgumentException("idField is required.");
		}
		if (labelField == null) {
			throw new IllegalArgumentException("labelField is required.");
		}

		this.list = list;
		this.idFieldAdapter = access.getAdapter(clazz).getPropertyAdapter(idField);
		this.labelFieldAdapter = access.getAdapter(clazz).getPropertyAdapter(labelField);

		if (idFieldAdapter == null) {
			throw new IllegalArgumentException("idField " + idField + " does not exist in class " + clazz + ".");
		}
		if (labelFieldAdapter == null) {
			throw new IllegalArgumentException("labelField " + idField + " does not exist in class " + clazz + ".");
		}
	}

	public List<OptionGroupModel> getOptionGroups() {
		return null;
	}

	public List<OptionModel> getOptions() {
		List<OptionModel> optionModelList = new ArrayList<OptionModel>();
		for (T obj : list) {
			optionModelList.add(new OptionModelImpl(nvl(labelFieldAdapter.get(obj)), idFieldAdapter.get(obj)));
		}
		return optionModelList;
	}

	public List<T> getList() {
		return list;
	}

	private String nvl(Object o) {
		if (o == null)
			return "";
		else
			return o.toString();
	}

}
