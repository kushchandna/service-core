package com.kush.utils.remoting.server;

import com.kush.utils.remoting.ResolutionFailedException;
import com.kush.utils.remoting.Resolvable;

public interface Resolver<R extends Resolvable> {

    Object resolve(R resolvable) throws ResolutionFailedException;
}
