package sw.annotations.processors.impl;

import arc.audio.*;
import arc.files.*;
import arc.struct.*;
import arc.util.*;
import com.squareup.javapoet.*;
import mindustry.*;
import sw.annotations.processors.*;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import java.util.*;

/**
 * @author Liz took from Glenn
 */
@SuppressWarnings("all")
public class AssetsProcessor extends BaseProcessor {
	public Seq<Asset> assets = new Seq<>();

	{
		rounds = 2;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> types = new HashSet<>();
		String prefix = modName + ".annotations.Annotations.";
		types.add(prefix + "ProcessAssets");
		return types;
	}

	@Override
	public void process(RoundEnvironment roundEnv) throws Exception {
		if (round == 1) {
			assets.add(
				new Asset() {
					@Override public String className() {
						return classPrefix + "Sounds";
					}
					
					@Override public CodeBlock load() {
						return CodeBlock.builder().add("return $T.tree.loadSound(name)", cName(Vars.class)).build();
					}
					
					@Override public Fi path(Fi root) {
						return root.child("assets/sounds");
					}
					
					@Override public TypeElement type() {
						return toType(Sound.class);
					}
					
					@Override public boolean validFile(Fi file) {
						return file.extEquals("ogg") || file.extEquals("mp3");
					}
				},
				new Asset() {
					@Override public String className() {
						return classPrefix + "Musics";
					}
					
					@Override public CodeBlock load() {
						return CodeBlock.builder().add("return $T.tree.loadMusic(name)", cName(Vars.class)).build();
					}
					
					@Override public Fi path(Fi root) {
						return root.child("assets/music");
					}
					
					@Override public TypeElement type() {
						return toType(Music.class);
					}
					
					@Override public boolean validFile(Fi file) {
						return file.extEquals("ogg") || file.extEquals("mp3");
					}
				}
			);
		} else if (round == 2) {
			for(Asset asset : assets) {
				TypeSpec.Builder assetClass = TypeSpec.classBuilder(asset.className())
				.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
				.addMethod(
					MethodSpec.constructorBuilder()
					.addStatement("throw new $T()", cName(AssertionError.class))
					.build()
				)
				.addMethod(
					MethodSpec.methodBuilder("asset")
					.addModifiers(Modifier.STATIC)
					.addParameter(tName(String.class), "name")
					.returns(tName(asset.type()))
					.addStatement(asset.load())
					.build()
				);
				
				MethodSpec.Builder loadMethod = MethodSpec.methodBuilder("load")
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.addStatement("if ($T.headless) return", cName(Vars.class));
				
				asset.path(rootDir).walk(file -> {
					if (asset.validFile(file)) {
						String fieldName = Strings.kebabToCamel(file.nameWithoutExtension());
						assetClass.addField(
							FieldSpec.builder(tName(asset.type()), fieldName)
							.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
							.build()
						);
						
						loadMethod.addStatement("$L = asset($S)", fieldName, file.nameWithoutExtension());
					}
				});
				
				assetClass.addMethod(loadMethod.build());
				write(assetClass.build());
			}
		}
	}

	/**
	 * Generates a class containing all the found assets
	 */
	interface Asset {
		// Generated class name
		String className();
		
		// code inside the asset method
		CodeBlock load();
		
		// Path for the asset's folder
		Fi path(Fi root);
		
		// Type of asset being processed
		TypeElement type();
		
		// Whether the found file is a valid file of this asset
		boolean validFile(Fi file);
	}
}