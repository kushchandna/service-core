package com.kush.utils.remoting.server;

import com.kush.utils.remoting.Resolvable;

public interface Resolver {

    Object resolve(Resolvable resolvable);
}
