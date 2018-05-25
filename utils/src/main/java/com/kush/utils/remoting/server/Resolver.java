package com.kush.utils.remoting.server;

import com.kush.utils.remoting.ResolutionFailedException;
import com.kush.utils.remoting.Resolvable;

public interface Resolver<T extends Resolvable> {

    Object resolve(T resolvable) throws ResolutionFailedException;
}
