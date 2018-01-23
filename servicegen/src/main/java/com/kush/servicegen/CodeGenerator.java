package com.kush.servicegen;

import java.io.File;
import java.io.IOException;

import javax.tools.JavaFileObject;

public interface CodeGenerator {

    JavaFileObject generate(String targetPackage, File targetDirectory) throws IOException;
}
