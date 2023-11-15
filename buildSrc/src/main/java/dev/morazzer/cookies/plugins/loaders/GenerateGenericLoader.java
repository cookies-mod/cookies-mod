package dev.morazzer.cookies.plugins.loaders;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.printer.DefaultPrettyPrinter;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.internal.tasks.DefaultSourceSetContainer;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.plugins.ide.idea.model.IdeaModel;

public class GenerateGenericLoader implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        Task generateAnnotationLoader = project.getTasks().create("generateAnnotationLoader");
        generateAnnotationLoader.getOutputs().cacheIf(element -> false);
        generateAnnotationLoader.getOutputs()
            .dir(project.getBuildDir().getAbsolutePath() + "/generated/sources/loaders/java/main");
        generateAnnotationLoader.getInputs()
            .files(Objects.requireNonNull(project.getExtensions().findByType(SourceSetContainer.class))
                .getByName("main").getAllJava())
            .withPropertyName("sources").withPathSensitivity(PathSensitivity.RELATIVE);
        generateAnnotationLoader.doLast(task -> {
            CombinedTypeSolver typeSolver = new CombinedTypeSolver(task.getProject().getExtensions()
                .getByType(SourceSetContainer.class).getByName("main").getAllJava().getSrcDirs().stream()
                .map(JavaParserTypeSolver::new).toArray(TypeSolver[]::new));

            JavaParser javaParser = new JavaParser(new ParserConfiguration().setSymbolResolver(new JavaSymbolSolver(
                typeSolver)).setLanguageLevel(ParserConfiguration.LanguageLevel.CURRENT));

            SourceSet main = task.getProject().getExtensions().getByType(SourceSetContainer.class).getByName("main");

            List<AnnotationDeclaration> annotationDeclarations = new ArrayList<>();

            main.getAllJava().matching(patternFilterable -> {
                patternFilterable.getIncludes().add("dev/morazzer/cookiesmod/**/*.java");
                patternFilterable.getExcludes().add("dev/morazzer/cookiesmod/**/generated/**/*.java");
            }).iterator().forEachRemaining(file -> {
                try {
                    ParseResult<CompilationUnit> parse = javaParser.parse(file);
                    if (!parse.isSuccessful() || parse.getResult().isEmpty()) {
                        throw new RuntimeException("Failed to parse " + file);
                    }
                    for (AnnotationDeclaration classOrInterfaceDeclaration : parse.getResult().get()
                        .findAll(AnnotationDeclaration.class)) {
                        if (classOrInterfaceDeclaration.isAnnotationPresent("GenerateLoader")) {
                            annotationDeclarations.add(classOrInterfaceDeclaration);
                        }
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });

            for (AnnotationDeclaration annotationDeclaration : annotationDeclarations) {
                if (annotationDeclaration.isAnnotationPresent("Target")) {
                    String target =
                        annotationDeclaration.getAnnotationByName("Target").get().asSingleMemberAnnotationExpr()
                            .getMemberValue().asFieldAccessExpr().getNameAsString();
                    CompilationUnit compilationUnit = new CompilationUnit("dev.morazzer.cookiesmod.generated")
                        .setStorage(generateAnnotationLoader.getProject()
                            .getBuildDir().getAbsoluteFile().toPath()
                            .resolve(
                                "generated/sources/loaders/java/main/dev/morazzer/cookiesmod/generated/%sLoader.java"
                                    .formatted(annotationDeclaration.getNameAsString())));

                    ClassOrInterfaceDeclaration loader =
                        compilationUnit.addClass(annotationDeclaration.getNameAsString() + "Loader");

                    switch (target) {
                        case "TYPE" -> {
                            List<ClassOrInterfaceDeclaration> list = new ArrayList<>();
                            main.getAllJava().matching(patternFilterable -> {
                                patternFilterable.getIncludes().add("dev/morazzer/cookiesmod/**/*.java");
                                patternFilterable.getExcludes().add("dev/morazzer/cookiesmod/**/generated/**/*.java");
                            }).iterator().forEachRemaining(file -> {
                                try {
                                    ParseResult<CompilationUnit> parse = javaParser.parse(file);
                                    if (!parse.isSuccessful() || parse.getResult().isEmpty()) {
                                        return;
                                    }
                                    list.addAll(parse.getResult().get().findAll(ClassOrInterfaceDeclaration.class));
                                } catch (FileNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                            BlockStmt getClasses =
                                loader.addMethod("getClasses", Modifier.Keyword.PUBLIC, Modifier.Keyword.STATIC)
                                    .setType("Class<?>[]")
                                    .createBody();

                            List<String> classNames = new ArrayList<>();
                            for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : list) {
                                if (!classOrInterfaceDeclaration.isAnnotationPresent(
                                    annotationDeclaration.getNameAsString())) {
                                    continue;
                                }
                                classNames.add(classOrInterfaceDeclaration.getFullyQualifiedName().get());
                            }
                            getClasses.addStatement(
                                "return new Class<?>[] {%s.class};".formatted(String.join(".class, ", classNames)));
                        }
                        case "METHOD" -> {
                            compilationUnit.addImport("java.lang.reflect.Method");
                            List<MethodDeclaration> list = new ArrayList<>();
                            main.getAllJava().matching(patternFilterable -> {
                                patternFilterable.getIncludes().add("dev/morazzer/cookiesmod/**/*.java");
                                patternFilterable.getExcludes().add("dev/morazzer/cookiesmod/**/generated/**/*.java");
                            }).iterator().forEachRemaining(file -> {
                                try {
                                    ParseResult<CompilationUnit> parse = javaParser.parse(file);
                                    if (!parse.isSuccessful() || parse.getResult().isEmpty()) {
                                        return;
                                    }
                                    list.addAll(parse.getResult().get().findAll(MethodDeclaration.class));
                                } catch (FileNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                            BlockStmt getMethods =
                                loader.addMethod("getMethods", Modifier.Keyword.PUBLIC, Modifier.Keyword.STATIC)
                                    .setType("Method[]")
                                    .createBody();
                            List<String> methods = new ArrayList<>();
                            for (MethodDeclaration methodDeclaration : list) {
                                if (!methodDeclaration.isAnnotationPresent(
                                    annotationDeclaration.getNameAsString())) {
                                    continue;
                                }
                                methods.add("%s.class.getMethod(\"%s\")".formatted(
                                    ((ClassOrInterfaceDeclaration) methodDeclaration.getParentNode()
                                        .get()).getFullyQualifiedName().get(),
                                    methodDeclaration.getNameAsString()
                                ));
                            }
                            getMethods.addStatement(new TryStmt().setTryBlock(new BlockStmt().addStatement(
                                    "return new Method[] {%s};".formatted(String.join(",", methods))))
                                .setCatchClauses(new NodeList<>(new CatchClause(
                                    new Parameter(new ClassOrInterfaceType(null, "NoSuchMethodException"), "e"),
                                    new BlockStmt().addStatement("throw new RuntimeException(e);")))));
                        }
                    }

                    compilationUnit.getStorage()
                        .ifPresent(storage -> storage.save(unit -> new DefaultPrettyPrinter().print(unit)));
                }
            }

        });

        project.getTasks().getByName("compileJava").dependsOn(generateAnnotationLoader);
        project.getExtensions().getByType(IdeaModel.class).getModule().getGeneratedSourceDirs()
            .addAll(generateAnnotationLoader.getOutputs().getFiles().getFiles());
        project.getExtensions().getByType(SourceSetContainer.class).getByName("main").getAllSource().getSrcDirs()
            .addAll(generateAnnotationLoader.getOutputs().getFiles().getFiles());
        generateAnnotationLoader.dependsOn(project.getTasks().getByName("generateAreaEnum"));
        generateAnnotationLoader.dependsOn(project.getTasks().getByName("generateModuleLoader"));
        project.afterEvaluate(project1 -> {
            DefaultSourceSetContainer sourceSets = (DefaultSourceSetContainer) project.getExtensions()
                .getByName("sourceSets");
            SourceSet main = sourceSets.getByName("main");
            main.getJava().srcDir(generateAnnotationLoader.getOutputs().getFiles().getFiles());
        });
    }
}
