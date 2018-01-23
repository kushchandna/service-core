package com.kush.servicegen;

import java.io.File;
import java.io.IOException;

import javax.tools.JavaFileObject;

public interface ServiceApiGenerator {

    JavaFileObject generateServiceApiClass(Class<?> serviceClass, String targetPackage, File targetDirectory) throws IOException;
}
