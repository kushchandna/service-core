package com.kush.servicegen;

import java.io.File;

import javax.tools.JavaFileObject;

public interface CodeGenerator {

    JavaFileObject generate(String targetPackage, File targetDirectory) throws CodeGenerationFailedException;
}
