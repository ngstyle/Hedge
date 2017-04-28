package me.nohc;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static java.lang.reflect.Modifier.PRIVATE;

/**
 * Created by chon on 2017/4/27.
 * What? How? Why?
 */

final class ClassValidator {
    static boolean isPrivate(Element annotatedClass) {
        return annotatedClass.getModifiers().contains(PRIVATE);
    }

    static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        // maybe inner class
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }
}
