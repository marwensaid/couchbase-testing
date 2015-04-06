/*
 * Copyright Christophe Jeunesse
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.dreameddeath.core.annotation.processor;

import com.dreameddeath.core.annotation.DocumentVersionUpgrader;
import com.dreameddeath.core.tools.annotation.processor.reflection.AbstractClassInfo;
import com.dreameddeath.core.upgrade.Utils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Set;

/**
 * Created by ceaj8230 on 28/11/2014.
 */
@SupportedAnnotationTypes(
        {"com.dreameddeath.core.annotation.DocumentVersionUpgrader"}
)
public class DocumentVersionUpgraderAnnotationProcessor extends AbstractProcessor {
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        for(Element baseElem:roundEnv.getElementsAnnotatedWith(DocumentVersionUpgrader.class)){
            DocumentVersionUpgrader annot =baseElem.getAnnotation(DocumentVersionUpgrader.class);
            Elements elementUtils = processingEnv.getElementUtils();
            try {
                String fileName= Utils.getFilename(annot);
                FileObject jfo = processingEnv.getFiler().createResource(
                        StandardLocation.CLASS_OUTPUT,
                        "",
                        fileName,
                        baseElem);

                AbstractClassInfo classInfo = AbstractClassInfo.getClassInfo((TypeElement) ((ExecutableElement)baseElem).getEnclosingElement());
                //String packageName = elementUtils.getPackageOf(baseElem).getQualifiedName().toString();
                //TypeElement classElem =;

                //String fullClassName = ((TypeElement) classElem).getQualifiedName().toString();
                //String realClassName = String.format("%s.%s", packageName, fullClassName.substring(packageName.length() + 1).replace(".", "$"));

                BufferedWriter bw = new BufferedWriter(jfo.openWriter());
                bw.write(classInfo.getName());
                bw.write(";");
                bw.write(baseElem.getSimpleName().toString());
                bw.write(";");
                bw.write(Utils.buildTargetVersion(annot));
                bw.flush();
                bw.close();
                messager.printMessage(Diagnostic.Kind.NOTE,"Creating file Upgrader "+fileName+" to upgrade to  "+ Utils.buildTargetVersion(annot));
            }
            catch(IOException e){
                messager.printMessage(Diagnostic.Kind.ERROR,"Cannot write with error"+e.getMessage());
            }
        }
        return true;
    }
}
