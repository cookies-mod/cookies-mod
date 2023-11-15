package dev.morazzer.cookies.plugins.modules;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.printer.DefaultPrettyPrinter;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.tools.ant.util.StreamUtils;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.internal.tasks.DefaultSourceSetContainer;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.plugins.ide.idea.model.IdeaModel;

public class GenerateModuleLoader implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        Task generateModuleLoader = project.getTasks().create("generateModuleLoader");
        generateModuleLoader.getOutputs().cacheIf(element -> false);
        generateModuleLoader.getOutputs()
                .dir(project.getBuildDir().getAbsolutePath() + "/generated/sources/moduleLoader/java/main");
        generateModuleLoader.getInputs()
                .files(Objects.requireNonNull(project.getExtensions().findByType(SourceSetContainer.class))
                        .getByName("main").getAllJava())
                .withPropertyName("sources").withPathSensitivity(PathSensitivity.RELATIVE);
        generateModuleLoader.doLast(task -> {
            CombinedTypeSolver typeSolver = new CombinedTypeSolver(task.getProject().getExtensions()
                .getByType(SourceSetContainer.class).getByName("main").getAllJava().getSrcDirs().stream()
                .filter(File::exists)
                .map(JavaParserTypeSolver::new).toArray(TypeSolver[]::new));

            JavaParser javaParser = new JavaParser(new ParserConfiguration().setSymbolResolver(new JavaSymbolSolver(
                    typeSolver)).setLanguageLevel(ParserConfiguration.LanguageLevel.CURRENT));

            SourceSet main = task.getProject().getExtensions().getByType(SourceSetContainer.class).getByName("main");
            List<ClassOrInterfaceDeclaration> list = StreamUtils.iteratorAsStream(main.getAllJava()
                    .matching(patternFilterable -> patternFilterable.getIncludes()
                            .add("dev/morazzer/cookiesmod/**/*.java")).iterator()).map(it -> {
                try {
                    ParseResult<CompilationUnit> parse = javaParser.parse(it);
                    if (!parse.isSuccessful() || parse.getResult().isEmpty()) {
                        return new ArrayList<ClassOrInterfaceDeclaration>();
                    }
                    return parse.getResult().get().findAll(ClassOrInterfaceDeclaration.class);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).flatMap(Collection::stream).toList();

            CompilationUnit compilationUnit = new CompilationUnit("dev.morazzer.cookiesmod.generated").setStorage(
                    generateModuleLoader.getProject()
                            .getBuildDir().getAbsoluteFile().toPath()
                            .resolve(
                                    "generated/sources/moduleLoader/java/main/dev/morazzer/cookiesmod/generated/ModuleLoader.java"));

            compilationUnit.addImport("java.util.function.Function");
            ClassOrInterfaceDeclaration moduleLoaderClass = compilationUnit.addClass("ModuleLoader");

            moduleLoaderClass.addField(
                    "Function<String, Boolean>",
                    "shouldLoadCallback",
                    Modifier.Keyword.PRIVATE
            );

            moduleLoaderClass.addMethod("shouldLoadCallback", Modifier.Keyword.PUBLIC)
                    .addParameter("Function<String, Boolean>", "shouldLoadTest")
                    .createBody()
                    .addStatement("shouldLoadCallback = shouldLoadTest;");

            moduleLoaderClass.addMethod("load", Modifier.Keyword.PRIVATE)
                    .addParameter("Module", "module")
                    .createBody()
                    .addStatement("module.load();")
                    .addStatement("this.loadedModules.put(module.getIdentifier().toString(), module);")
                    .addStatement("this.callbacks.forEach(callback -> callback.accept(module));");

            moduleLoaderClass.addField("List<Consumer<Module>>", "callbacks");

            moduleLoaderClass.addMethod("addModuleLoadCallback")
                    .addParameter("Consumer<Module>", "callback")
                    .createBody()
                    .addStatement("this.callbacks.add(callback);");

            compilationUnit.addImport("java.util.Map").addImport("java.util.LinkedHashMap")
                    .addImport("dev.morazzer.cookiesmod.modules.Module").addImport("java.util.function.Consumer")
                    .addImport("java.util.LinkedList").addImport("java.util.List");

            moduleLoaderClass.addField(
                    "Map<String, Module>",
                    "loadedModules",
                    Modifier.Keyword.PRIVATE,
                    Modifier.Keyword.FINAL
            );
            moduleLoaderClass.addInitializer().addStatement("loadedModules = new LinkedHashMap<>();")
                    .addStatement("this.callbacks = new LinkedList();");


            BlockStmt loadModules = moduleLoaderClass.addMethod(
                            "loadModules",
                            Modifier.Keyword.PUBLIC
                    )
                    .createBody();

            for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : list) {
                if (classOrInterfaceDeclaration.isAnnotationPresent("LoadModule") && classOrInterfaceDeclaration.getImplementedTypes()
                        .stream()
                        .anyMatch(classOrInterfaceType -> classOrInterfaceType.getName().asString().equals("Module"))) {
                    if (classOrInterfaceDeclaration.getFullyQualifiedName().isEmpty()) continue;
                    compilationUnit.addImport(classOrInterfaceDeclaration.getFullyQualifiedName().get());
                    Optional<AnnotationExpr> loadModule1 = classOrInterfaceDeclaration.getAnnotationByName(
                            "LoadModule");
                    if (loadModule1.isEmpty()) continue;
                    StringLiteralExpr loadModule = loadModule1.get().asSingleMemberAnnotationExpr().getMemberValue()
                            .asStringLiteralExpr();
                    String string = loadModule.asString();
                    //noinspection deprecation
                    String capitalize = WordUtils.capitalize(string.replaceAll("[^A-Za-z]", " ")).replace(" ", "");

                    moduleLoaderClass.addMethod("load%s".formatted(capitalize))
                            .createBody()
                            .addStatement("this.load(new %s());".formatted(classOrInterfaceDeclaration.getNameAsString()));

                    loadModules.addStatement("""
                            if (shouldLoadCallback.apply("%s")) {
                                load%s();
                            }
                            """.formatted(string, capitalize));
                    //moduleLoaderClass.addMethod("load",)
                }
            }

            compilationUnit.getStorage()
                    .ifPresent(storage -> storage.save(unit -> new DefaultPrettyPrinter().print(unit)));
        });

        project.getTasks().getByName("compileJava").dependsOn(generateModuleLoader);
        project.getExtensions().getByType(IdeaModel.class).getModule().getGeneratedSourceDirs()
                .addAll(generateModuleLoader.getOutputs().getFiles().getFiles());
        project.getExtensions().getByType(SourceSetContainer.class).getByName("main").getAllSource().getSrcDirs()
                .addAll(generateModuleLoader.getOutputs().getFiles().getFiles());
        generateModuleLoader.dependsOn(project.getTasks().getByName("generateAreaEnum"));
        project.afterEvaluate(project1 -> {
            DefaultSourceSetContainer sourceSets = (DefaultSourceSetContainer) project.getExtensions()
                    .getByName("sourceSets");
            SourceSet main = sourceSets.getByName("main");
            main.getJava().srcDir(generateModuleLoader.getOutputs().getFiles().getFiles());
        });
    }
}
