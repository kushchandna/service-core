package com.kush.utils.remoting.client;

import java.io.Closeable;

import com.kush.utils.remoting.ResolutionFailedException;
import com.kush.utils.remoting.Resolvable;

public interface Connection extends Closeable {

    Object resolve(Resolvable resolvable) throws ResolutionFailedException;
}
