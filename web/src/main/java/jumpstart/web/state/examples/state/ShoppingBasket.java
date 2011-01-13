package jumpstart.web.state.examples.state;

public class ShoppingBasket {
	private int _applesQuantity;
	private int _orangesQuantity;
	private int _bananasQuantity;
	
	public int getApplesQuantity() {
		return _applesQuantity;
	}

	public void setApplesQuantity(int applesQuantity) {
		_applesQuantity = applesQuantity;
	}

	public int getOrangesQuantity() {
		return _orangesQuantity;
	}

	public void setOrangesQuantity(int orangesQuantity) {
		_orangesQuantity = orangesQuantity;
	}

	public int getBananasQuantity() {
		return _bananasQuantity;
	}

	public void setBananasQuantity(int bananasQuantity) {
		_bananasQuantity = bananasQuantity;
	}
}
