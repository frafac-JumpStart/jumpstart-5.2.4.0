package jumpstart.business.domain.base;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import jumpstart.business.MessageUtil;
import jumpstart.util.StringUtil;

import org.hibernate.LazyInitializationException;

@SuppressWarnings("serial")
public abstract class BaseEntity implements Serializable {

	protected static final String DIVIDER = ", ";

	public abstract Serializable getIdForMessages();

	/**
	 * Useful when referencing objects that may cause LazyInitializationExceptions that you don't really care about. For
	 * example, if you replace this: <code>customer.toString()</code>, with this <code>toStringLazy(customer)</code>,
	 * then exceptions will be handled and one of these values returned:
	 * <ul>
	 * <li>The toString() value is returned if possible.</li>
	 * <li>"&lt;null&gt;" is returned if the specified object is null.</li>
	 * <li>"&lt;lazy&gt;" is returned if the object has not been loaded, ie. if toString() would cause a
	 * LazyInitializationException to be thrown.</li>
	 * </ul>
	 * 
	 * @param obj An object eg. customer.
	 * @return "&lt;null&gt;", "&lt;lazy&gt;", or the result, eg. of customer.toString().
	 */
	public String toStringLazy(Object obj) {

		// Need to find a non-hibernate technique

		if (obj == null) {
			return "<null>";
		}
		else if (!org.hibernate.Hibernate.isInitialized(obj)) {
			return "<lazy>";
		}
		else {
			return obj.toString();
		}
	}

	/**
	 * Useful when referencing objects that may cause LazyInitializationExceptions that you don't really care about. For
	 * example, if you replace this: <code>customer.getId().toString()</code>, with this
	 * <code>toStringLazy(customer, "getId")</code>, then exceptions will be handled and one of these values returned:
	 * <ul>
	 * <li>The toString() value is returned if possible.</li>
	 * <li>"&lt;null&gt;" is returned if the specified object is null.</li>
	 * <li>"&lt;null&gt;" is returned if the specified getter returns null.</li>
	 * <li>"&lt;lazy&gt;" is returned if the object has not been loaded, ie. if toString() would cause a
	 * LazyInitializationException to be thrown.</li>
	 * </ul>
	 * 
	 * @param obj An object eg. customer.
	 * @param getterName A method to execute on the object, eg. "getId".
	 * @return "&lt;null&gt;", "&lt;lazy&gt;", or the result, eg. of customer.getId().toString().
	 */
	public String toStringLazy(Object obj, String getterName) {

		// Need to find a non-hibernate technique

		if (obj == null) {
			return "<null>";
		}
		else if (!org.hibernate.Hibernate.isInitialized(obj)) {
			return "<lazy>";
		}
		else {
			return toStringLazyInternal(obj, getterName);
		}
	}

	private String toStringLazyInternal(Object obj, String getterName) {

		if (obj == null) {
			return "null";
		}

		// This technique might be very bad - when LazyInitializationException occurs it is logged
		// as ERROR and the session must be closed and discarded!
		// See http://opensource.atlassian.com/projects/hibernate/browse/HHH-2025

		try {
			Class<? extends Object> c = obj.getClass();
			Method getterMethod = c.getMethod(getterName, (Class[]) null);
			Object result = getterMethod.invoke(obj, (Object[]) null);
			if (result == null) {
				return "<null>";
			}
			return result.toString();
		}
		catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			if (t instanceof LazyInitializationException) {
				return "<lazy>";
			}
			else {
				throw new IllegalStateException("Exception in method toStringLazy(..) with obj=" + obj
						+ ", getterName=" + getterName + ".", e);
			}
		}
		catch (Exception e) {
			throw new IllegalStateException("Exception in method toStringLazy(..) with obj=" + obj + ", getterName="
					+ getterName + ".", e);
		}
	}

	public String sizeLazy(Collection<?> obj) {

		// Need to find a non-hibernate technique

		if (!org.hibernate.Hibernate.isInitialized(obj)) {
			return "<lazy>";
		}
		else if (obj == null) {
			return "<null>";
		}
		else {
			return Integer.toString(obj.size());
		}
	}

	public String abbreviate(String s, int maxLen) {
		return StringUtil.abbreviate(s, maxLen);
	}

	public Integer advance(Integer targetVersion) {
		return targetVersion == null ? 0 : targetVersion + 1;
	}

	/**
	 * Using this method ensures we have consistent message keys for turning Enum values into text. Eg. given a value
	 * whose type is UserStatuses and value is ACTIVE, it will generate message key "UserStatuses.ACTIVE" and return the
	 * corresponding message. The keys it generates match those which Tapestry generates when it puts Enums in a Select.
	 * 
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Enum> String getLabelForEnum(T value) {
		String s = MessageUtil.toText(value.getClass().getSimpleName() + "." + value.toString());
		return s;
	}

}
