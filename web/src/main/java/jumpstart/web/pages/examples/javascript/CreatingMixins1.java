package jumpstart.web.pages.examples.javascript;

import jumpstart.web.state.examples.javascript.MyOrder;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

public class CreatingMixins1 {

	// Work fields

	@SessionState
	@Property
	private MyOrder _myOrder;

	// The code
	
	void setupRender() {
		_myOrder.setApplesQuantity(0);
		_myOrder.setOrangesQuantity(0);
		_myOrder.setBananasQuantity(0);
	}

	Object onSuccessFromPlainForm() {
		orderOneApple();
		return CreatingMixins2.class;
	}

	Object onActionFromPlainOrderOneOrange() {
		orderOneOrange();
		return CreatingMixins2.class;
	}

	Object onOrderOneBanana() {
		orderOneBanana();
		return CreatingMixins2.class;
	}

	Object onSuccessFromMixinForm() {
		orderOneApple();
		return CreatingMixins2.class;
	}

	Object onActionFromMixinOrderOneOrange() {
		orderOneOrange();
		return CreatingMixins2.class;
	}

	void orderOneApple() {
		sleep(1500); // Sleep 1.5 seconds to simulate busy system
		_myOrder.setApplesQuantity(_myOrder.getApplesQuantity() + 1);
	}

	void orderOneOrange() {
		sleep(1500); // Sleep 1.5 seconds to simulate busy system
		_myOrder.setOrangesQuantity(_myOrder.getOrangesQuantity() + 1);
	}

	void orderOneBanana() {
		sleep(1500); // Sleep 1.5 seconds to simulate busy system
		_myOrder.setBananasQuantity(_myOrder.getBananasQuantity() + 1);
	}

	private void sleep(long duration) {
		try {
			Thread.sleep(duration);
		}
		catch (InterruptedException e) {
		}
	}

}
