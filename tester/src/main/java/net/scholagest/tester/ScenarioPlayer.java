package net.scholagest.tester;

import net.scholagest.tester.jaxb.TCall;
import net.scholagest.tester.jaxb.TScenario;

public class ScenarioPlayer {

	public void playScenario(TScenario scenario) {
		Placeholder placeholder = new Placeholder();
		ResponseAnalyzer responseAnalyzer = new ResponseAnalyzer(placeholder);

		for (TCall call : scenario.getCalls().getCall()) {
			new CallHandler(responseAnalyzer, placeholder)
					.handleCallAndException(scenario.getBaseURL(), call);
		}

		responseAnalyzer.displayResults();
	}
}
