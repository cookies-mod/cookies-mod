package dev.morazzer.cookies.plugins.areaenum;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.printer.DefaultPrettyPrinter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.text.WordUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.internal.tasks.DefaultSourceSetContainer;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.plugins.ide.idea.model.IdeaModel;

import javax.annotation.processing.Generated;
import javax.inject.Inject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AreaEnumPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        TaskProvider<GenerateEnum> generateAreaEnum = project.getTasks()
                .register("generateAreaEnum", GenerateEnum.class);
        GenerateEnum generateEnum = generateAreaEnum.get();
        project.beforeEvaluate(project1 -> {
            project1.getExtensions().getByType(IdeaModel.class).getModule().getGeneratedSourceDirs()
                    .addAll(generateEnum.getOutputs().getFiles().getFiles());
            project1.getExtensions().getByType(SourceSetContainer.class).getByName("main").getAllSource().getSrcDirs()
                    .addAll(generateEnum.getOutputs().getFiles().getFiles());
        });
        project.afterEvaluate(project1 -> {
            DefaultSourceSetContainer sourceSets = (DefaultSourceSetContainer) project.getExtensions()
                    .getByName("sourceSets");
            SourceSet main = sourceSets.getByName("main");
            main.getJava().srcDir(generateEnum.getOutputs().getFiles().getFiles());
        });
        project.getExtensions().getByType(IdeaModel.class).getModule().getGeneratedSourceDirs()
                .addAll(generateEnum.getOutputs().getFiles().getFiles());
        project.getExtensions().getByType(SourceSetContainer.class).getByName("main").getAllSource().getSrcDirs()
                .addAll(generateEnum.getOutputs().getFiles().getFiles());
        project.getTasks().getByName("compileJava").dependsOn(generateEnum);
    }

    static class GenerateEnum extends DefaultTask {


        @Inject
        public GenerateEnum(Project project) {
            getOutputs().cacheIf(element -> false);
            getOutputs().dir(project.getBuildDir().getAbsolutePath() + "/generated/sources/area-enum/java/main");
            getInputs().files(Objects.requireNonNull(project.getExtensions().findByType(SourceSetContainer.class))
                            .getByName("main").getAllJava()).withPropertyName("sources")
                    .withPathSensitivity(PathSensitivity.RELATIVE);

            doLast(task -> {
                CompilationUnit compilationUnit = new CompilationUnit("dev.morazzer.cookiesmod.generated").setStorage(
                        getProject().getBuildDir().getAbsoluteFile().toPath().resolve(
                                "generated/sources/area-enum/java/main/dev/morazzer/cookiesmod/generated/Area.java"));


                CompilationUnit isInterface = new CompilationUnit("dev.morazzer.cookiesmod.generated").setStorage(
                        getProject().getBuildDir().getAbsoluteFile().toPath().resolve(
                                "generated/sources/area-enum/java/main/dev/morazzer/cookiesmod/generated/IsInArea.java"));
                ClassOrInterfaceDeclaration isArea = isInterface.addInterface("IsInArea", Modifier.Keyword.PUBLIC);

                EnumDeclaration areas = compilationUnit.addEnum("Area");
                areas.addImplementedType("IsInArea");
                final String areaJson;
                try (InputStream inputStream = getProject().getBuildscript().getClassLoader()
                        .getResourceAsStream("areas.json")) {
                    if (inputStream == null) return;
                    areaJson = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                JsonArray jsonArray = new Gson().fromJson(areaJson, JsonArray.class);

                areas.addField(String.class,
                        "scoreboard",
                        Modifier.publicModifier().getKeyword(),
                        Modifier.finalModifier().getKeyword()
                );
                areas.addField(Boolean.class,
                        "regex",
                        Modifier.publicModifier().getKeyword(),
                        Modifier.finalModifier().getKeyword()
                );
                areas.addField(Character.class, "prefix", Modifier.Keyword.PUBLIC, Modifier.Keyword.FINAL);
                areas.addConstructor().addParameter(String.class, "scoreboard").createBody()
                        .addStatement("this(scoreboard, false);");

                areas.addConstructor().addParameter(String.class, "scoreboard").addParameter(Character.class, "prefix")
                        .createBody().addStatement("this(scoreboard, false, prefix);");

                areas.addConstructor().addParameter(String.class, "scoreboard").addParameter(Boolean.class, "regex")
                        .createBody().addStatement("this(scoreboard, regex, '\u23E3');");
                areas.addConstructor().addParameter(String.class, "scoreboard").addParameter(Boolean.class, "regex")
                        .addParameter(Character.class, "prefix").createBody()
                        .addStatement("this.scoreboard = prefix + \" \" + scoreboard;")
                        .addStatement("this.regex = regex;").addStatement("this.prefix = prefix;");
                areas.addAndGetAnnotation(Generated.class)
                        .addPair("date", new StringLiteralExpr(LocalDateTime.now().toString()))
                        .addPair("value", new StringLiteralExpr(GenerateEnum.class.getName()))
                        .addPair("comments", new StringLiteralExpr("Areas described as in the areas.json file"));
                isArea.addAndGetAnnotation(Generated.class)
                        .addPair("date", new StringLiteralExpr(LocalDateTime.now().toString()))
                        .addPair("value", new StringLiteralExpr(GenerateEnum.class.getName()))
                        .addPair("comments", new StringLiteralExpr("Interface used to add Area#isArea methods"));

                Map<String, List<String>> map = new LinkedHashMap<>();

                for (JsonElement jsonElement : jsonArray) {
                    if (!(jsonElement instanceof JsonObject jsonObject)) continue;
                    String name = jsonObject.get("name").getAsString();
                    EnumConstantDeclaration constant = areas.addEnumConstant(name)
                            .addArgument(new StringLiteralExpr(jsonObject.get("scoreboard").getAsString()));

                    if (jsonObject.has("regex")) {
                        constant.addArgument(new BooleanLiteralExpr(jsonObject.get("regex").getAsBoolean()));
                    }
                    if (jsonObject.has("prefix")) {
                        constant.addArgument(new CharLiteralExpr(jsonObject.get("prefix").getAsString()
                                .toCharArray()[0]));
                    }
                    List<String> type = map.getOrDefault(jsonObject.get("type").getAsString(), new LinkedList<>());
                    type.add(name);
                    map.put(jsonObject.get("type").getAsString(), type);

                    //noinspection deprecation
                    isArea.addMethod("is%s".formatted(WordUtils.capitalize(name.replace("_", " ").toLowerCase())
                                    .replace(" ", "")), Modifier.Keyword.PUBLIC, Modifier.Keyword.DEFAULT).setType("boolean")
                            .createBody().addStatement("return this.equals( Area.%s );".formatted(name));

                }

                areas.addMethod("isInArray", Modifier.Keyword.PRIVATE, Modifier.Keyword.STATIC).setType("boolean")
                        .addParameter("Area[]", "areas").addParameter("Area", "match").createBody().addStatement("""
                                for (Area area : areas) {
                                    if (area == match) {
                                        return true;
                                    }
                                }
                                """).addStatement("return false;");

                for (String key : map.keySet()) {
                    areas.addField("Area[]",
                            key.toUpperCase(),
                            Modifier.privateModifier().getKeyword(),
                            Modifier.staticModifier().getKeyword(),
                            Modifier.finalModifier().getKeyword()
                    );
                    areas.addStaticInitializer().addStatement("%s = new Area[] { %s };".formatted(key.toUpperCase(),
                            String.join(",", map.get(key))
                    ));
                    areas.addMethod("is%s".formatted(key.substring(0, 1).toUpperCase() + key.substring(1)),
                                    Modifier.Keyword.PUBLIC,
                                    Modifier.Keyword.STATIC
                            ).addParameter("Area", "area").setType("boolean").createBody()
                            .addStatement("return isInArray(%s, area);".formatted(key.toUpperCase()));
                }

                compilationUnit.getStorage()
                        .ifPresent(storage -> storage.save(unit -> new DefaultPrettyPrinter().print(unit)));
                isInterface.getStorage()
                        .ifPresent(storage -> storage.save(unit -> new DefaultPrettyPrinter().print(unit)));

            });
        }


    }
}
