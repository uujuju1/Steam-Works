package sw.annotations.processors.impl;

import sw.annotations.processors.*;

import javax.annotation.processing.*;
import java.util.*;

/**
 * @author Liz took from Glenn
 */
@SuppressWarnings("unused")
public class AssetsProcessor extends BaseProcessor {
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> types = new HashSet<>();
		String prefix = modName + ".annotations.Annotations.";
		types.add(prefix + "ProcessAssets");
		return types;
	}

	@Override
	public void process(RoundEnvironment roundEnv) throws Exception {
	}
}