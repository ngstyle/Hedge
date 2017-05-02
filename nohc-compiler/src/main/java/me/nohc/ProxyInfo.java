package me.nohc;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by chon on 2017/4/27.
 * What? How? Why?
 */

public class ProxyInfo {
    private String packageName;
    private String proxyClassName;
    private TypeElement typeElement;

    public Map<Integer, VariableElement> injectVariables = new HashMap<>();

    public static final String PROXY = "ViewInject";

    private ProcessingEnvironment processingEnv;
    public ProxyInfo(ProcessingEnvironment processingEnv, TypeElement classElement) {
        this.processingEnv = processingEnv;
        this.typeElement = classElement;
        PackageElement packageElement = processingEnv.getElementUtils().getPackageOf(classElement);
        String packageName = packageElement.getQualifiedName().toString();

        // classname
        String className = ClassValidator.getClassName(classElement, packageName);
        this.packageName = packageName;
        this.proxyClassName = className + "$$" + PROXY;
    }


    public String generateJavaCode() throws IOException {
//        StringBuilder builder = new StringBuilder();
//        builder.append("// Generated code. Do not modify!\n");
//        builder.append("package ").append(packageName).append(";\n\n");
//        builder.append("import me.nohc.*;\n");
//        builder.append('\n');
//
//        builder.append("public class ").append(proxyClassName).append(" implements " + ProxyInfo.PROXY + "<" + typeElement.getQualifiedName() + ">");
//        builder.append(" {\n");
//
//        generateMethods(builder);
//        builder.append('\n');
//
//        builder.append("}\n");

//        VariableElement element = injectVariables.get(id);
//        String name = element.getSimpleName().toString();
//        String type = element.asType().toString();

        //        return builder.toString();

        MethodSpec.Builder builder = MethodSpec.methodBuilder("inject")
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(typeElement.asType()),"host")
                .addParameter(TypeName.get(Object.class),"source")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);

        for (int id : injectVariables.keySet()) {
            VariableElement element = injectVariables.get(id);
            String name = element.getSimpleName().toString();
            String type = element.asType().toString();

            builder.beginControlFlow("if(source instanceof android.app.Activity)");
            builder.addStatement("host." + name + " = " + "(" + type + ")(((android.app.Activity)source).findViewById( " + id + "))");
            builder.endControlFlow();

            builder.beginControlFlow("else");
            builder.addStatement("host." + name + " = " + "(" + type + ")(((android.view.View)source).findViewById( " + id + "))");
            builder.endControlFlow();
        }


        MethodSpec methodSpec = builder.build();

        // generic
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get("me.nohc", "ViewInject"), TypeName.get(typeElement.asType()));

        TypeSpec typeSpec = TypeSpec.classBuilder(proxyClassName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(parameterizedTypeName)
                .addMethod(methodSpec)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
        javaFile.writeTo(processingEnv.getFiler());

        return null;

    }


    private void generateMethods(StringBuilder builder) {

        builder.append("@Override\n ");
        builder.append("public void inject(" + typeElement.getQualifiedName() + " host, Object source ) {\n");


        for (int id : injectVariables.keySet()) {
            VariableElement element = injectVariables.get(id);
            String name = element.getSimpleName().toString();
            String type = element.asType().toString();
            builder.append(" if(source instanceof android.app.Activity){\n");
            builder.append("host." + name).append(" = ");
            builder.append("(" + type + ")(((android.app.Activity)source).findViewById( " + id + "));\n");
            builder.append("\n}else{\n");
            builder.append("host." + name).append(" = ");
            builder.append("(" + type + ")(((android.view.View)source).findViewById( " + id + "));\n");
            builder.append("\n};");
        }
        builder.append("  }\n");


    }

    public String getProxyClassFullName() {
        return packageName + "." + proxyClassName;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

}
