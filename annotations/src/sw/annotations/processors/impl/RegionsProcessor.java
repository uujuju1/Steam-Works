package sw.annotations.processors.impl;

import arc.*;
import arc.struct.*;
import com.squareup.javapoet.*;
import sw.annotations.Annotations.*;
import sw.annotations.processors.*;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import java.util.*;

/**
 * Whenever a {@link arc.graphics.g2d.TextureRegion TextureRegion} is annotated with {@link Load @Load},
 * it'll generate a finder method inside the ContentRegionRegistry, which must be called for every
 * content in {@link mindustry.game.EventType.ContentInitEvent ContentInitEvent}
 *
 * @author Liz
 */
public class RegionsProcessor extends BaseProcessor {
	public ObjectMap<Element, Seq<Element>> annotated = new ObjectMap<>();

	{
		rounds = 1;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> types = new HashSet<>();
		String prefix = modName + ".annotations.Annotations.";
		types.add(prefix + "Load");
		types.add(prefix + "EnsureLoad");
		return types;
	}

	@Override
	public void process(RoundEnvironment roundEnv) throws Exception {
		if (round == 1) {
			TypeSpec.Builder regionsClass = TypeSpec.classBuilder(classPrefix + "ContentRegionRegistry")
			.addModifiers(Modifier.PUBLIC);
			
			MethodSpec.Builder loadMethod = MethodSpec.methodBuilder("load")
			.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
			.addParameter(tName(Object.class), "content");

			for (Element element : roundEnv.getElementsAnnotatedWith(Load.class)) {
				annotated.get(element.getEnclosingElement(), Seq::new).add(element);
			}

			for (Element base : annotated.keys().toSeq()) {
				loadMethod.beginControlFlow("if (content instanceof $T mapped)", cName(base));

				for (Element field : annotated.get(base)) {
					Load annotation = field.getAnnotation(Load.class);

					StringBuilder depth = new StringBuilder();
					for (int i = 0; i < annotation.lengths().length; i++) {
						loadMethod.beginControlFlow(
							"for (int INDEX$L = 0; INDEX$L < $L; INDEX$L++)",
							i, i, annotation.lengths()[i], i
						);

						depth.append("[INDEX").append(i).append("]");
					}

					loadMethod.addStatement(
						"mapped.$L$L = $T.atlas.find($L, $L)",
						field.getSimpleName(),
						depth.toString(),
						cName(Core.class),
						parse(annotation.value()),
						parse(annotation.fallBack())
					);

					for (int i = 0; i < annotation.lengths().length; i++) {
						loadMethod.endControlFlow();
					}
				}

				loadMethod.endControlFlow();
			}

			regionsClass.addMethod(loadMethod.build());

			write(regionsClass.build());
		}
	}

	public String parse(String other) {
		other = '"' + other + '"';
		return other
		.replace("%", modName)
		.replace("@", "\" + mapped.")
		.replace("#", "\" + INDEX")
		.replace("$", " + \"");
	}
}
