package com.dreameddeath.core.annotation.processor;

import com.dreameddeath.core.annotation.DocumentDef;
import com.dreameddeath.core.upgrade.Utils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Set;

/**
 * Created by CEAJ8230 on 26/11/2014.
 */
@SupportedAnnotationTypes(
        {"com.dreameddeath.core.annotation.DocumentDef"}
)
public class DocumentDefAnnotationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        for(Element classElem:roundEnv.getElementsAnnotatedWith(DocumentDef.class)){
            DocumentDef annot =classElem.getAnnotation(DocumentDef.class);
            Elements elementUtils = processingEnv.getElementUtils();
            try {
                String fileName= Utils.getFilename(annot, classElem);
                FileObject jfo = processingEnv.getFiler().createResource(
                        StandardLocation.CLASS_OUTPUT,
                        "",
                        fileName,
                        classElem);
                String packageName = elementUtils.getPackageOf(classElem).getQualifiedName().toString();
                String fullClassName = ((TypeElement) classElem).getQualifiedName().toString();
                String realClassName = new StringBuilder().append(packageName).append(".").append(fullClassName.substring(packageName.length() + 1).replace(".", "$")).toString();
                BufferedWriter bw = new BufferedWriter(jfo.openWriter());
                bw.write(realClassName);
                bw.flush();
                bw.close();
                messager.printMessage(Diagnostic.Kind.NOTE,"Creating file "+fileName+" for class "+realClassName);
            }
            catch(IOException e){
                messager.printMessage(Diagnostic.Kind.ERROR,"Cannot write with error"+e.getMessage());
            }
        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}