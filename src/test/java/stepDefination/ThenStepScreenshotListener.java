package stepDefination;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestStepFinished;

public class ThenStepScreenshotListener implements ConcurrentEventListener {

	@Override
	public void setEventPublisher(EventPublisher publisher) {
		publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
	}

	private void handleTestStepFinished(TestStepFinished event) {
		if (event.getTestStep() instanceof PickleStepTestStep) {
			PickleStepTestStep step = (PickleStepTestStep) event.getTestStep();
			String keyword = step.getStep().getKeyword().trim();

			if (keyword.equalsIgnoreCase("Then")) {
				Hooks.saveScreenshot(step.getStep().getText() + "_THEN");
			}
		}
	}
}